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
package net.sf.mbus4j.dataframes.datablocks.dif;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum DataFieldCode {

    NO_DATA(0x00, "No Data"),
    _8_BIT_INTEGER(0x01, "8 Bit Integer"),
    _16_BIT_INTEGER(0x02, "16 Bit Integer"),
    _24_BIT_INTEGER(0x03, "24 Bit Integer"),
    _32_BIT_INTEGER(0x04, "32 Bit Integer"),
    _32_BIT_REAL(0x05, "32 Bit Real"),
    _48_BIT_INTEGER(0x06, "48 Bit Integer"),
    _64_BIT_INTEGER(0x07, "64 Bit Integer"),
    SELECTION_FOR_READOUT(0x08, "Selection for readout"),
    _2_DIGIT_BCD(0x09, "2 digit BCD"),
    _4_DIGIT_BCD(0x0a, "4 digit BCD"),
    _6_DIGIT_BCD(0x0b, "6 digit BCD"),
    _8_DIGIT_BCD(0x0c, "8 digit BCD"),
    VARIABLE_LENGTH(0x0d, "variable length"),
    _12_DIGIT_BCD(0x0e, "12 digit BCD"),
    SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET(0x0f, "Special Function Manufacturer specific data"),
    SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS(0x1f, "Special Function Manufacturer specific data more records follow in next telegram"),
    SPECIAL_FUNCTION_IDLE_FILLER(0x2f, "Special Function Idle filler"),
    // 3,4,5,6 reserved
    SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST(0x7f, "Special Function Global readout request");
    private final String userFrendlyName;
    public final byte code;

    private DataFieldCode(int code, String name) {
        this.userFrendlyName = name;
        this.code = (byte) code;
    }

    @Override
    public String toString() {
        return userFrendlyName;
    }
}
