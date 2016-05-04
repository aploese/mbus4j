package net.sf.mbus4j.encoder;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Logger;
import net.sf.mbus4j.MBusUtils;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.ControlFrame;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.LongFrame;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.ShortFrame;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.UserDataResponse.StatusCode;
import net.sf.mbus4j.dataframes.datablocks.BcdValue;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateAndTimeDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateDataBlock;
import net.sf.mbus4j.dataframes.datablocks.EnhancedIdentificationDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RawDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RealDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.StringDataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.VifAscii;
import net.sf.mbus4j.dataframes.datablocks.vif.VifManufacturerSpecific;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFB;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeFC;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeError;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeManufacturerSpecific;
import net.sf.mbus4j.dataframes.datablocks.vif.VifePrimary;
import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.log.LogUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Encoder {

    private byte[] data;
    private int currentPos;
    private static Logger log = LogUtils.getEncoderLogger();

    public byte[] encode(Frame frame) {
        if (frame instanceof SingleCharFrame) {
            return encodeFrame((SingleCharFrame) frame);
        } else if (frame instanceof ShortFrame) {
            return encodeFrame((ShortFrame) frame);
        } else if (frame instanceof ControlFrame) {
            return encodeFrame((ControlFrame) frame);
        } else if (frame instanceof LongFrame) {
            return encodeFrame((LongFrame) frame);
        } else {
            return null;
        }
    }

    public byte[] encodeFrame(ControlFrame frame) {
        initFrame(frame);
        pushCField(frame);
        pushAField(frame);
        pushCIField(frame);
        writeChecksumAndStop(4, 7);
        return data;
    }

    public byte[] encodeFrame(LongFrame frame) {
        initFrame(frame);
        pushCField(frame);
        pushAField(frame);
        pushCIField(frame);
        if (frame instanceof ApplicationReset) {
            pushApplicationResetData((ApplicationReset) frame);
        } else if (frame instanceof GeneralApplicationError) {
            pushGeneralApplicationError((GeneralApplicationError) frame);
        } else if (frame instanceof UserDataResponse) {
            pushVariableDataStructure((UserDataResponse) frame);
        } else if (frame instanceof SelectionOfSlaves) {
            pushSelectionOfSlavesDataHeader((SelectionOfSlaves) frame);
            pushVariableDataBlocks(frame);
        } else if (frame instanceof SendUserData) {
            pushVariableDataBlocks(frame);
        } else {
            throw new RuntimeException("encode long frame" + frame.getClass().getName());
        }
        writeLenght((byte) (currentPos - 4));
        writeChecksumAndStop(4, currentPos);
        return data;
    }

    public byte[] encodeFrame(ShortFrame frame) {
        initFrame(frame);
        pushCField(frame);
        pushAField(frame);
        writeChecksumAndStop(1, 3);
        return data;
    }

    public byte[] encodeFrame(SingleCharFrame frame) {
        initFrame(frame);
        return data;
    }

    private void initFrame(Frame frame) {
        if (frame instanceof SingleCharFrame) {
            data = new byte[]{(byte) 0xe5};
            currentPos = -1;
        } else if (frame instanceof ShortFrame) {
            data = new byte[5];
            data[0] = 0x10;
            currentPos = 1;
        } else if (frame instanceof ControlFrame) {
            data = new byte[9];
            data[0] = 0x68;
            data[1] = 0x03;
            data[2] = 0x03;
            data[3] = 0x68;
            currentPos = 4;
        } else if (frame instanceof LongFrame) {
            data = new byte[0xFF];
            data[0] = 0x68;
            data[3] = 0x68;
            currentPos = 4;
        } else {
            throw new RuntimeException("Unknown frame");
        }
    }

    private boolean needDIFE(DataBlock db, int index) {
        return ((db.getStorageNumber() >> (1 + index * 4)) > 0x00)
                || (((db.getTariff() >> (index * 2)) << 0x04) > 0x00)
                || ((db.getSubUnit() >> index) > 0x00);
    }

    private boolean needVIFE(DataBlock db, int index) {
        if (db.getVifes().length == 0) {
            return db.getAction() == null ? false : index == 0;
        } else {
            return db.getVifes().length > index;
        }
    }

    private void pushAField(PrimaryAddress address) {
        data[currentPos++] = address.getAddress();
    }

    private void pushApplicationResetData(ApplicationReset applicationReset) {
        data[currentPos++] = (byte) (applicationReset.getTelegramType().id | applicationReset.getSubTelegram());
    }

    /* convert to byte nibble (LCD recomendation) */
    public int fromLcdDigit(char lcdDigit) {
        switch (lcdDigit) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'a':
            case 'A':
                return 0x0A;
            case 'b':
            case 'B':
                return 0x0B;
            case 'c':
            case 'C':
                return 0x0C;
            case ' ':
            case 'd':
            case 'D':
                return 0x0D;
            case 'e':
            case 'E':
                return 0x0E;
            case '-':
            case 'f':
            case 'F':
                return 0x0F;
            default:
                throw new RuntimeException("Should never ever happend!");
        }

    }

    private void pushBcdError(String value) {
        for (int i = value.length() - 1; i >= 0; i -= 2) {
            int v = fromLcdDigit(value.charAt(i)) | fromLcdDigit(value.charAt(i - 1)) << 4;
            data[currentPos++] = (byte) v;
        }
    }

    private void pushBcd(long value, int bcdDigits) {
        final boolean isNegative = value < 0;
        if (isNegative) {
            value = -value;
        }
        for (int i = bcdDigits / 2; i > 0; i--) {
            data[currentPos] = (byte) (value % 10);
            value /= 10;
            if ((i == 1) && (isNegative)) {
                data[currentPos++] |= (byte) (0xF0);
            } else {
                data[currentPos++] |= (byte) ((value % 10) << 4);
            }
            value /= 10;
        }
    }

    private void pushBytes(byte[] value) {
        if (value != null) {
            for (byte b : value) {
                pushInteger(b, 1);
            }
        }
    }

    private void pushCField(Frame frame) {
        switch (frame.getControlCode()) {
            case SND_NKE:
                data[currentPos] = 0x40;
                break;
            case SND_UD:
                if (frame instanceof SynchronizeAction) {
                    data[currentPos] = (byte) (0x53 + (((SynchronizeAction) frame).isFcb() ? 0x20 : 0));
                } else if (frame instanceof ApplicationReset) {
                    data[currentPos] = (byte) (0x53 + (((ApplicationReset) frame).isFcb() ? 0x20 : 0));
                } else if (frame instanceof SetBaudrate) {
                    data[currentPos] = (byte) (0x53 + (((SetBaudrate) frame).isFcb() ? 0x20 : 0));
                } else if (frame instanceof SendUserData) {
                    data[currentPos] = (byte) (0x53 + (((SendUserData) frame).isFcb() ? 0x20 : 0));
                } else if (frame instanceof SelectionOfSlaves) {
                    data[currentPos] = (byte) (0x53 + (((SelectionOfSlaves) frame).isFcb() ? 0x20 : 0));
                }

                break;
            case REQ_UD2:
                RequestClassXData req = (RequestClassXData) frame;
                data[currentPos] = (byte) (0x4B | (req.isFcb() ? 0x20 : 0) | (req.isFcv() ? 0x10 : 0));
                break;
            case REQ_UD1:
                req = (RequestClassXData) frame;
                data[currentPos] = (byte) (0x4A | (req.isFcb() ? 0x20 : 0) | (req.isFcv() ? 0x10 : 0));
                break;
            case RSP_UD:
                if (frame instanceof UserDataResponse) {
                    final UserDataResponse resp = (UserDataResponse) frame;
                    data[currentPos] = (byte) (0x08 | (resp.isAcd() ? 0x20 : 0) | (resp.isDfc() ? 0x10 : 0));
                } else if (frame instanceof GeneralApplicationError) {
                    final GeneralApplicationError gae = (GeneralApplicationError) frame;
                    data[currentPos] = (byte) (0x08 | (gae.isAcd() ? 0x20 : 0) | (gae.isDfc() ? 0x10 : 0));
                }
                break;
            default:

        }
        currentPos++;
    }

    private void pushCIField(ControlFrame frame) {
        if (frame instanceof SetBaudrate) {
            final SetBaudrate sb = (SetBaudrate) frame;
            switch (sb.getBaudrate()) {
                case 300:
                    data[currentPos] = (byte) 0xB8;
                    ;
                    break;
                case 600:
                    data[currentPos] = (byte) 0xB9;
                    break;
                case 1200:
                    data[currentPos] = (byte) 0xBA;
                    break;
                case 2400:
                    data[currentPos] = (byte) 0xBB;
                    break;
                case 4800:
                    data[currentPos] = (byte) 0xBC;
                    break;
                case 9600:
                    data[currentPos] = (byte) 0xBD;
                    break;
                case 19200:
                    data[currentPos] = (byte) 0xBE;
                    break;
                case 38400:
                    data[currentPos] = (byte) 0xBF;
                    break;

            }
        } else if (frame instanceof SynchronizeAction) {
            data[currentPos] = 0x54;
        }
        currentPos++;
    }

    private void pushCIField(LongFrame frame) {
        if (frame instanceof ApplicationReset) {
            data[currentPos] = 0x50;
        } else if (frame instanceof SendUserData) {
            data[currentPos] = 0x51;
        } else if (frame instanceof SelectionOfSlaves) {
            data[currentPos] = 0x52;
        } else if (frame instanceof GeneralApplicationError) {
            data[currentPos] = 0x70;
        } else if (frame instanceof UserDataResponse) {
            data[currentPos] = 0x72;
        }
        currentPos++;
    }

    private void pushData(DataBlock db) {
        switch (db.getDataFieldCode()) {
            case NO_DATA:
                break;
            case _8_BIT_INTEGER:
                pushInteger(((ByteDataBlock) db).getValue(), 1);
                break;
            case _16_BIT_INTEGER:
                if (db instanceof DateDataBlock) {
                    pushDate((DateDataBlock) db);
                } else {
                    pushInteger(((ShortDataBlock) db).getValue(), 2);
                }
                break;
            case _24_BIT_INTEGER:
                pushInteger(((IntegerDataBlock) db).getValue(), 3);
                break;
            case _32_BIT_INTEGER:
                if (db instanceof DateAndTimeDataBlock) {
                    pushTimeStamp((DateAndTimeDataBlock) db);
                } else {
                    pushInteger(((IntegerDataBlock) db).getValue(), 4);
                }
                break;
            case _32_BIT_REAL:
                pushInteger(Float.floatToIntBits(((RealDataBlock) db).getValue()), 4);
                break;
            case _48_BIT_INTEGER:
                pushInteger(((LongDataBlock) db).getValue(), 6);
                break;
            case _64_BIT_INTEGER:
                if (db instanceof EnhancedIdentificationDataBlock) {
                    pushEnhancedIdentificationDataBlockLong((EnhancedIdentificationDataBlock) db, currentPos);
                } else {
                    pushInteger(((LongDataBlock) db).getValue(), 8);
                }
                break;
            case SELECTION_FOR_READOUT:
                break;
            case _2_DIGIT_BCD:
                if (((ByteDataBlock) db).isBcdError()) {
                    pushBcdError(((BcdValue) db).getBcdError());
                } else {
                    pushBcd(((ByteDataBlock) db).getValue(), 2);
                }
                break;
            case _4_DIGIT_BCD:
                if (((ShortDataBlock) db).isBcdError()) {
                    pushBcdError(((BcdValue) db).getBcdError());
                } else {
                    pushBcd(((ShortDataBlock) db).getValue(), 4);
                }
                break;
            case _6_DIGIT_BCD:
                if (((IntegerDataBlock) db).isBcdError()) {
                    pushBcdError(((BcdValue) db).getBcdError());
                } else {
                    pushBcd(((IntegerDataBlock) db).getValue(), 6);
                }
                break;
            case _8_DIGIT_BCD:
                if (db instanceof EnhancedIdentificationDataBlock) {
                    pushEnhancedIdentificationDataBlockShort((EnhancedIdentificationDataBlock) db);
                } else {
                    if (((IntegerDataBlock) db).isBcdError()) {
                        pushBcdError(((BcdValue) db).getBcdError());
                    } else {
                        pushBcd(((IntegerDataBlock) db).getValue(), 8);
                    }
                }
                break;
            case VARIABLE_LENGTH:
                if (db instanceof StringDataBlock) {
                    pushString(((StringDataBlock) db).getValue());
                } else {
                    //TODO BCD
                    throw new RuntimeException("pushData variable length " + db.getClass().getName());
                }
                break;
            case _12_DIGIT_BCD:
                if (((LongDataBlock) db).isBcdError()) {
                    pushBcdError(((BcdValue) db).getBcdError());
                } else {
                    pushBcd(((LongDataBlock) db).getValue(), 12);
                }
                break;
            case SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST:
                break;
            case SPECIAL_FUNCTION_IDLE_FILLER:
                break;
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET:
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS:
                pushBytes(((RawDataBlock) db).getValue());
                break;
            default:
                throw new RuntimeException("push Data " + db.getDataFieldCode());
        }
    }

    private void pushDataInformationBlock(DataBlock db) {
        pushDIF(db);
        for (int i = 0; i < 10; i++) {
            if (!needDIFE(db, i)) {
                break;
            }
            pushDIFE(db, i);
        }
    }

    private void pushDataRecordHeader(DataBlock db) {
        pushDataInformationBlock(db);
        pushValueInformationBlock(db);
    }

    private void pushDate(DateDataBlock dateDataBlock) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateDataBlock.getValue());
        int val = cal.get(Calendar.DAY_OF_MONTH);
        val |= (((cal.get(Calendar.YEAR) - 2000) & 0x07) << 5);
        val |= (((cal.get(Calendar.YEAR) - 2000) & 0x78) << 9);
        val |= ((cal.get(Calendar.MONTH) + 1) << 8);
        pushInteger(val, 2);
    }

    private void pushDIF(DataBlock db) {
        data[currentPos] = db.getDataFieldCode().code;
        switch (db.getDataFieldCode()) {
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET:
                data[currentPos++] = 0x0F;
                break;
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS:
                data[currentPos++] = 0x1F;
                break;
            case SPECIAL_FUNCTION_IDLE_FILLER:
                data[currentPos++] = 0x2F;
                break;
            case SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST:
                data[currentPos++] = 0x7F;
                return;
            default:
                data[currentPos] |= needDIFE(db, 0) ? Decoder.EXTENTION_BIT : 0x00;
                if (db.getFunctionField() != null) {
                    data[currentPos] |= db.getFunctionField().code;
                }
                data[currentPos++] |= (db.getStorageNumber() & 0x01) << 6;
        }
    }

    private void pushDIFE(DataBlock db, int index) {
        data[currentPos] = needDIFE(db, index + 1) ? Decoder.EXTENTION_BIT : 0x00;
        data[currentPos] |= (db.getStorageNumber() >> (1 + index * 4)) & 0x0F;
        data[currentPos] |= ((db.getTariff() >> (index * 2)) << 0x04) & 0x30;
        data[currentPos++] |= ((db.getSubUnit() >> index) << 0x06) & 0x40;
    }

    private void pushEnhancedIdentificationDataBlockLong(EnhancedIdentificationDataBlock db, int currentPos) {
        pushBcd(db.getId(), 8);
        pushManufacturer(db.getMan());
        pushInteger(db.getVersion(), 1);
        pushInteger(db.getMedium().getId(), 1);
    }

    private void pushEnhancedIdentificationDataBlockShort(EnhancedIdentificationDataBlock db) {
        pushBcd(db.getId(), 8);
    }

    private void pushFixedDataHeader(UserDataResponse frame) {
        pushBcd(frame.getIdentNumber(), 8);
        pushManufacturer(frame.getManufacturer());
        data[currentPos++] = frame.getVersion();
        data[currentPos++] = (byte) frame.getMedium().getId();
        data[currentPos++] = (byte) frame.getAccessNumber();
        data[currentPos++] = StatusCode.toId(frame.getStatus());
        pushInteger(frame.getSignature(), 2);
    }

    private void pushGeneralApplicationError(GeneralApplicationError generalApplicationError) {
        data[currentPos++] = generalApplicationError.getError().id;
    }

    private void pushInteger(long value, int bytes) {
        for (int i = bytes - 1; i >= 0; i--) {
            data[currentPos++] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    private void pushManufacturer(String man) {
        pushInteger(MBusUtils.man2Short(man), 2);
    }

    private void pushObjectAction(DataBlock db) {
        pushInteger(db.getAction().id, 1);
    }

    private void pushSelectionOfSlavesDataHeader(SelectionOfSlaves frame) {
        pushInteger(frame.getBcdMaskedId(), 4);
        pushInteger(frame.getMaskedMan(), 2);
        data[currentPos++] = (byte) frame.getMaskedVersion();
        data[currentPos++] = (byte) frame.getMaskedMedium();
    }

    private void pushString(String value) {
        pushInteger(value.length(), 1);
        for (int i = value.length() - 1; i >= 0; i--) {
            pushInteger((byte) value.charAt(i), 1);
        }
    }

    private void pushTimeStamp(DateAndTimeDataBlock dateAndTimeDataBlock) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAndTimeDataBlock.getValue());
        int val = dateAndTimeDataBlock.isValid() ? 0x00 : 0x80;
        val |= dateAndTimeDataBlock.isSummerTime() ? 0x8000 : 0x00;
        val |= dateAndTimeDataBlock.isRes1() ? 0x40 : 0x00;
        val |= dateAndTimeDataBlock.isRes2() ? 0x4000 : 0x00;
        val |= dateAndTimeDataBlock.isRes3() ? 0x2000 : 0x00;
        val |= cal.get(Calendar.MINUTE);
        val |= cal.get(Calendar.HOUR_OF_DAY) << 8;
        val |= cal.get(Calendar.DAY_OF_MONTH) << 16;
        val |= (cal.get(Calendar.MONTH) + 1) << 24;
        val |= (((cal.get(Calendar.YEAR) - 2000) & 0x07) << 21) + (((cal.get(Calendar.YEAR) - 2000) & 0x78) << 25);
        pushInteger(val, 4);
    }

    private void pushValueInformationBlock(DataBlock db) {
        pushVIF(db);
        if (db.getAction() != null) {
            pushObjectAction(db);
        } else {
            for (int i = 0; i < 10; i++) {
                if (!needVIFE(db, i)) {
                    break;
                }
                pushVIFE(db, i);
            }
        }
    }

    private void pushVariableDataBlock(DataBlock db) {
        pushDataRecordHeader(db);
        pushData(db);
    }

    private void pushVariableDataBlocks(LongFrame frame) {
        for (DataBlock db : frame) {
            pushVariableDataBlock(db);
        }
    }

    private void pushVariableDataStructure(UserDataResponse frame) {
        pushFixedDataHeader(frame);
        pushVariableDataBlocks(frame);
    }

    private void pushVIF(DataBlock db) {
        if (db.getVif() == null) {
        } else if (db.getVif() instanceof VifPrimary) {
            data[currentPos] = needVIFE(db, 0) ? Decoder.EXTENTION_BIT : 0x00;
            data[currentPos++] |= ((VifPrimary) db.getVif()).getTableIndex();
        } else if (db.getVif() instanceof VifFB) {
            data[currentPos++] = (byte) 0xFB;
            data[currentPos] = needVIFE(db, 0) ? Decoder.EXTENTION_BIT : 0x00;
            data[currentPos++] |= ((VifFB) db.getVif()).getTableIndex();
        } else if (db.getVif() instanceof VifFD) {
            data[currentPos++] = (byte) 0xFD;
            data[currentPos] = needVIFE(db, 0) ? Decoder.EXTENTION_BIT : 0x00;
            data[currentPos++] |= ((VifFD) db.getVif()).getTableIndex();
        } else if (db.getVif() instanceof VifAscii) {
            data[currentPos++] = (byte) (needVIFE(db, 0) ? 0xFC : 0x7C);
            pushString(((VifAscii) db.getVif()).getValue());
        } else if (db.getVif() instanceof VifManufacturerSpecific) {
            data[currentPos] = needVIFE(db, 0) ? Decoder.EXTENTION_BIT : 0x00;
            data[currentPos++] |= 0x7F;
        } else {
            throw new RuntimeException("Unknown vif " + db.getVif());
        }
    }

    private void pushVIFE(DataBlock db, int index) {
        data[currentPos] = needVIFE(db, index + 1) ? Decoder.EXTENTION_BIT : 0x00;
        if (db.getVifes()[index] instanceof VifePrimary) {
            data[currentPos++] |= ((VifePrimary) db.getVifes()[index]).getTableIndex();
        } else if (db.getVifes()[index] instanceof VifeError) {
            data[currentPos++] |= ((VifeError) db.getVifes()[index]).getTableIndex();
        } else if (db.getVifes()[index] instanceof VifeManufacturerSpecific) {
            data[currentPos++] |= ((VifeManufacturerSpecific) db.getVifes()[index]).getVifeValue();
//        } else if (db.getVifes().get(index) instanceof VifeObjectAction) {
            //TODO
        }
    }

    private void writeChecksumAndStop(int start, int chIndex) {
        data[chIndex] = 0;
        for (int i = start; i < chIndex; i++) {
            data[chIndex] += data[i];
        }
        data[chIndex + 1] = 0x16;
        data = Arrays.copyOf(data, chIndex + 2);
    }

    private void writeLenght(byte i) {
        data[1] = i;
        data[2] = i;
    }
}
