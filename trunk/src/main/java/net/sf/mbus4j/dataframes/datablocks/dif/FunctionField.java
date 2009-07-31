/*
 * mbus4j - Open source drivers for mbus protocol (www.mbus.com) - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.dataframes.datablocks.dif;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
public enum FunctionField {

    INSTANTANEOUS_VALUE(0x00, "Instantaneous value"),
    MAXIMUM_VALUE(0x10, "Maximum value"),
    MINIMUM_VALUE(0x20, "Minimum value"),
    VALUE_DURING_ERROR_STATE(0x30, "Value during error state");
    private final String userFriendlyName;
    public final byte code;

    private FunctionField(int code, String name) {
        this.userFriendlyName = name;
        this.code = (byte) code;
    }

    @Override
    public String toString() {
        return userFriendlyName;
    }
}
