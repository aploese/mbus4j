/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

/**
 * C-Field SND_NKE
 * @author aploese
 */
public class SendInitSlave implements ShortFrame {

    private byte address;

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_NKE;
    }

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public boolean isFcb() {
        return false;
    }

    @Override
    public void setFcb(boolean fcb) {
        throw new UnsupportedOperationException("set FCB not supported.");
    }

    @Override
    public void setFcv(boolean fcv) {
        throw new UnsupportedOperationException("set FCV not supported.");
    }

    @Override
    public boolean isFcv() {
       return false;
    }

}
