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
package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum SiPrefix {

    PICO("p"),
    NANO("n"),
    MICRO("µ"),
    MILLI("m"),
    CENTI("c"),
    DECI("d"),
    ONE(""),
    DECA("D"),
    HECTO("h"),
    KILO("k"),
    MEGA("M"),
    GIGA("G");
    private final String userFriendlyName;

    private SiPrefix(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }

    @Override
    public String toString() {
        return userFriendlyName;
    }
}
