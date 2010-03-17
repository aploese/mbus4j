/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.json;

import net.sf.json.JSONObject;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.Frame.ControlCode;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateAndTimeDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateDataBlock;
import net.sf.mbus4j.dataframes.datablocks.EmptyDataBlock;
import net.sf.mbus4j.dataframes.datablocks.EnhancedIdentificationDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RawDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ReadOutDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RealDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.StringDataBlock;
import net.sf.mbus4j.dataframes.datablocks.VariableLengthDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.dataframes.datablocks.vif.VifePrimary;

/**
 *
 * @author aploese
 */
public class JSONFactory {

    public static String encodeHexByteArray(byte[] value) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x[");
        if (value != null) {
            for (byte b : value) {
                sb.append(String.format("%02X", b));
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static byte[] decodeHexByteArray(String value) {
        byte[] result = new byte[(value.length() -4 ) / 2];
        int resIndex = 0;
        for (int i = 3; i < value.length() -1; i +=2) {
            result[resIndex++] = (byte)Short.parseShort(value.substring(i, i +2), 16);
        }
        return result;
    }

    public static String encodeHexByte(byte b) {
        return String.format("0x%02x", b & 0xFF);
    }

    public static String encodeHexShort(short s) {
        return String.format("0x%04x", s & 0xFF);
    }

    public static byte decodeHexByte(JSONObject json, String key, byte defaultValue) {
        return json.containsKey(key) ? (byte)Short.parseShort(json.getString(key).substring(2), 16) : defaultValue;
    }

    public static short decodeHexShort(JSONObject json, String key, short defaultValue) {
        return json.containsKey(key) ? (short)Short.parseShort(json.getString(key).substring(2), 16) : defaultValue;
    }

    public static Frame createFrame(JSONObject json) {
        if (json.containsKey("controlCode")) {
            final ControlCode cc = ControlCode.valueOf(json.getString("controlCode"));
            final String sendUsedDataSubtype = json.containsKey("subType") ? json.getString("subType") : null;
            switch (cc) {
                case RSP_UD:
                    if (GeneralApplicationError.RSP_UD_SUBTYPE.equals(sendUsedDataSubtype)) {
                        return new GeneralApplicationError();
                    } else {
                        return new UserDataResponse();
                    }
                case SND_UD:
                    if (SendUserData.SEND_USER_DATA_SUBTYPE.equals(sendUsedDataSubtype)) {
                        return new SendUserData();
                    } else if (ApplicationReset.SEND_USER_DATA_SUBTYPE.equals(sendUsedDataSubtype)) {
                        return new ApplicationReset();
                    } else if (SynchronizeAction.SEND_USER_DATA_SUBTYPE.equals(sendUsedDataSubtype)) {
                        return new SynchronizeAction();
                    } else if (SetBaudrate.SEND_USER_DATA_SUBTYPE.equals(sendUsedDataSubtype)) {
                        return new SetBaudrate();
                    } else {
                        throw new UnsupportedOperationException("Unknown Send User Data Subcode: " + sendUsedDataSubtype);
                    }
                case REQ_UD1 :
                    return new RequestClassXData(ControlCode.REQ_UD1);
                case REQ_UD2 :
                    return new RequestClassXData(ControlCode.REQ_UD2);
                default:
                    throw new UnsupportedOperationException("Unknown ControlCode: " + cc);

            }
        }
        throw new UnsupportedOperationException("Unknown Class");
    }

    public static DataBlock createDataBlock(JSONObject json) {
        JSONObject drh = json.getJSONObject("drh");
        JSONObject dib = drh.getJSONObject("dib");
        DataFieldCode dfc = DataFieldCode.fromLabel(dib.getString("dataFieldCode"));
        JSONObject vib = drh.getJSONObject("vib");
        Vife[] vifes;
        Vif vif = null;
        if (!vib.isNullObject()) {
            vif = DataBlock.vifFromJSON(vib.getJSONObject("vif"));
            if (vib.containsKey("vifes")) {
                vifes = DataBlock.vifesFromJSON(vif.getVifType(), vib.getJSONArray("vifes"));
                for (Vife vife : vifes) {
                    if (vife instanceof VifePrimary) {
                        switch ((VifePrimary) vife) {
                            case START_DATE_TIME_OF:
                            case TIMESTAMP_OF_BEGIN_FIRST_LOWER:
                            case TIMESTAMP_OF_END_FIRST_LOWER:
                            case TIMESTAMP_BEGIN_LAST_LOWER:
                            case TIMESTAMP_END_LAST_LOWER:
                            case TIMESTAMP_BEGIN_FIRST_UPPER:
                            case TIMESTAMP_END_FIRST_UPPER:
                            case TIMESTAMP_BEGIN_LAST_UPPER:
                            case TIMESTAMP_END_LAST_UPPER:
                                return new DateAndTimeDataBlock(vif);
                            case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_S:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_MIN:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_H:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_D:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_S:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_MIN:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_H:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_D:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_S:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_MIN:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_H:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_D:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_S:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_MIN:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_H:
                                break;
                            case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_D:
                                break;
                            case DURATION_OF_FIRST_S:
                                break;
                            case DURATION_OF_FIRST_MIN:
                                break;
                            case DURATION_OF_FIRST_H:
                                break;
                            case DURATION_OF_FIRST_D:
                                break;
                            case DURATION_OF_LAST_S:
                                break;
                            case DURATION_OF_LAST_MIN:
                                break;
                            case DURATION_OF_LAST_H:
                                break;
                            case DURATION_OF_LAST_D:
                                break;
                            case TIMESTAMP_BEGIN_OF_FIRST:
                            case TIMESTAMP_END_OF_FIRST:
                            case TIMESTAMP_BEGIN_OF_LAST:
                            case TIMESTAMP_END_OF_LAST:
                                return new DateAndTimeDataBlock(vif);

                            default:
                        }
                    } else {
                        vifes = null;
                    }
                }
            }
        }
        switch (dfc) {
            case NO_DATA:
                return new EmptyDataBlock(dfc);
            case SELECTION_FOR_READOUT:
                return new ReadOutDataBlock(dfc);
            case SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST:
                return new ReadOutDataBlock(dfc);
            case SPECIAL_FUNCTION_IDLE_FILLER:
                return new EmptyDataBlock(dfc);
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET:
                return new RawDataBlock(dfc);
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS:
                return new RawDataBlock(dfc);
            case VARIABLE_LENGTH:
                String variableLengthType = dib.getString("variableLengthType");
                if (StringDataBlock.STRING.equals(variableLengthType)) {
                    return new StringDataBlock(dfc, vif);
                } else {
                    return new VariableLengthDataBlock(dfc);
                }
            case _12_DIGIT_BCD:
                return new LongDataBlock(dfc);
            case _16_BIT_INTEGER:
                if (VifPrimary.TIMEPOINT_DATE.equals(vif)) {
                    return new DateDataBlock(vif);
                } else {
                    return new ShortDataBlock(dfc);
                }
            case _24_BIT_INTEGER:
                return new IntegerDataBlock(dfc);
            case _2_DIGIT_BCD:
                return new ByteDataBlock(dfc);
            case _32_BIT_INTEGER:
                if (VifPrimary.TIMEPOINT_TIME_AND_DATE.equals(vif)) {
                    return new DateAndTimeDataBlock(vif);
                } else {
                    return new IntegerDataBlock(dfc);
                }
            case _32_BIT_REAL:
                return new RealDataBlock(dfc);
            case _48_BIT_INTEGER:
                return new LongDataBlock(dfc);
            case _4_DIGIT_BCD:
                return new ShortDataBlock(dfc);
            case _64_BIT_INTEGER:
                if (VifPrimary.ENHANCED_IDENTIFICATION_RECORD.equals(vif)) {
                    return new EnhancedIdentificationDataBlock(dfc);
                } else {
                    return new LongDataBlock(dfc);
                }
            case _6_DIGIT_BCD:
                return new IntegerDataBlock(dfc);
            case _8_BIT_INTEGER:
                return new ByteDataBlock(dfc);
            case _8_DIGIT_BCD:
                if (VifPrimary.ENHANCED_IDENTIFICATION_RECORD.equals(vif)) {
                    return new EnhancedIdentificationDataBlock(dfc);
                } else {
                    return new IntegerDataBlock(dfc);
                }
            default:
                throw new UnsupportedOperationException("Unknown ControlCode");
        }
    }

    public static boolean getBoolean(JSONObject json, String key, boolean defaultValue) {
        return json.containsKey(key) ? json.getBoolean(key) : defaultValue;
    }

    public static short getShort(JSONObject json, String key, short defaultValue) {
        return json.containsKey(key) ? (short)json.getInt(key) : defaultValue;
    }

}
