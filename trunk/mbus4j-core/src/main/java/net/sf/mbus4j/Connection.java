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
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public abstract class Connection implements JSONSerializable, Serializable, Closeable {

    public static final int DEFAULT_BAUDRATE = 2400;

    /**
     * @return the connState
     */
    public State getConnState() {
        return connState;
    }

    /**
     * @param connState the connState to set
     */
    protected void setConnState(State connState) {
        this.connState = connState;
    }

    /**
     * @return the loggingStream
     */
    public OutputStream getLoggingStream() {
        return loggingStream;
    }

    /**
     * @param loggingStream the loggingStream to set
     */
    public void setLoggingStream(OutputStream loggingStream) {
        this.loggingStream = loggingStream;
    }

    public enum State {

        OPENING, OPEN, CLOSING, CLOSED
    };

    private int bitPerSecond;

    private int responseTimeOutOffset;
    protected transient InputStream is;
    protected transient OutputStream os;
    private transient State connState = State.CLOSED;
    private transient OutputStream loggingStream; 

    public Connection() {

    }

    public Connection(int bitPerSecond, int responseTimeOutOffset) {
        this.bitPerSecond = bitPerSecond;
        this.responseTimeOutOffset = responseTimeOutOffset;
    }

    public InputStream getInputStream() {
        return is;
    }

    public OutputStream getOutputStrteam() {
        return os;
    }

    /**
     * @return the bitPerSecond
     */
    public int getBitPerSecond() {
        return bitPerSecond;
    }

    /**
     * @param baudrate the bitPerSecond to set
     */
    public void setBitPerSecond(int bitPerSecond) {
        this.bitPerSecond = bitPerSecond;
    }

    /**
     * @return the responseTimeOutOffset
     */
    public int getResponseTimeOutOffset() {
        return responseTimeOutOffset;
    }

    /**
     * @param responseTimeOutOffset the responseTimeOutOffset to set
     */
    public void setResponseTimeOutOffset(int responseTimeOutOffset) {
        this.responseTimeOutOffset = responseTimeOutOffset;
    }

    public abstract void open() throws IOException;

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();

        result.accumulate("bitPerSecond", bitPerSecond);
        result.accumulate("responseTimeOutOffset", responseTimeOutOffset);

        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        bitPerSecond = json.getInt("bitPerSecond");
        responseTimeOutOffset = json.getInt("responseTimeOutOffset");
    }

    private static final long serialVersionUID = -1;
    private static final int version = 1;

    // Serialization for saveDataSource
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeInt(bitPerSecond);
        out.writeInt(responseTimeOutOffset);
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
        bitPerSecond = in.readInt();
        responseTimeOutOffset = in.readInt();
    }

    /**
     * find fields in json and create approbirate connection instance
     */
    public static Connection createFromJSON(JSONObject json) {
        Connection result = null;
        if (json.containsKey(SerialPortConnection.SERIAL_CONNECTION)) {
            result = new SerialPortConnection();
            result.fromJSON(json.getJSONObject(SerialPortConnection.SERIAL_CONNECTION));
        } else if (json.containsKey(TcpIpConnection.TCP_IP_CONNECTION)) {
            result = new TcpIpConnection();
            result.fromJSON(json.getJSONObject(TcpIpConnection.TCP_IP_CONNECTION));
        }
        return result;
    }

    public abstract String getJsonFieldName();

}
