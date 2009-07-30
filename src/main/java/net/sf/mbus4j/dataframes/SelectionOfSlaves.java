/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author aploese
 */
public class SelectionOfSlaves implements LongFrame {

    private byte address;
    private int maskedId;
    private int maskedVersion;
    private int maskedMan;
    private int maskedMedium;
    private List<DataBlock> datablocks = new ArrayList<DataBlock>();

    public SelectionOfSlaves(SendUserData old) {
        this.address = old.getAddress();
    }

    public SelectionOfSlaves(byte address) {
        this.address = address;
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
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
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

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        final int pos = datablocks.indexOf(oldDataBlock);
        datablocks.remove(pos);
        datablocks.add(pos, newDataBlock);
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        return datablocks.add(dataBlock);
    }

    @Override
    public void setLastPackage(boolean isLastPackage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataBlock getLastDataBlock() {
        return datablocks.get(datablocks.size() - 1);
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return datablocks.iterator();
    }

}
