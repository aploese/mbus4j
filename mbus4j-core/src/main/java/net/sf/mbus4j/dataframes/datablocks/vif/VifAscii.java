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
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class VifAscii implements Vif {

    private String value;

    public VifAscii() {
    }

    public VifAscii(String value) {
        this.value = value;
    }

    @Override
    public Integer getExponent() {
        return null;
    }

    @Override
    public String getLabel() {
        return value;
    }

    @Override
    public SiPrefix getSiPrefix() {
        return null;
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurement() {
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof VifAscii) {
            return value.equals(((VifAscii) o).value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public VifTypes getVifType() {
        return VifTypes.ASCII;
    }
}
