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
public enum UnitOfMeasurement {

    DIMENSIONLESS(""),
    DEGREE("°"), 
    HERTZ("Hz"), 
    WATT_HOUR("Wh"),
    VA_HOUR("VAh"),
    VAR_HOUR("VARh"),
    JOULE("J"),
    LITRE("l"),
    CUBIC_METER("m³"),
    CUBIC_FEET("feet³"),
    AMERICAN_GALLON("american gallon"),
    AMERICAN_GALLON_PER_MINUTE("american gallon / min"),
    AMERICAN_GALLON_PER_HOUR("american gallon / h"),
    WATT("W"),
    VA("VA"),
    VAR("VAR"),
    GRAMM("g"),
    TONNS("t"),
    JOULE_PER_HOUR("J/h"),
    DEGREE_FAHRENHEIT("°F"),
    DEGREE_CELSIUS("°C"),
    VOLT("V"),
    AMPERE("A"),
    CURRENCY("Currency"),
    SECOND("s"),
    MINUTE("min"),
    HOUR("h"),
    DAY("d"),
    //    DAY_OF_MONTH("day of month"),
    MONTH("m"),
    YEAR("y"),
    BAUD("Baud"),
    BITTIMES("bittimes"),
    TIME_AND_DATE("time and date"),
    LITRE_PER_HOUR("l/h"),
    CUBIC_METER_PER_HOUR("m³/h"),
    LITRE_PER_MINUTE("l/min"),
    CUBIC_METER_PER_MINUTE("m³/min"),
    LITRE_PER_SECOND("l/s"),
    CUBIC_METER_PER_SECOND("m³/s"),
    GRAMM_PER_HOUR("g/h"),
    TONN_PER_HOUR("t/h"),
    KELVIN("K"),
    BAR("bar"),
    DATE("Date");
    private final String label;

    private UnitOfMeasurement(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public static UnitOfMeasurement fromLabel(String label) {
        if (label == null) {
            return null;
        }
        for (UnitOfMeasurement value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }
}
