/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j;

import java.io.IOException;
import java.net.Socket;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public class TcpIpConnection extends Connection {

    public static final int DEFAULT_RESPONSE_TIMEOUT_OFFSET = 600;
    private Socket socket;
    private String hostname;
    private int port;

    public TcpIpConnection() {
        super(SerialPortConnection.DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
    }

    public TcpIpConnection(String hostname, int port) {
        super(SerialPortConnection.DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
        this.hostname = hostname;
        this.port = port;
    }

    public TcpIpConnection(String hostname, int port, int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void open() throws IOException {
        setConnState(State.OPENING);
        socket = new Socket(hostname, port);
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
        result.accumulate("hostname", hostname);
        result.accumulate("port", port);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        hostname = json.getString("hostname");
        port = json.getInt("port");
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
