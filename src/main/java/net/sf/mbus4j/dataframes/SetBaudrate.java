/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

/**
 *
 * @author aploese
 */
public class SetBaudrate implements ControlFrame {

        private byte address;
    private boolean fcb;
    private int baudrate;

    public SetBaudrate(SendUserData old, int baudrate) {
        this.address = old.getAddress();
        this.fcb = old.isFcb();
        this.baudrate = baudrate;
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
        sb.append(String.format("baudrate = %d\n", baudrate));
        return sb.toString();
    }

    /**
     * @return the baudrate
     */
    public int getBaudrate() {
        return baudrate;
    }

    /**
     * @param baudrate the baudrate to set
     */
    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }


}
