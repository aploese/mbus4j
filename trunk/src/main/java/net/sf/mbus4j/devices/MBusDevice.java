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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public abstract class MBusDevice implements Iterable<Response> {

    public enum WellKnownMan {

        ACW;
    }
    private int primaryAddress;
    private int secondaryAddress;
    private int version;
    private List<Response> resonses;
    private boolean initialized;

    public MBusDevice(int primaryAddress, int secondaryAddress, int version) {
        this.primaryAddress = primaryAddress;
        this.secondaryAddress = secondaryAddress;
        this.version = version;
    }

    public abstract String getMan();

    public abstract MBusMedium getMedium();

    /**
     * @return the primaryAddress
     */
    public int getPrimaryAddress() {
        return primaryAddress;
    }

    /**
     * @return the secondaryAddress
     */
    public int getSecondaryAddress() {
        return secondaryAddress;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public Iterator<Response> iterator() {
        return resonses.iterator();
    }

    public abstract void readValues(Map<DataTag, DataBlock> map);

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
