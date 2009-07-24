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
public class SynchronizeAction implements ControlFrame {

    private byte address;
    private boolean fcb;

    public SynchronizeAction(SendUserData old) {
        this.address = old.getAddress();
        this.fcb = old.isFcb();
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_UD;
    }

    @Override
    public byte getAddress() {
        return address;
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isFcb = ").append(isFcb()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        return sb.toString();
    }
}
