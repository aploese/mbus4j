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
package net.sf.mbus4j.dataframes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SelectionOfSlaves implements LongFrame {

    private byte address;
    private int maskedId;
    private int maskedVersion;
    private int maskedMan;
    private int maskedMedium;
    private List<DataBlock> datablocks = new ArrayList<DataBlock>();

    public SelectionOfSlaves(byte address) {
        this.address = address;
    }

    public SelectionOfSlaves(SendUserData old) {
        this.address = old.getAddress();
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        return datablocks.add(dataBlock);
    }

    @Override
    public byte getAddress() {
        return address;
    }

    @Override
    public ControlCode getControlCode() {
        return ControlCode.SND_UD;
    }

    @Override
    public DataBlock getLastDataBlock() {
        return datablocks.get(datablocks.size() - 1);
    }

    /**
     * @return the maskedId
     */
    public int getMaskedId() {
        return maskedId;
    }

    /**
     * @return the maskedMan
     */
    public int getMaskedMan() {
        return maskedMan;
    }

    /**
     * @return the maskedMedium
     */
    public int getMaskedMedium() {
        return maskedMedium;
    }

    /**
     * @return the maskedVersion
     */
    public int getMaskedVersion() {
        return maskedVersion;
    }

    /**
     * @return the fcb
     */
    public boolean isFcb() {
        return false;
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return datablocks.iterator();
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        final int pos = datablocks.indexOf(oldDataBlock);
        datablocks.remove(pos);
        datablocks.add(pos, newDataBlock);
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    @Override
    public void setLastPackage(boolean isLastPackage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param maskedId the maskedId to set
     */
    public void setMaskedId(int maskedId) {
        this.maskedId = maskedId;
    }

    /**
     * @param maskedMan the maskedMan to set
     */
    public void setMaskedMan(int maskedMan) {
        this.maskedMan = maskedMan;
    }

    /**
     * @param maskedMedium the maskedMedium to set
     */
    public void setMaskedMedium(int maskedMedium) {
        this.maskedMedium = maskedMedium;
    }

    /**
     * @param maskedVersion the maskedVersion to set
     */
    public void setMaskedVersion(int maskedVersion) {
        this.maskedVersion = maskedVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        return sb.toString();
    }

}
