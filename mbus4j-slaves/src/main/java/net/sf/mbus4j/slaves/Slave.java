package net.sf.mbus4j.slaves;

/*
 * #%L
 * mbus4j-slaves
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.mbus4j.MBusUtils;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.mbus4j.log.LogUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slave
        implements Serializable,
        MBusResponseFramesContainer,
        JSONSerializable {

    private final static Logger log = LogUtils.getSlaveLogger();

    public static Slave fromResponse(UserDataResponse udr) {
        Slave result = new Slave(udr.getAddress(), udr.getIdentNumber(), udr.getManufacturer(), udr.getVersion(), udr.getMedium());
        result.setAccessnumber(udr.getAccessNumber());
        result.setAcd(udr.isAcd());
        result.setDfc(udr.isDfc());
        result.setSignature(udr.getSignature());
        result.setStatus(udr.getStatus());
        ResponseFrameContainer rfc = new ResponseFrameContainer();
        rfc.setRequestFrame(new RequestClassXData(Frame.ControlCode.REQ_UD2));
        rfc.setResponseFrame(udr);
        result.addResponseFrameContainer(rfc);
        return result;
    }

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
        for (int i = 0; i < responseFrameContainers.size(); i++) {
            if (applicationReset.equals(responseFrameContainers.get(i).getSelectFrame())) {
                selectedIndex = i;
                log.log(Level.FINE, "Set selected response container to {0} \"{1}\"", new Object[]{i, getResponseFrameContainer(i).getName()});
                break;
            }
        }
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
            throw new RuntimeException("Unknown Response class: "
                    + getSelectedResponseFrame().getClass().getName());
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
     * @param frame
     * @return
     */
    public boolean willHandleRequest(Frame frame) {
        if (frame instanceof SelectionOfSlaves) {
            SelectionOfSlaves selectionOfSlaves = (SelectionOfSlaves) frame;
            log.log(Level.INFO, "will handle SelectionOfSlaves: {0}", (selectionOfSlaves.getAddress() & 0xFF) == MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);

            return selectionOfSlaves.getAddress() == PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS;
        } else if (frame instanceof PrimaryAddress) {
            byte primaryAddress = ((PrimaryAddress) frame).getAddress();

            return willHandleByAddress(primaryAddress);
        } else if (frame instanceof RequestClassXData) {
            int primaryAddress = ((RequestClassXData) frame).getAddress() & 0xFF;

            return willHandleByAddress(primaryAddress);
        } else {
            return false;
        }
    }

    Frame handleSelectionOfSlaves(SelectionOfSlaves selectionOfSlaves) {
        if ((selectionOfSlaves.getAddress() & 0xFF) != MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS) {
            log.warning("NETWORK SELECT ERROR");

            return null;
        }

        if (selectionOfSlaves.matchAll(getIdentNumber(),
                getManufacturer(),
                getMedium(),
                getVersion())) {
            networkSelected = true;
            log.log(Level.INFO, "Network selected: {0}", slaveIdToString());

            return SingleCharFrame.SINGLE_CHAR_FRAME;
        } else {
            log.log(Level.INFO, "Network deselected: {0}", slaveIdToString());
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
        return String.format("address = 0x%02x, id = %08d, man = %s, medium = %s, version = 0x%02X",
                getAddress(),
                getIdentNumber(),
                getManufacturer(),
                getMedium(),
                getVersion());
    }

    private boolean willHandleByAddress(byte primaryAddress) {
        return (primaryAddress == PrimaryAddress.BROADCAST_NO_ANSWER_PRIMARY_ADDRESS)
                || (primaryAddress == PrimaryAddress.BROADCAST_WITH_ANSWER_PRIMARY_ADDRESS)
                || (primaryAddress == getAddress())
                || ((primaryAddress == PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS) && isNetworkSelected());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Slave) {
            Slave other = (Slave) o;

            return (getAddress() == other.getAddress()) && (getIdentNumber() == other.getIdentNumber())
                    && getManufacturer().equals(other.getManufacturer())
                    && getMedium().equals(other.getMedium()) && (getVersion() == other.getVersion());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.version;
        hash = 79 * hash + (this.medium != null ? this.medium.hashCode() : 0);
        hash = 79 * hash + this.address;
        hash = 79 * hash + this.identNumber;
        hash = 79 * hash + (this.manufacturer != null ? this.manufacturer.hashCode() : 0);
        return hash;
    }

    /**
     * track the version of self saved data !!!
     */
    private static final long serialVersionUID = -1;
    private final static int SERIAL_VERSION = 1;

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(SERIAL_VERSION);
        stream.writeByte(getAddress());
        stream.writeInt(getIdentNumber());
        stream.writeObject(getManufacturer());
        stream.writeObject(getMedium());
        stream.writeByte(getVersion());
        stream.writeBoolean(isAcd());
        stream.writeBoolean(isDfc());
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();

        switch (stream.readInt()) {
            case 1:
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
        result.accumulate("manufacturer",
                getManufacturer());
        result.accumulate("medium",
                getMedium().getLabel());
        result.accumulate("version",
                JSONFactory.encodeHexByte(getVersion()));
        result.accumulate("address",
                JSONFactory.encodeHexByte(getAddress()));
        result.accumulate("signature",
                JSONFactory.encodeHexShort(getSignature()));

        JSONArray jsonStatusArray = new JSONArray();

        for (UserDataResponse.StatusCode st : getStatus()) {
            jsonStatusArray.add(st.getLabel());
        }

        result.accumulate("accessnumber",
                getAccessnumber());
        result.accumulate("status", jsonStatusArray);
        result.accumulate("identNumber",
                getIdentNumber());
        result.accumulate("acd",
                isAcd());
        result.accumulate("dfc",
                isDfc());
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
        if (json.containsKey("selectedFrame")) {
            selectedIndex = json.getInt("selectedFrame");
        } else {
            selectedIndex = 0;
        }
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

    @Override
    public boolean addResponseFrameContainer(ResponseFrameContainer rfc) {
        return responseFrameContainers.add(rfc);
    }

    @Override
    public ResponseFrameContainer removeResponseFrameContainer(int i) {
        return responseFrameContainers.remove(i);
    }

    @Override
    public int responseFrameContainerIndexOf(ResponseFrameContainer rfc) {
        return responseFrameContainers.indexOf(rfc);
    }
}
