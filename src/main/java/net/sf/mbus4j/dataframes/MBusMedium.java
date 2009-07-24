/*
 *
 * $Id: MBusMedium.java 400 2009-03-11 19:54:32Z aploese $
 * 
 * @author aploese
 * 
 */
package net.sf.mbus4j.dataframes;

/**
 *
 * @author aploese
 */
public interface MBusMedium {

    int getId();
    String name();

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
        final public int id;
        final public String mname;

        StdMedium(int id, String mname) {
            this.id = id;
            this.mname = mname;
        }

        public static MBusMedium valueOf(int id) {
            for (StdMedium medium : StdMedium.values()) {
                if (medium.id == id) {
                    return medium;
                }
            }
            return new UnknownMBusMedium(id);
        }

        @Override
        public String toString() {
            return mname;
        }

        @Override
        public int getId() {
            return id;
        }


    }

    /** Something really bad happend... Someone dont take care of Specifications....
     * 
     */
    public static class UnknownMBusMedium implements MBusMedium {

        final public int id;

        public UnknownMBusMedium(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.format("Unknown ID (0x%02X)",id);
        }

        @Override
        public String name() {
            return Integer.toString(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }
}
