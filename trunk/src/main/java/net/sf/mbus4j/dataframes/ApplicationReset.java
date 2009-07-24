/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

import java.util.Iterator;
import net.sf.mbus4j.dataframes.Frame.ControlCode;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;


/**
 *
 * @author aploese
 */
public class ApplicationReset implements LongFrame {

    /**
     * @return the subcode
     */
    public TelegramType getTelegramType() {
        return telegramType;
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
        this.telegramType = telegramType.valueOf(value & 0xF0);
        this.subTelegram = value & 0x0F;
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

    /**
     * @return the subTelegram
     */
    public int getSubTelegram() {
        return subTelegram;
    }

    /**
     * @param subTelegram the subTelegram to set
     */
    public void setSubTelegram(int subTelegram) {
        this.subTelegram = subTelegram;
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
        final public byte id;
        final public String sname;


        TelegramType(int id, String sname) {
            this.id = (byte)id;
            this.sname = sname;
        }

    @Override
        public String toString() {
            return sname;
        }

       public static TelegramType valueOf(int value) {
           for (TelegramType c : TelegramType.values()) {
               if (c.id == value) {
                   return c;
               }
           }
           return null;
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
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLastPackage(boolean isLastPackage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataBlock getLastDataBlock() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void setFcb(boolean fcb) {
       this.fcb = fcb;
    }

    public boolean isFcb() {
        return fcb;
    }


}
