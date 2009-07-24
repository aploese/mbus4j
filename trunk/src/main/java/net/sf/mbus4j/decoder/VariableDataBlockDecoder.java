/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.decoder;

import net.sf.mbus4j.dataframes.datablocks.vif.AsciiVif;
import net.sf.mbus4j.dataframes.datablocks.vif.ManufacturerSpecificVif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.NotSupportedException;
import net.sf.mbus4j.dataframes.LongFrame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.BigDecimalDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateAndTimeDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.EmptyDataBlock;
import net.sf.mbus4j.dataframes.datablocks.EnhancedIdentificationDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.ObjectAction;
import net.sf.mbus4j.dataframes.datablocks.RawDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ReadOutDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RealDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.StringDataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement;
import net.sf.mbus4j.dataframes.datablocks.VariableLengthDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFB;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeError;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class VariableDataBlockDecoder {

    private final static Logger log = LoggerFactory.getLogger(VariableDataBlockDecoder.class);
    public VariableDataBlockDecoder() {
        super();
        setState(DecodeState.WAIT_FOR_INIT);
    }

    public void setState(DecodeState ds) {
       DecodeState oldState = this.ds;
       this.ds = ds;
        if (log.isDebugEnabled()) {
            log.debug(String.format("DecodeState change from: %20s => %s", oldState, ds));
        }
    }

    public DataBlock getDataBlock() {
        return db;
    }

    public void init(LongFrame frame) {
        db = null;
        ds = DecodeState.DIF;
        this.frame = frame;
        stack.clear();
    }

    public DecodeState getState() {
        return ds;
    }

    private void goFromVifOrVife(final byte b) {
        if ((b & PacketParser.EXTENTIONS_BIT) == PacketParser.EXTENTIONS_BIT) {
            setState(DecodeState.VIFE);
        } else {
            if (db instanceof ReadOutDataBlock) {
                setState(DecodeState.RESULT_AVAIL);
            } else {
                startCollectingValue();
            }
        }
    }

    private void decodeValueFromStack() {

        switch (db.getDataFieldCode()) {
            case NO_DATA:
                break;
            case _8_BIT_INTEGER:
                ((ByteDataBlock) db).setValue(stack.popByte());
                break;
            case _2_DIGIT_BCD:
                ((ByteDataBlock) db).setValue(stack.popBcdByte());
                break;
            case _16_BIT_INTEGER:
                if (db instanceof DateDataBlock) {
                    ((DateDataBlock) db).setValue(stack.popDate());
                } else {
                    ((ShortDataBlock) db).setValue(stack.popShort());
                }
                break;
            case _4_DIGIT_BCD:
                ((ShortDataBlock) db).setValue(stack.popBcdShort(4));
                break;
            case _24_BIT_INTEGER:
                ((IntegerDataBlock) db).setValue(stack.popInteger(3));
                break;
            case _6_DIGIT_BCD:
                ((IntegerDataBlock) db).setValue(stack.popBcdInteger(6));
                break;
            case _32_BIT_INTEGER:
                if (db instanceof DateAndTimeDataBlock) {
                    DateAndTimeDataBlock d = ((DateAndTimeDataBlock) db);
                    d.setValid(stack.peekIsTimestampValid());
                    d.setSummerTime(stack.peekIsTimestampSummertime());
                    d.setRes1(stack.peekIsTimestampRes1());
                    d.setRes2(stack.peekIsTimestampRes2());
                    d.setRes3(stack.peekIsTimestampRes3());
                    d.setValue(stack.popTimeStamp());
                } else {
                    ((IntegerDataBlock) db).setValue(stack.popInteger());
                }
                break;
            case _8_DIGIT_BCD:
                if (db instanceof EnhancedIdentificationDataBlock) {
                    ((EnhancedIdentificationDataBlock) db).setId(stack.popBcdInteger(8));
                } else {
                    ((IntegerDataBlock) db).setValue(stack.popBcdInteger(8));
                }
                break;
            case _32_BIT_REAL:
                ((RealDataBlock) db).setValue(stack.popFloat());
                break;
            case _48_BIT_INTEGER:
                ((LongDataBlock) db).setValue(stack.popLong(6));
                break;
            case _12_DIGIT_BCD:
                ((LongDataBlock) db).setValue(stack.popBcdLong(12));
                break;
            case _64_BIT_INTEGER:
                if (db instanceof EnhancedIdentificationDataBlock) {
                    decodeEnhancedIdentificationDataBlock();
                } else {
                    ((LongDataBlock) db).setValue(stack.popLong());
                }
                break;
            case VARIABLE_LENGTH:
                if (db instanceof RawDataBlock) {
                    ((RawDataBlock) db).setValue(stack.popBytes());
                } else if (db instanceof StringDataBlock) {
                    ((StringDataBlock) db).setValue(stack.popString());
                } else {
                    // TODO BIG DECIMAL and binray
                    throw new RuntimeException("decode variable lenght " + db.getClass().getName());
                }
                break;
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET:
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS:
                ((RawDataBlock) db).setValue(stack.popBytes());
                break;
            default:
                throw new RuntimeException("decode data value" + db.getDataFieldCode());
        }
        stack.clear();
        setState(DecodeState.RESULT_AVAIL);
    }

    private void decodeEnhancedIdentificationDataBlock() {
        final EnhancedIdentificationDataBlock b = (EnhancedIdentificationDataBlock) db;
        b.setMedium(MBusMedium.StdMedium.valueOf(stack.popByte()));
        b.setVersion(stack.popByte());
        b.setMan(stack.popMan());
        b.setId(stack.popBcdInteger(8));
    }

    public enum DecodeState {

        WAIT_FOR_INIT,
        DIF, DIFE,
        VIF, VIFE_FB, VIFE_FD, VIFE, ASCII_VIF_LENGTH, ASCII_VIF_COLLECT, COLLECT_MANUFACTURER_SPECIFIC_VIFE,
        SET_VARIABLE_LENGTH,
        COLLECTING_VALUE,
        ERROR,
        RESULT_AVAIL;
    }
    private DecodeState ds;
    private int difePos;
    private DataBlock db;
    private LongFrame frame;
    private Stack stack = new Stack();

    public DecodeState addByte(byte b, int bytesLeft) {
        switch (ds) {
            case DIF:
                decodeDIF(b, bytesLeft);
                difePos = 0;
                break;
            case DIFE:
                decodeDIFE(b, difePos++);
                break;
            case VIF:
                decodeVIF(b);
                break;
            case ASCII_VIF_LENGTH:
                stack.init(b & 0xFF);
                setState(DecodeState.ASCII_VIF_COLLECT);
                break;
            case COLLECT_MANUFACTURER_SPECIFIC_VIFE:
                ((ManufacturerSpecificVif) db.getVif()).addVIFE(b);
                if ((b & PacketParser.EXTENTIONS_BIT) == 0x00) {
                    startCollectingValue();
                }
                break;
            case ASCII_VIF_COLLECT:
                stack.push(b);
                if (stack.isFull()) {
                    ((AsciiVif) db.getVif()).setValue(stack.popString());
                    startCollectingValue();
                }
                break;
            case VIFE_FB:
                decodeVifExtention_FB(b);
                break;
            case VIFE_FD:
                decodeVifExtention_FD(b);
                break;
            case VIFE:
                decodeVIFE(b);
                break;
            case COLLECTING_VALUE:
                stack.push(b);
                if (stack.isFull()) {
                    decodeValueFromStack();
                }
                break;
            case SET_VARIABLE_LENGTH:
                if ((b & 0xFF) < 0xBF) {
                    db = new StringDataBlock(db);
                    startCollectingValue((b & 0xFF));
                } else if ((b & 0xFF) < 0xCF) {
                    db = new BigDecimalDataBlock(db);
                    startCollectingValue((b & 0xFF) - 0xC0);
                } else if ((b & 0xFF) < 0xDF) {
                    db = new BigDecimalDataBlock(db);
                    startCollectingValue((b & 0xFF) - 0xD0);
                } else if ((b & 0xFF) < 0xEF) {
                    throw new DecodeException("binary number ???? how to decode");
                } else if ((b & 0xFF) < 0xFA) {
                    throw new DecodeException("floating point to be defined");
                } else {
                    throw new DecodeException(String.format("reserved: 0x%02x ", b & 0xFF));
                }
                break;
            default:
                log.error("Unknown state: " + ds);
                setState(DecodeState.ERROR);
        }
        return ds;
    }

    private void startCollectingValue(int bytesLeft) {
        stack.init(bytesLeft);
        setState(DecodeState.COLLECTING_VALUE);
    }

    private void startCollectingValue() {
        switch (db.getDataFieldCode()) {
            case NO_DATA:
                stack.clear();
                setState(DecodeState.RESULT_AVAIL);
                return;
            case _8_BIT_INTEGER:
            case _2_DIGIT_BCD:
                stack.init(1);
                break;
            case _16_BIT_INTEGER:
            case _4_DIGIT_BCD:
                stack.init(2);
                break;
            case _24_BIT_INTEGER:
            case _6_DIGIT_BCD:
                stack.init(3);
                break;
            case _32_BIT_INTEGER:
            case _8_DIGIT_BCD:
            case _32_BIT_REAL:
                stack.init(4);
                break;
            case _48_BIT_INTEGER:
            case _12_DIGIT_BCD:
                stack.init(6);
                break;
            case _64_BIT_INTEGER:
                stack.init(8);
                break;
            case VARIABLE_LENGTH:
                setState(DecodeState.SET_VARIABLE_LENGTH);
                return;
            default:
                throw new RuntimeException("START COLLECTING VALUE" + db.getDataFieldCode());
        }

        setState(DecodeState.COLLECTING_VALUE);

    }

    /**
     *
     * @param dataField lengt encoding see table 5 chapter 6.3
     */
    private void createDataBlock(final byte dataField) {
        switch (dataField) {
            case 0x00:
                db = new EmptyDataBlock(DataFieldCode.NO_DATA);
                break;
            case 0x01:
                db = new ByteDataBlock(DataFieldCode._8_BIT_INTEGER);
                break;
            case 0x02:
                db = new ShortDataBlock(DataFieldCode._16_BIT_INTEGER);
                break;
            case 0x03:
                db = new IntegerDataBlock(DataFieldCode._24_BIT_INTEGER);
                break;
            case 0x04:
                db = new IntegerDataBlock(DataFieldCode._32_BIT_INTEGER);
                break;
            case 0x05:
                db = new RealDataBlock(DataFieldCode._32_BIT_REAL);
                break;
            case 0x06:
                db = new LongDataBlock(DataFieldCode._48_BIT_INTEGER);
                break;
            case 0x07:
                db = new LongDataBlock(DataFieldCode._64_BIT_INTEGER);
                break;
            case 0x08:
                db = new ReadOutDataBlock(DataFieldCode.SELECTION_FOR_READOUT);
                break;
            case 0x09:
                db = new ByteDataBlock(DataFieldCode._2_DIGIT_BCD);
                break;
            case 0x0A:
                db = new ShortDataBlock(DataFieldCode._4_DIGIT_BCD);
                break;
            case 0x0B:
                db = new IntegerDataBlock(DataFieldCode._6_DIGIT_BCD);
                break;
            case 0x0C:
                db = new IntegerDataBlock(DataFieldCode._8_DIGIT_BCD);
                break;
            case 0x0D:
                db = new VariableLengthDataBlock(DataFieldCode.VARIABLE_LENGTH);
                break;
            case 0x0E:
                db = new LongDataBlock(DataFieldCode._12_DIGIT_BCD);
                break;
            case 0x0F:
                setState(DecodeState.ERROR);
                throw new NotSupportedException("data field 0x0f not supported");
            default:
                setState(DecodeState.ERROR);
                throw new NotSupportedException(String.format("data field 0x%02x not supported", dataField));
        }
    }

    private void decodeDIFE(final byte b, int dFIEIndex) {
        db.setStorageNumber(db.getStorageNumber() | ((long) (b & 0x0F) << (1 + dFIEIndex * 4)));
        db.setTariff(db.getTariff() | (((b >> 4) & 0x03) << (dFIEIndex * 2)));
        db.setSubUnit((short) (db.getSubUnit() | ((b >> 6) & 0x01) << dFIEIndex));
        if ((b & PacketParser.EXTENTIONS_BIT) != PacketParser.EXTENTIONS_BIT) {
            setState(DecodeState.VIF);
        }
    }

    private void decodeVifExtention_FB(final byte b) {
        // Extended VID chapter 8.4.4 table b
        db.setVif(VifFB.valueOfTableIndex((byte) (b & PacketParser.EXTENTIONS_BIT_MASK)));
        goFromVifOrVife(b);
    }

    private void decodeVifExtention_FD(final byte b) {
        // Extended VID chapter 8.4.4 table a
        db.setVif(VifFD.valueOfTableIndex((byte) (b & PacketParser.EXTENTIONS_BIT_MASK)));
        goFromVifOrVife(b);
    }

    /**
     * see chapter 8.4.3
     * @param b
     * b will be expanded to int, so clear the sign, wich will be nagative in the case extention bit is set
     */
    private void decodeVIF(final byte b) {
        switch (b & PacketParser.EXTENTIONS_BIT_MASK) {
            case 0x7B:
                // decode vife table 8.4.4 b
                setState(DecodeState.VIFE_FB);
                return;
            case 0x7C:
                stack.clear();
                db.setVif(new AsciiVif());
                setState(DecodeState.ASCII_VIF_LENGTH);
                return;
            case 0x7D:
                // decode vife table 8.4.4 a
                setState(DecodeState.VIFE_FD);
                return;
            case 0x7E:
                db.setVif(VifStd.READOUT_SELECTION);
                break;
            case 0x7F:
                db.setVif(new ManufacturerSpecificVif(b));
                if ((b & PacketParser.EXTENTIONS_BIT) == PacketParser.EXTENTIONS_BIT) {
                    setState(DecodeState.COLLECT_MANUFACTURER_SPECIFIC_VIFE);
                    return;
                }
                break;
            default:
                db.setVif(VifStd.valueOfTableIndex((byte) (b & PacketParser.EXTENTIONS_BIT_MASK)));
                if (UnitOfMeasurement.DATE.equals(db.getUnitOfMeasurement())) {
                    db = new DateDataBlock(db);
                } else if (UnitOfMeasurement.TIME_AND_DATE.equals(db.getUnitOfMeasurement())) {
                    db = new DateAndTimeDataBlock(db);
                } else if (VifStd.ENHANCED_IDENTIFICATION_RECORD.equals(db.getVif())) {
                    db = new EnhancedIdentificationDataBlock(db);
                }
        }
        goFromVifOrVife(b);
    }

    private void decodeVIFE(final byte b) {
        // Extended VID chapter 8.4.5
        switch (frame.getControlCode()) {
            case RSP_UD:
                // Error Codes  Table 7 Chapter 6.6 0x00 to
                Vife vife = VifeStd.valueOfTableIndex((byte) (b & PacketParser.EXTENTIONS_BIT_MASK));
                if (vife == null) {
                    vife = VifeError.valueOfTableIndex((byte) (b & PacketParser.EXTENTIONS_BIT_MASK));
                }
                //TODO Throw unknown ... 
                if (vife instanceof VifeStd) {
                    switch ((VifeStd) vife) {
                        case START_DATE_TIME_OF:
                        case TIMESTAMP_OF_BEGIN_FIRST_LOWER:
                        case TIMESTAMP_OF_END_FIRST_LOWER:
                        case TIMESTAMP_BEGIN_LAST_LOWER:
                        case TIMESTAMP_END_LAST_LOWER:
                        case TIMESTAMP_BEGIN_FIRST_UPPER:
                        case TIMESTAMP_END_FIRST_UPPER:
                        case TIMESTAMP_BEGIN_LAST_UPPER:
                        case TIMESTAMP_END_LAST_UPPER:
                            db = new DateAndTimeDataBlock(db);
                            break;
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
                            db = new DateAndTimeDataBlock(db);
                            break;

                        default:
                    }
                }
                db.addVife(vife);
                break;
            case SND_UD:
                db.setObjectAction(ObjectAction.valueOf(b));

                break;
            default:
                setState(DecodeState.ERROR);
                throw new NotSupportedException(String.format("Dont know how to handele Control code %s ", frame.getControlCode()));
        }
        goFromVifOrVife(b);
    }

    private void decodeDIF(final byte b, int bytesLeft) {
        switch (b & 0xFF) {
            case 0x0F:
                db = new RawDataBlock(DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET);
                startCollectingValue(bytesLeft);
                return;
            case 0x1F:
                db = new RawDataBlock(DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS);
                if (bytesLeft == 0) {
                    setState(DecodeState.RESULT_AVAIL);
                } else {
                    startCollectingValue(bytesLeft);
                }
                frame.setLastPackage(false);
                return;
            case 0x2F:
                // Skip idlefiller next byte is DIF
                return;
            case 0x3F:
            case 0x4F:
            case 0x5F:
            case 0x6F:
                setState(DecodeState.ERROR);
                throw new DecodeException("reserved");  // Reserverd,
            case 0x7F:
                db = new ReadOutDataBlock(DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST);
                break;
            default:
                createDataBlock((byte) (b & 0x0F));
                switch ((b >> 4) & 0x03) {
                    case 0x00:
                        db.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
                        break;
                    case 0x01:
                        db.setFunctionField(FunctionField.MAXIMUM_VALUE);
                        break;
                    case 0x02:
                        db.setFunctionField(FunctionField.MINIMUM_VALUE);
                        break;
                    case 0x03:
                        db.setFunctionField(FunctionField.VALUE_DURING_ERROR_STATE);
                        break;
                    default:
                        throw new NotSupportedException("Function field");
                }
        }
        if (!DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST.equals(db.getDataFieldCode())) {
            db.setStorageNumber((b >> 6) & 0x01);
        }
        if ((b & PacketParser.EXTENTIONS_BIT) == PacketParser.EXTENTIONS_BIT) {
            setState(DecodeState.DIFE);
        } else {
            if (bytesLeft == 0) {
                setState(DecodeState.RESULT_AVAIL);
            } else {
                setState(DecodeState.VIF);
            }
        }

    }
}
