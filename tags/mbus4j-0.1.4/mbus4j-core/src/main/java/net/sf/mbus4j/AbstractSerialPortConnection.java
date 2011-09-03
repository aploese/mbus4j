/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
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
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public abstract class AbstractSerialPortConnection extends Connection {

    public static String parityToString(int parity) {
        switch (parity) {
            case SerialPort.PARITY_NONE:
                return "none";
            case SerialPort.PARITY_ODD:
                return "odd";
            case SerialPort.PARITY_EVEN:
                return "even";
            case SerialPort.PARITY_MARK:
                return "mark";
            case SerialPort.PARITY_SPACE:
                return "space";
            default:
                throw new IllegalArgumentException(String.format("Can't convert %d to parity", parity));
        }
    }

    public static int stringToParity(String parity) {
        if ("none".equals(parity)) {
            return SerialPort.PARITY_NONE;
        }
        if ("odd".equals(parity)) {
            return SerialPort.PARITY_ODD;
        }
        if ("even".equals(parity)) {
            return SerialPort.PARITY_EVEN;
        }
        if ("mark".equals(parity)) {
            return SerialPort.PARITY_MARK;
        }
        if ("space".equals(parity)) {
            return SerialPort.PARITY_SPACE;
        }
        throw new IllegalArgumentException(String.format("Can't convert %s to parity", parity));
    }

    public static String stopBitsToString(int stopBits) {
        switch (stopBits) {
            case SerialPort.STOPBITS_1:
                return "1";
            case SerialPort.STOPBITS_2:
                return "2";
            case SerialPort.STOPBITS_1_5:
                return "1.5";
            default:
                throw new IllegalArgumentException(String.format("Can't convert %d to stopBits", stopBits));
        }
    }

    public static int stringToStopBits(String stopBits) {
        if ("1".equals(stopBits)) {
            return SerialPort.STOPBITS_1;
        }
        if ("2".equals(stopBits)) {
            return SerialPort.STOPBITS_2;
        }
        if ("1.5".equals(stopBits)) {
            return SerialPort.STOPBITS_1_5;
        }
        throw new IllegalArgumentException(String.format("Can't convert %s to stopBits", stopBits));
    }

    public static String flowControlToString(int flowControl) {
        if (flowControl == 0) {
            return "none";
        }
        StringBuilder sb = new StringBuilder();
        if ((flowControl & (SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT)) == (SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT)) {
            sb.append("RTS|CTS");
        } else {
            if ((flowControl & SerialPort.FLOWCONTROL_RTSCTS_IN) == SerialPort.FLOWCONTROL_RTSCTS_IN) {
                sb.append("RTS|CTS inbound");
            }
            if ((flowControl & SerialPort.FLOWCONTROL_RTSCTS_OUT) == SerialPort.FLOWCONTROL_RTSCTS_OUT) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append("RTS|CTS outbound");
            }
        }
        if ((flowControl & (SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT)) == (SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT)) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append("XON|XOFF");
        } else {
            if ((flowControl & SerialPort.FLOWCONTROL_XONXOFF_IN) == SerialPort.FLOWCONTROL_XONXOFF_IN) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append("XON|XOFF inbound");
            }
            if ((flowControl & SerialPort.FLOWCONTROL_XONXOFF_OUT) == SerialPort.FLOWCONTROL_XONXOFF_OUT) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append("XON|XOFF outbound");
            }
        }
        if (sb.length() == 0) {
                throw new IllegalArgumentException(String.format("Can't convert %d to flowControl", flowControl));
        }
        return sb.toString();
    }

    public static int stringToFlowControl(String flowControl) {
        String[] worker = flowControl.split(",");
        if (worker.length == 1) {
            if ("none".equals(worker[0])) {
                return SerialPort.FLOWCONTROL_NONE;
            }
        }
        int result = 0;
        for (String val : worker) {
            if ("XON|XOFF".equals(val)) {
                result |= SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
                break;
            }
            if ("XON|XOFF inbound".equals(val)) {
                result |= SerialPort.FLOWCONTROL_XONXOFF_IN;
                break;
            }
            if ("XON|XOFF outbound".equals(val)) {
                result |= SerialPort.FLOWCONTROL_XONXOFF_OUT;
                break;
            }

            if ("RTS|CTS".equals(val)) {
                result |= SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
                break;
            }
            if ("RTS|CTS inbound".equals(val)) {
                result |= SerialPort.FLOWCONTROL_RTSCTS_IN;
                break;
            }
            if ("RTS|CTS outbound".equals(val)) {
                result |= SerialPort.FLOWCONTROL_RTSCTS_OUT;
                break;
            }
        }

        if (result == 0) {
            throw new IllegalArgumentException(String.format("Can't convert %s to flowControl", flowControl));
        }
        return result;
    }


    public static final int DEFAULT_RESPONSE_TIMEOUT_OFFSET = 50;
    public static final int DEFAULT_FLOW_CONTROL = SerialPort.FLOWCONTROL_NONE;
    public static final int DEFAULT_DATABITS = SerialPort.DATABITS_8;
    public static final int DEFAULT_STOPBITS = SerialPort.STOPBITS_1;
    public static final int DEFAULT_PARITY = SerialPort.PARITY_EVEN;
    public static final int DEFAULT_INPUTBUFFER_SIZE = 512;
    public static final int DEFAULT_OUTPUTBUFFER_SIZE = 512;
    private SerialPort sPort;
    private String portName;
    private int dataBits = DEFAULT_DATABITS;
    private int flowControl = DEFAULT_FLOW_CONTROL;
    private int stopBits = DEFAULT_STOPBITS;
    private int parity = DEFAULT_PARITY;
    private int inputBufferSize = DEFAULT_INPUTBUFFER_SIZE;
    private int outputBufferSize = DEFAULT_OUTPUTBUFFER_SIZE;

    public AbstractSerialPortConnection() {
        super(DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
    }

    public AbstractSerialPortConnection(String portName) {
        super(DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
        this.portName = portName;
    }

    public AbstractSerialPortConnection(String portName, int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
        this.portName = portName;
    }

    @Override
    public void open() throws IOException {
        try {
            setConnState(State.OPENING);
            // Obtain a CommPortIdentifier object for the port you want to open.
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
            // Open the port represented by the CommPortIdentifier object. Give
            // the open call a relatively long timeout of 30 seconds to allow
            // a different application to reliquish the port if the user
            // wants to.
            sPort = (SerialPort) portId.open(AbstractSerialPortConnection.class.getName(),
                    30000);
            sPort.setSerialPortParams(getBitPerSecond(), getDataBits(), getStopBits(), getParity());
            sPort.enableReceiveTimeout(1000);
            sPort.setInputBufferSize(getInputBufferSize());
            sPort.setOutputBufferSize(getOutputBufferSize());
            sPort.setFlowControlMode(getFlowControl());
            // load here to sppeddup furher access!!!
            is = sPort.getInputStream();
            os = sPort.getOutputStream();
            setConnState(State.OPEN);
        } catch (UnsupportedCommOperationException ex) {
            throw new RuntimeException(ex);
        } catch (PortInUseException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchPortException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        setConnState(State.CLOSING);
        sPort.close();
        setConnState(State.CLOSED);
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = super.toJSON(jsonSerializeType);
        result.accumulate("serialPort", portName);
        result.accumulate("dataBits", dataBits);
        result.accumulate("flowControl", flowControlToString(flowControl));
        result.accumulate("stopBits", stopBitsToString(stopBits));
        result.accumulate("parity", parityToString(parity));
        result.accumulate("inputBufferSize", inputBufferSize);
        result.accumulate("outputBufferSize", outputBufferSize);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        portName = json.getString("serialPort");
        dataBits = json.getInt("dataBits");
        flowControl = stringToFlowControl(json.getString("flowControl"));
        stopBits = stringToStopBits(json.getString("stopBits"));
        parity = stringToParity(json.getString("parity"));
        inputBufferSize = json.getInt("inputBufferSize");
        outputBufferSize = json.getInt("outputBufferSize");
    }
    private static final long serialVersionUID = -1;
    private static final int SERIAL_VERSION = 1;

    // Serialization for saveDataSource
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(SERIAL_VERSION);
        out.writeUTF(portName);
        out.writeInt(dataBits);
        out.writeInt(flowControl);
        out.writeInt((stopBits));
        out.writeInt(parity);
        out.writeInt(inputBufferSize);
        out.writeInt(outputBufferSize);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();
        switch (ver) {
            case 1:
                readObjectVer1(in);
                break;
        }
    }

    private void readObjectVer1(ObjectInputStream in) throws IOException {
        portName = in.readUTF();
        dataBits = in.readInt();
        flowControl = in.readInt();
        stopBits = in.readInt();
        parity = in.readInt();
        inputBufferSize = in.readInt();
        outputBufferSize = in.readInt();
    }

    /**
     * @return the portName
     */
    public String getPortName() {
        return portName;
    }

    /**
     * @param portName the portName to set
     */
    public void setPortName(String portName) {
        this.portName = portName;
    }

    /**
     * @return the dataBits
     */
    public int getDataBits() {
        return dataBits;
    }

    /**
     * @param dataBits the dataBits to set
     */
    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    /**
     * @return the flowControl
     */
    public int getFlowControl() {
        return flowControl;
    }

    /**
     * @param flowControl the flowControl to set
     */
    public void setFlowControl(int flowControl) {
        this.flowControl = flowControl;
    }

    /**
     * @return the stopBits
     */
    public int getStopBits() {
        return stopBits;
    }

    /**
     * @param stopBits the stopBits to set
     */
    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    /**
     * @return the parity
     */
    public int getParity() {
        return parity;
    }

    /**
     * @param parity the parity to set
     */
    public void setParity(int parity) {
        this.parity = parity;
    }

    /**
     * @return the inputBufferSize
     */
    public int getInputBufferSize() {
        return inputBufferSize;
    }

    /**
     * @param inputBufferSize the inputBufferSize to set
     */
    public void setInputBufferSize(int inputBufferSize) {
        this.inputBufferSize = inputBufferSize;
    }

    /**
     * @return the outputBufferSize
     */
    public int getOutputBufferSize() {
        return outputBufferSize;
    }

    /**
     * @param outputBufferSize the outputBufferSize to set
     */
    public void setOutputBufferSize(int outputBufferSize) {
        this.outputBufferSize = outputBufferSize;
    }
}
