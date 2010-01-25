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

import java.util.Iterator;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class ApplicationReset implements LongFrame {
    public static String SEND_USER_DATA_SUBTYPE = "application reset";

    public ApplicationReset() {
    }

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = new JSONObject();
        result.accumulate("controlCode", getControlCode());
        result.accumulate("subType", SEND_USER_DATA_SUBTYPE);
        result.accumulate("fcb", isFcb());
        result.accumulate("address", address & 0xFF);
        result.accumulate("telegramType", getTelegramType().getLabel());
        result.accumulate("subTelegram", getSubTelegram());
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        fcb = json.getBoolean("fcb");
        address = (byte)json.getInt("address");
        telegramType = TelegramType.fromLabel(json.getString("telegramType"));
        subTelegram = json.getInt("subTelegram");
    }

    public static enum TelegramType {

        ALL(0x00, "All"),
        USER_DATA(0x10, "User data"),
        SIMPLE_BILLING(0x20, "Simple billing"),
        ENHANCED_BILLING(0x30, "Enhanced billing"),
        MULTI_TARIFF_BILLING(0x40, "Multi tariff billing"),
        INSTANCIOUS_VALUES(0x50, "Instanious values"),
        LOAD_MANAGEMENT_VALUES_FOR_MANAGEMENT(0x60, "Load management values for management"),
        RESERVED_0x70(0x70, "Reserved 0x07"),
        INSTALLATION_AND_STARTUP(0x80, "Installation and startup"),
        TESTING(0x90, "Testing"),
        CALIBRATION(0xA0, "Calibration"),
        MANUFACTURING(0xB0, "Manufacturing"),
        DEVELOPMENT(0xC0, "Development"),
        SELFTEST(0xD0, "Selftest"),
        RESERVED_0xE0(0xE0, "Reserved 0xE0"),
        RESERVED_0xF0(0xF0, "Reserved 0xF0");

        public static TelegramType valueOf(int value) {
            for (TelegramType c : TelegramType.values()) {
                if (c.id == value) {
                    return c;
                }
            }
            return null;
        }
        final public byte id;
        final public String label;

        TelegramType(int id, String label) {
            this.id = (byte) id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }

        public String getLabel() {
            return label;
        }

        public static TelegramType fromLabel(String label) {
            for (TelegramType value : values()) {
                if (value.label.equals(label)) {
                    return value;
                }
            }
            return valueOf(label);
        }

    }
    private byte address;
    private boolean fcb;
    private TelegramType telegramType;
    private int subTelegram;

    public ApplicationReset(SendUserData old) {
        this.address = old.getAddress();
        this.setFcb(old.isFcb());
    }

    public ApplicationReset(TelegramType telegramType, int subTelegram) {
        this.telegramType = telegramType;
        this.subTelegram = subTelegram;
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the subTelegram
     */
    public int getSubTelegram() {
        return subTelegram;
    }

    /**
     * @return the subcode
     */
    public TelegramType getTelegramType() {
        return telegramType;
    }

    public boolean isFcb() {
        return fcb;
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return new Iterator<DataBlock>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public DataBlock next() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    public void setFcb(boolean fcb) {
        this.fcb = fcb;
    }

    /**
     * @param subTelegram the subTelegram to set
     */
    public void setSubTelegram(int subTelegram) {
        this.subTelegram = subTelegram;
    }

    /**
     * @param subcode the subcode to set
     */
    public void setTelegramType(TelegramType telegramType) {
        this.telegramType = telegramType;
    }

    /**
     * @param subcode the subcode to set
     */
    public void setTelegramTypeAndSubTelegram(int value) {
        this.telegramType = TelegramType.valueOf(value & 0xF0);
        this.subTelegram = value & 0x0F;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isFcb = ").append(isFcb()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
        sb.append("telegramType = ").append(getTelegramType()).append('\n');
        sb.append("subTelegram = ").append(getSubTelegram()).append('\n');
        return sb.toString();
    }
}
