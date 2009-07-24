/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes.datablocks.vif;

import java.util.HashMap;
import java.util.Map;
import static net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement.*;
import static net.sf.mbus4j.dataframes.datablocks.vif.Vife.*;

/**
 *
 * @author aploese
 */
public enum VifeStd implements Vife {

    PER_SECOND("/s"),
    PER_MINUTE("/min"),
    PER_HOUR("/h"),
    PER_DAY("/d"),
    PER_WEEK("/week"),
    PER_MONTH("/month"),
    PER_YEAR("/year"),
    PER_REVOLUTION("per revolution / measurement"),
    INCREMENT_INPUT_0("increment per input pulse on input channel #0"),
    INCREMENT_INPUT_1("increment per input pulse on input channel #1"),
    INCREMENT_OUTPUT_0("increment per output pulse on output channel #0"),
    INCREMENT_OUTPUT_1("increment per output pulse on output channel #1"),
    PER_LITER("/l"),
    PER_CUBIC_METER("/m³"),
    PER_KG("/kg"),
    PER_KELVIN("per K"),
    PER_KWH("/kWh"),
    PER_GJ("/GJ"),
    PER_KW("/kW"),
    PER_KELVIN_MUL_LITER("/(K*l)"),
    PER_VOLT("/V"),
    PER_AMPERE("/A"),
    MUL_BY_SEC("* s"),
    MUL_BY_SEC_PER_VOLT("* s/V"),
    MUL_BY_SEC_PER_AMPERE("* s/A"),
    START_DATE_TIME_OF("start date(/time) of"),
    UNCORRECTED_UNIT("VIF contains uncorrected unit instead of corrected unit"),
    ACCUMULATION_POSITIVE("Accumulation only if positive contributions"),
    ACCUMULATION_ABS_NEGATIVE("Accumulation of abs value only if negative contributions"),
    RESERVED_0X3D(),
    RESERVED_0X3E(),
    RESERVED_0X3F(),
    LOWER_LIMIT("lower limit value"),
    EXCEEDS_LOWER_LIMIT("# of exceeds of lower limit"),
    TIMESTAMP_OF_BEGIN_FIRST_LOWER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, BEGIN, FIRST, LOWER)),
    TIMESTAMP_OF_END_FIRST_LOWER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, END, FIRST, LOWER)),
    RESERVED_0X44(),
    RESERVED_0X45(),
    TIMESTAMP_BEGIN_LAST_LOWER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, BEGIN, LAST, LOWER)),
    TIMESTAMP_END_LAST_LOWER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, END, LAST, LOWER)),
    RESERVED_0X48(),
    RESERVED_0X49(),
    TIMESTAMP_BEGIN_FIRST_UPPER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, BEGIN, FIRST, UPPER)),
    TIMESTAMP_END_FIRST_UPPER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, END, FIRST, UPPER)),
    RESERVED_0X4C(),
    RESERVED_0X4D(),
    TIMESTAMP_BEGIN_LAST_UPPER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, BEGIN, LAST, UPPER)),
    TIMESTAMP_END_LAST_UPPER(String.format(TIMESTAMP_OF_LIMIT_EXCEED, END, LAST, UPPER)),
    //    0x50
    DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_S(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, LOWER, SECOND)),
    DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_MIN(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, LOWER, MINUTE)),
    DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_H(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, LOWER, HOUR)),
    DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_D(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, LOWER, DAY)),
    DURATION_OF_LIMIT_EXCEED_LAST_LOWER_S(String.format(DURATION_OF_LIMIT_EXCEED, LAST, LOWER, SECOND)),
    DURATION_OF_LIMIT_EXCEED_LAST_LOWER_MIN(String.format(DURATION_OF_LIMIT_EXCEED, LAST, LOWER, MINUTE)),
    DURATION_OF_LIMIT_EXCEED_LAST_LOWER_H(String.format(DURATION_OF_LIMIT_EXCEED, LAST, LOWER, HOUR)),
    DURATION_OF_LIMIT_EXCEED_LAST_LOWER_D(String.format(DURATION_OF_LIMIT_EXCEED, LAST, LOWER, DAY)),
    DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_S(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, UPPER, SECOND)),
    DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_MIN(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, UPPER, MINUTE)),
    DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_H(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, UPPER, HOUR)),
    DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_D(String.format(DURATION_OF_LIMIT_EXCEED, FIRST, UPPER, DAY)),
    DURATION_OF_LIMIT_EXCEED_LAST_UPPER_S(String.format(DURATION_OF_LIMIT_EXCEED, LAST, UPPER, SECOND)),
    DURATION_OF_LIMIT_EXCEED_LAST_UPPER_MIN(String.format(DURATION_OF_LIMIT_EXCEED, LAST, UPPER, MINUTE)),
    DURATION_OF_LIMIT_EXCEED_LAST_UPPER_H(String.format(DURATION_OF_LIMIT_EXCEED, LAST, UPPER, HOUR)),
    DURATION_OF_LIMIT_EXCEED_LAST_UPPER_D(String.format(DURATION_OF_LIMIT_EXCEED, LAST, UPPER, DAY)),
    //0x60
    //  DURATION_OF
    DURATION_OF_FIRST_S(String.format(DURATION_OF, FIRST, SECOND)),
    DURATION_OF_FIRST_MIN(String.format(DURATION_OF, FIRST, MINUTE)),
    DURATION_OF_FIRST_H(String.format(DURATION_OF, FIRST, HOUR)),
    DURATION_OF_FIRST_D(String.format(DURATION_OF, FIRST, DAY)),
    DURATION_OF_LAST_S(String.format(DURATION_OF, LAST, SECOND)),
    DURATION_OF_LAST_MIN(String.format(DURATION_OF, LAST, MINUTE)),
    DURATION_OF_LAST_H(String.format(DURATION_OF, LAST, HOUR)),
    DURATION_OF_LAST_D(String.format(DURATION_OF, LAST, DAY)),
    RESERVED_0X68(),
    RESERVED_0X69(),
    TIMESTAMP_BEGIN_OF_FIRST(String.format(TIMESTAMP_OF, BEGIN, FIRST)),
    TIMESTAMP_END_OF_FIRST(String.format(TIMESTAMP_OF, END, FIRST)),
    RESERVED_0X6C(),
    RESERVED_0X6D(),
    TIMESTAMP_BEGIN_OF_LAST(String.format(TIMESTAMP_OF, BEGIN, END)),
    TIMESTAMP_END_OF_LAST(String.format(TIMESTAMP_OF, END, LAST)),
    FACTOR_E__6(FACTOR + " 0.000001"),
    FACTOR_E__5(FACTOR + " 0.00001"),
    FACTOR_E__4(FACTOR + " 0.0001"),
    FACTOR_E__3(FACTOR + " 0.001"),
    FACTOR_E__2(FACTOR + " 0.01"),
    FACTOR_E__1(FACTOR + " 0.1"),
    FACTOR_E_0(FACTOR + " 1"),
    FACTOR_E_1(FACTOR + " 10"),
    CONST_E__3(CONST + "0.001"),
    CONST_E__2(CONST + "0.01"),
    CONST_E__1(CONST + "0.1"),
    CONST_E_0(CONST + " 1"),
    RESERVED_0X7C(),
    FACTOR_E_3(FACTOR + " 1000"),
    FUTUR_VALUE("future value"),
    MAN_SPEC("next VIFE's and data of this block are manufacturer specific");
    private final String friendlyName;
    private static Map<Byte, VifeStd> map;

    private VifeStd(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    private VifeStd() {
        this.friendlyName = String.format("Reserved 0x%02x", ordinal() + 0x20);
    }

    @Override
    public String toString() {
        return friendlyName;
    }

    public static VifeStd valueOfTableIndex(byte ordinal) {
        if (map == null) {
            map = new HashMap<Byte, VifeStd>(values().length);
            for (VifeStd val : values()) {
                map.put((byte) (val.ordinal() + 0x20), val);
//                System.out.println(String.format("0x%02x %s", val.ordinal() + 0x20, val));
            }
        }
        return map.get(ordinal);
    }

    public byte getTableIndex() {
        return (byte) (ordinal() + 0x20);
    }
}
