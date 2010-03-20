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
package net.sf.mbus4j.master;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.sf.mbus4j.MBusConstants;

import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.devices.DeviceFactory;
import net.sf.mbus4j.devices.GenericDevice;
import net.sf.mbus4j.devices.Sender;
import net.sf.mbus4j.encoder.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the MBus devices connected via inputStream/OutputStream.
 * 
 * @author arnep@users.sourceforge.net
 * @version $Id$
 *
 * TODO: Handle multi Resoṕonses ClassXData (connect or chaon them together)
 */
public class MBusMaster implements Iterable<GenericDevice>, Sender {

    public void cancel() {
//TODO impement        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static GenericDevice getDevice(Iterable<GenericDevice> deviceList, ValueRequestPointLocator locator) {
        for (GenericDevice dev : deviceList) {
            if ((dev.getAddress() == locator.getAddress())
                    && (dev.getIdentNumber() == locator.getIdentnumber())
                    && dev.getManufacturer().equals(locator.getManufacturer())
                    && dev.getMedium().equals(locator.getMedium())
                    && (dev.getVersion() == locator.getVersion())) {
                return dev;
            }
        }
        return null;
    }

    public static DataBlock getDataBlock(Frame frame, ValueRequestPointLocator locator) {
            if (frame instanceof UserDataResponse) {
                for (DataBlock db : (UserDataResponse)frame) {
                    if (db.getDataFieldCode().equals(locator.getDifCode()) &&
                            db.getVif().equals(locator.getVif()) &&
                            db.getFunctionField().equals(locator.getFunctionField()) &&
                            (db.getStorageNumber() == locator.getStorageNumber()) &&
                            (db.getSubUnit() == locator.getDeviceUnit()) &&
                            (db.getTariff() == locator.getTariff()) &&
                            Arrays.equals(db.getVifes(), locator.getVifes())) {
                            return db;
                    }
                }

            } else {
                    throw new RuntimeException("Response is not a UserDataResponse but: " + frame);
               }
        throw new RuntimeException("can't find datablock of locator");
    }

    private DataBlock getTimeStampDB(GenericDevice dev, ValueRequestPointLocator locator) {
        // TODO implement
        return null;
    }

    private class StreamListener implements Runnable {

        @Override
        public void run() {
            try {
                int theData;
                Decoder parser = new Decoder();
                try {
                    while (!closed) {
                        try {
                            if ((theData = is.read()) == -1) {
                                if (log.isTraceEnabled()) {
                                    log.trace("Thread interrupted or eof on waiting occured");
                                }
                            } else {
                                if (log.isTraceEnabled()) {
                                    log.trace(String.format("Data received 0x%02x", theData));
                                }
                                try {
                                    if (parser.addByte((byte) theData) != null) {
                                        setLastFrame(parser.getFrame());
                                    }
                                } catch (Exception e) {
                                    log.error("Error during createPackage()", e);
                                }
                            }
                        } catch (NullPointerException npe) {
                            if (!closed) {
                                throw new RuntimeException(npe);
                            }
                        }
                    }
                    log.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    log.error("run()", e);
                } catch (Exception e) {
                    log.info("finished waiting for packages", e);

                }
            } finally {
            }
        }
    }
    private final static Logger log = LoggerFactory.getLogger(MBusMaster.class);
    private List<GenericDevice> devices = new ArrayList<GenericDevice>();
    private InputStream is;
    private OutputStream os;
    private Encoder encoder = new Encoder();
    private boolean closed;
    private Thread t;
    private StreamListener streamListener = new StreamListener();
    private int bitPerSecond;
    private final Queue<Frame> frameQueue = new ConcurrentLinkedQueue<Frame>();

    public MBusMaster() {
        super();
    }

    public boolean addDevice(GenericDevice device) {
        return devices.add(device);
    }

    public void addSlaveByAddress(int address) throws InterruptedException, IOException {
        Frame f = sendRequestUserData(address);
        if (f instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse) f;
            //TODO detect dev and create
            log.info(String.format("add device: address = 0x%02X, id = = %08d, man = %s, medium = %s, version = 0x%02X", udr.getAddress(), udr.getIdentNumber(), udr.getManufacturer(), udr.getMedium(), udr.getVersion()));
            devices.add(DeviceFactory.createDevice(udr, new RequestClassXData(Frame.ControlCode.REQ_UD2, (byte) address)));
            //TODO check wenn dev braucht mehr ( falsces udr)
        } else {
            log.info(String.format("no device at address = 0x%02X", address));
        }
    }

    public void clear() {
        devices.clear();
    }

    private void clearFrameQueue() {
        synchronized (frameQueue) {
            frameQueue.clear();
        }
    }

    public void close() throws InterruptedException {
        closed = true;
        Thread.sleep(100); //TODO wait?
        t.interrupt();
    }

    /**
     * @TODO implement
     */
    public void deselectBySecondaryAddress() {
    }

    public int deviceCount() {
        return devices.size();
    }

    public MBusResponseFramesContainer getDevice(int i) {
        return devices.get(i);
    }

    public MBusResponseFramesContainer[] getDevices() {
        return devices.toArray(new MBusResponseFramesContainer[deviceCount()]);
    }

    /**
     * Idle time is 33 bit periods see M-Bus doc chaper 5.4
     * @return
     */
    private long getIdleTime() {
        return 33000 / bitPerSecond;
    }

    /**
     * Timeout is 330 bit periods + 50ms see M-Bus doc chaper 5.4
     * 2 times max (256) packet size added
     * @return
     */
    private long getResponseTimeout() {
        return ((512 * 11 + 330) * 1000) / bitPerSecond + 50;
    }

    @Override
    public Iterator<GenericDevice> iterator() {
        return devices.iterator();
    }

    public void releaseStreams() {
        closed = true;
        is = null;
        os = null;
    }

    private Frame removeFrame() {
        return frameQueue.remove();
    }

    public void searchDevicesBySecondaryAddressing() throws IOException, InterruptedException {
        widcardSearch(0x00, 7, (short) 0xFFFF, (byte) 0xFF, (byte) 0xFF);
    }

    /**
     * Search by primary address (0..250)
     * 253 is for slave select
     * 254 broadcast addres to all devices
     *
     * @return
     */
    public MBusResponseFramesContainer[] searchDevicesByPrimaryAddress() throws IOException, InterruptedException {
        return searchDevicesByPrimaryAddress(0, MBusConstants.LAST_REGULAR_PRIMARY_ADDRESS);
    }

    public MBusResponseFramesContainer[] searchDevicesByPrimaryAddress(int first, int last) throws IOException, InterruptedException {
        clear();
        for (int i = first; i <= last; i++) {
            addSlaveByAddress(i);
        }
        return getDevices();
    }

    //TODO
    public void selectBySecondaryAddress() {
    }

    @Override
    public Frame send(Frame frame) throws IOException, InterruptedException {
        for (int trys = 0; trys <= 2; trys++) {
            clearFrameQueue();
            final byte[] b = encoder.encode(frame);
            os.write(b);
            os.flush();
            final long time = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug(String.format("Data Sent (try: %d): %s ", trys, Decoder.bytes2Ascii(b)));
            }
            Frame result = waitAndPollFrame(getResponseTimeout());
            log.info(String.format("Answer took %d ms", System.currentTimeMillis() - time));
            if (result != null) {
                return result;
            } else {
                Thread.sleep(getIdleTime());
                if (trys < 2) {
                    log.warn("Timeout Reply");
                } else {
                    log.error("Timeout Reply");
                    //TODO throw Ex?
                    return null;
                }
            }
        }
        return null;
    }

    public void sendRequestUserData() throws IOException, InterruptedException {
        sendRequestUserData(devices);
    }

    public Frame sendRequestUserData(int address) throws IOException, InterruptedException {
        RequestClassXData req = new RequestClassXData(Frame.ControlCode.REQ_UD2, (byte) address);
        return send(req);
    }

    public Map<GenericDevice, Frame> sendRequestUserData(Iterable<GenericDevice> devices) throws IOException, InterruptedException {
        Map<GenericDevice, Frame> result = new HashMap<GenericDevice, Frame>();
        for (GenericDevice dev : devices) {
            result.put(dev, sendRequestUserData(dev.getAddress()));
        }
        return result;
    }

    public void sendSlaveSelect(byte[] idWithMask, short manWithMask, byte versionWithMask) {
        //TODO search...
    }

    public void sendSlaveSelect(MBusResponseFramesContainer dev) {
        //TODO search...
    }

    private synchronized void setLastFrame(Frame frame) {
        log.debug(String.format("New frame parsed %s", frame));
        synchronized (frameQueue) {
            frameQueue.add(frame);
            frameQueue.notifyAll();
        }
    }

    public void setStreams(InputStream is, OutputStream os, int bitPerSecond) {
        this.is = is;
        this.os = os;
        this.bitPerSecond = bitPerSecond;
        closed = false;
        start();
    }

    private void start() {
        closed = false;
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
    }

    private Frame waitAndPollFrame(long timeout) throws InterruptedException {
        System.out.println("TIMEOUT: " + timeout);
        synchronized (frameQueue) {
            if (frameQueue.peek() == null) {
                if (timeout > 0) {
                    frameQueue.wait(timeout);
                }
            }
            return frameQueue.poll();
        }
    }

    private int waitForSingleChars(long timeout) throws InterruptedException {
        int result = 0;
        final long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time <= timeout) {
            Frame frame = waitAndPollFrame(time - System.currentTimeMillis() + timeout);
            if (frame instanceof SingleCharFrame) {
                result++;
            } else {
                return result;
            }
        }
        return result;
    }

    //TODO all BCD
    public void widcardSearch(int leadingBcdDigitsId, int maskLength, short bcdMan, byte bcdVersion, byte bcdMedium) throws IOException, InterruptedException {
        clear();
        log.debug(String.format("widcardSearch leadingBcdDigitsId: 0x%08X, maskLength: %d", leadingBcdDigitsId, maskLength));
        SelectionOfSlaves sud = new SelectionOfSlaves((byte) MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS);
        int idMask = 0;
        for (int i = 1; i <= maskLength; i++) {
            idMask <<= 4;
            idMask |= 0x0F;
        }

        for (int i = 0; i <= 9; i++) {
            sud.setBcdId((leadingBcdDigitsId << (maskLength * 4)) + idMask);
            sud.setBcdMan(bcdMan);
            sud.setBcdVersion(bcdVersion);
            sud.setBcdMedium(bcdMedium);
            Frame result = send(sud);
            if (result instanceof SingleCharFrame) {
                log.debug(String.format("got answer with mask: 0x%08X", leadingBcdDigitsId));
                int answers = waitForSingleChars(getResponseTimeout());
                if (answers == 0) {
                    log.debug(String.format("detect slave with mask: 0x%08X", leadingBcdDigitsId));
                    addSlaveByAddress(MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS);
                } else {
                    if (maskLength > 0) {
                        log.debug(String.format("multiple slaves (%d) with mask: 0x%08X", answers, leadingBcdDigitsId));
                        widcardSearch(leadingBcdDigitsId << 4, maskLength - 1, bcdMan, bcdVersion, bcdMedium);
                    } else {                    // wenn idMask == 8 man ver und medium ???
                        log.error(String.format("Cant separate slaves (%d) with id: 0x%08X", answers, leadingBcdDigitsId));
                    }
                }
            } else {
                log.debug(String.format("no slave with mask: 0x%08X", leadingBcdDigitsId));
            }
            leadingBcdDigitsId++;
        }
    }

    public void readValues(ValueRequest<?> requests) throws IOException, InterruptedException {
        //Create devices if neccecary
        Set<GenericDevice> deviceSet = new HashSet<GenericDevice>();
        for (ValueRequestPointLocator locator : requests) {
            GenericDevice myDevice = getDevice(devices, locator);

            if (myDevice == null) {
                myDevice = DeviceFactory.createDevice(locator.getAddress(), locator.getManufacturer(), locator.getMedium(), locator.getVersion(), locator.getIdentnumber());
                devices.add(myDevice);
            }
            deviceSet.add(myDevice);
        }
        //get data from devices
        //TODO hounor frames and addressing
        Map<GenericDevice, Frame> responses = sendRequestUserData(deviceSet);
        //pack response
        for (ValueRequestPointLocator locator : requests) {
            GenericDevice dev = getDevice(deviceSet, locator);
            DataBlock db = getDataBlock(responses.get(dev), locator);
            locator.setDb(db);
            db = getTimeStampDB(dev, locator);
            locator.setTimestampDb(db);
        }
    }

    }
