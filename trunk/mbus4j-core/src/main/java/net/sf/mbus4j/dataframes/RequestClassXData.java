/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009  Arne Plöse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.dataframes;

import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class RequestClassXData implements ShortFrame {

    private byte address;
    private boolean fcb;
    private boolean fcv;
    private ControlCode controlCode;

    public RequestClassXData(boolean fcb, boolean fcv, ControlCode controlCode) {
        this.fcb = fcb;
        this.fcv = fcv;
        this.controlCode = controlCode;
    }

    public RequestClassXData(ControlCode controlCode) {
        this(false, true, controlCode);
    }

    public RequestClassXData(ControlCode controlCode, byte address) {
        this(false, true, controlCode);
        this.address = address;
    }

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public ControlCode getControlCode() {
        return controlCode;
    }

    @Override
    public boolean isFcb() {
        return fcb;
    }

    @Override
    public boolean isFcv() {
        return fcv;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    @Override
    public void setFcv(boolean fcv) {
        this.fcv = fcv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isFcb = ").append(isFcb()).append('\n');
        sb.append("isFcv = ").append(isFcv()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        return sb.toString();
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        result.accumulate("controlCode", getControlCode().getLabel());
        if (JsonSerializeType.ALL == jsonSerializeType) {
            result.accumulate("fcb", isFcb());
            result.accumulate("fcv", isFcv());
            result.accumulate("address", address);
        }
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        setFcb(JSONFactory.getBoolean(json, "fcb", false));
        setFcv(JSONFactory.getBoolean(json, "fcv", false));
        setAddress(JSONFactory.decodeHexByte(json, "address", (byte)0));
    }
}
