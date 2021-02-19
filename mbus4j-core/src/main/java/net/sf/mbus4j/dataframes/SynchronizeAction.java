/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package net.sf.mbus4j.dataframes;

import net.sf.json.JSONObject;

import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SynchronizeAction
        implements SendUserDataFrame {

    public static final String SEND_USER_DATA_SUBTYPE = "synchronize action";
    private byte address;
    private boolean fcb;

    public SynchronizeAction(SendUserData old) {
        this.address = old.getAddress();
        this.fcb = old.isFcb();
    }

    public SynchronizeAction() {
    }

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_UD;
    }

    /**
     * @return the fcb
     */
    @Override
    public boolean isFcb() {
        return fcb;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    /**
     * @param fcb the fcb to set
     */
    @Override
    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("fcb = ").append(isFcb()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));

        return sb.toString();
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        result.accumulate("controlCode",
                getControlCode());
        result.accumulate("subType", SEND_USER_DATA_SUBTYPE);

        if (JsonSerializeType.ALL == jsonSerializeType) {
            result.accumulate("fcb",
                    isFcb());
            result.accumulate("address", address & 0xFF);
        }

        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        fcb = json.getBoolean("fcb");
        address = (byte) json.getInt("address");
    }
}
