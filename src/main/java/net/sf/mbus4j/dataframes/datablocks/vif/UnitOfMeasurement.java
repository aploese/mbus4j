/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
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
