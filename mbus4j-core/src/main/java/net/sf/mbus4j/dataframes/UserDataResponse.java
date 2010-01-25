/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.dataframes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.json.JSONFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class UserDataResponse implements LongFrame, PrimaryAddress, Cloneable {

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = new JSONObject();
                    result.accumulate("controlCode", getControlCode());
            result.accumulate("manufacturer", getManufacturer());
            result.accumulate("medium", getMedium().getLabel());
            result.accumulate("version", getVersion());
            if (!isTemplate) {
            result.accumulate("identNumber", getIdentNumber());
            result.accumulate("address", getAddress() & 0xFF);
            result.accumulate("accessNumber", getAccessNumber());
            result.accumulate("acd", isAcd());
            result.accumulate("dfc", isDfc());
            result.accumulate("signature", getSignature());
            JSONArray jsonStatusArray = new JSONArray();
            for (StatusCode st : getStatus()) {
                jsonStatusArray.add(st.getLabel());
            }
            result.accumulate("status", jsonStatusArray);
            }
            JSONArray jsonDataBlocks = new JSONArray();
            for (DataBlock db: this) {
                jsonDataBlocks.add(db.toJSON(isTemplate));
            }
            result.accumulate("dataBlocks", jsonDataBlocks);
            return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
            setManufacturer(json.getString("manufacturer"));
            setMedium(MBusMedium.StdMedium.fromLabel(json.getString("medium")));
            setVersion((byte)json.getInt("version"));
            setIdentNumber(json.getInt("identNumber"));
            setAddress(json.containsKey("address") ? (byte)json.getInt("address") : 0);
            setAccessNumber(json.containsKey("accessNumber") ? (short)json.getInt("accessNumber") : 0);
            setAcd(json.containsKey("acd") ? json.getBoolean("acd") : false);
            setDfc(json.containsKey("dfc") ? json.getBoolean("dfc") : false);
            setSignature(json.containsKey("signature") ? (short)json.getInt("signature") : 0);
            if (json.containsKey("status")) {
                JSONArray statusArray = json.getJSONArray("status");
                if (statusArray.size() == 0) {
                    setStatus(new StatusCode[0]);
                } else {
                    status = new StatusCode[statusArray.size()];
                    for (int i = 0; i < status.length; i++) {
                        status[i] = StatusCode.fromLabel(statusArray.getString(i));
                    }
                }
            }
            
            JSONArray jsonDataBlocks = json.getJSONArray("dataBlocks");
            for (int i = 0; i < jsonDataBlocks.size(); i++) {
                DataBlock db = JSONFactory.createDataBlock(jsonDataBlocks.getJSONObject(i));
                db.fromJSON(jsonDataBlocks.getJSONObject(i));
                addDataBlock(db);
            }
    }

    public static enum StatusCode {
        // Taken from Chapter 6.6 Fig 27

        APPLICATION_NO_ERROR(0x00, "No application error"),
        APPLICATION_BUSY(0x01, "Application busy"),
        APPLICATION_ANY_ERROR(0x02, "Any application error"),
        APPLICATION_RESERVED(0x03, "Application reserved"),
        // Taken from Chapter 6.2 Fig 16
        POWER_LOW(0x04, "Power Low"),
        PERMANENT_ERROR(0x08, "Permanent error"),
        TEMPORARY_ERROR(0x10, "Temporary error"),
        MAN_SPEC_0X20(0x20, "Specific to manufacturer 0x20"),
        MAN_SPEC_0X40(0x40, "Specific to manufacturer 0x40"),
        MAN_SPEC_0X80(0x80, "Specific to manufacturer 0x80");

        public static byte toId(StatusCode[] values) {
            byte result = 0;
            for (StatusCode sc : values) {
                result |= sc.id;
            }
            return result;
        }
        public final byte id;
        private final String label;

        private StatusCode(int id, String description) {
            this.id = (byte) id;
            this.label = description;
        }

        @Override
        public String toString() {
            return label;
        }

        public String getLabel() {
            return label;
        }

        public static StatusCode fromLabel(String label) {
            for (StatusCode value : values()) {
                if (value.getLabel().equals(label)) {
                    return value;
                }
            }
            return valueOf(label);
        }
    }

    private boolean acd;
    private boolean dfc;
    private byte version;
    private short accessNumber;
    private StatusCode[] status;
    private short signature;
    private MBusMedium medium;
    private int identNumber;
    private String manufacturer;
    private List<DataBlock> dataBlocks = new ArrayList<DataBlock>();
    private byte address;

    public UserDataResponse() {
        super();
    }

    public UserDataResponse(boolean acd, boolean dfc) {
        this.acd = acd;
        this.dfc = dfc;
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        return dataBlocks.add(dataBlock);
    }

    public boolean  addAllDataBlocks(List<DataBlock> list) {
        return dataBlocks.addAll(list);
    }

    public void addStatus(StatusCode status) {
        if (this.status == null) {
            this.status = new StatusCode[]{status};
        } else {
            this.status = Arrays.copyOf(this.status, this.status.length + 1);
            this.status[this.status.length - 1] = status;
        }
    }

    public void clearDataBlocks() {
        dataBlocks.clear();
    }

    @Override
    public UserDataResponse clone() throws CloneNotSupportedException {
        UserDataResponse result = (UserDataResponse) super.clone();
        result.dataBlocks = new ArrayList<DataBlock>();
        result.dataBlocks.addAll(dataBlocks);
        return result;
    }

    public short getAccessNumber() {
        return accessNumber;
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
     * @param i
     * @return the dataBlocks
     */
    public DataBlock getDataBlock(int i) {
        return dataBlocks.get(i);
    }

    public int getDataBlockCount() {
        return dataBlocks.size();
    }

    public int getIdentNumber() {
        return identNumber;
    }

    @Override
    public DataBlock getLastDataBlock() {
        return dataBlocks.get(dataBlocks.size() - 1);
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public MBusMedium getMedium() {
        return medium;
    }

    public short getSignature() {
        return signature;
    }

    public StatusCode[] getStatus() {
        return status;
    }

    public byte getVersion() {
        return version;
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

    /**
     * Indicates wheter there are more Packages to follow or no.
     * @return
     */
    public boolean isLastPackage() {
        return !DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS.equals(getLastDataBlock().getDataFieldCode());
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return dataBlocks.iterator();
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        final int pos = dataBlocks.indexOf(oldDataBlock);
        dataBlocks.remove(pos);
        dataBlocks.add(pos, newDataBlock);
    }

    public void setAccessNumber(short accessNumber) {
        this.accessNumber = accessNumber;
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

    public void setIdentNumber(int identNumber) {
        this.identNumber = identNumber;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

    public void setSignature(short signature) {
        this.signature = signature;
    }

    public void setStatus(StatusCode[] status) {
        this.status = status;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isAcd = ").append(isAcd()).append('\n');
        sb.append("isDfc = ").append(isDfc()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        sb.append(String.format("ident number = %08d\n", identNumber));
        sb.append("manufacturer = ").append(manufacturer).append('\n');
        sb.append(String.format("version = 0x%02X\n", version));
        sb.append("accessnumber = ").append(accessNumber).append('\n');
        sb.append(String.format("status = %s\n", Arrays.toString(status)));
        sb.append(String.format("signature = 0x%04X\n", signature));
        sb.append("medium = ").append(medium.toString()).append('\n');
        sb.append("lastPackage = ").append(isLastPackage()).append('\n');
        for (int i = 0; i < dataBlocks.size(); i++) {
            sb.append(String.format("datablock[%d]:\n", i));
            dataBlocks.get(i).toString(sb, "  ");
        }
        return sb.toString();
    }
}
