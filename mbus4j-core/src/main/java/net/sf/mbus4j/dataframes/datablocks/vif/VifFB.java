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
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.GIGA;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.KILO;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MEGA;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.MILLI;
import static net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix.ONE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.AMERICAN_GALLON;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.AMERICAN_GALLON_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.AMERICAN_GALLON_PER_MINUTE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.CUBIC_FEET;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.CUBIC_METER;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DEGREE_CELSIUS;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DEGREE_FAHRENHEIT;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.JOULE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.JOULE_PER_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.TONNS;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.WATT;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.DEGREE;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.VAR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.VA;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.WATT_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.VAR_HOUR;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.HERTZ;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum VifFB implements Vif {

// DO NOT CHANGE ORDER!!!!
    ENERGY_KWH_E_2(ENERGY, KILO, WATT_HOUR, 2),
    ENERGY_MWH_E_0(ENERGY, MEGA, WATT_HOUR, 0),
    REACTIVE_ENERGY_KVARH_E_0(REACTIVE_ENERGY, KILO, VAR_HOUR, 0),
    REACTIVE_ENERGY_KVARH_E_1(REACTIVE_ENERGY, KILO, VAR_HOUR, 1),
    RESERVED_0X04(),
    RESERVED_0X05(),
    RESERVED_0X06(),
    RESERVED_0X07(),
    ENERGY_MJ_2(ENERGY, MEGA, JOULE, 2),
    ENERGY_GJ_0(ENERGY, GIGA, JOULE, 0),
    RESERVED_0X0A(),
    RESERVED_0X0B(),
    RESERVED_0X0C(),
    RESERVED_0X0D(),
    RESERVED_0X0E(),
    RESERVED_0X0F(),
    VOLUME_CBM_E_2(VOLUME, ONE, CUBIC_METER, 2),
    VOLUME_CBM_E_3(VOLUME, ONE, CUBIC_METER, 3),
    RESERVED_0X12(),
    RESERVED_0X13(),
    REACTIVE_POWER_VAR_E_0(REACTIVE_POWER, ONE, VAR, 0),
    RESERVED_0X15(),
    RESERVED_0X16(),
    RESERVED_0X17(),
    MASS_TONN_E_2(MASS, ONE, TONNS, 2),
    MASS_KILO_TONN(MASS, KILO, TONNS, 3),
    RESERVED_0X1A(),
    RESERVED_0X1B(),
    RESERVED_0X1C(),
    RESERVED_0X1D(),
    RESERVED_0X1E(),
    RESERVED_0X1F(),
    RESERVED_0X20(),
    VOLUME_CBF_E__1(VOLUME, ONE, CUBIC_FEET, -1),
    VOLUME_AM_GALLON_E__1(VOLUME, ONE, AMERICAN_GALLON, -1),
    VOLUME_AM_GALLON_E_0(VOLUME, ONE, AMERICAN_GALLON, 0),
    VOLUME_FLOW_AM_GALLON_PER_MINUTE_E__3(VOLUME_FLOW, ONE, AMERICAN_GALLON_PER_MINUTE, -3),
    VOLUME_FLOW_AM_GALLON_PER_MINUTE_E_0(VOLUME_FLOW, ONE, AMERICAN_GALLON_PER_MINUTE, 0),
    VOLUME_FLOW_AM_GALLON_PER_HOUR_E_0(VOLUME_FLOW, ONE, AMERICAN_GALLON_PER_HOUR, 0),
    RESERVED_0X27(),
    POWER_KW_E_2(POWER, KILO, WATT, 2),
    POWER_MW_E_0(POWER, MEGA, WATT, 0),
    RESERVED_0X2A(),
    PHASE_U_I_E__1_DEGREE(PHASE_U_I, ONE, DEGREE, -1),
    RESERVED_0X2C(),
    RESERVED_0X2D(),
    FREQUENCY_HERTZ_E__1(FREQUENCY, ONE, HERTZ, -1),
    RESERVED_0X2F(),
    POWER_MEGA_JOULE_PER_HOUR_E_2(POWER, MEGA, JOULE_PER_HOUR, 2),
    POWER_GJ_PER_HOUR_E_0(POWER, GIGA, JOULE_PER_HOUR, 0),
    RESERVED_0X32(),
    RESERVED_0X33(),
    RESERVED_0X34(),
    RESERVED_0X35(),
    RESERVED_0X36(),
    RESERVED_0X37(),
    RESERVED_0X38(),
    RESERVED_0X39(),
    RESERVED_0X3A(),
    RESERVED_0X3B(),
    RESERVED_0X3C(),
    RESERVED_0X3D(),
    RESERVED_0X3E(),
    RESERVED_0X3F(),
    RESERVED_0X40(),
    RESERVED_0X41(),
    RESERVED_0X42(),
    RESERVED_0X43(),
    RESERVED_0X44(),
    RESERVED_0X45(),
    RESERVED_0X46(),
    RESERVED_0X47(),
    RESERVED_0X48(),
    RESERVED_0X49(),
    RESERVED_0X4A(),
    RESERVED_0X4B(),
    RESERVED_0X4C(),
    RESERVED_0X4D(),
    RESERVED_0X4E(),
    RESERVED_0X4F(),
    RESERVED_0X50(),
    RESERVED_0X51(),
    RESERVED_0X52(),
    RESERVED_0X53(),
    COMPLEX_POWER_VA_E__1(COMPLEX_POWER, ONE, VA, -1),
    RESERVED_0X55(),
    RESERVED_0X56(),
    RESERVED_0X57(),
    FLOW_TEMPERATURE_MILLI_F_E_0(FLOW_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 0),
    FLOW_TEMPERATURE_MILLI_F_E_1(FLOW_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 1),
    FLOW_TEMPERATURE_MILLI_F_E_2(FLOW_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 2),
    FLOW_TEMPERATURE_F_E_0(FLOW_TEMPERATURE, ONE, DEGREE_FAHRENHEIT, 0),
    RETURN_TEMPERATURE_MILLI_F_E_0(RETURN_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 0),
    RETURN_TEMPERATURE_MILLI_F_E_1(RETURN_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 1),
    RETURN_TEMPERATURE_MILLI_F_E_2(RETURN_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 2),
    RETURN_TEMPERATURE_F_E_0(RETURN_TEMPERATURE, ONE, DEGREE_FAHRENHEIT, 0),
    TEMPERATURE_DIFFERENCE_MILLI_F_E_0(TEMPERATURE_DIFFERENCE, MILLI, DEGREE_FAHRENHEIT, 0),
    TEMPERATURE_DIFFERENCE_MILLI_F_E_1(TEMPERATURE_DIFFERENCE, MILLI, DEGREE_FAHRENHEIT, 1),
    TEMPERATURE_DIFFERENCE_MILLI_F_E_2(TEMPERATURE_DIFFERENCE, MILLI, DEGREE_FAHRENHEIT, 2),
    TEMPERATURE_DIFFERENCE_F_E_0(TEMPERATURE_DIFFERENCE, ONE, DEGREE_FAHRENHEIT, 0),
    EXTERNAL_TEMPERATURE_MILLI_F_E_0(EXTERNAL_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 0),
    EXTERNAL_TEMPERATURE_MILLI_F_E_1(EXTERNAL_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 1),
    EXTERNAL_TEMPERATURE_MILLI_F_E_2(EXTERNAL_TEMPERATURE, MILLI, DEGREE_FAHRENHEIT, 2),
    EXTERNAL_TEMPERATURE_F_E_0(EXTERNAL_TEMPERATURE, ONE, DEGREE_FAHRENHEIT, 0),
    RESERVED_0X68(),
    RESERVED_0X69(),
    RESERVED_0X6A(),
    RESERVED_0X6B(),
    RESERVED_0X6C(),
    RESERVED_0X6D(),
    RESERVED_0X6E(),
    RESERVED_0X6F(),
    COLD_OR_WARM_TEMPERATURE_LIMIT_MILLI_F_E_0(COLD_OR_WARM_TEMPERATURE_LIMIT, MILLI, DEGREE_FAHRENHEIT, 0),
    COLD_OR_WARM_TEMPERATURE_LIMIT_MILLI_F_E_1(COLD_OR_WARM_TEMPERATURE_LIMIT, MILLI, DEGREE_FAHRENHEIT, 1),
    COLD_OR_WARM_TEMPERATURE_LIMIT_MILLI_F_E_2(COLD_OR_WARM_TEMPERATURE_LIMIT, MILLI, DEGREE_FAHRENHEIT, 2),
    COLD_OR_WARM_TEMPERATURE_LIMIT_F_E_0(COLD_OR_WARM_TEMPERATURE_LIMIT, ONE, DEGREE_FAHRENHEIT, 0),
    COLD_OR_WARM_TEMPERATURE_LIMIT_MILLI_C_E_0(COLD_OR_WARM_TEMPERATURE_LIMIT, MILLI, DEGREE_CELSIUS, 0),
    COLD_OR_WARM_TEMPERATURE_LIMIT_MILLI_C_E_1(COLD_OR_WARM_TEMPERATURE_LIMIT, MILLI, DEGREE_CELSIUS, 1),
    COLD_OR_WARM_TEMPERATURE_LIMIT_MILLI_C_E_2(COLD_OR_WARM_TEMPERATURE_LIMIT, MILLI, DEGREE_CELSIUS, 2),
    COLD_OR_WARM_TEMPERATURE_LIMIT_C_E_0(COLD_OR_WARM_TEMPERATURE_LIMIT, ONE, DEGREE_CELSIUS, 0),
    CUMUL_COUNT_MAX_POWER_MILLI_W_E_0(CUMUL_COUNT_MAX_POWER, MILLI, WATT, 0),
    CUMUL_COUNT_MAX_POWER_MILLI_W_E_1(CUMUL_COUNT_MAX_POWER, MILLI, WATT, 1),
    CUMUL_COUNT_MAX_POWER_MILLI_W_E_2(CUMUL_COUNT_MAX_POWER, MILLI, WATT, 2),
    CUMUL_COUNT_MAX_POWER_W_E_0(CUMUL_COUNT_MAX_POWER, ONE, WATT, 0),
    CUMUL_COUNT_MAX_POWER_W_E_1(CUMUL_COUNT_MAX_POWER, ONE, WATT, 1),
    CUMUL_COUNT_MAX_POWER_W_E_2(CUMUL_COUNT_MAX_POWER, ONE, WATT, 2),
    CUMUL_COUNT_MAX_POWER_KILO_W_E_0(CUMUL_COUNT_MAX_POWER, KILO, WATT, 0),
    CUMUL_COUNT_MAX_POWER_KILO_W_E_1(CUMUL_COUNT_MAX_POWER, KILO, WATT, 1);

    public static VifFB valueOfTableIndex(byte ordinal) {
        if (map == null) {
            map = new HashMap<Byte, VifFB>(0xFE);
            for (VifFB val : values()) {
                map.put((byte) val.ordinal(), val);
            }
        }
        return map.get(ordinal);
    }
    private final String label;
    private final UnitOfMeasurement unit;
    private final SiPrefix siPrefix;
    private final Integer exponent;
    private static Map<Byte, VifFB> map;

    private VifFB() {
        this.label = String.format("VifFB Reserved 0x%02x", ordinal());
        this.siPrefix = null;
        this.unit = null;
        this.exponent = null;
    }

    private VifFB(String label, SiPrefix siPrefix, UnitOfMeasurement unit, int exponent) {
        this.label = label;
        this.siPrefix = siPrefix;
        this.unit = unit;
        this.exponent = exponent;
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

    public static VifFB assemble(String label, UnitOfMeasurement unitOfMeasurement, SiPrefix siPrefix, Integer exponent) {
        for (VifFB value : values()) {
            if (value.getLabel().equals(label) && ((unitOfMeasurement == value.getUnitOfMeasurement()) || ((unitOfMeasurement != null) && unitOfMeasurement.equals(value.getUnitOfMeasurement()))) && ((siPrefix == value.getSiPrefix()) || ((siPrefix != null) && siPrefix.equals(value.getSiPrefix()))) && ((exponent == value.getExponent()) || ((exponent != null) && exponent.equals(value.getExponent())))) {
                return value;
            }
        }
        return valueOf(label);
    }

    @Override
    public VifTypes getVifType() {
        return VifTypes.FB_EXTENTION;
    }
}
