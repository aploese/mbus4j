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
public class SendInitSlave implements ShortFrame {

    private byte address;

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_NKE;
    }

    @Override
    public boolean isFcb() {
        return false;
    }

    @Override
    public boolean isFcv() {
        return false;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public void setFcb(boolean fcb) {
        throw new UnsupportedOperationException("set FCB not supported.");
    }

    @Override
    public void setFcv(boolean fcv) {
        throw new UnsupportedOperationException("set FCV not supported.");
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
