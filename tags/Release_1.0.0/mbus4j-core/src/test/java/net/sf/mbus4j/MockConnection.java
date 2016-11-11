package net.sf.mbus4j;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.log.LogUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public abstract class MockConnection extends Connection {

    protected static final Logger log = LogUtils.getMasterLogger();

    public MockConnection(int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
    }

    protected MBusTestInputStream getMockIs() {
        return (MBusTestInputStream) is;
    }

    protected MBusTestOutputStream getMockOs() {
        return (MBusTestOutputStream) os;
    }

    class Data {

        final String request;
        final String response;
        final long noResponseWaitTime;
        final StackTraceElement[] stackTrace;

        Data(String request, long noResponseWaitTime) {
            this.response = null;
            this.request = request;
            this.noResponseWaitTime = noResponseWaitTime;
            this.stackTrace = new Error().fillInStackTrace().getStackTrace();
        }

        Data(String request, String response) {
            this.response = response;
            this.request = request;
            this.noResponseWaitTime = 0;
            this.stackTrace = new Error().fillInStackTrace().getStackTrace();
        }
    }

    public class MBusTestInputStream extends InputStream {

        byte[] buffer = new byte[0];
        int readPtr;
        long endWaitTime;
        StackTraceElement[] stackTrace;
        private boolean closed;
        final Object readLock = new Object();
        boolean locked = true;

        @Override
        public void close() {
            synchronized (readLock) {
                closed = true;
                locked = false;
                readLock.notifyAll();
            }
        }

        private boolean isNoDataLeft() {
            return readPtr == buffer.length;
        }

        @Override
        public int read() throws IOException {
            synchronized (readLock) {
                int result;
                if (locked) {
                    log.fine("ReadLock is locked ... will wait");
                    try {
                        readLock.wait();
                        log.fine("ReadLock released ... will read from is ");
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (closed) {
                    return -1;
                }
                result = buffer[readPtr++] & 0xFF;
                if ((readPtr == buffer.length) && (buffer.length > 0)) {
                    log.info(String.format("Readed from InputStream: %s", Decoder.bytes2Ascii(buffer)));
                    this.locked = true;
                    lastByteReading(endWaitTime);
                }
                log.fine(String.format("Return from is.read 0x%08X", result));
                return result;
            }
        }

        protected void releaseReadLock() {
            synchronized (readLock) {
                if (buffer.length == 0) {
                    log.info("Bufer is empty ReadLock NOT released");
                    return;
                }
                locked = false;
                readLock.notifyAll();
                log.info("ReadLock released");
            }
        }

        public synchronized void setData(Data data) {
            log.info(String.format("Set InputStream buffer: %s timeout: %d", data.response, data.noResponseWaitTime));
            buffer = data.response != null ? Decoder.ascii2Bytes(data.response) : new byte[0];
            readPtr = 0;
            this.endWaitTime = data.noResponseWaitTime;
            this.stackTrace = data.stackTrace;
            synchronized (readLock) {
                this.locked = true;
                log.info("ReadLock set");
            }
        }
    }

    public class MBusTestOutputStream extends OutputStream {

        private int ptr;
        private byte[] expected = new byte[0];
        private StackTraceElement[] stackTrace;

        private boolean isNoDataLeft() {
            return ptr == expected.length;
        }

        public void setData(Data data) {
            log.info(String.format("Set OutputStream buffer: %s", data.request));
            ptr = 0;
            expected = data.request != null ? Decoder.ascii2Bytes(data.request) : new byte[0];
            this.stackTrace = data.stackTrace;
        }

        @Override
        public synchronized void write(int b) throws IOException {
            log.fine(String.format("Write to OutputStream: 0x%02X", b & 0xFF));
            if (ptr > expected.length - 1) {
                ptr++;
                IOException ioe = new IOException(String.format("%s at outside pos: %d was: 0x%02x ", Decoder.bytes2Ascii(expected), ptr - 1, b & 0xFF));
                ioe.setStackTrace(stackTrace);
                throw ioe;
            }
            if ((b & 0xFF) != (expected[ptr] & 0xFF)) {
                ptr++;
                String exp = Decoder.bytes2Ascii(expected);
                IOException ioe = new IOException(String.format("%s[%s]%s at pos: %d expected: 0x%02x but was: 0x%02x ", exp.substring(0, ptr * 2 - 2), exp.substring(ptr * 2 - 2, ptr * 2), exp.substring(ptr * 2, exp.length()), ptr - 1, expected[ptr - 1] & 0xFF, b & 0xFF));
                ioe.setStackTrace(stackTrace);
                throw ioe;
            }
            ptr++;
            if (expected.length == ptr) {
                log.info(String.format("Written to OutputStream: %s", Decoder.bytes2Ascii(expected)));
                lastByteWriting();
            }
        }
    }
    List<Data> data = new ArrayList<>();

    @Override
    public void close() throws IOException {
        setConnState(State.CLOSING);
        os.close();
        is.close();
        setConnState(State.CLOSED);
    }

    @Override
    public void open() throws IOException {
        setConnState(State.OPENING);
        is = new MBusTestInputStream();
        os = new MBusTestOutputStream();
        setConnState(State.OPEN);
    }

    protected abstract void lastByteReading(long endWaitTime) throws IOException;

    protected abstract void lastByteWriting() throws IOException;

    /**
     * Checks if data is empty, if not throws an exception to mark the position
     * @return
     * @throws IOException if data is not empty
     */
    public boolean checkNoDataLeft() throws IOException {
        if (!data.isEmpty()) {
            IOException ioe = new IOException("Was not called");
            ioe.setStackTrace(data.get(0).stackTrace);
            throw ioe;
        }
        return getMockIs().isNoDataLeft()&& getMockOs().isNoDataLeft();
    }

    public void replay() {
        setNextData(false);
    }

    protected abstract void setNextData(boolean removeFirst);

}
