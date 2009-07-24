/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aploese
 */
public enum VifeError implements Vife {
    NO_ERROR("No error"),
    TOO_MANY_DIFES("Too many DIFE's"),
    STORAGE_NUMBER_NOT_IMPLEMENTED("Storage number not implemented"),
    UNIT_NUMBER_NOT_IMPLEMENTED("Unit number not implemented"),
    TARIFF_NUMBER_NOT_IMPLEMENTED("Tariff number not implemented"),
    FUNCTION_NOT_IMPLEMENTED("Function not implemented"),
    DATA_CLASS_NOT_IMPLEMENTED("Data class not implemented"),
    DATA_SIZE_NOT_IMPLEMENTED("Data size not implemented"),
    DIF_ERROR_RESERVED_0X08("DIF Error Reserved 0x08"),
    DIF_ERROR_RESERVED_0X09("DIF Error Reserved 0x09"),
    DIF_ERROR_RESERVED_0X0A("DIF Error Reserved 0x0A"),
    TOO_MANY_VIFES("Too many VIFE's"),
    ILLEGAL_VIF_GROUP("Illegal VIF-Group"),
    ILLEGAL_VIF_EXPONENT("Illegal VIF-Exponent"),
    VIF_DIF_MISMATCH("VIF/DIF mismatch"),
    UNIMPLEMENTED_ACTION("Unimplemented action"),
    VIF_ERROR_RESERVED_0X10("VIF Error Reserved 0x10"),
    VIF_ERROR_RESERVED_0X11("VIF Error Reserved 0x11"),
    VIF_ERROR_RESERVED_0X12("VIF Error Reserved 0x12"),
    VIF_ERROR_RESERVED_0X13("VIF Error Reserved 0x13"),
    VIF_ERROR_RESERVED_0X14("VIF Error Reserved 0x14"),
    NO_DATA_AVAILABLE("No data available (undefined value)"),
    DATA_OVERFLOW("Data overflow"),
    DATA_UNDERFLOW(" Data underflow"),
    DATA_ERROR(" Data error"),
    DATA_ERROR_RESERVED_0X19("Data Error Reserved 0x19"),
    DATA_ERROR_RESERVED_0X1A("Data Error Reserved 0x1A"),
    DATA_ERROR_RESERVED_0X1B("Data Error Reserved 0x1B"),
    PREMATURE_END_OF_RECORD("Premature end of record"),
    OTHER_ERROR_RESERVED_0X1D("Other Error Reserved 0x1D"),
    OTHER_ERROR_RESERVED_0X1E("Other Error Reserved 0x1E"),
    OTHER_ERROR_RESERVED_0X1F("Other Error Reserved 0x1F");

    private final String name;
    private static Map<Byte, VifeError> map;

    private VifeError(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

        public static VifeError valueOfTableIndex(byte ordinal) {
        if (map == null) {
            map = new HashMap<Byte, VifeError>(values().length);
            for (VifeError val : values()) {
                map.put((byte)val.ordinal(), val);
            }
        }
        return map.get(ordinal);
    }

            public byte getTableIndex() {
        return (byte)ordinal();
    }



}
