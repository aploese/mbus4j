/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.master;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.decoder.PacketParser;
import net.sf.mbus4j.devices.DeviceFactory;
import net.sf.mbus4j.devices.MBusDevice;
import net.sf.mbus4j.encoder.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the MBus devices connected via inputStream/OutputStream.
 * 
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Master implements Iterable<MBusDevice> {

    private class StreamListener implements Runnable {

        @Override
        public void run() {
            try {
                int theData;
                PacketParser parser = new PacketParser();
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
    public final static int UNCONFIGURED_PRIMARY_ADDRESS = 0x00;
    public final static int FIRST_REGULAR_PRIMARY_ADDRESS = 0x01;
    public final static int LAST_REGULAR_PRIMARY_ADDRESS = 0xFA;// 250
    public final static int SLAVE_SELECT_PRIMARY_ADDRESS = 0xFD; // 253
    public final static int BROADCAST_WITH_ANSWER_PRIMARY_ADDRESS = 0xFE;// 254
    public final static int BROADCAST_NO_ANSWER_PRIMARY_ADDRESS = 0xFF;//255
    private static Logger log;

    public static SerialPort openPort(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        return openPort(portName, 2400);
    }

    public static SerialPort openPort(String portName, int baudrate) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        // Obtain a CommPortIdentifier object for the port you want to open.
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        SerialPort sPort = (SerialPort) portId.open(Master.class.getName(), 30000);
        sPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
        sPort.enableReceiveTimeout(1000);
        sPort.setInputBufferSize(512);
        sPort.setOutputBufferSize(512);
        sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        return sPort;
    }
    private List<MBusDevice> devices = new ArrayList<MBusDevice>();
    private InputStream is;
    private OutputStream os;
    private Encoder encoder = new Encoder();
    private boolean closed;
    private Thread t;
    private StreamListener streamListener = new StreamListener();
    private int bitPerSecond;
    private final Queue<Frame> frameQueue = new ConcurrentLinkedQueue<Frame>();

    public Master() {
        super();
        log = LoggerFactory.getLogger(Master.class);
    }

    public boolean addDevice(MBusDevice device) {
        return devices.add(device);
    }

    public void addSlaveByAddress(int address) throws InterruptedException, IOException {
        Frame f = sendRequestUserData(address);
        if (f instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse) f;
            //TODO detect dev and create
            devices.add(DeviceFactory.createDevice(udr));
            //TODO check wenn dev braucht mehr ( falsces udr)
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

    public MBusDevice getDevice(int i) {
        return devices.get(i);
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
    public Iterator<MBusDevice> iterator() {
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

    public void search() throws IOException, InterruptedException {
        widcardSearch(0x00, 7, 0xFFFF, 0xFF, 0xFF);
    }

    /**
     * Search by primary address (0..250)
     * 253 is for slave select
     * 254 broadcast addres to all devices
     *
     * @return
     */
    public MBusDevice[] searchDevicesByPrimaryAddress() throws IOException, InterruptedException {
        return searchDevicesByPrimaryAddress(0, LAST_REGULAR_PRIMARY_ADDRESS);
    }

    public MBusDevice[] searchDevicesByPrimaryAddress(int first, int last) throws IOException, InterruptedException {
        clear();
        for (int i = first; i <= last; i++) {
            addSlaveByAddress(i);
        }
        return devices.toArray(new MBusDevice[devices.size()]);
    }

    //TODO
    public void selectBySecondaryAddress() {
    }

    private Frame send(Frame frame) throws IOException, InterruptedException {
        for (int trys = 0; trys <= 2; trys++) {
            clearFrameQueue();
            final byte[] b = encoder.encode(frame);
            os.write(b);
            os.flush();
            final long time = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug(String.format("Data Sent (try: %d): %s ", trys, PacketParser.bytes2Ascii(b)));
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

    public Frame sendRequestUserData(int primaryAddress) throws IOException, InterruptedException {
        RequestClassXData req = new RequestClassXData(Frame.ControlCode.REQ_UD2);
        req.setAddress((byte) primaryAddress);
        return send(req);
    }

    public void sendRequestUserData(Iterable<MBusDevice> devices) throws IOException, InterruptedException {
        for (MBusDevice dev : devices) {
            sendRequestUserData(dev.getPrimaryAddress());
        }
    }

    public void sendSlaveSelect(byte[] idWithMask, short manWithMask, byte versionWithMask) {
        //TODO search...
    }

    public void sendSlaveSelect(MBusDevice dev) {
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
    public void widcardSearch(int leadingBcdDigitsId, int maskLength, int maskedMan, int maskedVersion, int maskedMedium) throws IOException, InterruptedException {
        clear();
        log.debug(String.format("widcardSearch leadingBcdDigitsId: 0x%08X, maskLength: %d", leadingBcdDigitsId, maskLength));
        SelectionOfSlaves sud = new SelectionOfSlaves((byte) Master.SLAVE_SELECT_PRIMARY_ADDRESS);
        int idMask = 0;
        for (int i = 1; i <= maskLength; i++) {
            idMask <<= 4;
            idMask |= 0x0F;
        }

        for (int i = 0; i <= 9; i++) {
            sud.setMaskedId((leadingBcdDigitsId << (maskLength * 4)) + idMask);
            sud.setMaskedMan(maskedMan);
            sud.setMaskedVersion(maskedVersion);
            sud.setMaskedMedium(maskedMedium);
            Frame result = send(sud);
            if (result instanceof SingleCharFrame) {
                log.debug(String.format("got answer with mask: 0x%08X", leadingBcdDigitsId));
                int answers = waitForSingleChars(getResponseTimeout());
                if (answers == 0) {
                    log.debug(String.format("detect slave with mask: 0x%08X", leadingBcdDigitsId));
                    addSlaveByAddress(SLAVE_SELECT_PRIMARY_ADDRESS);
                } else {
                    if (maskLength > 0) {
                        log.debug(String.format("multiple slaves (%d) with mask: 0x%08X", answers, leadingBcdDigitsId));
                        widcardSearch(leadingBcdDigitsId << 4, maskLength - 1, maskedMan, maskedVersion, maskedMedium);
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
}
