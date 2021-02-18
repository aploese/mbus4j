package net.sf.mbus4j.dataframes;

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
import net.sf.json.JSONObject;

import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author Arne Plöse
 */
@Deprecated
public class ResponseFrameContainer
        implements JSONSerializable {

    public final static String DEFAULT_NAME = "default";
    private Frame selectFrame;
    private Frame requestFrame;
    private Frame responseFrame;
    private String name = DEFAULT_NAME;

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        result.accumulate("name", name);

        if (selectFrame != null) {
            result.accumulate("select",
                    selectFrame.toJSON(jsonSerializeType));
        }

        if (requestFrame != null) {
            result.accumulate("request",
                    requestFrame.toJSON(jsonSerializeType));
        }

        if (responseFrame != null) {
            result.accumulate("response",
                    responseFrame.toJSON(jsonSerializeType));
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
