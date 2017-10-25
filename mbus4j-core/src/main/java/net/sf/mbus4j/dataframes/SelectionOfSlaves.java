package net.sf.mbus4j.dataframes;

/*
 * #%L
 * mbus4j-core
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * #L%
 */
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.json.JsonSerializeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.mbus4j.MBusUtils;

/**
 * @TODO implement masked FabricationNumber as last fallback
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SelectionOfSlaves implements LongFrame, RequestFrame<SingleCharFrame> {

    public enum WildcardNibbles {

        ID_0 {
                    @Override
                    public boolean isFirst() {
                        return true;
                    }
                },
        ID_1,
        ID_2,
        ID_3,
        ID_4,
        ID_5,
        ID_6,
        ID_7,
        MAN_0,
        MAN_1,
        MAN_2,
        MAN_3,
        MEDIUM_0,
        MEDIUM_1,
        VERSION_0,
        VERSION_1 {
                    @Override
                    public boolean isLast() {
                        return true;
                    }
                };

        public WildcardNibbles next() {
            return WildcardNibbles.values()[this.ordinal() + 1];
        }

        public WildcardNibbles prev() {
            return WildcardNibbles.values()[this.ordinal() - 1];
        }

        public boolean isLast() {
            return false;
        }

        public boolean isFirst() {
            return false;
        }

    }

    private byte address;
    private int bcdMaskedId;
    private byte maskedVersion;
    private short maskedMan;
    private byte maskedMedium;
    private final List<DataBlock> datablocks = new ArrayList<>();

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
     * @return the bcdMaskedId
     */
    public int getBcdMaskedId() {
        return bcdMaskedId;
    }

    /**
     * @return the maskedMan
     */
    public short getMaskedMan() {
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

    /**
     * @param bcdMaskedId the bcdMaskedId to set
     */
    public void setBcdMaskedId(int bcdMaskedId) {
        this.bcdMaskedId = bcdMaskedId;
    }

    /**
     * @param maskedMan the maskedMan to set
     */
    public void setMaskedMan(short maskedMan) {
        this.maskedMan = maskedMan;
    }

    /**
     * @param maskedMedium the maskedMedium to set
     */
    public void setMaskedMedium(byte maskedMedium) {
        this.maskedMedium = maskedMedium;
    }

    /**
     * @param maskedVersion the maskedVersion to set
     */
    public void setMaskedVersion(byte maskedVersion) {
        this.maskedVersion = maskedVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        sb.append(String.format("bcdMaskedId = 0x%08X\n", bcdMaskedId));
        sb.append(String.format("maskedMan = 0x%04X\n", maskedMan));
        sb.append(String.format("maskedVersion = 0x%02X\n", maskedVersion));
        sb.append(String.format("maskedMedium = 0x%02X\n", maskedMedium));

        return sb.toString();
    }

    public boolean matchId(int id) {
        int hexBcdId = MBusUtils.int2Bcd(id);
        int hexMask = bcdMaskedId;

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

    public boolean matchAll(int id, String man, MBusMedium medium, int version) {
        return matchId(id) && matchMan(man) && matchMedium(medium) && matchVersion(version);
    }

    private boolean matchMan(String man) {
        int hexMan = MBusUtils.man2Short(man);
        int hexMask = maskedMan;

        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) != 0x0F) && ((hexMask & 0x0F) != (hexMan & 0x0F))) {
                return false;
            }

            hexMask >>= 4;
            hexMan >>= 4;
        }

        return true;
    }

    private boolean matchMedium(MBusMedium medium) {
        int hexMedium = medium.getId();
        int hexMask = maskedMedium;

        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) != 0x0F) && ((hexMask & 0x0F) != (hexMedium & 0x0F))) {
                return false;
            }

            hexMask >>= 4;
            hexMedium >>= 4;
        }

        return true;
    }

    private boolean matchVersion(int version) {
        int hexVersion = version;
        int hexMask = maskedVersion;

        for (int i = 0; i < 8; i++) {
            if (((hexMask & 0x0F) != 0x0F) && ((hexMask & 0x0F) != (hexVersion & 0x0F))) {
                return false;
            }

            hexMask >>= 4;
            hexVersion >>= 4;
        }

        return true;
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public byte getWildcardNibble(WildcardNibbles wildcardNibbles) {
        switch (wildcardNibbles) {
            case ID_0:
                return (byte) (bcdMaskedId & 0x0F);
            case ID_1:
                return (byte) ((bcdMaskedId >>> 4) & 0x0F);
            case ID_2:
                return (byte) ((bcdMaskedId >>> 8) & 0x0F);
            case ID_3:
                return (byte) ((bcdMaskedId >>> 12) & 0x0F);
            case ID_4:
                return (byte) ((bcdMaskedId >>> 16) & 0x0F);
            case ID_5:
                return (byte) ((bcdMaskedId >>> 20) & 0x0F);
            case ID_6:
                return (byte) ((bcdMaskedId >>> 24) & 0x0F);
            case ID_7:
                return (byte) ((bcdMaskedId >>> 28) & 0x0F);
            case MAN_0:
                return (byte) (maskedMan & 0x0F);
            case MAN_1:
                return (byte) ((maskedMan >>> 4) & 0x0F);
            case MAN_2:
                return (byte) ((maskedMan >>> 8) & 0x0F);
            case MAN_3:
                return (byte) ((maskedMan >>> 12) & 0x0F);
            case MEDIUM_0:
                return (byte) (maskedMedium & 0x0F);
            case MEDIUM_1:
                return (byte) ((maskedMedium >>> 4) & 0x0F);
            case VERSION_0:
                return (byte) (maskedVersion & 0x0F);
            case VERSION_1:
                return (byte) ((maskedVersion >>> 4) & 0x0F);
            default:
                throw new RuntimeException("Cant handle " + wildcardNibbles);
        }
    }

    public void maskWildcardNibble(WildcardNibbles wildcardNibbles) {
        setWildcardNibble(wildcardNibbles, (byte)0x0F);
    }
    
    public void setWildcardNibble(WildcardNibbles wildcardNibbles, byte value) {
        if ((value & 0xF0) != 0) {
            throw new RuntimeException("Upper nibble must be 0");
        }
        switch (wildcardNibbles) {
            case ID_0:
                bcdMaskedId = (bcdMaskedId & 0xFFFFFFF0) | value;
                break;
            case ID_1:
                bcdMaskedId = (bcdMaskedId & 0xFFFFFF0F) | value << 4;
                break;
            case ID_2:
                bcdMaskedId = (bcdMaskedId & 0xFFFFF0FF) | value << 8;
                break;
            case ID_3:
                bcdMaskedId = (bcdMaskedId & 0xFFFF0FFF) | value << 12;
                break;
            case ID_4:
                bcdMaskedId = (bcdMaskedId & 0xFFF0FFFF) | value << 16;
                break;
            case ID_5:
                bcdMaskedId = (bcdMaskedId & 0xFF0FFFFF) | value << 20;
                break;
            case ID_6:
                bcdMaskedId = (bcdMaskedId & 0xF0FFFFFF) | value << 24;
                break;
            case ID_7:
                bcdMaskedId = (bcdMaskedId & 0x0FFFFFFF) | value << 28;
                break;
            case MAN_0:
                maskedMan = (short) ((maskedMan & 0xFFF0) | value);
                break;
            case MAN_1:
                maskedMan = (short) ((maskedMan & 0xFF0F) | value << 4);
                break;
            case MAN_2:
                maskedMan = (short) ((maskedMan & 0xF0FF) | value << 8);
                break;
            case MAN_3:
                maskedMan = (short) ((maskedMan & 0x0FFF) | value << 12);
                break;
            case MEDIUM_0:
                maskedMedium = (byte) ((maskedMedium & 0xF0) | value);
                break;
            case MEDIUM_1:
                maskedMedium = (byte) ((maskedMedium & 0xF0) | value << 4);
                break;
            case VERSION_0:
                maskedVersion = (byte) ((maskedVersion & 0xF0) | value);
                break;
            case VERSION_1:
                maskedVersion = (byte) ((maskedVersion & 0xF0) | value << 4);
                break;
            default:
                throw new RuntimeException("Cant handle " + wildcardNibbles);
        }
    }

    public boolean isWildcardNibble(WildcardNibbles wildcardNibbles) {
        switch (wildcardNibbles) {
            case ID_0:
                return (bcdMaskedId & 0x0000000F) == 0x0000000F;
            case ID_1:
                return (bcdMaskedId & 0x000000F0) == 0x000000F0;
            case ID_2:
                return (bcdMaskedId & 0x00000F00) == 0x00000F00;
            case ID_3:
                return (bcdMaskedId & 0x0000F000) == 0x0000F000;
            case ID_4:
                return (bcdMaskedId & 0x000F0000) == 0x000F0000;
            case ID_5:
                return (bcdMaskedId & 0x00F00000) == 0x00F00000;
            case ID_6:
                return (bcdMaskedId & 0x0F000000) == 0x0F000000;
            case ID_7:
                return (bcdMaskedId & 0xF0000000) == 0xF0000000;
            case MAN_0:
                return (maskedMan & 0x000F) == 0x000F;
            case MAN_1:
                return (maskedMan & 0x00F0) == 0x00F0;
            case MAN_2:
                return (maskedMan & 0x0F00) == 0x0F00;
            case MAN_3:
                return (maskedMan & 0xF000) == 0xF000;
            case MEDIUM_0:
                return (maskedMedium & 0x0F) == 0x0F;
            case MEDIUM_1:
                return (maskedMedium & 0xF0) == 0xF0;
            case VERSION_0:
                return (maskedVersion & 0x0F) == 0x0F;
            case VERSION_1:
                return (maskedVersion & 0xF0) == 0xF0;
            default:
                throw new RuntimeException("Cant handle " + wildcardNibbles);
        }
    }
}
