/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public abstract class Connection implements JSONSerializable {

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

    public enum State {OPENING, OPEN, CLOSING, CLOSED};

    private int bitPerSecond;

    private int responseTimeOutOffset;
    protected InputStream is;
    protected OutputStream os;
    private State connState = State.CLOSED;

    public Connection() {

    }

    public Connection (int bitPerSecond, int responseTimeOutOffset) {
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

    public abstract void close() throws IOException;

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

}
