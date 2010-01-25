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

import java.math.BigDecimal;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class BigDecimalDataBlock extends DataBlock {

    private BigDecimal value;

    public BigDecimalDataBlock(DataBlock dataBlock) {
        super(dataBlock);
    }

    BigDecimalDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    @Override
    public String getValueAsString() {
        return getValue() != null ? getValue().toPlainString() : null;
    }

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = super.toJSON(isTemplate);
         if (!isTemplate) {
       result.accumulate("data", getValue().toEngineeringString());
         }        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        setValue(new BigDecimal(json.getString("data")));
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
