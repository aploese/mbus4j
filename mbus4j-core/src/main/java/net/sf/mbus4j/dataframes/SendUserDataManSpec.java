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
public class SendUserDataManSpec
        implements SendUserDataFrame {

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
    @Override
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
    @Override
    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
