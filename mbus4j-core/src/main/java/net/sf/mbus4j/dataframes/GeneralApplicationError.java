package net.sf.mbus4j.dataframes;

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
import java.util.Iterator;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class GeneralApplicationError implements LongFrame, PrimaryAddress, ResponseFrame {

    public static String RSP_UD_SUBTYPE = "general application error";

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        result.accumulate("controlCode", getControlCode());
        result.accumulate("subType", RSP_UD_SUBTYPE);
        if (jsonSerializeType.ALL == jsonSerializeType) {
            result.accumulate("acd", isAcd());
            result.accumulate("dfc", isDfc());
            result.accumulate("address", address & 0xFF);
            result.accumulate("error", error.getLabel());
        }
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        acd = json.getBoolean("acd");
        dfc = json.getBoolean("dfc");
        address = (byte) json.getInt("address");
        error = GeneralApplicationErrorEnum.fromLabel(json.getString("error"));
    }

    public static enum GeneralApplicationErrorEnum {

        UNSPECIFIED_ERROR(0x00, "Unspecified error"),
        UNIMPLEMENTED_CI_FIELD(0x01, "unimplemented ci-field"),
        BUFFER_TOO_LONG(0x02, "buffer too long, truncated"),
        TOO_MANX_RECORDS(0x03, "too manx records"),
        REMATURE_END_OF_RECORDS(0x04, "premature end of records"),
        MORE_THAN_10_DIFES(0x05, "more tham 10 DIFE's"),
        MORE_THAN_10_VIFES(0x06, "more tham 10 VIFE's"),
        RESERVED_0X07(0x07, "Reserved 0x07"),
        APP_TOO_BUSY_FOR_HANDLING_READOUT_REQUEST(0x08, "application to busy for handling readout request"),
        TOO_MANY_READOUTS(0x09, "too many readouts(for slaves with limited readouts per time");

        private static GeneralApplicationErrorEnum valueOf(int id) {
            for (GeneralApplicationErrorEnum e : GeneralApplicationErrorEnum.values()) {
                if (e.id == id) {
                    return e;
                }
            }
            return null;
        }
        public final byte id;
        private final String label;

        private GeneralApplicationErrorEnum(int id, String label) {
            this.id = (byte) id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }

        public String getLabel() {
            return label;
        }

        public static GeneralApplicationErrorEnum fromLabel(String label) {
            for (GeneralApplicationErrorEnum value : values()) {
                if (value.label.equals(label)) {
                    return value;
                }
            }
            return valueOf(label);
        }
    }
    private boolean acd;
    private boolean dfc;
    private byte address;
    private GeneralApplicationErrorEnum error;

    public GeneralApplicationError() {
        super();
    }

    public GeneralApplicationError(UserDataResponse old) {
        this.acd = old.isAcd();
        this.dfc = old.isDfc();
        this.address = old.getAddress();
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the address
     */
    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.RSP_UD;
    }

    /**
     * @return the error
     */
    public GeneralApplicationErrorEnum getError() {
        return error;
    }

    @Override
    public DataBlock getLastDataBlock() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the acd
     */
    public boolean isAcd() {
        return acd;
    }

    /**
     * @return the dfc
     */
    public boolean isDfc() {
        return dfc;
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return new Iterator<DataBlock>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public DataBlock next() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param acd the acd to set
     */
    public void setAcd(boolean acd) {
        this.acd = acd;
    }

    /**
     * @param address the address to set
     */
    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    /**
     * @param dcf the dfc to set
     */
    public void setDfc(boolean dfc) {
        this.dfc = dfc;
    }

    public void setError(byte b) {
        error = GeneralApplicationErrorEnum.valueOf(b);
    }

    /**
     * @param error the error to set
     */
    public void setError(GeneralApplicationErrorEnum error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("acd = ").append(isAcd()).append('\n');
        sb.append("dfc = ").append(isDfc()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        sb.append("error = ").append(error).append('\n');
        return sb.toString();
    }
}
