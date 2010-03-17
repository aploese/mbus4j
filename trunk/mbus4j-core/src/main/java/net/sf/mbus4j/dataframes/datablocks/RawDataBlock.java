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
package net.sf.mbus4j.dataframes.datablocks;

import net.sf.json.JSONObject;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.json.JSONFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class RawDataBlock extends DataBlock {

    private byte[] value;

    public RawDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        if (value != null) {
            for (byte b : value) {
                sb.append(String.format("%02X", b));
            }
        }
        return sb.toString();
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setValue(int... value) {
        this.value = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            this.value[i] = (byte) value[i];
        }
    }

    @Override
    public void toString(StringBuilder sb, String inset) {
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        sb.append(inset).append("value = ").append(getValueAsString()).append("\n");
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        if (getValue() != null) {
            json.accumulate("data", JSONFactory.encodeHexByteArray(getValue()));
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        if (json.containsKey("data")) {
            value = JSONFactory.decodeHexByteArray(json.getString("data"));
        } else {
            value = null;
        }
    }

    @Override
    public void setValue(String text) {
        value = new byte[text.length() / 2];
        for (int i = 0; i < text.length() / 2; i++) {
            value[i] = (byte)Integer.parseInt(text.substring(i* 2, i * 2 +1), 16);
        }

    }
}
