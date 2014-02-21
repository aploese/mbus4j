package net.sf.mbus4j.dataframes;

/*
 * #%L
 * mbus4j-core
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
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
 * #L%
 */
/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum MBusMedium {

    OTHER(0x00, "Other"),
    OIL(0x01, "Oil"),
    ELECTRICITY(0x02, "Electricity"),
    GAS(0x03, "Gas"),
    HEAT(0x04, "Heat"),
    STEAM(0x05, "Stream"),
    HOT_WATER(0x06, "Warm Water (30°C...90°C"),
    WATER(0x07, "Water"),
    HEAT_COST_ALLOCATOR(0x08, "Heat Cost Allocator"),
    COMPRESSED_AIR(0x09, "Compressed Air"),
    COOLING_LOAD_METER_OUTLET(0x0A, "Cooling load meter (Volume measured at return temperature: outlet)"),
    COOLING_LOAD_METER_INLET(0x0B, "Cooling load meter (Volume measured at flow temperature: inlet)"),
    HEAT_INLET(0x0C, "Heat (Volume measured at flow temperature: inlet)"),
    HEAT_COOLING_LOAD_METER(0x0D, "Heat/Cooling load meter"),
    BUS_SYSTEM(0x0E, "Bus/System component"),
    UNKNOWN_MEDIUM(0x0F, "Unknown Medium"),
    RESERVED_0X10(0x10),
    RESERVED_0X11(0x11),
    RESERVED_0X12(0x12),
    RESERVED_0X13(0x13),
    RESERVED_0X14(0x14),
    RESERVED_0X15(0x15, "Hot water (>=90°C)"),
    COLD_WATER(0x16, "Cold Water"),
    DUAL_WATER(0x17, "Dual register (hot/cold) Water meter"),
    PRESSURE(0x18, "Pressure"),
    AD_CONVERTER(0x19, "A/D Converter"),
    RESERVED_0X1A(0x1A),
    RESERVED_0X1B(0x1B),
    RESERVED_0X1C(0x1C),
    RESERVED_0X1D(0x1D),
    RESERVED_0X1E(0x1E),
    RESERVED_0X1F(0x1F),
    RESERVED_0X20(0x20),
    RESERVED_FOR_VALVE(0x21, "Reserved for valve"),
    RESERVED_0X22(0x22),
    RESERVED_0X23(0x23),
    RESERVED_0X24(0x24),
    RESERVED_0X25(0x25),
    RESERVED_0X26(0x26),
    RESERVED_0X27(0x27),
    RESERVED_0X28(0x28),
    RESERVED_0X29(0x29),
    RESERVED_0X2A(0x2A),
    RESERVED_0X2B(0x2B),
    RESERVED_0X2C(0x2C),
    RESERVED_0X2D(0x2D),
    RESERVED_0X2E(0x2E),
    RESERVED_0X2F(0x2F),
    RESERVED_0X30(0x30),
    RESERVED_0X31(0x31),
    RESERVED_0X32(0x32),
    RESERVED_0X33(0x33),
    RESERVED_0X34(0x34),
    RESERVED_0X35(0x35),
    RESERVED_0X36(0x36),
    RESERVED_0X37(0x37),
    RESERVED_0X38(0x38),
    RESERVED_0X39(0x39),
    RESERVED_0X3A(0x3A),
    RESERVED_0X3B(0x3B),
    RESERVED_0X3C(0x3C),
    RESERVED_0X3D(0x3D),
    RESERVED_0X3E(0x3E),
    RESERVED_0X3F(0x3F),
    RESERVED_0X40(0x40),
    RESERVED_0X41(0x41),
    RESERVED_0X42(0x42),
    RESERVED_0X43(0x43),
    RESERVED_0X44(0x44),
    RESERVED_0X45(0x45),
    RESERVED_0X46(0x46),
    RESERVED_0X47(0x47),
    RESERVED_0X48(0x48),
    RESERVED_0X49(0x49),
    RESERVED_0X4A(0x4A),
    RESERVED_0X4B(0x4B),
    RESERVED_0X4C(0x4C),
    RESERVED_0X4D(0x4D),
    RESERVED_0X4E(0x4E),
    RESERVED_0X4F(0x4F),
    RESERVED_0X50(0x50),
    RESERVED_0X51(0x51),
    RESERVED_0X52(0x52),
    RESERVED_0X53(0x53),
    RESERVED_0X54(0x54),
    RESERVED_0X55(0x55),
    RESERVED_0X56(0x56),
    RESERVED_0X57(0x57),
    RESERVED_0X58(0x58),
    RESERVED_0X59(0x59),
    RESERVED_0X5A(0x5A),
    RESERVED_0X5B(0x5B),
    RESERVED_0X5C(0x5C),
    RESERVED_0X5D(0x5D),
    RESERVED_0X5E(0x5E),
    RESERVED_0X5F(0x5F),
    RESERVED_0X60(0x60),
    RESERVED_0X61(0x61),
    RESERVED_0X62(0x62),
    RESERVED_0X63(0x63),
    RESERVED_0X64(0x64),
    RESERVED_0X65(0x65),
    RESERVED_0X66(0x66),
    RESERVED_0X67(0x67),
    RESERVED_0X68(0x68),
    RESERVED_0X69(0x69),
    RESERVED_0X6A(0x6A),
    RESERVED_0X6B(0x6B),
    RESERVED_0X6C(0x6C),
    RESERVED_0X6D(0x6D),
    RESERVED_0X6E(0x6E),
    RESERVED_0X6F(0x6F),
    RESERVED_0X70(0x70),
    RESERVED_0X71(0x71),
    RESERVED_0X72(0x72),
    RESERVED_0X73(0x73),
    RESERVED_0X74(0x74),
    RESERVED_0X75(0x75),
    RESERVED_0X76(0x76),
    RESERVED_0X77(0x77),
    RESERVED_0X78(0x78),
    RESERVED_0X79(0x79),
    RESERVED_0X7A(0x7A),
    RESERVED_0X7B(0x7B),
    RESERVED_0X7C(0x7C),
    RESERVED_0X7D(0x7D),
    RESERVED_0X7E(0x7E),
    RESERVED_0X7F(0x7F),
    RESERVED_0X80(0x80),
    RESERVED_0X81(0x81),
    RESERVED_0X82(0x82),
    RESERVED_0X83(0x83),
    RESERVED_0X84(0x84),
    RESERVED_0X85(0x85),
    RESERVED_0X86(0x86),
    RESERVED_0X87(0x87),
    RESERVED_0X88(0x88),
    RESERVED_0X89(0x89),
    RESERVED_0X8A(0x8A),
    RESERVED_0X8B(0x8B),
    RESERVED_0X8C(0x8C),
    RESERVED_0X8D(0x8D),
    RESERVED_0X8E(0x8E),
    RESERVED_0X8F(0x8F),
    RESERVED_0X90(0x90),
    RESERVED_0X91(0x91),
    RESERVED_0X92(0x92),
    RESERVED_0X93(0x93),
    RESERVED_0X94(0x94),
    RESERVED_0X95(0x95),
    RESERVED_0X96(0x96),
    RESERVED_0X97(0x97),
    RESERVED_0X98(0x98),
    RESERVED_0X99(0x99),
    RESERVED_0X9A(0x9A),
    RESERVED_0X9B(0x9B),
    RESERVED_0X9C(0x9C),
    RESERVED_0X9D(0x9D),
    RESERVED_0X9E(0x9E),
    RESERVED_0X9F(0x9F),
    RESERVED_0XA0(0xA0),
    RESERVED_0XA1(0xA1),
    RESERVED_0XA2(0xA2),
    RESERVED_0XA3(0xA3),
    RESERVED_0XA4(0xA4),
    RESERVED_0XA5(0xA5),
    RESERVED_0XA6(0xA6),
    RESERVED_0XA7(0xA7),
    RESERVED_0XA8(0xA8),
    RESERVED_0XA9(0xA9),
    RESERVED_0XAA(0xAA),
    RESERVED_0XAB(0xAB),
    RESERVED_0XAC(0xAC),
    RESERVED_0XAD(0xAD),
    RESERVED_0XAE(0xAE),
    RESERVED_0XAF(0xAF),
    RESERVED_0XB0(0xB0),
    RESERVED_0XB1(0xB1),
    RESERVED_0XB2(0xB2),
    RESERVED_0XB3(0xB3),
    RESERVED_0XB4(0xB4),
    RESERVED_0XB5(0xB5),
    RESERVED_0XB6(0xB6),
    RESERVED_0XB7(0xB7),
    RESERVED_0XB8(0xB8),
    RESERVED_0XB9(0xB9),
    RESERVED_0XBA(0xBA),
    RESERVED_0XBB(0xBB),
    RESERVED_0XBC(0xBC),
    RESERVED_0XBD(0xBD),
    RESERVED_0XBE(0xBE),
    RESERVED_0XBF(0xBF),
    RESERVED_0XC0(0xC0),
    RESERVED_0XC1(0xC1),
    RESERVED_0XC2(0xC2),
    RESERVED_0XC3(0xC3),
    RESERVED_0XC4(0xC4),
    RESERVED_0XC5(0xC5),
    RESERVED_0XC6(0xC6),
    RESERVED_0XC7(0xC7),
    RESERVED_0XC8(0xC8),
    RESERVED_0XC9(0xC9),
    RESERVED_0XCA(0xCA),
    RESERVED_0XCB(0xCB),
    RESERVED_0XCC(0xCC),
    RESERVED_0XCD(0xCD),
    RESERVED_0XCE(0xCE),
    RESERVED_0XCF(0xCF),
    RESERVED_0XD0(0xD0),
    RESERVED_0XD1(0xD1),
    RESERVED_0XD2(0xD2),
    RESERVED_0XD3(0xD3),
    RESERVED_0XD4(0xD4),
    RESERVED_0XD5(0xD5),
    RESERVED_0XD6(0xD6),
    RESERVED_0XD7(0xD7),
    RESERVED_0XD8(0xD8),
    RESERVED_0XD9(0xD9),
    RESERVED_0XDA(0xDA),
    RESERVED_0XDB(0xDB),
    RESERVED_0XDC(0xDC),
    RESERVED_0XDD(0xDD),
    RESERVED_0XDE(0xDE),
    RESERVED_0XDF(0xDF),
    RESERVED_0XE0(0xE0),
    RESERVED_0XE1(0xE1),
    RESERVED_0XE2(0xE2),
    RESERVED_0XE3(0xE3),
    RESERVED_0XE4(0xE4),
    RESERVED_0XE5(0xE5),
    RESERVED_0XE6(0xE6),
    RESERVED_0XE7(0xE7),
    RESERVED_0XE8(0xE8),
    RESERVED_0XE9(0xE9),
    RESERVED_0XEA(0xEA),
    RESERVED_0XEB(0xEB),
    RESERVED_0XEC(0xEC),
    RESERVED_0XED(0xED),
    RESERVED_0XEE(0xEE),
    RESERVED_0XEF(0xEF),
    RESERVED_0XF0(0xF0),
    RESERVED_0XF1(0xF1),
    RESERVED_0XF2(0xF2),
    RESERVED_0XF3(0xF3),
    RESERVED_0XF4(0xF4),
    RESERVED_0XF5(0xF5),
    RESERVED_0XF6(0xF6),
    RESERVED_0XF7(0xF7),
    RESERVED_0XF8(0xF8),
    RESERVED_0XF9(0xF9),
    RESERVED_0XFA(0xFA),
    RESERVED_0XFB(0xFB),
    RESERVED_0XFC(0xFC),
    RESERVED_0XFD(0xFD),
    RESERVED_0XFE(0xFE),
    RESERVED_0XFF(0xFF);

    final public int id;
    final public String label;

    MBusMedium(int id, String label) {
        this.id = id;
        this.label = label;
    }

    MBusMedium(int id) {
        this.id = id;
        this.label = String.format("Reserved 0x%02x", id);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public static MBusMedium fromLabel(String label) {
        for (MBusMedium value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }

        return valueOf(label);
    }

    public static MBusMedium valueOf(byte b) {
        for (MBusMedium value : values()) {
            if (value.getId() == (b & 0xFF)) {
                return value;
            }
        }

        throw new IllegalArgumentException(String.format("Cant get MBusMedium from 0x%02x", b & 0xFF));
    }

}
