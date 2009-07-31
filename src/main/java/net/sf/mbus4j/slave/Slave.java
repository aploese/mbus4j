/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.slave;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.master.Master;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slave {

    /**
     * Template for creating a valid udr - no datablocks.
     */
    private UserDataResponse udrTemplate;

    public Slave() {
        super();
    }

    public Slave(int primaryAddress, int id, String man, int version, MBusMedium medium) {
        super();
        udrTemplate = new UserDataResponse();
        udrTemplate.setAddress((byte) primaryAddress);
        udrTemplate.setIdentNumber(id);
        udrTemplate.setManufacturer(man);
        udrTemplate.setVersion((byte) version);
        udrTemplate.setMedium(medium);
        udrTemplate.setStatus(new UserDataResponse.StatusCode[]{UserDataResponse.StatusCode.APPLICATION_NO_ERROR});
    }

    public byte getAddress() {
        return udrTemplate.getAddress();
    }

    public int getId() {
        return udrTemplate.getIdentNumber();
    }

    public String getMan() {
        return udrTemplate.getManufacturer();
    }

    public MBusMedium getMedium(MBusMedium medium) {
        return udrTemplate.getMedium();
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

    public void setAddress(byte address) {
        udrTemplate.setAddress(address);
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

    /**
     *
     * @param primaryAddress
     * @return
     */
    public boolean willHandleRequest(Frame frame) {
        if (frame instanceof PrimaryAddress) {
            int primaryAddress = ((PrimaryAddress) frame).getAddress() & 0xFF;
            return primaryAddress == Master.BROADCAST_NO_ANSWER_PRIMARY_ADDRESS || primaryAddress == Master.BROADCAST_WITH_ANSWER_PRIMARY_ADDRESS || primaryAddress == getAddress();
        } else {
            return false;
        }
    }
}
