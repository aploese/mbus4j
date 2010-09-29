/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
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
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j.master;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import net.sf.mbus4j.MBusConstants;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.Connection;
import net.sf.mbus4j.SerialPortConnection;
import net.sf.mbus4j.TcpIpConnection;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

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
        JSONSerializable {

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
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        JSONObject json = toJSON(JsonSerializeType.SLAVE_CONFIG);
        String text = json.toString(1);
        osw.write(text, 0, text.length());
        osw.flush();
        osw.close();
    }

    private class StreamListener
            implements Runnable {

        private Decoder parser = new Decoder();

        @Override
        public void run() {
            try {
                int theData;
                try {
                    while (!isClosed()) {
                        try {
                            if ((theData = conn.getInputStream().read()) == -1) {
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
                            if (!isClosed()) {
                                throw new RuntimeException(npe);
                            }
                        }
                    }

                    log.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    if (isClosed()) {
                        log.debug("Port Closed", e);
                    } else {
                        log.error("run()", e);
                    }
                } catch (Exception e) {
                    if (isClosed()) {
                        log.debug("Port Closed", e);
                    } else {
                        log.info("finished waiting for packages", e);
                    }
                }
            } finally {
                parser.reset();
            }
        }

        private boolean isClosed() {
            return conn == null ? true : conn.getConnState().equals(Connection.State.CLOSED) || conn.getConnState().equals(Connection.State.CLOSING);
        }
    }
    private final static Logger log = LoggerFactory.getLogger(MBusMaster.class);
    private List<GenericDevice> devices = new ArrayList<GenericDevice>();
    private Encoder encoder = new Encoder();
    private Thread t;
    private StreamListener streamListener = new StreamListener();
    private final Queue<Frame> frameQueue = new ConcurrentLinkedQueue<Frame>();
    private Connection conn;

    public MBusMaster() {
        super();
    }

    /**
     *
     * @param device
     * @return true if the device is not Already in the List Identnumber, Manufacturer and Medium
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

    public GenericDevice addDeviceByAddress(int address)
            throws InterruptedException, IOException {
        Frame f = sendRequestUserData(address);
        GenericDevice result = null;
        if (f instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse) f;
            //TODO detect dev and create
            result = DeviceFactory.createDevice(udr, new RequestClassXData(Frame.ControlCode.REQ_UD2, (byte) address));
            addDevice(result);
            log.info(String.format("added device: address = 0x%02X, id = = %08d, man = %s, medium = %s, version = 0x%02X",
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

    public void close() throws InterruptedException, IOException {
        if (conn != null) {
            conn.close();
            Thread.sleep(100);
            t.interrupt();
        }
    }

    /**
     * @TODO implement
     */
    public void deselectBySecondaryAddress() {
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
     * @return
     */
    private long getIdleTime() {
        return 33000 / conn.getBitPerSecond();
    }

    /**
     * Timeout is 330 bit periods + 50ms see M-Bus doc chaper 5.4
     * 2 times max (256) packet size added
     * @return
     */
    private long getResponseTimeout() {
        return ((((512 * 11) + 330) * 1000) / conn.getBitPerSecond()) + conn.getResponseTimeOutOffset();
    }

    @Override
    public Iterator<GenericDevice> iterator() {
        return devices.iterator();
    }

    private Frame removeFrame() {
        return frameQueue.remove();
    }

    public GenericDevice[] searchDevicesBySecondaryAddressing(int maxTries) throws IOException, InterruptedException {
        return widcardSearch(0x00, 7, (short) 0xFFFF, (byte) 0xFF, (byte) 0xFF, maxTries);
    }

    /**
     * Search by primary address (0..250)
     * 253 is for slave select
     * 254 broadcast addres to all devices
     *
     * @return
     */
    public GenericDevice[] searchDevicesByPrimaryAddress()
            throws IOException, InterruptedException {
        return searchDevicesByPrimaryAddress(0, MBusConstants.LAST_REGULAR_PRIMARY_ADDRESS);
    }

    public GenericDevice[] searchDevicesByPrimaryAddress(int first, int last)
            throws IOException, InterruptedException {
        List<GenericDevice> result = new ArrayList<GenericDevice>();
        for (int i = first; i <= last; i++) {
            GenericDevice c = addDeviceByAddress(i);
            if (c != null) {
                result.add(c);
            }
        }
        return result.toArray(new GenericDevice[result.size()]);
    }

    //TODO
    public void selectBySecondaryAddress() {
    }

    @Override
    public Frame send(Frame frame)
            throws IOException, InterruptedException {
        return send(frame, DEFAULT_MAY_TRIES);
    }

    @Override
    public Frame send(Frame frame, int maxTries)
            throws IOException, InterruptedException {
        for (int tries = 0; tries <= maxTries; tries++) {
            clearFrameQueue();

            final byte[] b = encoder.encode(frame);
            conn.getOutputStrteam().write(b);
            conn.getOutputStrteam().flush();

            final long time = System.currentTimeMillis();

            if (log.isDebugEnabled()) {
                log.debug(String.format("Data Sent (try: %d): %s ",
                        tries,
                        Decoder.bytes2Ascii(b)));
            }

            Frame result = waitAndPollFrame(getResponseTimeout());
            log.info(String.format("Answer took %d ms", System.currentTimeMillis() - time));

            if (result != null) {
                return result;
            } else {
                Thread.sleep(getIdleTime());

                if (tries < 2) {
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

    public void sendRequestUserData()
            throws IOException, InterruptedException {
        sendRequestUserData(devices);
    }

    public Frame sendRequestUserData(int address)
            throws IOException, InterruptedException {
        RequestClassXData req = new RequestClassXData(Frame.ControlCode.REQ_UD2, (byte) address);

        return send(req);
    }

    public Map<GenericDevice, Frame> sendRequestUserData(Iterable<GenericDevice> devices)
            throws IOException, InterruptedException {
        Map<GenericDevice, Frame> result = new HashMap<GenericDevice, Frame>();

        for (GenericDevice dev : devices) {
            result.put(dev,
                    sendRequestUserData(dev.getAddress()));
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

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public void open() throws IOException {
        conn.open();
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
    }

    private Frame waitAndPollFrame(long timeout)
            throws InterruptedException {
        log.debug("timeout: " + timeout);

        synchronized (frameQueue) {
            if (frameQueue.peek() == null) {
                if (timeout > 0) {
                    frameQueue.wait(100 + conn.getResponseTimeOutOffset());
                    //TODO apl 100ms is enought???  
                    //If there is nothing until now, there will be nothing in the future
                    if (this.streamListener.parser.getState() != Decoder.DecodeState.EXPECT_START) {
                        frameQueue.wait(timeout - 100 - conn.getResponseTimeOutOffset());
                    }
                }
            }
            return frameQueue.poll();
        }
    }

    private int waitForSingleChars(long timeout)
            throws InterruptedException {
        int result = 0;
        final long time = System.currentTimeMillis();

        while ((System.currentTimeMillis() - time) <= timeout) {
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
    public GenericDevice[] widcardSearch(int leadingBcdDigitsId, int maskLength, short bcdMan, byte bcdVersion, byte bcdMedium, int maxTries)
            throws IOException, InterruptedException {
        List<GenericDevice> result = new ArrayList<GenericDevice>();
        log.debug(String.format("widcardSearch leadingBcdDigitsId: 0x%08X, maskLength: %d", leadingBcdDigitsId,
                maskLength));

        SelectionOfSlaves selOfSl = new SelectionOfSlaves((byte) MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS);
        int idMask = 0;

        for (int i = 1; i <= maskLength; i++) {
            idMask <<= 4;
            idMask |= 0x0F;
        }

        for (int i = 0; i <= 9; i++) {
            selOfSl.setBcdId((leadingBcdDigitsId << (maskLength * 4)) + idMask);
            selOfSl.setBcdMan(bcdMan);
            selOfSl.setBcdVersion(bcdVersion);
            selOfSl.setBcdMedium(bcdMedium);

            Frame resultFrame = send(selOfSl, maxTries);

            if (resultFrame instanceof SingleCharFrame) {
                log.debug(String.format("got answer with mask: 0x%08X", leadingBcdDigitsId));

                int answers = waitForSingleChars(getResponseTimeout());

                if (answers == 0) {
                    log.debug(String.format("detect slave with mask: 0x%08X", leadingBcdDigitsId));
                    GenericDevice dev = addDeviceByAddress(MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS);
                    result.add(dev);
                } else {
                    if (maskLength > 0) {
                        log.debug(String.format("multiple slaves (%d) with mask: 0x%08X", answers, leadingBcdDigitsId));
                        GenericDevice[] devs = widcardSearch(leadingBcdDigitsId << 4, maskLength - 1, bcdMan, bcdVersion, bcdMedium, maxTries);
                        result.addAll(Arrays.asList(devs));
                    } else { // wenn idMask == 8 man ver und medium ???
                        log.error(String.format("Cant separate slaves (%d) with id: 0x%08X", answers,
                                leadingBcdDigitsId));
                    }
                }
            } else {
                log.debug(String.format("no slave with mask: 0x%08X", leadingBcdDigitsId));
            }

            leadingBcdDigitsId++;
        }
        return result.toArray(new GenericDevice[result.size()]);
    }

    public UserDataResponse readResponseBySecondary(int bcdId, String man, Byte version, MBusMedium medium, int maxTries) throws IOException, InterruptedException {
        final short bcdMan = (man == null || man.length() == 0) ? (short)0xFFFF : MBusConstants.man2Short(man);
        byte bcdVersion = (version == null) ? (byte)0xFF : version;
        byte bcdMedium = (medium == null) ? (byte) 0xFF : (byte) medium.getId();

        if (selectDevice(bcdId, bcdMan, bcdVersion, bcdMedium, maxTries)) {
            return readResponse(MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS);
        } else {
            return null;
        }
    }

    public UserDataResponse readResponse(int address) throws IOException, InterruptedException {
       Frame f = sendRequestUserData(address);
        if (f instanceof UserDataResponse) {
            return (UserDataResponse) f;
        } else {
//TODO Ex??
           return null;
        }
     }

    public boolean selectDevice(int bcdId, short bcdMan, byte bcdVersion, byte bcdMedium, int maxTries) throws IOException, InterruptedException {
        SelectionOfSlaves selOfSl = new SelectionOfSlaves((byte) MBusConstants.SLAVE_SELECT_PRIMARY_ADDRESS);
        selOfSl.setBcdId(bcdId);
        selOfSl.setBcdMan(bcdMan);
        selOfSl.setBcdVersion(bcdVersion);
        selOfSl.setBcdMedium(bcdMedium);
            Frame resultFrame = send(selOfSl, maxTries);

            if (resultFrame instanceof SingleCharFrame) {
                int answers = waitForSingleChars(getResponseTimeout());
                return answers == 0;
            } else {
                return false;
            }
    }

    public void readValues(ValueRequest<?> requests)
            throws IOException, InterruptedException {
        //Create devices if neccecary
        Set<GenericDevice> deviceSet = new HashSet<GenericDevice>();

        for (ValueRequestPointLocator locator : requests) {
            GenericDevice myDevice = getDevice(devices, locator);

            if (myDevice == null) {
                myDevice =
                        DeviceFactory.createDevice(locator.getAddress(),
                        locator.getManufacturer(),
                        locator.getMedium(),
                        locator.getVersion(),
                        locator.getIdentnumber());
                addDevice(myDevice);
            }

            deviceSet.add(myDevice);
        }

        //get data from devices
        //TODO hounor frames and addressing
        Map<GenericDevice, Frame> responses = sendRequestUserData(deviceSet);

        //pack response
        for (ValueRequestPointLocator locator : requests) {
            GenericDevice dev = getDevice(deviceSet, locator);
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
        if (json.containsKey("serialConnection")) {
            conn = new SerialPortConnection();
            conn.fromJSON(json.getJSONObject("serialConnection"));
        } else if (json.containsKey("tcpIpConnection")) {
            conn = new TcpIpConnection();
            conn.fromJSON(json.getJSONObject("tcpIpConnection"));
        }

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

        if (conn instanceof SerialPortConnection) {
            result.accumulate("serialConnection", conn.toJSON(jsonSerializeType));
        } else if (conn instanceof TcpIpConnection) {
            result.accumulate("tcpIpConnection", conn.toJSON(jsonSerializeType));
        }

        JSONArray jsonDevices = new JSONArray();

        for (GenericDevice device : devices) {
            jsonDevices.add(device.toJSON(jsonSerializeType));
        }

        result.accumulate("devices", jsonDevices);

        return result;
    }
}
