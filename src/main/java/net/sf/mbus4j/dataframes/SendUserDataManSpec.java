/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

import net.sf.mbus4j.dataframes.Frame.ControlCode;

/**
 *
 * @author aploese
 */
public class SendUserDataManSpec implements ControlFrame {

    private byte address;
    private boolean fcb;
    private int ciField;

    public SendUserDataManSpec(SendUserData sendUserData, int ciField) {
        this.ciField = ciField;
        this.address = sendUserData.getAddress();
        this.fcb = sendUserData.isFcb();
    }

    /**
     * @return the ciField
     */
    public int getCiField() {
        return ciField;
    }

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

    /**
     * @return the fcb
     */
    public boolean isFcb() {
        return fcb;
    }

    /**
     * @param fcb the fcb to set
     */
    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    /**
     * @param ciField the ciField to set
     */
    public void setCiField(int ciField) {
        this.ciField = ciField;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_UD;
    }

}
