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
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class RequestClassXData implements ShortFrame<UserDataResponse> {

    private byte address;
    private boolean fcb;
    private boolean fcv;
    private final ControlCode controlCode;

    public RequestClassXData(byte address, boolean fcb, boolean fcv, ControlCode controlCode) {
    	this.address = address;
    	this.fcb = fcb;
        this.fcv = fcv;
        this.controlCode = controlCode;
    }

    public RequestClassXData(byte address, ControlCode controlCode) {
        this(address, DEFAULT_FCB, DEFAULT_FCV, controlCode);
    }

    public RequestClassXData(boolean fcb, boolean fcv, ControlCode controlCode) {
    	this.fcb = fcb;
        this.fcv = fcv;
        this.controlCode = controlCode;
	}

	public RequestClassXData(ControlCode controlCode) {
        this.controlCode = controlCode;
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

    public void setFcv(boolean fcv) {
        this.fcv = fcv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("ifb = ").append(isFcb()).append('\n');
        sb.append("fcv = ").append(isFcv()).append('\n');
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
            result.accumulate("address", address & 0xFF);
        }
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        fcb = JSONFactory.getBoolean(json, "fcb", DEFAULT_FCB);
        fcv = JSONFactory.getBoolean(json, "fcv", DEFAULT_FCV);
        address = (byte)json.getInt("address");
    }

    @Override
    public void toggleFcb() {
        fcb = !fcb;
    }
}
