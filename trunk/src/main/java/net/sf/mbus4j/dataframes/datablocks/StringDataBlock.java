/*
 * mbus4j - Open source drivers for mbus protocol (www.mbus.com) - http://mbus4j.sourceforge.net/
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
public class StringDataBlock extends DataBlock {

    private String data;

    public StringDataBlock(DataBlock dataBlock) {
        super(dataBlock);
    }

    StringDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public StringDataBlock(DataFieldCode dfc, Vif vif) {
        super(dfc, vif);
    }

    public String getValue() {
        return data;
    }

    @Override
    public String getValueAsString() {
        return data;
    }

    public void setValue(String data) {
        this.data = data;
    }
}
