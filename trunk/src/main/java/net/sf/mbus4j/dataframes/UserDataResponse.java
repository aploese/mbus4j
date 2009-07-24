/*
 *
 * $Id: UserDataResponse.java 407 2009-03-19 08:38:27Z aploese $
 *
 * @author aploese
 *
 */
package net.sf.mbus4j.dataframes;

import net.sf.mbus4j.dataframes.datablocks.*;
import net.sf.mbus4j.dataframes.*;
import java.util.Iterator;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.mbus4j.dataframes.Frame.ControlCode;

/**
 *
 * @author aploese
 */
public class UserDataResponse implements LongFrame, PrimaryAddress, Cloneable {

    private boolean lastPackage = true;
    private boolean acd;
    private boolean dfc;

    public UserDataResponse() {
        super();
    }

    public UserDataResponse(boolean acd, boolean dfc) {
        this.acd = acd;
        this.dfc = dfc;
    }

    @Override
    public DataBlock getLastDataBlock() {
        return dataBlocks.get(dataBlocks.size() - 1);
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        final int pos = dataBlocks.indexOf(oldDataBlock);
        dataBlocks.remove(pos);
        dataBlocks.add(pos, newDataBlock);
    }

    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

    @Override
    public UserDataResponse clone() throws CloneNotSupportedException {
        UserDataResponse result = (UserDataResponse) super.clone();
        result.dataBlocks = new ArrayList<DataBlock>();
        result.dataBlocks.addAll(dataBlocks);
        return result;
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

    @Override
    public ControlCode getControlCode() {
        return ControlCode.RSP_UD;
    }

    /**
     * @return the acd
     */
    public boolean isAcd() {
        return acd;
    }

    /**
     * @param acd the acd to set
     */
    public void setAcd(boolean acd) {
        this.acd = acd;
    }

    /**
     * @return the dfc
     */
    public boolean isDfc() {
        return dfc;
    }

    /**
     * @param dcf the dfc to set
     */
    public void setDfc(boolean dfc) {
        this.dfc = dfc;
    }

    @Override
    public boolean  addDataBlock(DataBlock dataBlock) {
        return dataBlocks.add(dataBlock);
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return dataBlocks.iterator();
    }

    public void clearDataBlocks() {
        dataBlocks.clear();
    }

    public enum StatusCode {
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


        public final byte id;
        private final String description;

        private StatusCode(int id, String description) {
            this.id = (byte)id;
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

        public static byte toId(StatusCode[] values) {
            byte result = 0;
            for (StatusCode sc : values) {
                result |= sc.id;
            }
            return result;
        }
    }

    private byte version;
    private short accessNumber;
    private StatusCode[] status;
    private short signature;
    private MBusMedium medium;
    private int identNumber;
    private String manufacturer;
    private List<DataBlock> dataBlocks = new ArrayList<DataBlock>();
    private byte address;

    /**
     * @return the address
     */
    @Override
    public byte getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    public void setIdentNumber(int identNumber) {
        this.identNumber = identNumber;
    }

    public int getIdentNumber() {
        return identNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public byte getVersion() {
        return version;
    }

    public short getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(short accessNumber) {
        this.accessNumber = accessNumber;
    }

    public StatusCode[] getStatus() {
        return status;
    }

    public void setStatus(StatusCode[] status) {
        this.status = status;
    }

    public void addStatus(StatusCode status) {
        if (this.status == null) {
            this.status = new StatusCode[]{status};
        } else {
        this.status = Arrays.copyOf(this.status, this.status.length + 1);
        this.status[this.status.length - 1] = status;
        }
    }

    public short getSignature() {
        return signature;
    }

    public void setSignature(short signature) {
        this.signature = signature;
    }

    public MBusMedium getMedium() {
        return medium;
    }

    @Override
    public void setLastPackage(boolean lastPackage) {
        this.lastPackage = lastPackage;
    }

    /**
     * Indicates wheter there are more Packages to follow or no.
     * @return
     */
    public boolean isLastPackage() {
        return lastPackage;
    }

    public String toJavaClassString() throws IOException {
        InputStream is = UserDataResponse.class.getResourceAsStream(
                "MBusMedium.template");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        String classname = getManufacturer() + "_" + getMedium() + "_V_" +
                getVersion();
        while (null != (line = br.readLine())) {
            if (line.indexOf("$classname$") > 0) {
                sb.append(line.replace("$classname$", classname));
                sb.append('\n');
            } else if (line.indexOf("$manufactor$") > 0) {
                sb.append(line.replace("$manufactor$", getManufacturer()));
                sb.append('\n');
            } else if (line.indexOf("$medium$") > 0) {
                sb.append(line.replace("$medium$", getMedium().toString()));
                sb.append('\n');
            } else if (line.indexOf("$version$") > 0) {
                sb.append(line.replace("$version$", Integer.toString(
                        getVersion())));
                sb.append('\n');
            } else if (line.indexOf("$blockcount$") > 0) {
                sb.append(line.replace("$blockcount$", Integer.toString(
                        dataBlocks.size())));
                sb.append('\n');
            } else if (line.indexOf("$blockclass$") > 0) {
                for (int i = 0; i < dataBlocks.size(); i++) {
                    DataBlock db = dataBlocks.get(i);
                    String blockLine;
                    blockLine = line.replace("$index$", Integer.toString(i));
                    blockLine = line.replace("$blockclass$", db.getClass().
                            getSimpleName());
                    blockLine = blockLine.replace("$paramDescription$", db.getParamDescr() != null ? db.getParamDescr() : "");
                    blockLine = blockLine.replace("$unitOfMeasurement$", db.getUnitOfMeasurement() != null ? db.getUnitOfMeasurement().toString() : "");
                    blockLine = blockLine.replace("$exponent$", db.getExponent() != null
                            ? db.getExponent().toString() : "0");
                    blockLine = blockLine.replace("$tariff$", Integer.toString(
                            db.getTariff()));
                    blockLine = blockLine.replace("$storageNo$", Long.toString(
                            db.getStorageNumber()));
                    sb.append(
                            blockLine);
                    sb.append('\n');
                }
            } else {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isAcd = ").append(isAcd()).append('\n');
        sb.append("isDcf = ").append(isDfc()).append('\n');
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

    public void setVersion(byte version) {
        this.version = version;
    }
}
