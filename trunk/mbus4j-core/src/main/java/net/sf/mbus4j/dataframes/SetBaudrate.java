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

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SetBaudrate implements ControlFrame {
    public static String SEND_USER_DATA_SUBTYPE = "set baudrate";

    private byte address;
    private boolean fcb;
    private int baudrate;

    public SetBaudrate(SendUserData old, int baudrate) {
        this.address = old.getAddress();
        this.fcb = old.isFcb();
        this.baudrate = baudrate;
    }

    public SetBaudrate() {
    }

    @Override
    public byte getAddress() {
        return address;
    }

    /**
     * @return the baudrate
     */
    public int getBaudrate() {
        return baudrate;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_UD;
    }

    /**
     * @return the fcb
     */
    public boolean isFcb() {
        return fcb;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    /**
     * @param baudrate the baudrate to set
     */
    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    /**
     * @param fcb the fcb to set
     */
    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isFcb = ").append(isFcb()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        sb.append(String.format("baudrate = %d\n", baudrate));
        return sb.toString();
    }

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = new JSONObject();
        result.accumulate("controlCode", getControlCode());
        result.accumulate("subType", SEND_USER_DATA_SUBTYPE);
        result.accumulate("fcb", isFcb());
        result.accumulate("address", address & 0xFF);
        result.accumulate("baudrate", baudrate);
        return result;
   }

    @Override
    public void fromJSON(JSONObject json) {
        fcb = json.getBoolean("fcb");
        address = (byte)json.getInt("address");
        baudrate = json.getInt("baudrate");
    }
}
