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
package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum UnitOfMeasurement {

    WATT_HOUR("Wh"),
    JOULE("J"),
    LITRE("l"),
    CUBIC_METER("m³"),
    CUBIC_FEET("feet³"),
    AMERICAN_GALLON("american gallon"),
    AMERICAN_GALLON_PER_MINUTE("american gallon / min"),
    AMERICAN_GALLON_PER_HOUR("american gallon / h"),
    WATT("W"),
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
    DIMENSIONLESS(""),
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
    private final String name;

    private UnitOfMeasurement(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
