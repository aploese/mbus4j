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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
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

    private final String label;
    private static Map<Byte, VifeError> map;

    public static VifeError valueOfTableIndex(byte ordinal) {
        if (map == null) {
            map = new HashMap<Byte, VifeError>(values().length);
            for (VifeError val : values()) {
                map.put((byte) val.ordinal(), val);
            }
        }
        return map.get(ordinal);
    }

    private VifeError(String label) {
        this.label = label;
    }

    public byte getTableIndex() {
        return (byte) ordinal();
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public VifeTypes getVifeType() {
        return VifeTypes.ERROR;
    }

    public static VifeError fromLabel(String label) {
        for (VifeError value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }

}
