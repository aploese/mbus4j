/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes.datablocks.vif;

import net.sf.mbus4j.dataframes.datablocks.*;

/**
 *
 * @author aploese
 */
public interface Vif {

    final static String VOLUME = "Volume";
    final static String ENERGY = "Energy";
    final static String MASS = "Mass";
    final static String VOLUME_FLOW = "Volume Flow";
    final static String POWER = "Power";
    final static String FLOW_TEMPERATURE = "Flow Temperature";
    final static String RETURN_TEMPERATURE = "Return Temperature";
    final static String TEMPERATURE_DIFFERENCE = "Temperature Difference";
    final static String EXTERNAL_TEMPERATURE = "External Temperature";
    final static String COLD_OR_WARM_TEMPERATURE_LIMIT = "Cold / Warm Temperature Limit";
    final static String CUMUL_COUNT_MAX_POWER = "Cumul. count max power";
    final static String CREDIT = "Credit of the nominal local legal currency units";
    final static String DEBIT = "Debit of the nominal local legal currency units ";
    final static String STORAGE_INTERVAL = "Storage interval";
    final static String DURATION_SINCE_LAST_READOUT = "Duration since last readout";
    final static String DURATION_OF_TARIFF = "Duration of tariff";
    final static String PERIOD_OF_TARIFF = "Period of tariff";
    final static String VOLTAGE = "Voltage";
    final static String CURRENT = "Current";
    final static String DURATION_SINCE_LAST_CUMULATION = "Duatation since last cumulation";
    final static String OPERATING_TIME_BYTTERY = "Operating time battery";
    final static String ON_TIME = "On Time";
    final static String OPERATING_TIME = "Operating Time";
    final static String MASS_FLOW = "Mass flow";
    final static String PRESSURE = "Pressure";
    final static String TIME_POINT = "Time Point";
    final static String AVERAGING_DURATION = "Averaging Duration";
    final static String ACTUALLY_DURATION = "Actually Duration";

    UnitOfMeasurement getUnitOfMeasurement();
    
    SiPrefix getSiPrefix();

    Integer getExponent();

    /**
     * User friendly name for use in guis
     * @return
     */
    String getLabel();

}
