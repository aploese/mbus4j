/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class ShortDataBlock extends DataBlock {

    private short value = 0;

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
        return value;
    }

    @Override
    public String getValueAsString() {
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
}
