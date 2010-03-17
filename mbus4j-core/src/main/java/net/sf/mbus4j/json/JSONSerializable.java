/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.json;

import net.sf.json.JSONObject;

/**
 *
 * @author aploese
 */
public interface JSONSerializable {

    JSONObject toJSON(JsonSerializeType jsonSerializeType);

    void fromJSON(JSONObject json);

}
