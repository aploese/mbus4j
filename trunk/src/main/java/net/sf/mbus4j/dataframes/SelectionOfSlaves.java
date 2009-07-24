/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes;

/**
 *
 * @author aploese
 */
public class SelectionOfSlaves implements ControlFrame {

    private byte address;
    private boolean fcb;
    private int maskedId;
    private int maskedVersion;
    private int maskedMan;
    private int maskedMedium;

    public SelectionOfSlaves(SendUserData old) {
        this.address = old.getAddress();
        this.fcb = old.isFcb();
    }

    public SelectionOfSlaves(byte address, boolean isFcb) {
        this.address = address;
        this.fcb = isFcb;
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

    /**
     * @return the maskedId
     */
    public int getMaskedId() {
        return maskedId;
    }

    /**
     * @param maskedId the maskedId to set
     */
    public void setMaskedId(int maskedId) {
        this.maskedId = maskedId;
    }

    /**
     * @return the maskedVersion
     */
    public int getMaskedVersion() {
        return maskedVersion;
    }

    /**
     * @param maskedVersion the maskedVersion to set
     */
    public void setMaskedVersion(int maskedVersion) {
        this.maskedVersion = maskedVersion;
    }

    /**
     * @return the maskedMan
     */
    public int getMaskedMan() {
        return maskedMan;
    }

    /**
     * @param maskedMan the maskedMan to set
     */
    public void setMaskedMan(int maskedMan) {
        this.maskedMan = maskedMan;
    }

    /**
     * @return the maskedMedium
     */
    public int getMaskedMedium() {
        return maskedMedium;
    }

    /**
     * @param maskedMedium the maskedMedium to set
     */
    public void setMaskedMedium(int maskedMedium) {
        this.maskedMedium = maskedMedium;
    }

}
