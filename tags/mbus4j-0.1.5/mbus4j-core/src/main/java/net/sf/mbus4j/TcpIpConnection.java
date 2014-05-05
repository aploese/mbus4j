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
import java.net.Socket;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public class TcpIpConnection extends Connection {

    public static final int DEFAULT_RESPONSE_TIMEOUT_OFFSET = 600;
    static final String TCP_IP_CONNECTION = "tcpIpConnection";
    private transient Socket socket;
    private String host;
    private int port;

    public TcpIpConnection() {
        super(Connection.DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
    }

    public TcpIpConnection(String host, int port) {
        super(Connection.DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
        this.host = host;
        this.port = port;
    }

    public TcpIpConnection(String host, int port, int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
        this.host = host;
        this.port = port;
    }

    @Override
    public void open() throws IOException {
        setConnState(State.OPENING);
        socket = new Socket(host, port);
        // load here to sppeddup furher access!!!
        is = socket.getInputStream();
        os = socket.getOutputStream();
        setConnState(State.OPEN);
    }

    @Override
    public void close() throws IOException {
        setConnState(State.CLOSING);
        socket.close();
        setConnState(State.CLOSED);
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = super.toJSON(jsonSerializeType);
        result.accumulate("host", host);
        result.accumulate("port", port);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        host = json.getString("host");
        port = json.getInt("port");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private static final long serialVersionUID = -1;
    private static final int SERIAL_VERSION = 1;

    // Serialization for saveDataSource
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(SERIAL_VERSION);
        out.writeUTF(host);
        out.writeInt(port);
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
        host = in.readUTF();
        port = in.readInt();
    }

    @Override
    public String getJsonFieldName() {
        return TCP_IP_CONNECTION;
    }

}
