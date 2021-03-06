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
public class SendInitSlave implements ShortFrame<SingleCharFrame> {

    private byte address;

    public SendInitSlave() {
    }

    public SendInitSlave(byte address) {
        this.address = address;
    }

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_NKE;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFcb() {
        return false;
    }

    @Override
    public void setFcv(boolean fcv) {
        if (fcv) {
            throw new IllegalArgumentException("Can't set fcv to true!");
        }
    }

    @Override
    public boolean isFcv() {
        return false;
    }

    @Override
    public void setFcb(boolean fcb) {
        if (fcb) {
            throw new IllegalArgumentException("Can't set fcb to true!");
        }
    }

}
