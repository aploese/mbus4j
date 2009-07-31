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

import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.KILO;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MEGA;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MICRO;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MILLI;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.ONE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.BAR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.CUBIC_METER;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.CUBIC_METER_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.CUBIC_METER_PER_MINUTE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DAY;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DEGREE_CELSIUS;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.GRAMM;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.GRAMM_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.JOULE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.JOULE_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.KELVIN;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.LITRE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.LITRE_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.LITRE_PER_MINUTE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.LITRE_PER_SECOND;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.MINUTE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.SECOND;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.TIME_AND_DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.TONNS;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.TONN_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.WATT;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.WATT_HOUR;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
public enum VifStd implements Vif {

    ENERGY_MILLI_WH_E_0(0x00, ENERGY, MILLI, WATT_HOUR, 0),
    ENERGY_MILLI_WH_E_1(0x01, ENERGY, MILLI, WATT_HOUR, 1),
    ENERGY_MILLI_WH_E_2(0x02, ENERGY, MILLI, WATT_HOUR, 2),
    ENERGY_WH_E_0(0x03, ENERGY, ONE, WATT_HOUR, 0),
    ENERGY_WH_E_1(0x04, ENERGY, ONE, WATT_HOUR, 1),
    ENERGY_WH_E_2(0x05, ENERGY, ONE, WATT_HOUR, 2),
    ENERGY_KILO_WH_E_0(0x06, ENERGY, KILO, WATT_HOUR, 0),
    ENERGY_KILO_WH_E_1(0x07, ENERGY, KILO, WATT_HOUR, 1),
    ENERGY_J_E_0(0x08, ENERGY, ONE, JOULE, 0),
    ENERGY_J_E_1(0x09, ENERGY, ONE, JOULE, 1),
    ENERGY_J_E_2(0x0A, ENERGY, ONE, JOULE, 2),
    ENERGY_KILO_J_E_0(0x0B, ENERGY, KILO, JOULE, 0),
    ENERGY_KILO_J_E_1(0x0C, ENERGY, KILO, JOULE, 1),
    ENERGY_KILO_J_E_2(0x0D, ENERGY, KILO, JOULE, 2),
    ENERGY_MEGA_J_E_0(0x0E, ENERGY, MEGA, JOULE, 0),
    ENERGY_MEGA_J_E_1(0x0F, ENERGY, MEGA, JOULE, 1),
    VOLUME_MILLI_L_E_0(0x10, VOLUME, MILLI, LITRE, 0),
    VOLUME_MILLI_L_E_1(0x11, VOLUME, MILLI, LITRE, 1),
    VOLUME_MILLI_L_E_2(0x12, VOLUME, MILLI, LITRE, 2),
    VOLUME_L_E_0(0x13, VOLUME, ONE, LITRE, 0),
    VOLUME_L_E_1(0x14, VOLUME, ONE, LITRE, 1),
    VOLUME_L_E_2(0x15, VOLUME, ONE, LITRE, 2),
    VOLUME_CBM_E_0(0x16, VOLUME, ONE, CUBIC_METER, 0),
    VOLUME_CBM_E_1(0x17, VOLUME, ONE, CUBIC_METER, 1),
    MASS_G_E_0(0x18, MASS, ONE, GRAMM, 0),
    MASS_G_E_1(0x19, MASS, ONE, GRAMM, 1),
    MASS_G_E_2(0x1A, MASS, ONE, GRAMM, 2),
    MASS_KILO_G_E_0(0x1B, MASS, KILO, GRAMM, 0),
    MASS_KILO_G_E_1(0x1C, MASS, KILO, GRAMM, 1),
    MASS_KILO_G_E_2(0x1D, MASS, KILO, GRAMM, 2),
    MASS_TONNS_E_0(0x1E, MASS, ONE, TONNS, 0),
    MASS_TONNS_E_1(0x1F, MASS, ONE, TONNS, 1),
    ON_TIME_S(0x20, ON_TIME, SECOND),
    ON_TIME_MIN(0x21, ON_TIME, MINUTE),
    ON_TIME_H(0x22, ON_TIME, HOUR),
    ON_TIME_D(0x23, ON_TIME, DAY),
    OPERATING_TIME_S(0x24, OPERATING_TIME, SECOND),
    OPERATING_TIME_MIN(0x25, OPERATING_TIME, MINUTE),
    OPERATING_TIME_H(0x26, OPERATING_TIME, HOUR),
    OPERATING_TIME_D(0x27, OPERATING_TIME, DAY),
    POWER_MILLI_W_E_0(0x28, POWER, MILLI, WATT, 0),
    POWER_MILLI_W_E_1(0x29, POWER, MILLI, WATT, 1),
    POWER_MILLI_W_E_2(0x2A, POWER, MILLI, WATT, 2),
    POWER_W_E_0(0x2B, POWER, ONE, WATT, 0),
    POWER_W_E_1(0x2C, POWER, ONE, WATT, 1),
    POWER_W_E_2(0x2D, POWER, ONE, WATT, 2),
    POWER_KILO_W_E_0(0x2E, POWER, KILO, WATT, 0),
    POWER_KILO_W_E_1(0x2F, POWER, KILO, WATT, 1),
    POWER_J_PER_H_E_0(0x30, POWER, ONE, JOULE_PER_HOUR, 0),
    POWER_J_PER_H_E_1(0x31, POWER, ONE, JOULE_PER_HOUR, 1),
    POWER_J_PER_H_E_2(0x32, POWER, ONE, JOULE_PER_HOUR, 2),
    POWER_KILO_J_PER_H_E_0(0x33, POWER, KILO, JOULE_PER_HOUR, 0),
    POWER_KILO_J_PER_H_E_1(0x34, POWER, KILO, JOULE_PER_HOUR, 1),
    POWER_KILO_J_PER_H_E_2(0x35, POWER, KILO, JOULE_PER_HOUR, 2),
    POWER_MEGA_J_PER_H_E_0(0x36, POWER, MEGA, JOULE_PER_HOUR, 0),
    POWER_MEGA_J_PER_H_E_1(0x37, POWER, MEGA, JOULE_PER_HOUR, 1),
    VOLUME_FLOW_MILLI_L_PER_H_E_0(0x38, VOLUME_FLOW, MILLI, LITRE_PER_HOUR, 0),
    VOLUME_FLOW_MILLI_L_PER_H_E_1(0x39, VOLUME_FLOW, MILLI, LITRE_PER_HOUR, 1),
    VOLUME_FLOW_MILLI_L_PER_H_E_2(0x3A, VOLUME_FLOW, MILLI, LITRE_PER_HOUR, 2),
    VOLUME_FLOW_L_PER_H_E_0(0x3B, VOLUME_FLOW, ONE, LITRE_PER_HOUR, 0),
    VOLUME_FLOW_L_PER_H_E_1(0x3C, VOLUME_FLOW, ONE, LITRE_PER_HOUR, 1),
    VOLUME_FLOW_L_PER_H_E_2(0x3D, VOLUME_FLOW, ONE, LITRE_PER_HOUR, 2),
    VOLUME_FLOW_CBM_PER_H_E_0(0x3E, VOLUME_FLOW, ONE, CUBIC_METER_PER_HOUR, 0),
    VOLUME_FLOW_CBM_PER_H_E_1(0x3F, VOLUME_FLOW, ONE, CUBIC_METER_PER_HOUR, 1),
    VOLUME_FLOW_MICRO_L_PER_MIN_E_2(0x40, VOLUME_FLOW, MICRO, LITRE_PER_MINUTE, 2),
    VOLUME_FLOW_MILLI_L_PER_MIN_E_0(0x41, VOLUME_FLOW, MILLI, LITRE_PER_MINUTE, 0),
    VOLUME_FLOW_MILLI_L_PER_MIN_E_1(0x42, VOLUME_FLOW, MILLI, LITRE_PER_MINUTE, 1),
    VOLUME_FLOW_MILLI_L_PER_MIN_E_2(0x43, VOLUME_FLOW, MILLI, LITRE_PER_MINUTE, 2),
    VOLUME_FLOW_L_PER_MIN_E_0(0x44, VOLUME_FLOW, ONE, LITRE_PER_MINUTE, 0),
    VOLUME_FLOW_L_PER_MIN_E_1(0x45, VOLUME_FLOW, ONE, LITRE_PER_MINUTE, 1),
    VOLUME_FLOW_L_PER_MIN_E_2(0x46, VOLUME_FLOW, ONE, LITRE_PER_MINUTE, 2),
    VOLUME_FLOW_CBM_PER_MIN_E_0(0x47, VOLUME_FLOW, ONE, CUBIC_METER_PER_MINUTE, 0),
    VOLUME_FLOW_MICRO_L_PER_S_E_0(0x48, VOLUME_FLOW, MICRO, LITRE_PER_SECOND, 0),
    VOLUME_FLOW_MICRO_L_PER_S_E_1(0x49, VOLUME_FLOW, MICRO, LITRE_PER_SECOND, 1),
    VOLUME_FLOW_MICRO_L_PER_S_E_2(0x4A, VOLUME_FLOW, MICRO, LITRE_PER_SECOND, 2),
    VOLUME_FLOW_MILLI_L_PER_S_E_0(0x4B, VOLUME_FLOW, MILLI, LITRE_PER_SECOND, 0),
    VOLUME_FLOW_MILLI_L_PER_S_E_1(0x4C, VOLUME_FLOW, MILLI, LITRE_PER_SECOND, 1),
    VOLUME_FLOW_MILLI_L_PER_S_E_2(0x4D, VOLUME_FLOW, MILLI, LITRE_PER_SECOND, 2),
    VOLUME_FLOW_L_PER_S_E_0(0x4E, VOLUME_FLOW, ONE, LITRE_PER_SECOND, 0),
    VOLUME_FLOW_L_PER_S_E_1(0x4F, VOLUME_FLOW, ONE, LITRE_PER_SECOND, 1),
    MASS_FLOW_GRAMM_PER_H_E_0(0x50, MASS_FLOW, ONE, GRAMM_PER_HOUR, 0),
    MASS_FLOW_GRAMM_PER_H_E_1(0x51, MASS_FLOW, ONE, GRAMM_PER_HOUR, 1),
    MASS_FLOW_GRAMM_PER_H_E_2(0x52, MASS_FLOW, ONE, GRAMM_PER_HOUR, 2),
    MASS_FLOW_KG_PER_H_E_0(0x53, MASS_FLOW, KILO, GRAMM_PER_HOUR, 0),
    MASS_FLOW_KG_PER_H_E_1(0x54, MASS_FLOW, KILO, GRAMM_PER_HOUR, 1),
    MASS_FLOW_KG_PER_H_E_2(0x55, MASS_FLOW, KILO, GRAMM_PER_HOUR, 2),
    MASS_FLOW_T_PER_H_E_0(0x56, MASS_FLOW, ONE, TONN_PER_HOUR, 0),
    MASS_FLOW_T_PER_H_E_1(0x57, MASS_FLOW, ONE, TONN_PER_HOUR, 1),
    FLOW_TEMPERATURE_C_E__3(0x58, FLOW_TEMPERATURE, ONE, DEGREE_CELSIUS, -3),
    FLOW_TEMPERATURE_C_E__2(0x59, FLOW_TEMPERATURE, ONE, DEGREE_CELSIUS, -2),
    FLOW_TEMPERATURE_C_E__1(0x5A, FLOW_TEMPERATURE, ONE, DEGREE_CELSIUS, -1),
    FLOW_TEMPERATURE_C_E_0(0x5B, FLOW_TEMPERATURE, ONE, DEGREE_CELSIUS, 0),
    RETURN_TEMPERATURE_C_E__3(0x5C, RETURN_TEMPERATURE, ONE, DEGREE_CELSIUS, -3),
    RETURN_TEMPERATURE_C_E__2(0x5D, RETURN_TEMPERATURE, ONE, DEGREE_CELSIUS, -2),
    RETURN_TEMPERATURE_C_E__1(0x5E, RETURN_TEMPERATURE, ONE, DEGREE_CELSIUS, -1),
    RETURN_TEMPERATURE_C_E_0(0x5F, RETURN_TEMPERATURE, ONE, DEGREE_CELSIUS, 0),
    TEMPERATURE_DIFFERENCE_K_E__3(0x60, TEMPERATURE_DIFFERENCE, ONE, KELVIN, -3),
    TEMPERATURE_DIFFERENCE_K_E__2(0x61, TEMPERATURE_DIFFERENCE, ONE, KELVIN, -2),
    TEMPERATURE_DIFFERENCE_K_E__1(0x62, TEMPERATURE_DIFFERENCE, ONE, KELVIN, -1),
    TEMPERATURE_DIFFERENCE_K_E_0(0x63, TEMPERATURE_DIFFERENCE, ONE, KELVIN, 0),
    EXTERNAL_TEMPERATURE_C_E__3(0x64, EXTERNAL_TEMPERATURE, ONE, DEGREE_CELSIUS, -3),
    EXTERNAL_TEMPERATURE_C_E__2(0x65, EXTERNAL_TEMPERATURE, ONE, DEGREE_CELSIUS, -2),
    EXTERNAL_TEMPERATURE_C_E__1(0x66, EXTERNAL_TEMPERATURE, ONE, DEGREE_CELSIUS, -1),
    EXTERNAL_TEMPERATURE_C_E_0(0x67, EXTERNAL_TEMPERATURE, ONE, DEGREE_CELSIUS, 0),
    PRESSURE_MILLI_BAR_E__3(0x68, PRESSURE, MILLI, BAR, 0),
    PRESSURE_MILLI_BAR_E__2(0x69, PRESSURE, MILLI, BAR, 1),
    PRESSURE_MILLI_BAR_E__1(0x6A, PRESSURE, MILLI, BAR, 2),
    PRESSURE_BAR_E_0(0x6B, PRESSURE, ONE, BAR, 0),
    TIMEPOINT_DATE(0x6C, TIME_POINT, DATE),
    TIMEPOINT_TIME_AND_DATE(0x6D, TIME_POINT, TIME_AND_DATE),
    UNITS_FOR_H_C_A(0x6E, "Units for H.C.A."),
    RESERVED_0X6F(0x6F),
    AVERAGING_DURATION_S(0x70, AVERAGING_DURATION, SECOND),
    AVERAGING_DURATION_MIN(0x71, AVERAGING_DURATION, MINUTE),
    AVERAGING_DURATION_H(0x72, AVERAGING_DURATION, HOUR),
    AVERAGING_DURATION_D(0x73, AVERAGING_DURATION, DAY),
    ACTUALLY_DURATION_S(0x74, ACTUALLY_DURATION, SECOND),
    ACTUALLY_DURATION_MIN(0x75, ACTUALLY_DURATION, MINUTE),
    ACTUALLY_DURATION_H(0x76, ACTUALLY_DURATION, HOUR),
    ACTUALLY_DURATION_D(0x77, ACTUALLY_DURATION, DAY),
    FABRICATION_NO(0x78, "Fabrication No"),
    ENHANCED_IDENTIFICATION_RECORD(0x79, "Enhanced Identification Record"),
    BUS_ADDRESS(0x7A, "Bus Address"),
    EXTENSION_OF_VIF_CODES_FB(0x7B, "true VIF is given in the first VIFE FB extention"),
    ASCII_EXTENTION(0x7C, "ASCII EXT"),
    EXTENSION_OF_VIF_CODES_FD(0x7D, "true VIF is given in the first VIFE FD extention"),
    READOUT_SELECTION(0x7E, "Readout selection of all storage numbers, all tariffs and all VIF");

    public static VifStd valueOfTableIndex(int vifCode) {
        if (map == null) {
            map = new HashMap<Integer, VifStd>(0xFE);
            for (VifStd val : values()) {
                map.put(val.vifCode, val);
            }
        }
        return map.get(vifCode);
    }
    private final String friendlyName;
    private final SiPrefix siPrefix;
    private final UnitOfMeasurement unit;
    private final int vifCode;
    private final Integer exponent;
    private static Map<Integer, VifStd> map;

    private VifStd(int vifCode) {
        this.vifCode = vifCode;
        this.friendlyName = String.format("Reserved 0x%02x", vifCode);
        this.siPrefix = null;
        this.unit = null;
        this.exponent = null;
    }

    private VifStd(int vifCode, String friendlyName) {
        this.vifCode = vifCode;
        this.friendlyName = friendlyName;
        this.siPrefix = null;
        this.unit = null;
        this.exponent = null;
    }

    private VifStd(int vifCode, String friendlyName, SiPrefix siPrefix, UnitOfMeasurement unit, int exponent) {
        this.vifCode = vifCode;
        this.friendlyName = friendlyName;
        this.siPrefix = siPrefix;
        this.unit = unit;
        this.exponent = exponent;
    }

    private VifStd(int vifCode, String friendlyName, UnitOfMeasurement unit) {
        this.vifCode = vifCode;
        this.friendlyName = friendlyName;
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
        return friendlyName;
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

    public int getVifCode() {
        return vifCode;
    }

    @Override
    public String toString() {
        if (exponent != null) {
            return String.format("10^%d %s%s", exponent, siPrefix != null ? siPrefix : "", unit != null ? unit : "");
        } else {
            return String.format("%s%s", siPrefix != null ? siPrefix : "", unit != null ? unit : "");
        }
    }
}
