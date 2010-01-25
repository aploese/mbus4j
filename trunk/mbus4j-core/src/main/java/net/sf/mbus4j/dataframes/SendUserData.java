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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.json.JSONFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SendUserData implements LongFrame {

    public static String SEND_USER_DATA_SUBTYPE = "send user data";
    private List<DataBlock> dataBlocks = new ArrayList<DataBlock>();
    private byte address;
    private boolean fcb;

    public SendUserData(boolean fcb) {
        this.fcb = fcb;
    }

    public SendUserData() {
    }

    @Override
    public boolean addDataBlock(DataBlock dataBlock) {
        return dataBlocks.add(dataBlock);
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
        return dataBlocks.get(dataBlocks.size() - 1);
    }

    public boolean isFcb() {
        return fcb;
    }

    @Override
    public Iterator<DataBlock> iterator() {
        return dataBlocks.iterator();
    }

    @Override
    public void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock) {
        final int pos = dataBlocks.indexOf(oldDataBlock);
        dataBlocks.remove(pos);
        dataBlocks.add(pos, newDataBlock);
    }

    @Override
    public void setAddress(byte address) {
        this.address = address;
    }

    public void setFcb(boolean fcb) {
        this.fcb = fcb;
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
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = new JSONObject();
        result.accumulate("controlCode", getControlCode());
        result.accumulate("subType", SEND_USER_DATA_SUBTYPE);
        result.accumulate("fcb", isFcb());
        result.accumulate("address", address & 0xFF);
        JSONArray jsonDataBlocks = new JSONArray();
        for (DataBlock db : this) {
            jsonDataBlocks.add(db.toJSON(isTemplate));
        }
        result.accumulate("dataBlocks", jsonDataBlocks);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        fcb = json.getBoolean("fcb");
        address = (byte)json.getInt("address");

        JSONArray jsonDataBlocks = json.getJSONArray("dataBlocks");
            for (int i = 0; i < jsonDataBlocks.size(); i++) {
                DataBlock db = JSONFactory.createDataBlock(jsonDataBlocks.getJSONObject(i));
                db.fromJSON(jsonDataBlocks.getJSONObject(i));
                addDataBlock(db);
            }

    }
}
