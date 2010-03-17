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
package net.sf.mbus4j.slaves;

import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.MBusConstants;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.UserDataResponse.StatusCode;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slave implements Serializable, MBusResponseFramesContainer, JSONSerializable {

    private final static Logger log = LoggerFactory.getLogger(Slave.class);
    /**
     * Template for creating a valid udr - no datablocks.
     */
    private boolean networkSelected;
    protected List<ResponseFrameContainer> responseFrameContainers = new ArrayList<ResponseFrameContainer>();
    private StatusCode[] status;
    private byte version;
    private MBusMedium medium;
    private byte address;
    private int identNumber;
    private String manufacturer;
    private short signature;
    private short accessNumber;
    private boolean acd;
    private boolean dfc;
    private int selectedIndex;

    public Slave() {
        super();
    }

    public Slave(int primaryAddress, int id, String man, int version, MBusMedium medium) {
        this();
        networkSelected = false;
        setAddress((byte) primaryAddress);
        setIdentNumber(id);
        setManufacturer(man);
        setVersion((byte) version);
        setMedium(medium);
        setStatus(new UserDataResponse.StatusCode[]{UserDataResponse.StatusCode.APPLICATION_NO_ERROR});
        selectedIndex = -1;
    }

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public int getIdentNumber() {
        return identNumber;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public MBusMedium getMedium() {
        return medium;
    }

    @Override
    public byte getVersion() {
        return version;
    }

    @Override
    public StatusCode[] getStatus() {
        return status;
    }

    @Override
    public void setStatus(StatusCode[] status) {
        this.status = status;
    }

    @Override
    public short getSignature() {
        return signature;
    }

    @Override
    public void setSignature(short signature) {
        this.signature = signature;
    }

    public Frame handleApplicationReset(ApplicationReset applicationReset) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleReqUd1(RequestClassXData requestClassXData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Frame handleReqUd2(RequestClassXData request) {
        if (getSelectedResponseFrame() instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse) getSelectedResponseFrame();
            udr.setAccessNumber(accessNumber++);
            udr.setAcd(acd);
            udr.setAddress(address);
            udr.setDfc(dfc);
            udr.setIdentNumber(identNumber);
            udr.setManufacturer(manufacturer);
            udr.setMedium(medium);
            udr.setSignature(signature);
            udr.setStatus(status);
            udr.setVersion(version);
            return udr;
        } else if (getSelectedResponseFrame() == null) {
            UserDataResponse udr = new UserDataResponse();
            udr.setAccessNumber(accessNumber++);
            udr.setAcd(acd);
            udr.setAddress(address);
            udr.setDfc(dfc);
            udr.setIdentNumber(identNumber);
            udr.setManufacturer(manufacturer);
            udr.setMedium(medium);
            udr.setSignature(signature);
            udr.setStatus(status);
            udr.setVersion(version);
            return udr;
        } else {
            throw new RuntimeException("Unknown Response class: " + getSelectedResponseFrame().getClass().getName());
        }
    }

    public Frame handleSendInitSlave(SendInitSlave sendInitSlave) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleSendUserData(SendUserData sendUserData) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleSendUserDataManSpec(SendUserDataManSpec sendUserDataManSpec) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public void setIdentNumber(int id) {
        this.identNumber = id;
    }

    @Override
    public void setManufacturer(String man) {
        this.manufacturer = man;
    }

    @Override
    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

    @Override
    public void setVersion(byte version) {
        this.version = version;
    }

    /**
     *
     * @param primaryAddress
     * @return
     */
    public boolean willHandleRequest(Frame frame) {
        if (frame instanceof SelectionOfSlaves) {
            SelectionOfSlaves selectionOfSlaves = (SelectionOfSlaves) frame;
            log.info("will handle SelectionOfSlaves: " + ((selectionOfSlaves.getAddress() & 0xFF) == MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS));
            return (selectionOfSlaves.getAddress() & 0xFF) == MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS;
        } else if (frame instanceof PrimaryAddress) {
            int primaryAddress = ((PrimaryAddress) frame).getAddress() & 0xFF;
            return willHandleByAddress(primaryAddress);
        } else if (frame instanceof RequestClassXData) {
            int primaryAddress = ((RequestClassXData) frame).getAddress() & 0xFF;
            return willHandleByAddress(primaryAddress);
        } else {
            return false;
        }
    }

    Frame handleSelectionOfSlaves(SelectionOfSlaves selectionOfSlaves) {
        if ((selectionOfSlaves.getAddress() & 0xFF) != MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS) {
            log.warn("NETWORK SELECT ERROR");
            return null;
        }
        if (selectionOfSlaves.matchAll(getIdentNumber(), getManufacturer(), getMedium(), getVersion())) {
            networkSelected = true;
            log.info("Network selected: " + slaveIdToString());
            return SingleCharFrame.SINGLE_CHAR_FRAME;
        } else {
            log.info("Network deselected: " + slaveIdToString());
            networkSelected = false;
            return null;
        }
    }

    /**
     * @return the networkSelected
     */
    public boolean isNetworkSelected() {
        return networkSelected;
    }

    /**
     * @param networkSelected the networkSelected to set
     */
    public void setNetworkSelected(boolean networkSelected) {
        this.networkSelected = networkSelected;
    }

    public String slaveIdToString() {
        return String.format("address = 0x%02x, id = %08d, man = %s, medium = %s, version = 0x%02X", getAddress(), getIdentNumber(), getManufacturer(), getMedium(), getVersion());
    }

    private boolean willHandleByAddress(int primaryAddress) {
        return primaryAddress == MBusConstants.BROADCAST_NO_ANSWER_PRIMARY_ADDRESS || primaryAddress == MBusConstants.BROADCAST_WITH_ANSWER_PRIMARY_ADDRESS || primaryAddress == (getAddress() & 0xFF) || ((primaryAddress == MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS) && isNetworkSelected());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Slave) {
            Slave other = (Slave) o;
            return (getAddress() == other.getAddress()) && (getIdentNumber() == other.getIdentNumber()) && getManufacturer().equals(other.getManufacturer()) && getMedium().equals(other.getMedium()) && (getVersion() == other.getVersion());
        } else {
            return false;
        }
    }
    /**
     * track the version of self saved data !!!
     */
    private final static int MY_SERIAL_VERSION = 0;

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(MY_SERIAL_VERSION);
        stream.writeByte(getAddress());
        stream.writeInt(getIdentNumber());
        stream.writeObject(getManufacturer());
        stream.writeObject(getMedium());
        stream.writeByte(getVersion());
        stream.writeBoolean(isAcd());
        stream.writeBoolean(isDfc());
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        switch (stream.readInt()) {
            case MY_SERIAL_VERSION:
                setAddress(stream.readByte());
                setIdentNumber(stream.readInt());
                setManufacturer((String) stream.readObject());
                setMedium((MBusMedium) stream.readObject());
                setVersion(stream.readByte());
                setAcd(stream.readBoolean());
                setDfc(stream.readBoolean());
                break;
            default:
        }
    }

    @Override
    public boolean isAcd() {
        return acd;
    }

    @Override
    public boolean isDfc() {
        return dfc;
    }

    @Override
    public void setAcd(boolean acd) {
        this.acd = acd;
    }

    @Override
    public void setDfc(boolean dfc) {
        this.dfc = dfc;
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        result.accumulate("manufacturer", getManufacturer());
        result.accumulate("medium", getMedium().getLabel());
        result.accumulate("version", JSONFactory.encodeHexByte(getVersion()));
        result.accumulate("address", JSONFactory.encodeHexByte(getAddress()));
        result.accumulate("signature", JSONFactory.encodeHexShort(getSignature()));
        JSONArray jsonStatusArray = new JSONArray();
        for (UserDataResponse.StatusCode st : getStatus()) {
            jsonStatusArray.add(st.getLabel());
        }
        result.accumulate("accessnumber", getAccessnumber());
        result.accumulate("status", jsonStatusArray);
        result.accumulate("identNumber", getIdentNumber());
        result.accumulate("acd", isAcd());
        result.accumulate("dfc", isDfc());
        result.accumulate("selectedFrame", selectedIndex);
        JSONArray jsonSlaveFrameContainer = new JSONArray();
        for (ResponseFrameContainer sfc : responseFrameContainers) {
            jsonSlaveFrameContainer.add(sfc.toJSON(jsonSerializeType));
        }
        result.accumulate("slaveFrameContainers", jsonSlaveFrameContainer);
        return result;
    }

    @Override
    public Iterator<ResponseFrameContainer> iterator() {
        return responseFrameContainers.iterator();
    }

    @Override
    public short getAccessnumber() {
        return accessNumber;
    }

    @Override
    public void setAccessnumber(short accessnumber) {
        this.accessNumber = accessnumber;
    }

    @Override
    public void fromJSON(JSONObject json) {
        setManufacturer(json.getString("manufacturer"));
        setMedium(MBusMedium.fromLabel(json.getString("medium")));
        setVersion(JSONFactory.decodeHexByte(json, "version", (byte) 0));
        setIdentNumber(json.getInt("identNumber"));
        setAddress(JSONFactory.decodeHexByte(json, "address", (byte) 0));
        setAccessnumber(JSONFactory.getShort(json, "accessNumber", (short) 0));
        setAcd(JSONFactory.getBoolean(json, "acd", false));
        setDfc(JSONFactory.getBoolean(json, "dfc", false));
        setSignature(JSONFactory.decodeHexShort(json, "signature", (short) 0));
        if (json.containsKey("status")) {
            JSONArray statusArray = json.getJSONArray("status");
            if (statusArray.size() == 0) {
                setStatus(new UserDataResponse.StatusCode[0]);
            } else {
                UserDataResponse.StatusCode[] status = new UserDataResponse.StatusCode[statusArray.size()];
                for (int i = 0; i < status.length; i++) {
                    status[i] = UserDataResponse.StatusCode.fromLabel(statusArray.getString(i));
                }
                setStatus(status);
            }
        }

        JSONArray jsonSlaveFrameContainers = json.getJSONArray("slaveFrameContainers");
        for (int i = 0; i < jsonSlaveFrameContainers.size(); i++) {
            ResponseFrameContainer sfc = new ResponseFrameContainer();
            sfc.fromJSON(jsonSlaveFrameContainers.getJSONObject(i));
            responseFrameContainers.add(sfc);
        }
        selectedIndex = json.getInt("selectedFrame");

    }

    @Override
    public ResponseFrameContainer getResponseFrameContainer(int index) {
        return responseFrameContainers.get(index);
    }

    @Override
    public ResponseFrameContainer[] getResponseFrameContainers() {
        return responseFrameContainers.toArray(new ResponseFrameContainer[responseFrameContainers.size()]);
    }

    @Override
    public int getResponseFrameContainerCount() {
        return responseFrameContainers.size();
    }

    private Frame getSelectedResponseFrame() {
        if (selectedIndex >= 0) {
            return getResponseFrameContainer(selectedIndex).getResponseFrame();
        } else {
            return null;
        }
    }
}
