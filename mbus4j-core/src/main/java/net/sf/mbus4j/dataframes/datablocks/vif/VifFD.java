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

import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.KILO;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MEGA;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MICRO;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MILLI;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.NANO;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.ONE;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.PICO;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.AMPERE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.CURRENCY;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DAY;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.MINUTE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.MONTH;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.SECOND;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.TIME_AND_DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.VOLT;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.YEAR;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum VifFD implements Vif {

    CREDIT_CURRENCY_E__3(CREDIT, ONE, CURRENCY, -3),
    CREDIT_CURRENCY_E__2(CREDIT, ONE, CURRENCY, -2),
    CREDIT_CURRENCY_E__1(CREDIT, ONE, CURRENCY, -1),
    CREDIT_CURRENCY_E_0(CREDIT, ONE, CURRENCY, 0),
    DEBIT_CURRENCY_E__3(DEBIT, ONE, CURRENCY, -3),
    DEBIT_CURRENCY_E__2(DEBIT, ONE, CURRENCY, -2),
    DEBIT_CURRENCY_E__1(DEBIT, ONE, CURRENCY, -1),
    DEBIT_CURRENCY_E_0(DEBIT, ONE, CURRENCY, 0),
    ACCESS_NUMBER("Access Number  (transmission count)"),
    DEVIVE_TYPE("Device type"),
    MANUFACTURER("Manufacturer"),
    PARAMETER_SET_IDENTIFICATION("Parameter set identification"),
    MODEL("Model/Version"),
    HARDWARE_VERSION("Hardware version #"),
    FIRMWARE_VERSION("Metrology (firmware) version #"),
    SOFTWARE_VERSION("Other software version #"),
    CUSTOMER_LOCATION("Other location"),
    CUSTOMER("Customer"),
    ACCESS_CODE_USER("Access Code User"),
    ACCESS_CODE_OPERATOR("Access Code Operator"),
    ACCESS_CODE_SYSTEM_OPERATOR("Access Code System Operator"),
    ACCESS_CODE_DEVELOPER("Access Code Developer"),
    PASSWORD("Password"),
    ERROR_FLAGS("Error flags (binary)"),
    ERROR_MASK("Error mask"),
    RESERVED_0X19(),
    DIGITAL_OUTPUT("Digital Output (binary)"),
    DIGITAL_INPUT("Digital Input (binary)"),
    BAUDRATE("Baudrate [Baud]"),
    RESPONSE_DELAY_TIME("Response delay time [bittimes]"),
    RETRY("Retry"),
    REMOTE_CONTROL("Remote control (device specific)"),
    FIRST_STORAGE("First storage # for cyclic storage"),
    LAST_STORAGE("Last storage # for cyclic storage"),
    SIZE_OF_STORAGE_BLOCK("Size of storage block"),
    RESERVED_0X23(),
    STORAGE_INTERVAL_S(STORAGE_INTERVAL, SECOND),
    STORAGE_INTERVAL_MIN(STORAGE_INTERVAL, MINUTE),
    STORAGE_INTERVAL_H(STORAGE_INTERVAL, HOUR),
    STORAGE_INTERVAL_D(STORAGE_INTERVAL, DAY),
    STORAGE_INTERVAL_MONTH(STORAGE_INTERVAL, MONTH),
    STORAGE_INTERVAL_Y(STORAGE_INTERVAL, YEAR),
    RESERVED_0X2A(),
    TIME_POINT_S("Time point second (0 to 59)"),
    DURATION_SINCE_LAST_READOUT_S(DURATION_SINCE_LAST_READOUT, SECOND),
    DURATION_SINCE_LAST_READOUT_MIN(DURATION_SINCE_LAST_READOUT, MINUTE),
    DURATION_SINCE_LAST_READOUT_H(DURATION_SINCE_LAST_READOUT, HOUR),
    DURATION_SINCE_LAST_READOUT_D(DURATION_SINCE_LAST_READOUT, DAY),
    START_OF_TARIFF("Start of tariff", TIME_AND_DATE),
    DURATION_OF_TARIFF_MIN(DURATION_OF_TARIFF, MINUTE),
    DURATION_OF_TARIFF_H(DURATION_OF_TARIFF, HOUR),
    DURATION_OF_TARIFF_D(DURATION_OF_TARIFF, DAY),
    PERIOD_OF_TARIFF_S(PERIOD_OF_TARIFF, SECOND),
    PERIOD_OF_TARIFF_MIN(PERIOD_OF_TARIFF, MINUTE),
    PERIOD_OF_TARIFF_H(PERIOD_OF_TARIFF, HOUR),
    PERIOD_OF_TARIFF_D(PERIOD_OF_TARIFF, DAY),
    PERIOD_OF_TARIFF_MONTH(PERIOD_OF_TARIFF, MONTH),
    PERIOD_OF_TARIFF_Y(PERIOD_OF_TARIFF, YEAR),
    DIMENSIONLESS("dimensionless / no VIF"),
    RESERVED_0X3B(),
    RESERVED_0X3C(),
    RESERVED_0X3D(),
    RESERVED_0X3E(),
    RESERVED_0X3F(),
    VOLTAGE_NANO_V_E_0(VOLTAGE, NANO, VOLT, 0),
    VOLTAGE_NANO_V_E_1(VOLTAGE, NANO, VOLT, 1),
    VOLTAGE_NANO_V_E_2(VOLTAGE, NANO, VOLT, 2),
    VOLTAGE_MICRO_V_E_0(VOLTAGE, MICRO, VOLT, 0),
    VOLTAGE_MICRO_V_E_1(VOLTAGE, MICRO, VOLT, 1),
    VOLTAGE_MICRO_V_E_2(VOLTAGE, MICRO, VOLT, 2),
    VOLTAGE_MILLI_V_E_0(VOLTAGE, MILLI, VOLT, 0),
    VOLTAGE_MILLI_V_E_1(VOLTAGE, MILLI, VOLT, 1),
    VOLTAGE_MILLI_V_E_2(VOLTAGE, MILLI, VOLT, 2),
    VOLTAGE_V_E_0(VOLTAGE, ONE, VOLT, 0),
    VOLTAGE_V_E_1(VOLTAGE, ONE, VOLT, 1),
    VOLTAGE_V_E_2(VOLTAGE, ONE, VOLT, 2),
    VOLTAGE_KILO_V_E_0(VOLTAGE, KILO, VOLT, 0),
    VOLTAGE_KILO_V_E_1(VOLTAGE, KILO, VOLT, 1),
    VOLTAGE_KILO_V_E_2(VOLTAGE, KILO, VOLT, 2),
    VOLTAGE_MEGA_V_E_0(VOLTAGE, MEGA, VOLT, 0),
    CURRENT_PICO_A_E_0(CURRENT, PICO, AMPERE, 0),
    CURRENT_PICO_A_E_1(CURRENT, PICO, AMPERE, 1),
    CURRENT_PICO_A_E_2(CURRENT, PICO, AMPERE, 2),
    CURRENT_NANO_A_E_0(CURRENT, NANO, AMPERE, 0),
    CURRENT_NANO_A_E_1(CURRENT, NANO, AMPERE, 1),
    CURRENT_NANO_A_E_2(CURRENT, NANO, AMPERE, 2),
    CURRENT_MICRO_A_E_0(CURRENT, MICRO, AMPERE, 0),
    CURRENT_MICRO_A_E_1(CURRENT, MICRO, AMPERE, 1),
    CURRENT_MICRO_A_E_2(CURRENT, MICRO, AMPERE, 2),
    CURRENT_MILLI_A_E_0(CURRENT, MILLI, AMPERE, 0),
    CURRENT_MILLI_A_E_1(CURRENT, MILLI, AMPERE, 1),
    CURRENT_MILLI_A_E_2(CURRENT, MILLI, AMPERE, 2),
    CURRENT_A_E_0(CURRENT, ONE, AMPERE, 0),
    CURRENT_A_E_1(CURRENT, ONE, AMPERE, 1),
    CURRENT_A_E_2(CURRENT, ONE, AMPERE, 2),
    CURRENT_KILO_A_E_3(CURRENT, KILO, AMPERE, 0),
    RESET_COUINTER("Reset counter"),
    CUMULATION_COUNTER("Cumulation counter"),
    CONTROL_SIGNAL("Control signal"),
    DAY_OF_WEEK("Day of week"),
    WEEK_NUMBER("Week number"),
    TIMEPOINT_OF_DAY_CHANGE("Timepoint of day change"),
    STATE_OF_PARAMETER_ACTIVATION("State of parameter activation"),
    SPECIAL_SUPPLIER_INFORMATION("Special supplier information"),
    DURATION_SINCE_LAST_CUMULATION_H(DURATION_SINCE_LAST_CUMULATION, HOUR),
    DURATION_SINCE_LAST_CUMULATION_D(DURATION_SINCE_LAST_CUMULATION, DAY),
    DURATION_SINCE_LAST_CUMULATION_MONTH(DURATION_SINCE_LAST_CUMULATION, MONTH),
    DURATION_SINCE_LAST_CUMULATION_Y(DURATION_SINCE_LAST_CUMULATION, YEAR),
    OPERATIONG_TIME_BATTERY_H(OPERATING_TIME_BYTTERY, HOUR),
    OPERATIONG_TIME_BATTERY_D(OPERATING_TIME_BYTTERY, DAY),
    OPERATIONG_TIME_BATTERY_MONTH(OPERATING_TIME_BYTTERY, MONTH),
    OPERATIONG_TIME_BATTERY_Y(OPERATING_TIME_BYTTERY, YEAR),
    DATE_AND_TIME_OF_BATTERY_CHANGE("Date and time of battery change", TIME_AND_DATE),
    RESERVED_0X71(),
    DAYLIGHT_SAVNING("Day light saving (beginning, ending, deviation"),
    LISTENING_WINDOW_MANAGEMENT("Listening windeow management"),
    REMAINING_BATTERY_LIVE_TIME("Remaining battery life time (days)"),
    NUMBER_OF_TIMES_THE_METER_WAS_STOPPED("#times the meter was stopped"),
    RESERVED_0X76(),
    RESERVED_0X77(),
    RESERVED_0X78(),
    RESERVED_0X79(),
    RESERVED_0X7A(),
    RESERVED_0X7B(),
    RESERVED_0X7C(),
    RESERVED_0X7D(),
    RESERVED_0X7E(),
    RESERVED_0X7F();

    public static VifFD valueOfTableIndex(byte ordinal) {
        if (map == null) {
            map = new HashMap<>(0xFE);
            for (VifFD val : values()) {
                map.put((byte) val.ordinal(), val);
                //             System.out.println(String.format("0x%02x %s\t%s\t%s", val.ordinal(), val, val.getUnitOfMeasurement(), val.getExponent()));
            }
        }
        return map.get(ordinal);
    }
    private final String label;
    private final SiPrefix siPrefix;
    private final UnitOfMeasurement unit;
    private final Integer exponent;
    private static Map<Byte, VifFD> map;

    private VifFD() {
        this.label = String.format("VifFD Reserved 0x%02x", ordinal());
        this.siPrefix = null;
        this.unit = null;
        this.exponent = null;
    }

    private VifFD(String label) {
        this.label = label;
        this.siPrefix = null;
        this.unit = null;
        this.exponent = null;
    }

    private VifFD(String label, SiPrefix siPrefix, UnitOfMeasurement unit, int exponent) {
        this.label = label;
        this.siPrefix = siPrefix;
        this.unit = unit;
        this.exponent = exponent;
    }

    private VifFD(String label, UnitOfMeasurement unit) {
        this.label = label;
        this.siPrefix = null;
        this.unit = unit;
        this.exponent = null;
    }

    /**
     * @return the exponent
     */
    @Override
    public Integer getExponent() {
        return exponent;
    }

    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @return the siPrefix
     */
    @Override
    public SiPrefix getSiPrefix() {
        return siPrefix;
    }

    public byte getTableIndex() {
        return (byte) ordinal();
    }

    /**
     * @return the unit
     */
    @Override
    public UnitOfMeasurement getUnitOfMeasurement() {
        return unit;
    }

    @Override
    public String toString() {
        return VifToString.vifToString(this);
    }

    public static VifFD assemble(String label, UnitOfMeasurement unitOfMeasurement, SiPrefix siPrefix, Integer exponent) {
        for (VifFD value : values()) {
            if (value.getLabel().equals(label) && ((unitOfMeasurement == value.getUnitOfMeasurement()) || ((unitOfMeasurement != null) && unitOfMeasurement.equals(value.getUnitOfMeasurement()))) && ((siPrefix == value.getSiPrefix()) || ((siPrefix != null) && siPrefix.equals(value.getSiPrefix()))) && ((exponent == value.getExponent()) || ((exponent != null) && exponent.equals(value.getExponent())))) {
                return value;
            }
        }
        return valueOf(label);
    }

    @Override
    public VifTypes getVifType() {
        return VifTypes.FD_EXTENTION;
    }
}
