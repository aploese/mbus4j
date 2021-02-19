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
package net.sf.mbus4j.dataframes;

import java.util.Objects;

/**
 *
 * @author Arne Plöse
 */
public class DeviceId {

    public final byte version;
    public final MBusMedium medium;
    public final int identNumber;
    public final String manufacturer;
    public final byte address;

    public DeviceId(byte version, MBusMedium medium, int identNumber, String manufacturer, byte address) {
        this.version = version;
        this.medium = medium;
        this.identNumber = identNumber;
        this.manufacturer = manufacturer;
        this.address = address;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeviceId other = (DeviceId) obj;
        return equals(other.version, other.identNumber, other.manufacturer, other.medium);
    }

    public boolean equals(byte version, int identNumber, String manufacturer, MBusMedium medium) {
        if (this.version != version) {
            return false;
        }
        if (this.identNumber != identNumber) {
            return false;
        }
        if (!Objects.equals(this.manufacturer, manufacturer)) {
            return false;
        }
        return this.medium == medium;
    }

    /**
     * computes equals and if only the address differs throws an
     * IllegalArgumentException
     *
     * @param version
     * @param identNumber
     * @param manufacturer
     * @param medium
     * @param address
     * @return
     * @throws IllegalArgumentException
     */
    public boolean equalsCheckAddress(byte version, int identNumber, String manufacturer, MBusMedium medium, byte address) throws IllegalArgumentException {
        if (equals(version, identNumber, manufacturer, medium)) {
            if (this.address != address) {
                throw new IllegalArgumentException("primary Adress differs");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.version;
        hash = 83 * hash + Objects.hashCode(this.medium);
        hash = 83 * hash + this.identNumber;
        hash = 83 * hash + Objects.hashCode(this.manufacturer);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("version=0x%02x, medium = %s, identNumber = %08d, manufacturer = %s, address = 0x%02x", version, medium, identNumber, manufacturer, address);
    }

}
