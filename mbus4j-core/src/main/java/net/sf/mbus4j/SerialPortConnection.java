package net.sf.mbus4j;

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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import net.sf.atmodem4j.spsw.Baudrate;
import net.sf.atmodem4j.spsw.DataBits;
import net.sf.atmodem4j.spsw.FlowControl;
import net.sf.atmodem4j.spsw.LoggingSerialPortSocket;
import net.sf.atmodem4j.spsw.Parity;
import net.sf.atmodem4j.spsw.SerialPortSocket;
import net.sf.atmodem4j.spsw.StopBits;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public class SerialPortConnection extends Connection {

    static final String SERIAL_CONNECTION = "serialConnection";

    public static final int DEFAULT_RESPONSE_TIMEOUT_OFFSET = 50;
    public static final Set<FlowControl> FLOW_CONTROL = FlowControl.getFC_NONE();
    public static DataBits DATA_BITS = DataBits.DB_8;
    public static StopBits STOP_BITS = StopBits.SB_1;
    public static Parity PARITY = Parity.EVEN;

    private String portName;
    private SerialPortSocket sPort;

    public SerialPortConnection() {
        super(DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
    }

    public SerialPortConnection(String portName) {
        super(DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
        this.portName = portName;
    }

    public SerialPortConnection(String portName, int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
        this.portName = portName;
    }

    @Override
    public void open() throws IOException {
        try {
            setConnState(State.OPENING);
            sPort = SerialPortSocket.FACTORY.createSerialPortSocket(portName);
            if (getLoggingStream() != null) {
                sPort = new LoggingSerialPortSocket(sPort, getLoggingStream());
            }
            sPort.openRaw(Baudrate.fromValue(getBitPerSecond()), DATA_BITS, STOP_BITS, PARITY, FlowControl.getFC_NONE());
            is = sPort.getInputStream();
            os = sPort.getOutputStream();

            setConnState(State.OPEN);
        } catch (UnsatisfiedLinkError | NoClassDefFoundError ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        setConnState(State.CLOSING);
        try {
            sPort.close();
        } finally {
            setConnState(State.CLOSED);
        }
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = super.toJSON(jsonSerializeType);
        result.accumulate("serialPort", portName);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        portName = json.getString("serialPort");
    }
    private static final long serialVersionUID = -1;
    private static final int SERIAL_VERSION = 2;

    // Serialization for saveDataSource
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(SERIAL_VERSION);
        out.writeUTF(portName);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();
        switch (ver) {
            case 1:
                readObjectVer1(in);
                break;
            case 2:
                readObjectVer2(in);
                break;
        }
    }

    private void readObjectVer1(ObjectInputStream in) throws IOException {
        portName = in.readUTF();
        //dataBits = 
        in.readInt();
        //flowControl = 
        in.readInt();
        //stopBits = 
        in.readInt();
        //parity = 
        in.readInt();
        in.readInt();
        in.readInt();
    }

    private void readObjectVer2(ObjectInputStream in) throws IOException {
        portName = in.readUTF();
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
    public DataBits getDataBits() {
        return DATA_BITS;
    }

    /**
     * @return the flowControl
     */
    public Set<FlowControl> getFlowControl() {
        return FLOW_CONTROL;
    }

    /**
     * @return the stopBits
     */
    public StopBits getStopBits() {
        return STOP_BITS;
    }

    /**
     * @return the parity
     */
    public Parity getParity() {
        return PARITY;
    }

    @Override
    public String getJsonFieldName() {
        return SERIAL_CONNECTION;
    }
}
