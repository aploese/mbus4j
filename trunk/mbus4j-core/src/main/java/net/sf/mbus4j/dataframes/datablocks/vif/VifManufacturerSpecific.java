/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j.dataframes.datablocks.vif;

import java.util.Arrays;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class VifManufacturerSpecific implements Vif {

    public static boolean isManufacturerSecific(String label) {
        return label.startsWith("Manufacturer specific coding including VIFE's (VIF == 0x");
    }

    public static VifManufacturerSpecific fromLabel(String label) {
        if (!isManufacturerSecific(label)) {
            throw new IllegalArgumentException("label is no man. spec. data !");
        }

        String[] splitted = label.split("0x");
        VifManufacturerSpecific result = new VifManufacturerSpecific((byte)Short.parseShort(splitted[1].substring(0, 2), 16));
        return result;
    }

    final byte vifByte;

    public VifManufacturerSpecific(byte b) {
        vifByte = b;
    }

    @Override
    public Integer getExponent() {
        return null;
    }

    @Override
    public String getLabel() {
        return String.format("0x%02X", vifByte);
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

    public byte getVifByte() {
        return vifByte;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof VifManufacturerSpecific)) {
            return false;
        }
        VifManufacturerSpecific o = (VifManufacturerSpecific)other;
        return vifByte == o.vifByte;
    }

    @Override
    public VifTypes getVifType() {
        return VifTypes.MANUFACTURER_SPECIFIC;
    }
}
