/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.mbus4j.dataframes.Frame.ControlCode;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author aploese
 */
public class SendUserData implements LongFrame {

    private List<DataBlock> dataBlocks = new ArrayList<DataBlock>();
    private byte address;
    private boolean fcb;
    private boolean lastPackage;

    public SendUserData(boolean fcb) {
        this.fcb = fcb;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("control code = ").append(getControlCode()).append('\n');
        sb.append("isFcb = ").append(isFcb()).append('\n');
        sb.append(String.format("address = 0x%02X\n", address));
            for (int i = 0; i < dataBlocks.size(); i++) {
                sb.append("datablock[").append(i).append("]:\n");
                dataBlocks.get(i).toString(sb, "  ");
            }
        return sb.toString();
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        final int pos = dataBlocks.indexOf(oldDataBlock);
        dataBlocks.remove(pos);
        dataBlocks.add(pos, newDataBlock);
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        return dataBlocks.add(dataBlock);
    }

    @Override
    public void setLastPackage(boolean isLastPackage) {
        lastPackage = isLastPackage;
    }

    @Override
    public DataBlock getLastDataBlock() {
        return dataBlocks.get(dataBlocks.size() -1);
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return dataBlocks.iterator();
    }

}
