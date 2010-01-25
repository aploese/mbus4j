/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.dataframes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.encoder.Encoder;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SelectionOfSlaves implements LongFrame {

    private byte address;
    private int bcdId;
    private byte bcdVersion;
    private short bcdMan;
    private byte bcdMedium;
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
     * @return the bcdId
     */
    public int getBcdId() {
        return bcdId;
    }

    /**
     * @return the bcdMan
     */
    public short getBcdMan() {
        return bcdMan;
    }

    /**
     * @return the bcdMedium
     */
    public int getBcdMedium() {
        return bcdMedium;
    }

    /**
     * @return the bcdVersion
     */
    public int getBcdVersion() {
        return bcdVersion;
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

    /**
     * @param bcdId the maskedId to set
     */
    public void setBcdId(int bcdId) {
        this.bcdId = bcdId;
    }

    /**
     * @param bcdMan the bcdMan to set
     */
    public void setBcdMan(short bcdMan) {
        this.bcdMan = bcdMan;
    }

    /**
     * @param maskedMedium the bcdMedium to set
     */
    public void setBcdMedium(byte bcdMedium) {
        this.bcdMedium = bcdMedium;
    }

    /**
     * @param maskedVersion the bcdVersion to set
     */
    public void setBcdVersion(byte bcdVersion) {
        this.bcdVersion = bcdVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        sb.append(String.format("bcdId = 0x%08X\n", bcdId));
        sb.append(String.format("bcdMan = 0x%04X\n", bcdMan));
        sb.append(String.format("bcdVersion = 0x%02X\n", bcdVersion));
        sb.append(String.format("bcdMedium = 0x%02X\n", bcdMedium));
        return sb.toString();
    }

    public boolean matchId(int id) {
        int hexBcdId = (int)toBcd(id, 8);
        int hexMask = bcdId;
        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) == 0x0F) || ((hexMask & 0x0F) == (hexBcdId & 0x0F))) {
              hexMask >>= 4;
              hexBcdId >>= 4;
            } else {
                return false;
            }
        }
        return true;
    }

    public static long toBcd(long value, int bcdDigits) {
        long result = 0;
        for (int i = bcdDigits; i > 0; i -= 2) {
            result |= (value % 10) << 4 * (bcdDigits - i);
            value /= 10;
            result |= (value % 10) << (4 + 4 * (bcdDigits - i));
            value /= 10;
        }
        return result;
    }

    public boolean matchAll(int id, String man, MBusMedium medium, int version) {
        return matchId(id) && matchMan(man) && matchMedium(medium) && matchVersion(version);
    }

    private boolean matchMan(String man) {
        int hexBcdMan = (int)toBcd(Encoder.man2Short(man), 4);
        int hexMask = bcdMan;
        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) != 0x0F) && ((hexMask & 0x0F) != (hexBcdMan & 0x0F))) {
                return false;
            }
            hexMask >>= 4;
            hexBcdMan >>= 4;
        }
        return true;
    }

    private boolean matchMedium(MBusMedium medium) {
        int hexBcdMedium = (int)toBcd(medium.getId(), 2);
        int hexMask = bcdMedium;
        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) != 0x0F) && ((hexMask & 0x0F) != (hexBcdMedium & 0x0F))) {
                return false;
            }
            hexMask >>= 4;
            hexBcdMedium >>= 4;
        }
        return true;
    }

    private boolean matchVersion(int version) {
        int hexBcdVersion = (int)toBcd(version, 2);
        int hexMask = bcdVersion;
        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) != 0x0F) && ((hexMask & 0x0F) != (hexBcdVersion & 0x0F))) {
                return false;
            }
            hexMask >>= 4;
            hexBcdVersion >>= 4;
        }
        return true;
    }

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
