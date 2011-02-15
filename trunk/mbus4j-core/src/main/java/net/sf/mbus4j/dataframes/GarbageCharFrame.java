/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public class GarbageCharFrame implements Frame {

    private final byte b;

    public GarbageCharFrame(byte b) {
        super();
        this.b = b;
    }

    @Override
    public ControlCode getControlCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the b
     */
    public byte getB() {
        return b;
    }

}
