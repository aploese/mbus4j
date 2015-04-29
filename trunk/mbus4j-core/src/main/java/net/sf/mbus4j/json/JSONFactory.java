package net.sf.mbus4j.json;

/*
 * #%L
 * mbus4j-core
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * #L%
 */
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.Frame.ControlCode;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.LongFrame;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.VariableLengthType;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

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
        byte[] result = new byte[(value.length() - 4) / 2];
        int resIndex = 0;
        for (int i = 3; i < value.length() - 1; i += 2) {
            result[resIndex++] = (byte) Short.parseShort(value.substring(i, i + 2), 16);
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
        return json.containsKey(key) ? (byte) Short.parseShort(json.getString(key).substring(2), 16) : defaultValue;
    }

    public static short decodeHexShort(JSONObject json, String key, short defaultValue) {
        return json.containsKey(key) ? (short) Short.parseShort(json.getString(key).substring(2), 16) : defaultValue;
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
                    switch (sendUsedDataSubtype) {
                        case SendUserData.SEND_USER_DATA_SUBTYPE:
                            return new SendUserData();
                        case ApplicationReset.SEND_USER_DATA_SUBTYPE:
                            return new ApplicationReset();
                        case SynchronizeAction.SEND_USER_DATA_SUBTYPE:
                            return new SynchronizeAction();
                        case SetBaudrate.SEND_USER_DATA_SUBTYPE:
                            return new SetBaudrate();
                        default:
                            throw new UnsupportedOperationException("Unknown Send User Data Subcode: " + sendUsedDataSubtype);
                    }
                case REQ_UD1:
                    return new RequestClassXData(ControlCode.REQ_UD1);
                case REQ_UD2:
                    return new RequestClassXData(ControlCode.REQ_UD2);
                default:
                    throw new UnsupportedOperationException("Unknown ControlCode: " + cc);

            }
        }
        throw new UnsupportedOperationException("Unknown Class");
    }

    public static Class<? extends DataBlock> getDataBlockClass(JSONObject json) {
        JSONObject drh = json.getJSONObject("drh");
        JSONObject dib = drh.getJSONObject("dib");
        VariableLengthType variableLengthType = null;
        if (dib.containsKey("variableLengthType")) {
            variableLengthType = VariableLengthType.fromLabel(dib.getString("variableLengthType"));
        }
        DataFieldCode dfc = DataFieldCode.fromLabel(dib.getString("dataFieldCode"));
        JSONObject vib = drh.getJSONObject("vib");
        Vife[] vifes = null;
        Vif vif = null;
        if (!vib.isNullObject()) {
            vif = DataBlock.vifFromJSON(vib.getJSONObject("vif"));
            if (vib.containsKey("vifes")) {
                vifes = DataBlock.vifesFromJSON(vif.getVifType(), vib.getJSONArray("vifes"));
            }
        }
        return DataBlock.getDataBlockClass(vif, vifes, dfc, variableLengthType);
    }

    public static boolean getBoolean(JSONObject json, String key, boolean defaultValue) {
        return json.containsKey(key) ? json.getBoolean(key) : defaultValue;
    }

    public static short getShort(JSONObject json, String key, short defaultValue) {
        return json.containsKey(key) ? (short) json.getInt(key) : defaultValue;
    }

    public static void readDataBlocks(LongFrame longFrame, JSONObject json) {
        JSONArray jsonDataBlocks = json.getJSONArray("dataBlocks");

        for (int i = 0; i < jsonDataBlocks.size(); i++) {
            DataBlock db;
            try {
                db = JSONFactory.getDataBlockClass(jsonDataBlocks.getJSONObject(i)).newInstance();
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            }
            db.fromJSON(jsonDataBlocks.getJSONObject(i));
            longFrame.addDataBlock(db);
        }
    }
}
