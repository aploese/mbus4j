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
public interface Vif {

    final static String FREQUENCY = "Frequency";
    final static String PHASE_U_I = "Phase U/I";
    final static String VOLUME = "Volume";
    final static String ENERGY = "Energy";
    final static String REACTIVE_ENERGY = "Reactive Energy";
    final static String COMPLEX_ENERGY = "Complex Energy";
    final static String MASS = "Mass";
    final static String VOLUME_FLOW = "Volume Flow";
    final static String POWER = "Power";
    final static String REACTIVE_POWER = "Reactive Power";
    final static String COMPLEX_POWER = "Complex Power";
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

    Integer getExponent();

    /**
     * User friendly name
     *
     * @return
     */
    String getLabel();

    SiPrefix getSiPrefix();

    UnitOfMeasurement getUnitOfMeasurement();

    /**
     * Type of the vif i.e. ascii, primary extention FB, extention FD
     *
     * @return
     */
    VifTypes getVifType();

    final static class VifToString {

        final static String vifToString(Vif vif) {
            StringBuilder sb = new StringBuilder();
            sb.append(vif.getLabel());
            if (vif.getExponent() != null) {
                sb.append(" * 10^").append(vif.getExponent());
            }
            if ((vif.getSiPrefix() != null) || (vif.getUnitOfMeasurement() != null)) {
                sb.append('[');
                if (vif.getSiPrefix() != null) {
                    sb.append(vif.getSiPrefix());
                }
                if (vif.getUnitOfMeasurement() != null) {
                    sb.append(vif.getUnitOfMeasurement());
                }
                sb.append(']');
            }
            return sb.toString();
        }
    }

}
