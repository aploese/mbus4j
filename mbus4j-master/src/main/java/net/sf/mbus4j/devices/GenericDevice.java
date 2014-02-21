package net.sf.mbus4j.devices;

/*
 * #%L
 * mbus4j-master
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

import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.UserDataResponse.StatusCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class GenericDevice
        implements MBusResponseFramesContainer,
        JSONSerializable {

    private byte address;
    private List<ResponseFrameContainer> responseFrameContainers = new ArrayList<ResponseFrameContainer>();
    private int identNumber;
    private byte version;
    private boolean acd;
    private boolean dfc;
    private StatusCode[] status;
    private short accessnumber;
    private short signature;
    private String manufacturer;
    private MBusMedium medium;

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public void setIdentNumber(int identNumber) {
        this.identNumber = identNumber;
    }

    @Override
    public void setVersion(byte version) {
        this.version = version;
    }

    @Override
    public boolean isAcd() {
        return acd;
    }

    @Override
    public void setAcd(boolean acd) {
        this.acd = acd;
    }

    @Override
    public boolean isDfc() {
        return dfc;
    }

    @Override
    public void setDfc(boolean dfc) {
        this.dfc = dfc;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public void setMedium(MBusMedium medium) {
        this.medium = medium;
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
    public byte getVersion() {
        return version;
    }

    @Override
    public Iterator<ResponseFrameContainer> iterator() {
        return responseFrameContainers.iterator();
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

    @Override
    public short getAccessnumber() {
        return accessnumber;
    }

    @Override
    public void setAccessnumber(short accessnumber) {
        this.accessnumber = accessnumber;
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

    public GenericDevice() {
    }

    public GenericDevice(byte address, String manufacturer, MBusMedium medium, byte version, int identNumber) {
        super();
        this.manufacturer = manufacturer;
        this.medium = medium;
        this.address = address;
        this.version = version;
        this.identNumber = identNumber;
    }

    public GenericDevice(UserDataResponse udResp, Frame requestFrame) {
        this(udResp.getAddress(),
                udResp.getManufacturer(),
                udResp.getMedium(),
                udResp.getVersion(),
                udResp.getIdentNumber());
        setAccessnumber(udResp.getAccessNumber());
        setAcd(udResp.isAcd());
        setDfc(udResp.isDfc());
        setStatus(udResp.getStatus());
        setSignature(udResp.getSignature());
        ResponseFrameContainer rfc = new ResponseFrameContainer();
        rfc.setResponseFrame(udResp);
        rfc.setRequestFrame(requestFrame);
        responseFrameContainers.add(rfc);
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

        if (getStatus() != null) {
            for (UserDataResponse.StatusCode st : getStatus()) {
                jsonStatusArray.add(st.getLabel());
            }
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

        JSONArray jsonSlaveFrameContainer = new JSONArray();

        for (ResponseFrameContainer sfc : responseFrameContainers) {
            jsonSlaveFrameContainer.add(sfc.toJSON(jsonSerializeType));
        }

        result.accumulate("slaveFrameContainers", jsonSlaveFrameContainer);

        return result;
    }

    public boolean addResponseFrameContainer(ResponseFrameContainer container) {
        if ((container.getName() == null) || (container.getName().equals(ResponseFrameContainer.DEFAULT_NAME))) {
            container.setName("frame " + responseFrameContainers.size());
        }
        return responseFrameContainers.add(container);
    }

    ;

    @Override
    public ResponseFrameContainer removeResponseFrameContainer(int i) {
        return responseFrameContainers.remove(i);
    }

    @Override
    public int responseFrameContainerIndexOf(ResponseFrameContainer rfc) {
        return responseFrameContainers.indexOf(rfc);
    }
}
