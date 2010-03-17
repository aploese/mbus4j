/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes;

import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public class ResponseFrameContainer implements JSONSerializable {

    private Frame selectFrame;
    private Frame requestFrame;
    private Frame responseFrame;
    private String name = "default";

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        result.accumulate("name", name);
        if (selectFrame != null) {
            result.accumulate("select", selectFrame.toJSON(jsonSerializeType));
        }
        if (requestFrame != null) {
            result.accumulate("request", requestFrame.toJSON(jsonSerializeType));
        }
        if (responseFrame != null) {
            result.accumulate("response", responseFrame.toJSON(jsonSerializeType));
        }
        return result;
    }

    /**
     * @return the SelectFrame
     */
    public Frame getSelectFrame() {
        return selectFrame;
    }

    /**
     * @param SelectionFrames the SelectFrame to set
     */
    public void setSelectFrame(Frame selectFrame) {
        this.selectFrame = selectFrame;
    }

    /**
     * @return the requestFrame
     */
    public Frame getRequestFrame() {
        return requestFrame;
    }

    /**
     * @param requestFrame the requestFrame to set
     */
    public void setRequestFrame(Frame requestFrame) {
        this.requestFrame = requestFrame;
    }

    /**
     * @return the responseFrame
     */
    public Frame getResponseFrame() {
        return responseFrame;
    }

    /**
     * @param responseFrame the responseFrame to set
     */
    public void setResponseFrame(Frame responseFrame) {
        this.responseFrame = responseFrame;
    }

    @Override
    public void fromJSON(JSONObject json) {
        name = json.getString("name");
        if (json.containsKey("select")) {
            JSONObject jsonSelectFrame = json.getJSONObject("select");
            selectFrame = JSONFactory.createFrame(jsonSelectFrame);
            selectFrame.fromJSON(jsonSelectFrame);
        } else {
            selectFrame = null;
        }

        if (json.containsKey("request")) {
            JSONObject jsonRequestFrame = json.getJSONObject("request");
            requestFrame = JSONFactory.createFrame(jsonRequestFrame);
            requestFrame.fromJSON(jsonRequestFrame);
        } else {
            requestFrame = null;
        }

        if (json.containsKey("response")) {
            JSONObject jsonResponseFrame = json.getJSONObject("response");
            responseFrame = JSONFactory.createFrame(jsonResponseFrame);
            responseFrame.fromJSON(jsonResponseFrame);
        } else {
            responseFrame = null;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
