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
public class SendUserDataManSpec implements ControlFrame {

    private byte address;
    private boolean fcb;
    private int ciField;

    public SendUserDataManSpec(SendUserData sendUserData, int ciField) {
        this.ciField = ciField;
        this.address = sendUserData.getAddress();
        this.fcb = sendUserData.isFcb();
    }

    /**
     * @return the address
     */
    @Override
    public byte getAddress() {
        return address;
    }

    /**
     * @return the ciField
     */
    public int getCiField() {
        return ciField;
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

    /**
     * @param address the address to set
     */
    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    /**
     * @param ciField the ciField to set
     */
    public void setCiField(int ciField) {
        this.ciField = ciField;
    }

    /**
     * @param fcb the fcb to set
     */
    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
