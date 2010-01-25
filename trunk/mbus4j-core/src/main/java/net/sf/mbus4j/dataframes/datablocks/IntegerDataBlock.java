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
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class IntegerDataBlock extends DataBlock {

    private int value;

    public IntegerDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public IntegerDataBlock(DataFieldCode dif, FunctionField functionField, short subUnit, int tariff, long storageNumber, Vif vif, Vife... vifes) {
        super(dif, functionField, subUnit, tariff, storageNumber, vif, vifes);
    }

    public IntegerDataBlock(DataFieldCode dif, Vif vif) {
        super(dif, vif);
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        switch (getDataFieldCode()) {
            case _24_BIT_INTEGER:
            case _32_BIT_INTEGER:
                return Integer.toString(value);
            case _6_DIGIT_BCD:
                return String.format("%06d", value);
            case _8_DIGIT_BCD:
                return String.format("%08d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    @Override
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = super.toJSON(isTemplate);
               if (!isTemplate) {
  result.accumulate("data", getValue());
               }        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        setValue(json.getInt("data"));
    }


}
