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

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class LongDataBlock extends DataBlock {

    private long value;

    public LongDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    /**
     * @return the value
     */
    public long getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        switch (getDataFieldCode()) {
            case _48_BIT_INTEGER:
            case _64_BIT_INTEGER:
                return Long.toString(value);
            case _8_DIGIT_BCD:
                return String.format("%08d", value);
            case _12_DIGIT_BCD:
                return String.format("%012d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }
}
