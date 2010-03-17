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
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class ShortDataBlock extends DataBlock {

    private short value;
    private String bcdError;

    public boolean isBcdError() {
        return bcdError != null;
    }

    public ShortDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public ShortDataBlock(DataFieldCode dif, Vif vif) {
        super(dif, vif);
    }

    /**
     * @return the value
     */
    public short getValue() {
        if (isBcdError()) {
            throw new IllegalArgumentException("No value BCD Error: " + bcdError);
        } else {
            return value;
        }
    }

    @Override
    public String getValueAsString() {
        if (bcdError != null) {
            return "BCD Error: " + bcdError;
        }
        switch (getDataFieldCode()) {
            case _16_BIT_INTEGER:
                return Short.toString(value);
            case _4_DIGIT_BCD:
                return String.format("%04d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }
    }

    /**
     * @param value the value to set
     */
    public void setValue(short value) {
        this.value = value;
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        if (!isBcdError()) {
            json.accumulate("data", getValue());
        } else {
            JSONObject jsonBcdError = new JSONObject();
            jsonBcdError.accumulate("bcdErrorCode", getBcdError());
            json.accumulate("data", jsonBcdError);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        if (json.get("data") instanceof JSONObject) {
            JSONObject data = json.getJSONObject("data");
            if (data.containsKey("bcdErrorCode")) {
                bcdError = data.getString("bcdErrorCode");
            } else {
                throw new IllegalArgumentException("Unknown value at data: " + data.toString(1));
            }
        } else {
            setValue((short) json.getInt("data"));
        }
    }

    /**
     * @return the bcdError
     */
    public String getBcdError() {
        return bcdError;
    }

    /**
     * @param bcdError the bcdError to set
     */
    public void setBcdError(String bcdError) {
        this.bcdError = bcdError;
    }

    @Override
    public void setValue(String text) {
        try {
            value = Short.parseShort(text);
            bcdError = null;
        } catch (NumberFormatException ex) {
            value = 0;
            bcdError = text;
        }
    }
}
