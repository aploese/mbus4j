/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author Arne Plöse
 */
public class VifeManufacturerSpecific implements Vife {

    private byte vifeValue;

    public VifeManufacturerSpecific(byte vifeValue) {
        this.vifeValue = vifeValue;
    }

    @Override
    public String getLabel() {
        return String.format("%02X", vifeValue);
    }

    @Override
    public VifeTypes getVifeType() {
        return VifeTypes.MAN_SPEC;
    }

    /**
     * @return the vifeValue
     */
    public byte getVifeValue() {
        return vifeValue;
    }

    /**
     * @param vifeValue the vifeValue to set
     */
    public void setVifeValue(byte vifeValue) {
        this.vifeValue = vifeValue;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof VifeManufacturerSpecific) {
            return ((VifeManufacturerSpecific) other).vifeValue == vifeValue;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.vifeValue;
        return hash;
    }

}
