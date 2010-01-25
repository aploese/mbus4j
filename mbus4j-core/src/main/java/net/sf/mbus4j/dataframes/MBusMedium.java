/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.dataframes;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public interface MBusMedium {

    public static enum StdMedium implements MBusMedium {

        OTHER(0x00, "Other"),
        OIL(0x01, "Oil"),
        ELECTRICITY(0x02, "Electricity"),
        GAS(0x03, "Gas"),
        HEAT_OUTLET(0x04, "Heat/outlet"),
        STEAM(0x05, "Stream"),
        HOT_WATER(0x06, "Hot Water"),
        WATER(0x07, "Water"),
        HEAT_COST_ALLOCATOR(0x08, "Heat Cost Allocator"),
        COMPRESSED_AIR(0x09, "Compressed Air"),
        COOLING_LOAD_METER_OUTLET(0x0A, "Cooling load meter/outlet"),
        COOLING_LOAD_METER_INLET(0x0B, "Cooling load meter/inlet"),
        HEAT_INLET(0x0C, "Heat/inlet"),
        HEAT_COOLING_LOAD_METER(0x0D, "Heat/Cooling load meter"),
        BUS_SYSTEM(0x0E, "Bus/System"),
        UNKNOWN_MEDIUM(0x0F, "Unknown Medium"),
        RESERVED_0X10(0x10, "Reserved 0x10"),
        RESERVED_0X11(0x11, "Reserved 0x11"),
        RESERVED_0X12(0x12, "Reserved 0x12"),
        RESERVED_0X13(0x13, "Reserved 0x13"),
        RESERVED_0X14(0x14, "Reserved 0x14"),
        RESERVED_0X15(0x15, "Reserved 0x15"),
        COLD_WATER(0x16, "Cold Water"),
        DUAL_WATER(0x17, "Dual Water"),
        PRESSURE(0x18, "Pressure"),
        AD_CONVERTER(0x19, "A/D Converter");

        public static MBusMedium valueOf(int id) {
            for (StdMedium medium : StdMedium.values()) {
                if (medium.id == id) {
                    return medium;
                }
            }
            return new UnknownMBusMedium(id);
        }
        final public int id;
        final public String label;

        StdMedium(int id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }

        @Override
        public String getLabel() {
            return label;
        }

        public static MBusMedium fromLabel(String label) {
            for (StdMedium value : values()) {
                if (value.getLabel().equals(label)) {
                    return value;
                }
            }
            return UnknownMBusMedium.fromLabel(label);
        }
    }

    /** Something really bad happend... Someone dont take care of Specifications....
     *
     */
    public static class UnknownMBusMedium implements MBusMedium {

        public static UnknownMBusMedium fromLabel(String label) {
            String[] splitted = label.split("0x");
            return new UnknownMBusMedium(Integer.parseInt(splitted[1].substring(0, 2), 16));
        }
        final public int id;

        public UnknownMBusMedium(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String name() {
            return Integer.toString(id);
        }

        @Override
        public String toString() {
            return getLabel();
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof UnknownMBusMedium)) {
                return false;
            }
            UnknownMBusMedium o = (UnknownMBusMedium) other;
            return id == o.id;
        }

        @Override
        public String getLabel() {
            return String.format("Unknown ID (0x%02X)", id);
        }
    }

    int getId();

    String getLabel();

    String name();
}
