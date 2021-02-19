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
public enum SiPrefix {

    PICO("p", -15),
    NANO("n", -12),
    MICRO("µ", -6),
    MILLI("m", -3),
    CENTI("c", -2),
    DECI("d", -1),
    ONE("", 0),
    DECA("D", 1),
    HECTO("h", 2),
    KILO("k", 3),
    MEGA("M", 6),
    GIGA("G", 12);
    private final String label;
    private final double factor;
    private final int exponent;

    private SiPrefix(String label, int exponent) {
        this.label = label;
        this.factor = Math.pow(10, exponent);
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        return label;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    public static SiPrefix fromLabel(String label) {
        if (label == null) {
            return null;
        }
        for (SiPrefix value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }

    /**
     * @return the factor
     */
    final public double getFactor() {
        return factor;
    }

    /**
     * @return the pow10
     */
    final public int getExponent() {
        return exponent;
    }

}
