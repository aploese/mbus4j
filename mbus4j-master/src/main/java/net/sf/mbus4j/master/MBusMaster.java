package net.sf.mbus4j.master;

/*
 * #%L
 * mbus4j-master
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
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileOutputStream;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.devices.DeviceFactory;
import net.sf.mbus4j.devices.GenericDevice;
import net.sf.mbus4j.devices.Sender;
import net.sf.mbus4j.encoder.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.Connection;
import net.sf.mbus4j.MBusAddressing;
import net.sf.mbus4j.MBusUtils;
import net.sf.mbus4j.dataframes.GarbageCharFrame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.decoder.DecoderListener;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;
import net.sf.mbus4j.log.LogUtils;

/**
 * Handles the MBus devices connected via inputStream/OutputStream.
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 *
 * TODO: Handle multi Resoṕonses ClassXData (connect or chaon them together)
 */
public class MBusMaster
        implements Iterable<GenericDevice>,
        Sender,
        JSONSerializable,
        Closeable {

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
            for (DataBlock db : (UserDataResponse) frame) {
                if (db.getDataFieldCode().equals(locator.getDifCode())
                        && db.getVif().equals(locator.getVif())
                        && db.getFunctionField().equals(locator.getFunctionField())
                        && (db.getStorageNumber() == locator.getStorageNumber())
                        && (db.getSubUnit() == locator.getDeviceUnit())
                        && (db.getTariff() == locator.getTariff())
                        && Arrays.equals(db.getVifes(),
                                locator.getVifes())) {
                    return db;
                }
            }
        } else if (frame == null) {
            return null;
        } else {
            throw new RuntimeException("Response is not a UserDataResponse but: " + frame);
        }

        throw new RuntimeException("can't find datablock of locator");
    }

    private DataBlock getTimeStampDB(GenericDevice dev, ValueRequestPointLocator locator) {
        // TODO implement
        return null;
    }

    public Connection getConnection() {
        return conn;
    }

    public void writeJsonStream(FileOutputStream os) throws UnsupportedEncodingException, IOException {
        try (OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
            JSONObject json = toJSON(JsonSerializeType.SLAVE_CONFIG);
            String text = json.toString(1);
            osw.write(text, 0, text.length());
            osw.flush();
        }
    }

    /**
     * @return the lastByteSended
     */
    public long getLastByteSended() {
        return lastByteSended;
    }

    private class StreamListener implements Runnable, DecoderListener {

        private final Decoder parser = new Decoder(this);

        @Override
        public void run() {
            log.fine("Thread MBus StreamListener Started");
            parser.reset();  // Just to make sure to have a clean parser
            try {
                try {
                    while (!isClosed()) {
                        try {
                            final int theData = conn.getInputStream().read();
                            if (theData == -1) {
                                if (log.isLoggable(Level.FINEST)) {
                                    log.finest("Thread interrupted or eof on waiting occured");
                                }
                            } else {
                                try {
                                    //TODO LOGGING 
                                    parser.addByte((byte) theData);
                                } catch (Exception e) {
                                    log.log(Level.SEVERE, "Error during createPackage()", e);
                                    parser.reset();
                                }
                            }
                        } catch (NullPointerException npe) {
                            if (!isClosed()) {
                                throw new RuntimeException(npe);
                            }
                        }
                    }
                    log.info("Thread MBus StreamListener Will stop");
                } catch (IOException e) {
                    if (isClosed()) {
                        log.log(Level.FINE, "Port Closed", e);
                    } else {
                        log.log(Level.SEVERE, "run()", e);
                    }
                } catch (RuntimeException e) {
                    if (isClosed()) {
                        log.log(Level.FINE, "Port Closed", e);
                    } else {
                        log.log(Level.INFO, "finished waiting for packages", e);
                    }
                }
            } catch (Throwable t) {
                log.log(Level.SEVERE, "END", t);
            } finally {
                log.fine("Thread MBus StreamListener Stopped");
                parser.reset();
            }
        }

        private boolean isClosed() {
            return conn == null ? true : conn.getConnState().equals(Connection.State.CLOSED) || conn.getConnState().equals(Connection.State.CLOSING);
        }

        @Override
        public void success(Frame frame) {
            log.log(Level.FINER, "New frame parsed {0}", frame);

            synchronized (frameQueue) {
                frameQueue.add(frame);
                frameQueue.notifyAll();
            }
        }

        void resetDecoder() {
            parser.reset();
        }

    }
    private final static Logger log = LogUtils.getMasterLogger();
    private final List<GenericDevice> devices = new ArrayList<>();
    private final Encoder encoder = new Encoder();
    private Thread t;
    private final StreamListener streamListener = new StreamListener();
    private final Queue<Frame> frameQueue = new ConcurrentLinkedQueue<>();
    private Connection conn;
    private long lastByteSended;

    public MBusMaster() {
        super();
    }

    /**
     *
     * @param device
     * @return true if the device is not Already in the List Identnumber,
     * Manufacturer and Medium
     * @TODO refresh devicedata???
     */
    public boolean addDevice(GenericDevice device) {
        for (GenericDevice dev : devices) {
            if (dev.getIdentNumber() == device.getIdentNumber()) {
                if (dev.getManufacturer().equals(device.getManufacturer()) && dev.getMedium().equals(device.getMedium())) {
                    return false;
                }
            }
        }
        return devices.add(device);
    }

    public GenericDevice searchDeviceByAddress(byte address)
            throws InterruptedException, IOException {
        Frame f = sendRequestUserData(address);
//        Frame f = sendInitSlave(address);
        GenericDevice result = null;
        if (f instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse) f;
            //TODO detect dev and create
            result = DeviceFactory.createDevice(udr, new RequestClassXData(Frame.ControlCode.REQ_UD2, (byte) address));
            addDevice(result);
            log.info(String.format("found device: address = 0x%02X, id = %08d, man = %s, medium = %s, version = 0x%02X",
                    udr.getAddress(),
                    udr.getIdentNumber(),
                    udr.getManufacturer(),
                    udr.getMedium(),
                    udr.getVersion()));
            //TODO check wenn dev braucht mehr ( falsces udr)
        } else {
            log.info(String.format("no device at address = 0x%02X", address));
        }
        return result;
    }

    public int deviceIndexOf(GenericDevice d) {
        return devices.indexOf(d);
    }

    public void clearDevices() {
        devices.clear();
    }

    private void clearFrameQueue() {
        synchronized (frameQueue) {
            frameQueue.clear();
        }
    }

    @Override
    public void close() throws IOException {
        if (conn != null) {
            log.fine("TRY CLOSING");
            conn.close();
            log.fine("CLOSED");
            /* TODO thread.interrrupt does not work in native posix blocking read ...
             try {
             Thread.sleep(10);
             } catch (InterruptedException ex) {
             throw new IOException(ex);
             }
             log.fine("Do INterrupt");
             t.interrupt();
             try {
             Thread.sleep(10);
             } catch (InterruptedException ex) {
             throw new IOException(ex);
             }
             */
            t = null;
        }
    }

    public int deviceCount() {
        return devices.size();
    }

    public GenericDevice getDevice(int i) {
        return devices.get(i);
    }

    public GenericDevice[] getDevices() {
        return devices.toArray(new GenericDevice[deviceCount()]);
    }

    /**
     * Idle time is 33 bit periods see M-Bus doc chaper 5.4
     *
     * @return
     */
    public long getIdleTime() {
        return 33000 / conn.getBitPerSecond();
    }

    /**
     * Timeout is 330 bit periods + 50ms see M-Bus doc chaper 5.4 2 times max
     * (256) packet size added
     *
     * @return
     */
    public long getResponseTimeout() {
        return ((((512 * 11) + 330) * 1000) / conn.getBitPerSecond()) + conn.getResponseTimeOutOffset();
    }

    public long getShortResponseTimeout() {
        return getIdleTime() * 10 + conn.getResponseTimeOutOffset();
    }

    @Override
    public Iterator<GenericDevice> iterator() {
        return devices.iterator();
    }

    private Frame removeFrame() {
        return frameQueue.remove();
    }

    public GenericDevice[] searchDevicesBySecondaryAddressing() throws IOException, InterruptedException {
        return widcardSearch(0xFFFFFFFF, (short) 0xFFFF, (byte) 0xFF, (byte) 0xFF);
    }

    /**
     * Search by primary address (0..250) 253 is for slave select 254 broadcast
     * addres to all devices
     *
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public GenericDevice[] searchDevicesByPrimaryAddress()
            throws IOException, InterruptedException {
        return searchDevicesByPrimaryAddress((byte) 0, MBusUtils.LAST_REGULAR_PRIMARY_ADDRESS);
    }

    public GenericDevice[] searchDevicesByPrimaryAddress(byte first, byte last)
            throws IOException, InterruptedException {
        List<GenericDevice> result = new ArrayList<>();
        final short fAddr = (short) (first & 0xFF);
        final short lAddr = (short) (last & 0xFF);
        for (short i = fAddr; i <= lAddr; i++) {
            GenericDevice c = searchDeviceByAddress((byte) i);
            if (c != null) {
                result.add(c);
            }
        }
        return result.toArray(new GenericDevice[result.size()]);
    }

    @Override
    public Frame send(Frame frame, int maxTries, long timeout)
            throws IOException, InterruptedException {
        for (int tries = 0; tries < maxTries; tries++) {
            clearFrameQueue();

            final byte[] b = encoder.encode(frame);

            //Reset the decoder from old leftovers...
            streamListener.resetDecoder();

            conn.getOutputStrteam().write(b);
            conn.getOutputStrteam().flush();
            lastByteSended = System.currentTimeMillis();

            Frame result = pollFrameOrWaitUntil(lastByteSended + timeout);

            if (result != null) {
                return result;
            } else {
                Thread.sleep(getIdleTime());
            }
        }
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "max tries({0}) reached .. aborting send to: {1}", new Object[]{maxTries, frame});
        }
        return null;
    }

    public Map<GenericDevice, Frame> sendRequestUserData(MBusAddressing addressing)
            throws IOException, InterruptedException {
        Map<GenericDevice, MBusAddressing> devMap = new HashMap<>();
        for (GenericDevice d : devices) {
            devMap.put(d, addressing);
        }
        return sendRequestUserData(devMap);
    }

    public Frame sendInitSlave(byte address) throws IOException, InterruptedException {
        SendInitSlave req = new SendInitSlave(address);
        return send(req, DEFAULT_SEND_TRIES, getResponseTimeout());
    }

    public Frame sendSetNewAddress(byte address, byte newAddress) throws InterruptedException, IOException {
        SendUserData req = new SendUserData();
        req.setAddress(address);
        req.addDataBlock(new ByteDataBlock(DataFieldCode._8_BIT_INTEGER, VifPrimary.BUS_ADDRESS, newAddress));
        return send(req, DEFAULT_SEND_TRIES, getResponseTimeout());
    }

    public Frame sendRequestUserData(byte address)
            throws IOException, InterruptedException {
        RequestClassXData req = new RequestClassXData(Frame.ControlCode.REQ_UD2, address);
        req.setFcb(true);
        return send(req, DEFAULT_SEND_TRIES, getResponseTimeout());
    }

    public Map<GenericDevice, Frame> sendRequestUserData(Map<GenericDevice, MBusAddressing> devices)
            throws IOException, InterruptedException {
        Map<GenericDevice, Frame> result = new HashMap<>();

        byte address;
        for (GenericDevice dev : devices.keySet()) {
            MBusAddressing addressing = devices.get(dev);
            if (MBusAddressing.SECONDARY.equals(addressing)) {
                selectDevice(dev);
                address = MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS;
            } else {
                address = dev.getAddress();
            }
            result.put(dev, sendRequestUserData(address));
            if (MBusAddressing.SECONDARY.equals(addressing)) {
                //TODO deselect??
            }
        }

        return result;
    }

    public int sendSlaveSelect(int bcdMaskedId, short maskedMan, byte maskedVersion,
            byte maskedMedium, int maxTries) throws IOException, InterruptedException {

        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Will select Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X", bcdMaskedId, maskedMan, maskedVersion, maskedMedium));
        }
        SelectionOfSlaves selOfSl = new SelectionOfSlaves((byte) MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);
        selOfSl.setBcdMaskedId(bcdMaskedId);
        selOfSl.setMaskedMan(maskedMan);
        selOfSl.setMaskedVersion(maskedVersion);
        selOfSl.setMaskedMedium(maskedMedium);

        int result;
        Frame resultFrame = send(selOfSl, maxTries, getShortResponseTimeout());
        if (resultFrame == null) {
            return 0;
        }
        if (resultFrame instanceof SingleCharFrame) {
            log.log(Level.FINE, "Slave selected {0}", bcdMaskedId);
            result = 1;
        } else if (resultFrame instanceof GarbageCharFrame) {
            log.log(Level.FINE, "Multiple Slaves selected {0}", bcdMaskedId);
            result = 2;
        } else {
            log.severe(String.format("unexpected Frame received \n \"%s\" \n tried to select Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X", resultFrame.toString(), bcdMaskedId, maskedMan, maskedVersion, maskedMedium));
            return 0;
        }
        log.fine("Wait for more Answers of slave select");
        result += waitForSingleCharsOrGarbage(getShortResponseTimeout());
        return result;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public Closeable open() throws IOException {
        conn.open();
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
        return this;
    }

    private Frame pollFrameOrWaitUntil(long endTime) throws InterruptedException {
        synchronized (frameQueue) {
            while (endTime - System.currentTimeMillis() > 0) {
                if (frameQueue.peek() == null) {
                    log.log(Level.FINE, "Wait max for {0} ms", endTime - System.currentTimeMillis());
                    frameQueue.wait(endTime - System.currentTimeMillis());
                } else {
                    return frameQueue.poll();
                }
            }
            return frameQueue.poll();
        }
    }

    private int waitForSingleCharsOrGarbage(long timeout)
            throws InterruptedException {
        int result = 0;

        while ((System.currentTimeMillis() - lastByteSended) <= timeout) {
            Frame frame = pollFrameOrWaitUntil(lastByteSended + timeout);

            if (frame instanceof SingleCharFrame) {
                result++;
            } else if (frame instanceof GarbageCharFrame) {
                result++;
            } else {
                return result;
            }
        }

        return result;
    }

    private int getLeftmostMaskedNibble(int value) {
        int mask = 0xF0000000;
        for (int nibblePos = 7; nibblePos >= 0; nibblePos--) {
            if ((value & mask) == mask) {
                return nibblePos;
            } else {
                mask >>>= 4;
            }
        }
        return -1;
    }

    private int exchangeNibbleAtPos(int nibblePos, int value, int nibbleValue) {
        int mask = ~(0x0F << (nibblePos * 4));
        int nibbleToSet = (nibbleValue & 0x0F) << (nibblePos * 4);
        return (value & mask) | nibbleToSet;
    }

    //TODO detect end change all maked BCD
    public GenericDevice[] widcardSearch(int bcdMaskedId, short bcdMaskedMan, byte bcdMaskedVersion, byte bcdMaskedMedium)
            throws IOException, InterruptedException {
        List<GenericDevice> result = new ArrayList<>();
        log.fine(String.format("widcardSearch bcdMaskedId: 0x%08X", bcdMaskedId));
        int answers = sendSlaveSelect(bcdMaskedId, bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium, DEFAULT_SEND_TRIES);
        if (answers == 0) {
            log.fine(String.format("no slave with bcdMaskedId: 0x%08X", bcdMaskedId));
        } else if (answers == 1) {
            log.fine(String.format("detect slave with bcdMaskedId: 0x%08X", bcdMaskedId));
            GenericDevice dev = searchDeviceByAddress(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);
            if (dev == null) {
                // someone does not play by the rule so try to fint them nevertheless
                log.info(String.format("maybe multiple slaves (%d) with mask: 0x%08X", answers, bcdMaskedId));
                int leftmostMaskedNibble = getLeftmostMaskedNibble(bcdMaskedId);
                if (leftmostMaskedNibble >= 0) {
                    for (int i = 0; i <= 9; i++) {
                        GenericDevice[] devs = widcardSearch(exchangeNibbleAtPos(leftmostMaskedNibble, bcdMaskedId, i), bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium);
                        result.addAll(Arrays.asList(devs));
                    }
                } else {
                    log.warning(String.format("Can't separate slaves (%d) with id: 0x%08X", answers,
                            bcdMaskedId));
                }
            } else {
                result.add(dev);
            }
        } else {
            log.fine(String.format("multiple slaves (%d) with mask: 0x%08X", answers, bcdMaskedId));
            int leftmostMaskedNibble = getLeftmostMaskedNibble(bcdMaskedId);
            if (leftmostMaskedNibble >= 0) {
                for (int i = 0; i <= 9; i++) {
                    GenericDevice[] devs = widcardSearch(exchangeNibbleAtPos(leftmostMaskedNibble, bcdMaskedId, i), bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium);
                    result.addAll(Arrays.asList(devs));
                }
            } else {
                log.warning(String.format("Can't separate slaves (%d) with id: 0x%08X", answers,
                        bcdMaskedId));
            }
        }
        return result.toArray(new GenericDevice[result.size()]);
    }

    public UserDataResponse readResponseBySecondary(int bcdId, String man, Byte version, MBusMedium medium, int maxTries) throws IOException, InterruptedException {
        final short bcdMan = (man == null || man.length() == 0) ? (short) 0xFFFF : MBusUtils.man2Short(man);
        byte bcdVersion = (version == null) ? (byte) 0xFF : version;
        byte bcdMedium = (medium == null) ? (byte) 0xFF : (byte) medium.getId();

        if (selectDevice(bcdId, bcdMan, bcdVersion, bcdMedium, maxTries)) {
            return readResponse(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);
        } else {
            return null;
        }
    }

    public UserDataResponse readResponse(byte address) throws IOException, InterruptedException {
        Frame fi = sendInitSlave(address);
        Frame f = sendRequestUserData(address);
        if (f instanceof UserDataResponse) {
            return (UserDataResponse) f;
        } else {
//TODO Ex??
            return null;
        }
    }

    public boolean selectDevice(int bcdMaskedId, short maskedMan, byte maskedVersion, byte maskedMedium, int maxTries) throws IOException, InterruptedException {
        int answers = sendSlaveSelect(bcdMaskedId, maskedMan, maskedVersion, maskedMedium, maxTries);
        if (answers > 1) {
            log.warning(String.format("Can't select select (too many) Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X", bcdMaskedId, maskedMan, maskedVersion, maskedMedium));
        } else if (answers == 0) {
            log.warning(String.format("Can't select select (none) Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X", bcdMaskedId, maskedMan, maskedVersion, maskedMedium));
        }
        return answers == 1;
    }

    public boolean selectDevice(int id, String manufacturer, byte version, MBusMedium medium) throws IOException, InterruptedException {
        return selectDevice(MBusUtils.int2Bcd(id),
                MBusUtils.man2Short(manufacturer),
                version,
                (byte) medium.getId(), DEFAULT_SEND_TRIES);
    }

    public boolean setPrimaryAddressOfDevice(byte newAddress, MBusResponseFramesContainer dev) throws IOException, InterruptedException {
        if (selectDevice(dev)) {
            final Frame f = sendSetNewAddress(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS, newAddress);
            sendInitSlave(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);
            if (f instanceof SingleCharFrame) {
                dev.setAddress(newAddress);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setPrimaryAddressOfDevice(byte newAddress, int id, String manufacturer, byte version, MBusMedium medium) throws IOException, InterruptedException {
        if (selectDevice(id, manufacturer, version, medium)) {
            final Frame f = sendSetNewAddress(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS, newAddress);
            sendInitSlave(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);
            return f instanceof SingleCharFrame;
        } else {
            return false;
        }
    }

    public boolean selectDevice(MBusResponseFramesContainer dev) throws IOException, InterruptedException {
        return selectDevice(MBusUtils.int2Bcd(dev.getIdentNumber()),
                MBusUtils.man2Short(dev.getManufacturer()),
                dev.getVersion(),
                (byte) dev.getMedium().getId(), DEFAULT_SEND_TRIES);
    }

    public void readValues(ValueRequest<?> requests)
            throws IOException, InterruptedException {
        //Create devices if neccecary
        HashMap<GenericDevice, MBusAddressing> result = new HashMap<>();

        for (ValueRequestPointLocator locator : requests) {
            GenericDevice myDevice = getDevice(devices, locator);

            if (myDevice == null) {
                myDevice
                        = DeviceFactory.createDevice(locator.getAddress(),
                                locator.getManufacturer(),
                                locator.getMedium(),
                                locator.getVersion(),
                                locator.getIdentnumber());
                addDevice(myDevice);
            }

            result.put(myDevice, locator.getAddressing());
        }

        //get data from devices
        //TODO honor frames 
        Map<GenericDevice, Frame> responses = sendRequestUserData(result);

        //pack response
        for (ValueRequestPointLocator locator : requests) {
            GenericDevice dev = getDevice(result.keySet(), locator);
            DataBlock db = getDataBlock(responses.get(dev),
                    locator);
            locator.setDb(db);
            db = getTimeStampDB(dev, locator);
            locator.setTimestampDb(db);
        }
    }

    public static MBusMaster readJsonStream(InputStream is)
            throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        MBusMaster result = new MBusMaster();
        result.fromJSON(JSONObject.fromObject(sb.toString()));

        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        conn = Connection.createFromJSON(json);

        JSONArray jsonDevices = json.getJSONArray("devices");

        for (int i = 0; i < jsonDevices.size(); i++) {
            GenericDevice device = new GenericDevice();
            device.fromJSON(jsonDevices.getJSONObject(i));
            addDevice(device);
        }
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();

        result.accumulate(conn.getJsonFieldName(), conn.toJSON(jsonSerializeType));

        JSONArray jsonDevices = new JSONArray();

        for (GenericDevice device : devices) {
            jsonDevices.add(device.toJSON(jsonSerializeType));
        }

        result.accumulate("devices", jsonDevices);

        return result;
    }
}
