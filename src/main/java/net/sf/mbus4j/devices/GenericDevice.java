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
package net.sf.mbus4j.devices;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
class GenericDevice implements MBusResponseFramesContainer, ProxyDevice {

    private int address;

    @Override
    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public void setId(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setVersion(int version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAcd() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAcd(boolean acd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDfc() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDfc(boolean dfc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMan(String man) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMedium(MBusMedium medium) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAddress() {
        return address;
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseFrame getResponseFrame(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseFrame[] getResponseFrames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getResponseFrameCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<ResponseFrame> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<ResponseFrame, Collection<DataBlock>> readValues(Sender sender, ResponseFrame... responseFrames) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class GenericResponseFrame extends  ResponseFrame {

        @Override
        public Iterable<DataBlock> getDataBlocksIterable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getDataBlockCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Frame[] getinitFrames() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public DataBlock getDataBlock(int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Iterator<DataBlock> iterator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private void addDataBlock(DataBlock db) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

    }

    private String man;
    private MBusMedium medium;
    private GenericResponseFrame responseFrame;

    public GenericDevice() {

    }

    public GenericDevice(int primaryAddress, String man, MBusMedium medium, int version, int id) {
        super();
        this.man = man;
        this.medium = medium;

    }

    public GenericDevice(UserDataResponse udResp) {
        this(udResp.getAddress(), udResp.getManufacturer(), udResp.getMedium(), udResp.getVersion(), udResp.getIdentNumber());
        responseFrame = new GenericResponseFrame();
        for (DataBlock db : udResp) {
            responseFrame.addDataBlock(db);
        }
    }

    @Override
    public String getMan() {
        return man;
    }

    @Override
    public MBusMedium getMedium() {
        return medium;
    }

    /**
     * TODO move up and impement finding of response freame if nothing is given ???
     * @param sender
     * @param responseFrames
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    /*
    @Override
    public Map<ResponseFrame, Map<DataBlock, DataBlock>> readValues(Sender sender, ResponseFrame... responseFrames) {
        if ((responseFrames != null) && (responseFrames.length > 1)) {
            throw new IllegalArgumentException("responseFrame must be null or only one frame allowed");
        }
        if ((responseFrames.length == 1) && (responseFrames[0] != getResponseFrame(0))) {
            throw new IllegalArgumentException("responseFrame and default frame mismatch");
        }
        Frame received = sender.send(new RequestClassXData(Frame.ControlCode.REQ_UD2, (byte) getAddress()));
        Map<ResponseFrame, Map<DataTag, DataBlock>> result = new HashMap<ResponseFrame, Map<DataTag, DataBlock>>();
        if (received instanceof UserDataResponse) {
            Map<DataTag, DataBlock> valueMap = new HashMap<DataTag, DataBlock>();
            result.put(getResponseFrame(0), valueMap);
            for (DataBlock db : (UserDataResponse) received) {
                for (DataTag dt : getResponseFrame(0)) {
                    if (dt.isKeyOf(db)) {
                        valueMap.put(dt, db);
                    }
                }
            }
        }

        return result;
    }
*/

}
