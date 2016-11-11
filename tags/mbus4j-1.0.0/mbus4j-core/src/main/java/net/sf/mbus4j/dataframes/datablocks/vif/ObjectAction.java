package net.sf.mbus4j.dataframes.datablocks.vif;

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
public enum ObjectAction {

    WRITE(0x00, "write(replace)"),
    ADD_VALUE(0x01, "add value"),
    SUBTRACT_VALUE(0x02, "subtract value"),
    OR(0x03, "or"),
    AND(0x04, "and"),
    XOR(0x05, "xor"),
    AND_NOT(0x06, "and not"),
    CLEAR(0x07, "clear"),
    ADD_ENTRY(0x08, "add entry"),
    DELETE_ENTRY(0x09, "delete entry"),
    RESERVED_0X0A(0x0A, "Reserved 0x0A"),
    FREEZE_DATA(0x0B, "freeze data"),
    ADD_TO_READOUT_LIST(0x0C, "add to readout list"),
    DELETE_FROM_READOUT_LIST(0x0D, "delete from readout list");

    public static ObjectAction valueOf(int id) {
        for (ObjectAction oa : ObjectAction.values()) {
            if (oa.id == id) {
                return oa;
            }
        }
        return null;
    }
    public final int id;
    private final String label;

    private ObjectAction(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public static ObjectAction fromLabel(String label) {
        for (ObjectAction value : values()) {
            if (value.label.equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }
}
