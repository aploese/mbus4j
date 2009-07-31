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
package net.sf.mbus4j.dataframes.datablocks.vif;

import java.util.Arrays;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
public class ManufacturerSpecificVif implements Vif {

    private byte[] vifes;
    final byte vife;

    public ManufacturerSpecificVif(byte b) {
        vife = b;
        vifes = new byte[0];
    }

    public void addVIFE(byte b) {
        vifes = Arrays.copyOf(vifes, vifes.length + 1);
        vifes[vifes.length - 1] = b;
    }

    @Override
    public Integer getExponent() {
        return null;
    }

    @Override
    public String getLabel() {
        return toString();
    }

    /**
     * @return the siPrefix
     */
    @Override
    public SiPrefix getSiPrefix() {
        return null;
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurement() {
        return null;
    }

    public byte[] getVifes() {
        return vifes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Manufacturer specific coding including VIFE's (VIF == 0x%02X) VIFE's = [", vife));
        for (byte b : vifes) {
            sb.append(String.format("%02X", b));
        }
        sb.append("]");
        return sb.toString();
    }
}
