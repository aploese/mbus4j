package net.sf.mbus4j.decoder;

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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.mbus4j.MBusUtils;
import net.sf.mbus4j.NotSupportedException;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.LongFrame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.ResponseFrame;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.log.LogUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Decoder {

    public enum DecodeState {

        EXPECT_START,
        LONG_LENGTH_1,
        LONG_LENGTH_2,
        START_LONG_PACK,
        C_FIELD,
        A_FIELD,
        CI_FIELD,
        APPLICATION_RESET_SUBCODE,
        GENERAL_APPLICATION_ERRORCODE,
        IDENT_NUMBER,
        MANUFACTURER,
        VERSION,
        MEDIUM,
        ACCESS_NUMBER,
        STATUS,
        SIGNATURE,
        VARIABLE_DATA_BLOCK,
        CHECKSUM,
        END_SIGN, 
        SUCCESS,
        ERROR;
    }

    private final static Logger log = LogUtils.getDecoderLogger();
    public static final byte EXTENTION_BIT = (byte) 0x80;

    public static byte[] ascii2Bytes(String s) {
        byte[] result = new byte[s.length() / 2];

        for (int i = 0; i < (s.length() / 2); i++) {
            result[i]
                    = (byte) Integer.parseInt(s.substring(i * 2, (i * 2) + 2),
                            16);
        }

        return result;
    }

    public static String bytes2Ascii(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length);

        for (byte b : byteArray) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
    private int expectedLengt;
    private byte checksum;
    private Frame parsingFrame;
    private final Stack stack = new Stack();
    private int dataPos;
    private byte start;
    private final VariableDataBlockDecoder vdbd = new VariableDataBlockDecoder();
    private DecodeState state = DecodeState.EXPECT_START;

    public Decoder() {
    }

    public Frame parse(InputStream is) throws IOException {
        reset();
        do {
        int  data = is.read();
            if (data == -1) {
                throw new EOFException("Closed");
            }
            addByte((byte)data);
        } while (state != DecodeState.SUCCESS && state != DecodeState.ERROR);
        return parsingFrame;
    } 
    
    private void addByte(final byte b) {
        checksum += b;
        dataPos++;

        if (start != 0) {
            if (expectedLengt == dataPos - 1) {
                if (state != DecodeState.CHECKSUM) {
                    throw  new DecodeException("expectedLengt reached: data discarted!",  parsingFrame);
                }
            }
        }

        switch (state) {
            case EXPECT_START:
                switch (b & 0xFF) {
                    case 0x68:
                        dataPos = -3;
                        parsingFrame = null;
                        start = b;
                        expectedLengt = 0xFF;//max possible
                        setState(DecodeState.LONG_LENGTH_1);
                        return;
                    case 0x10:
                        dataPos = -1;
                        parsingFrame = null;
                        start = b;
                        expectedLengt = 2;
                        setState(DecodeState.C_FIELD);
                        return;
                    case 0xE5:
                        parsingFrame = SingleCharFrame.SINGLE_CHAR_FRAME;
                        state = DecodeState.SUCCESS;
                        return;
                    default:
                    throw  new DecodeException("Garbage collected",  parsingFrame);
                }

            case LONG_LENGTH_1:
                expectedLengt = b & 0xFF;
                setState(DecodeState.LONG_LENGTH_2);

                return;

            case LONG_LENGTH_2:

                if (expectedLengt != (b & 0xFF)) {
                    //Try to synchronize test last byte was start.
                    if (expectedLengt == 0x68) {
                        //last byte was start
                        expectedLengt = b & 0xFF;
                        dataPos = -2;
                        return;
                    } else if (0x68 == (b & 0xFF)) {
                        //this byte is start
                        setState(DecodeState.LONG_LENGTH_1);
                        dataPos = -3;
                        return;
                    } else {
                        throw new DecodeException("got to second lengt byte, but nowhere to go!", parsingFrame);
                    }
                } else {
                    setState(DecodeState.START_LONG_PACK);
                    return;
                }

            case START_LONG_PACK:

                if (b == 0x68) {
                    setState(DecodeState.C_FIELD);
                    return;
                } else {
                    throw new DecodeException("second start byte of long/control frame mismatch: data discarted!", parsingFrame);
                }

            case C_FIELD:
                checksum = b;
                setState(DecodeState.A_FIELD);

                switch (start) {
                    case 0x10:

                        switch (b & 0xFF) {
                            case 0x40://SND_NKE
                                parsingFrame = new SendInitSlave();
                                return;
                            case 0x5A://REQ_UD1 FCB is clear
                                parsingFrame = new RequestClassXData(false, true, Frame.ControlCode.REQ_UD1);
                                return;
                            case 0x5B://REQ_UD2 FCB is clear
                                parsingFrame = new RequestClassXData(false, true, Frame.ControlCode.REQ_UD2);
                                return;
                            case 0x7A://REQ_UD1 FCB is set
                                parsingFrame = new RequestClassXData(true, false, Frame.ControlCode.REQ_UD1);
                                return;
                            case 0x7B://REQ_UD2 FCB is set
                                parsingFrame = new RequestClassXData(false, true, Frame.ControlCode.REQ_UD2);
                                return;
                            default:
                                // are we collecting garbage? - maybe try to recover.
                                throw  new DecodeException("short frame c field reached: data discarted!", parsingFrame);
                        }

                    case 0x68:

                        switch (b & 0xFF) {
                            case 0x08://RSP_UD ACD is clear DFC is clear
                                parsingFrame = new UserDataResponse(false, false);
                                return;
                            case 0x18://RSP_UD ACD is clear DFC is set
                                parsingFrame = new UserDataResponse(false, true);
                                return;
                            case 0x28://RSP_UD ACD is set DFC is clear
                                parsingFrame = new UserDataResponse(true, false);
                                return;
                            case 0x38://RSP_UD ACD is set DFC is set
                                parsingFrame = new UserDataResponse(true, true);
                                return;
                            case 0x53://SND_UD FCB is clear 
                                parsingFrame = new SendUserData(false);
                                return;
                            case 0x73://SND_UD FCB is set
                                parsingFrame = new SendUserData(true);
                                return;
                            default:
                                // are we collecting garbage? - maybe try to recover.
                                throw new DecodeException("control/long frame c field reached: data discarted!", parsingFrame);
                        }

                    default:
                        throw new DecodeException(String.format("C Field dont know where to go: %02x", b), parsingFrame);
                }

            case A_FIELD:

                try {
                    ((PrimaryAddress) parsingFrame).setAddress(b);
                } catch (ClassCastException e) {
                    throw new DecodeException("No set Address Ex:" + parsingFrame.getClass(), parsingFrame);
                }

                switch (start) {
                    case 0x10:
                        setState(DecodeState.CHECKSUM);
                        return;
                    case 0x68:
                        setState(DecodeState.CI_FIELD);
                        return;
                    default:
                        throw new DecodeException(String.format("A Field dont know where to go start: %02x", start), parsingFrame);
                }

            case CI_FIELD:
                //ControlFrame or LongFrame 
                if (parsingFrame instanceof SendUserData) {
                    decodeCiSendUserData(b & 0xFF);
                    return;
                } else if (parsingFrame instanceof UserDataResponse) {
                    decodeCiUserDataResponse(b & 0xFF);
                    return;
                } else {
                    throw new DecodeException("CI Field dont know where to go: " + parsingFrame.getClass(), parsingFrame);
                }

            case GENERAL_APPLICATION_ERRORCODE:

                try {
                    ((GeneralApplicationError) parsingFrame).setError(b);
                    setState(DecodeState.CHECKSUM);
                    return;
                } catch (ClassCastException e) {
                    throw new DecodeException("GENERAL_APPLICATION_ERRORCODE Field dont know where to go: " + parsingFrame.getClass(), parsingFrame);
                }

            case APPLICATION_RESET_SUBCODE:

                try {
                    ((ApplicationReset) parsingFrame).setTelegramTypeAndSubTelegram(b);
                    setState(DecodeState.CHECKSUM);
                    return;
                } catch (ClassCastException e) {
                    throw new DecodeException("APPLICATION_RESET_SUBCODE Field dont know where to go: " + parsingFrame.getClass(), parsingFrame);
                }

            case IDENT_NUMBER:
                stack.push(b);

                if (stack.isFull()) {
                    if (parsingFrame instanceof SelectionOfSlaves) {
                        ((SelectionOfSlaves) parsingFrame).setBcdMaskedId(stack.popInteger(4));
                    } else {
                        ((UserDataResponse) parsingFrame).setIdentNumber(stack.popBcdInteger(8));
                    }

                    stack.init(2);
                    setState(DecodeState.MANUFACTURER);
                }

                return;

            case MANUFACTURER:
                stack.push(b);

                if (stack.isFull()) {
                    if (parsingFrame instanceof SelectionOfSlaves) {
                        ((SelectionOfSlaves) parsingFrame).setMaskedMan(stack.popShort());
                    } else {
                        ((UserDataResponse) parsingFrame).setManufacturer(stack.popMan());
                    }

                    stack.clear();
                    setState(DecodeState.VERSION);
                }

                return;

            case VERSION:

                if (parsingFrame instanceof SelectionOfSlaves) {
                    ((SelectionOfSlaves) parsingFrame).setMaskedVersion((byte) (b & 0xFF));
                } else {
                	//TODO BCD???
                    ((UserDataResponse) parsingFrame).setVersion(((byte)b));
                }

                setState(DecodeState.MEDIUM);

                return;

            case MEDIUM:

                if (parsingFrame instanceof SelectionOfSlaves) {
                    ((SelectionOfSlaves) parsingFrame).setMaskedMedium((byte) (b & 0xFF));
                    setState(DecodeState.CHECKSUM);
                } else {
                    ((UserDataResponse) parsingFrame).setMedium(MBusMedium.valueOf(b));
                    setState(DecodeState.ACCESS_NUMBER);
                }

                return;

            case ACCESS_NUMBER:
                ((UserDataResponse) parsingFrame).setAccessNumber((short) (b & 0x00FF));
                setState(DecodeState.STATUS);

                return;

            case STATUS:
                ((UserDataResponse) parsingFrame).clearStatus();

                switch (b & 0x03) {
                    case 0x00:
                        ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.APPLICATION_NO_ERROR);
                        break;

                    case 0x01:
                        ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.APPLICATION_BUSY);
                        break;

                    case 0x02:
                        ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.APPLICATION_ANY_ERROR);
                        break;

                    case 0x03:
                        ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.APPLICATION_RESERVED);
                        break;
                }

                if ((b & 0x04) == 0x04) {
                    ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.POWER_LOW);
                }

                if ((b & 0x08) == 0x08) {
                    ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.PERMANENT_ERROR);
                }

                if ((b & 0x10) == 0x10) {
                    ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.TEMPORARY_ERROR);
                }

                if ((b & 0x20) == 0x20) {
                    ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.MAN_SPEC_0X20);
                }

                if ((b & 0x40) == 0x40) {
                    ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.MAN_SPEC_0X40);
                }

                if ((b & 0x80) == 0x80) {
                    ((UserDataResponse) parsingFrame).addStatus(UserDataResponse.StatusCode.MAN_SPEC_0X80);
                }

                stack.init(2);
                setState(DecodeState.SIGNATURE);

                return;

            case SIGNATURE:
                stack.push(b);

                if (stack.isFull()) {
                    ((UserDataResponse) parsingFrame).setSignature(stack.popShort());
                    stack.clear();

                    if (expectedLengt == dataPos) {
                        setState(DecodeState.CHECKSUM);
                    } else {
                        setState(DecodeState.VARIABLE_DATA_BLOCK);
                    }
                }

                return;

            case VARIABLE_DATA_BLOCK:

                if (vdbd.getState().equals(VariableDataBlockDecoder.DecodeState.WAIT_FOR_INIT)) {
                    vdbd.init(((LongFrame) parsingFrame));
                }
                try {
                    switch (vdbd.addByte(b, expectedLengt - dataPos)) {
                        case ERROR:
                            vdbd.setState(VariableDataBlockDecoder.DecodeState.WAIT_FOR_INIT);
                            return;

                        case RESULT_AVAIL:
                            ((LongFrame) parsingFrame).addDataBlock(vdbd.getDataBlock());
                            vdbd.setState(VariableDataBlockDecoder.DecodeState.WAIT_FOR_INIT);

                            if ((expectedLengt - dataPos) == 0) {
                                setState(DecodeState.CHECKSUM);
                            }
                            return;
                        default:
                            return;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw  new DecodeException("collect variable data block: data discarted!", parsingFrame);
                }

            case CHECKSUM:
                checksum -= b;

                if (checksum == b) {
                    setState(DecodeState.END_SIGN);

                    break;
                } else {
                    throw  new DecodeException("checksum mismatch: data discarted!", parsingFrame);
                }

            case END_SIGN:
                if (b == 0x16) {
                    state = DecodeState.SUCCESS;                    
                } else {
                    throw  new DecodeException("end sign not found: data discarted!", parsingFrame);
                }
                break;
            default:
                throw new DecodeException("Should never ever happen!: Unknown State!" + state, parsingFrame);
        }
    }

    private void decodeCiSendUserData(int b) {
        switch (b) {
            case 0x50:
                parsingFrame = new ApplicationReset((SendUserData) parsingFrame);
                setState(DecodeState.APPLICATION_RESET_SUBCODE);

                break;

            case 0x51:
                setState(DecodeState.VARIABLE_DATA_BLOCK);

                break;

            case 0x52:
                parsingFrame = new SelectionOfSlaves((SendUserData) parsingFrame);
                stack.init(4);
                setState(DecodeState.IDENT_NUMBER);

                break;

            case 0x54:
                parsingFrame = new SynchronizeAction((SendUserData) parsingFrame);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xB8:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 300);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xB9:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 600);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xBA:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 1200);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xBB:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 2400);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xBC:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 4800);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xBD:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 9600);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xBE:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 19200);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xBF:
                parsingFrame = new SetBaudrate((SendUserData) parsingFrame, 38400);
                setState(DecodeState.CHECKSUM);

                break;

            case 0xA0:
            case 0xA1:
            case 0xA2:
            case 0xA3:
            case 0xA4:
            case 0xA5:
            case 0xA6:
            case 0xA7:
            case 0xA8:
            case 0xA9:
            case 0xAA:
            case 0xAB:
            case 0xAC:
            case 0xAD:
            case 0xAE:
            case 0xAF:
                parsingFrame = new SendUserDataManSpec((SendUserData) parsingFrame, b);
                setState(DecodeState.CHECKSUM);

                break;

            default:
                NotSupportedException e = new NotSupportedException(String.format(
                        "CI field of SND_UD: 0x%02x | %s",
                        b,
                        parsingFrame.getClass().getName()));
                log.log(Level.SEVERE, "decodeCiSendUserData", e);
                reset();
                throw e;
        }
    }

    private void decodeCiUserDataResponse(int b) {
        switch (b) {
            case 0x70:
                parsingFrame = new GeneralApplicationError((UserDataResponse) parsingFrame);
                setState(DecodeState.GENERAL_APPLICATION_ERRORCODE);

                break;

            case 0x72:
                stack.init(4);
                setState(DecodeState.IDENT_NUMBER);

                break;

            default:
                NotSupportedException e = new NotSupportedException(String.format(
                        "CI field of UD_RESP: 0x%02x | %s",
                        b,
                        parsingFrame.getClass().getName()));
                log.log(Level.SEVERE, "decodeCiUserDataResponse", e);
                reset();
                throw e;
        }
    }

    public DecodeState getState() {
        return state;
    }

    private void setState(DecodeState state) {
        DecodeState oldState = this.state;
        this.state = state;

        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "{0} => {1}", new Object[]{oldState, state});
        }
    }

    private void reset() {
        start = 0;
        expectedLengt = 0;
        vdbd.reset();
        setState(DecodeState.EXPECT_START);
    }

}
