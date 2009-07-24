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
public class RequestClassXData implements ShortFrame {
    private byte address;
    private boolean fcb;
    private boolean fcv;
    private ControlCode controlCode;

    public RequestClassXData(boolean fcb, boolean fcv, ControlCode controlCode) {
        this.fcb = fcb;
        this.fcv = fcv;
        this.controlCode = controlCode;
    }

    public RequestClassXData(ControlCode controlCode) {
        this(false, true, controlCode);
    }

    @Override
    public ControlCode getControlCode() {
        return controlCode;
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
    public void setFcb(boolean fcb) {
       this.fcb = fcb;
    }

    @Override
    public boolean isFcb() {
        return fcb;
    }
    @Override
    public void setFcv(boolean fcv) {
       this.fcv = fcv;
    }

    @Override
    public boolean isFcv() {
        return fcv;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isFcb = ").append(isFcb()).append('\n');
        sb.append("isFcv = ").append(isFcv()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        return sb.toString();
    }

}
