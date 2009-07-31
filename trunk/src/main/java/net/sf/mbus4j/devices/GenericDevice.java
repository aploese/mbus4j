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
package net.sf.mbus4j.devices;

import java.util.Map;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
class GenericDevice extends MBusDevice {

    private final String man;
    private final MBusMedium medium;

    public GenericDevice(int primaryAddress, String man, MBusMedium medium, int version, int secondaryAddress) {
        super(primaryAddress, secondaryAddress, version);
        this.man = man;
        this.medium = medium;
    }

    @Override
    public String getMan() {
        return man;
    }

    @Override
    public MBusMedium getMedium() {
        return medium;
    }

    @Override
    public void readValues(Map<DataTag, DataBlock> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
