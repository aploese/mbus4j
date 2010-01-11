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
package net.sf.mbus4j.slave;

import java.io.IOException;
import java.io.Serializable;
import javax.management.RuntimeErrorException;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.master.MBusMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slave implements Serializable {

    private final static Logger log = LoggerFactory.getLogger(Slave.class);

    /**
     * Template for creating a valid udr - no datablocks.
     */
    private transient UserDataResponse udrTemplate;
    private boolean networkSelected;

    public Slave() {
        super();
        udrTemplate = new UserDataResponse();
    }

    public Slave(int primaryAddress, int id, String man, int version, MBusMedium medium) {
        this();
        networkSelected = false;
        udrTemplate.setAddress((byte) primaryAddress);
        udrTemplate.setIdentNumber(id);
        udrTemplate.setManufacturer(man);
        udrTemplate.setVersion((byte) version);
        udrTemplate.setMedium(medium);
        udrTemplate.setStatus(new UserDataResponse.StatusCode[]{UserDataResponse.StatusCode.APPLICATION_NO_ERROR});
    }

    public int getAddress() {
        return udrTemplate.getAddress();
    }

    public int getId() {
        return udrTemplate.getIdentNumber();
    }

    public String getMan() {
        return udrTemplate.getManufacturer();
    }

    public MBusMedium getMedium() {
        return udrTemplate.getMedium();
    }

    public int getVersion() {
        return udrTemplate.getVersion();
    }

    public Frame handleApplicationReset(ApplicationReset applicationReset) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleReqUd1(RequestClassXData requestClassXData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Frame handleReqUd2(RequestClassXData request) {
        UserDataResponse result;
        try {
            result = udrTemplate.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        udrTemplate.setAccessNumber((short) (udrTemplate.getAccessNumber() + 1));
        udrTemplate.clearDataBlocks();
        return result;
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

    public void setAddress(int address) {
        udrTemplate.setAddress((byte)address);
    }

    public void setId(int id) {
        udrTemplate.setIdentNumber(id);
    }

    public void setMan(String man) {
        udrTemplate.setManufacturer(man);
    }

    public void setMedium(MBusMedium medium) {
        udrTemplate.setMedium(medium);
    }

    public void setVersion(int version) {
        udrTemplate.setVersion((byte)version);
    }

    /**
     *
     * @param primaryAddress
     * @return
     */
    public boolean willHandleRequest(Frame frame) {
        if (frame instanceof SelectionOfSlaves) {
            SelectionOfSlaves selectionOfSlaves = (SelectionOfSlaves)frame;
            log.info("will handle SelectionOfSlaves: " + ((selectionOfSlaves.getAddress()  & 0xFF) ==  MBusMaster.SLAVE_SELECT_PRIMARY_ADDRESS));
            return (selectionOfSlaves.getAddress() & 0xFF) ==  MBusMaster.SLAVE_SELECT_PRIMARY_ADDRESS;
        } else if (frame instanceof PrimaryAddress) {
            int primaryAddress = ((PrimaryAddress) frame).getAddress() & 0xFF;
            return willHandleByAddress(primaryAddress);
        } else if (frame instanceof RequestClassXData) {
            int primaryAddress = ((RequestClassXData)frame).getAddress() & 0xFF;
            return willHandleByAddress(primaryAddress);
        } else {
            return false;
        }
    }

    Frame handleSelectionOfSlaves(SelectionOfSlaves selectionOfSlaves) {
        if ((selectionOfSlaves.getAddress() & 0xFF)!=  MBusMaster.SLAVE_SELECT_PRIMARY_ADDRESS) {
            log.warn("NETWORK SELECT ERROR");
            return null;
        }
        if (selectionOfSlaves.matchAll(getId(), getMan(), getMedium(), getVersion())) {
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
        return String.format("address = 0x%02x, id = %08d, man = %s, medium = %s, version = 0x%02X", getAddress(), getId(), getMan(), getMedium(), getVersion());
    }

    private boolean willHandleByAddress(int primaryAddress) {
       return primaryAddress == MBusMaster.BROADCAST_NO_ANSWER_PRIMARY_ADDRESS || primaryAddress == MBusMaster.BROADCAST_WITH_ANSWER_PRIMARY_ADDRESS || primaryAddress == (getAddress() & 0xFF) || ((primaryAddress == MBusMaster.SLAVE_SELECT_PRIMARY_ADDRESS) && isNetworkSelected());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Slave) {
            Slave other = (Slave)o;
            return (getAddress() == other.getAddress()) && (getId() == other.getId()) && getMan().equals(other.getMan()) && getMedium().equals(other.getMedium()) && (getVersion() == other.getVersion());
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
            stream.writeInt(getId());
            stream.writeObject(getMan());
            stream.writeObject(getMedium());
            stream.writeByte(getVersion());
            stream.writeBoolean(isAcd());
            stream.writeBoolean(isDfc());
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        // construct
        udrTemplate = new UserDataResponse();

        switch (stream.readInt()) {
            case MY_SERIAL_VERSION:
            setAddress(stream.readByte());
            setId(stream.readInt());
            setMan((String)stream.readObject());
            setMedium((MBusMedium)stream.readObject());
            setVersion(stream.readByte());
            setAcd(stream.readBoolean());
            setDfc(stream.readBoolean());
            break;
                default :
            }
    }

    public boolean isAcd() {
        return udrTemplate.isAcd();
    }

    public boolean isDfc() {
        return udrTemplate.isDfc();
    }

    public void setAcd(boolean acd) {
        udrTemplate.setAcd(acd);
    }

    public void setDfc(boolean dfc) {
        udrTemplate.setDfc(dfc);
    }

}
