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
public enum VifeFC implements Vife {

// DO NOT CHANGE ORDER!!!!
    UNKNOWN_0X00(),
    PHASE_L1("Phase L1"),
    PHASE_L2("Phase L2"),
    PHASE_L3("Phase L3"),
    UNKNOWN_0X04(),
    PHASE_L1_L2("Phase L1 - L2"),
    PHASE_L2_L3("Phase L2 - L3"),
    PHASE_L3_L1("Phase L3 - L1"),
    UNKNOWN_0X08(),
    UNKNOWN_0X09(),
    UNKNOWN_0X0A(),
    UNKNOWN_0X0B(),
    UNKNOWN_0X0C(),
    UNKNOWN_0X0D(),
    UNKNOWN_0X0E(),
    UNKNOWN_0X0F(),
    UNKNOWN_0X10(),
    UNKNOWN_0X11(),
    UNKNOWN_0X12(),
    UNKNOWN_0X13(),
    UNKNOWN_0X14(),
    UNKNOWN_0X15(),
    UNKNOWN_0X16(),
    UNKNOWN_0X17(),
    UNKNOWN_0X18(),
    UNKNOWN_0X19(),
    UNKNOWN_0X1A(),
    UNKNOWN_0X1B(),
    UNKNOWN_0X1C(),
    UNKNOWN_0X1D(),
    UNKNOWN_0X1E(),
    UNKNOWN_0X1F(),
    UNKNOWN_0X20(),
    UNKNOWN_0X21(),
    UNKNOWN_0X22(),
    UNKNOWN_0X23(),
    UNKNOWN_0X24(),
    UNKNOWN_0X25(),
    UNKNOWN_0X26(),
    UNKNOWN_0X27(),
    UNKNOWN_0X28(),
    UNKNOWN_0X29(),
    UNKNOWN_0X2A(),
    UNKNOWN_0X2B(),
    UNKNOWN_0X2C(),
    UNKNOWN_0X2D(),
    UNKNOWN_0X2E(),
    UNKNOWN_0X2F(),
    UNKNOWN_0X30(),
    UNKNOWN_0X31(),
    UNKNOWN_0X32(),
    UNKNOWN_0X33(),
    UNKNOWN_0X34(),
    UNKNOWN_0X35(),
    UNKNOWN_0X36(),
    UNKNOWN_0X37(),
    UNKNOWN_0X38(),
    UNKNOWN_0X39(),
    UNKNOWN_0X3A(),
    UNKNOWN_0X3B(),
    UNKNOWN_0X3C(),
    UNKNOWN_0X3D(),
    UNKNOWN_0X3E(),
    UNKNOWN_0X3F(),
    UNKNOWN_0X40(),
    UNKNOWN_0X41(),
    UNKNOWN_0X42(),
    UNKNOWN_0X43(),
    UNKNOWN_0X44(),
    UNKNOWN_0X45(),
    UNKNOWN_0X46(),
    UNKNOWN_0X47(),
    UNKNOWN_0X48(),
    UNKNOWN_0X49(),
    UNKNOWN_0X4A(),
    UNKNOWN_0X4B(),
    UNKNOWN_0X4C(),
    UNKNOWN_0X4D(),
    UNKNOWN_0X4E(),
    UNKNOWN_0X4F(),
    UNKNOWN_0X50(),
    UNKNOWN_0X51(),
    UNKNOWN_0X52(),
    UNKNOWN_0X53(),
    UNKNOWN_0X54(),
    UNKNOWN_0X55(),
    UNKNOWN_0X56(),
    UNKNOWN_0X57(),
    UNKNOWN_0X58(),
    UNKNOWN_0X59(),
    UNKNOWN_0X5A(),
    UNKNOWN_0X5B(),
    UNKNOWN_0X5C(),
    UNKNOWN_0X5D(),
    UNKNOWN_0X5E(),
    UNKNOWN_0X5F(),
    UNKNOWN_0X60(),
    UNKNOWN_0X61(),
    UNKNOWN_0X62(),
    UNKNOWN_0X63(),
    UNKNOWN_0X64(),
    UNKNOWN_0X65(),
    UNKNOWN_0X66(),
    UNKNOWN_0X67(),
    UNKNOWN_0X68(),
    UNKNOWN_0X69(),
    UNKNOWN_0X6A(),
    UNKNOWN_0X6B(),
    UNKNOWN_0X6C(),
    UNKNOWN_0X6D(),
    UNKNOWN_0X6E(),
    UNKNOWN_0X6F(),
    UNKNOWN_0X70(),
    UNKNOWN_0X71(),
    UNKNOWN_0X72(),
    UNKNOWN_0X73(),
    UNKNOWN_0X74(),
    UNKNOWN_0X75(),
    UNKNOWN_0X76(),
    UNKNOWN_0X77(),
    UNKNOWN_0X78(),
    UNKNOWN_0X79(),
    UNKNOWN_0X7A(),
    UNKNOWN_0X7B(),
    UNKNOWN_0X7C(),
    UNKNOWN_0X7D(),
    UNKNOWN_0X7E(),
    UNKNOWN_0X7F();

    public final static VifeFC valueOfTableIndex(byte ordinal) {
        return map[ordinal];
    }
    private final String label;
    private final static VifeFC[] map = values();

    private VifeFC() {
        this.label = String.format("VifFC Unknown 0x%02x", ordinal());
    }

    private VifeFC(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public byte getTableIndex() {
        return (byte) ordinal();
    }

    @Override
    public String toString() {
        return label;
    }

    public static VifeFC fromLabel(String label) {
        for (VifeFC value : map) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }

    @Override
    public VifeTypes getVifeType() {
        return VifeTypes.FC_EXTENSION;
    }

}
