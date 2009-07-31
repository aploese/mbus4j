/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
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
public interface Vife {

    public final static String FACTOR = "Multiplicative correction factor";
    public final static String CONST = "Additive correction constant";
    public final static String TIMESTAMP_OF_LIMIT_EXCEED = "=> Date(/time) %s of %s %s limit exceed";
    public final static String DURATION_OF_LIMIT_EXCEED = "=> Duration of %s %s limit exceeds %s";
    public final static String DURATION_OF = "=> Duration of %s %s";
    public final static String TIMESTAMP_OF = "=> Date(/time) %s of %s";
    public final static String BEGIN = "begin";
    public final static String END = "end";
    public final static String FIRST = "first";
    public final static String LAST = "last";
    public final static String LOWER = "lower";
    public final static String UPPER = "upper";
}
