/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author aploese
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

    public void setAddress(byte address) {
        udrTemplate.setAddress(address);
    }

    public int getId() {
        return udrTemplate.getIdentNumber();
    }

    public void setId(int id) {
        udrTemplate.setIdentNumber(id);
    }


     public String getMan() {
        return udrTemplate.getManufacturer();
    }

    public void setMan(String man) {
        udrTemplate.setManufacturer(man);
    }

    public MBusMedium getMedium(MBusMedium medium) {
        return udrTemplate.getMedium();
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

    public Frame handleSendUserData(SendUserData sendUserData) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleApplicationReset(ApplicationReset applicationReset) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleReqUd1(RequestClassXData requestClassXData) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Frame handleSendInitSlave(SendInitSlave sendInitSlave) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }

    public Frame handleSendUserDataManSpec(SendUserDataManSpec sendUserDataManSpec) {
        return SingleCharFrame.SINGLE_CHAR_FRAME;
    }
}
