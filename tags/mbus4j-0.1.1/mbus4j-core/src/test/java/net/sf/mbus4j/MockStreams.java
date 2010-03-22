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
package net.sf.mbus4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.mbus4j.decoder.Decoder;

import org.slf4j.Logger;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public abstract class MockStreams {

    class Data {

        final String request;
        final String response;
        final long noResponseWaitTime;

        Data(String request, long noResponseWaitTime) {
            this.response = null;
            this.request = request;
            this.noResponseWaitTime = noResponseWaitTime;
        }

        Data(String request, String response) {
            this.response = response;
            this.request = request;
            this.noResponseWaitTime = 0;
        }
    }

    public class MBusTestInputStream extends InputStream {

        byte[] buffer = new byte[0];
        int readPtr;
        long endWaitTime;
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

        private boolean isOK() {
            return readPtr == buffer.length;
        }

        @Override
        public int read() throws IOException {
            synchronized (readLock) {
                int result;
                if (locked) {
                    getLog().debug("ReadLock is locked ... will wait");
                    try {
                        readLock.wait();
                        getLog().debug("ReadLock released ... will read from is ");
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (closed) {
                    return -1;
                }
                result = buffer[readPtr++] & 0xFF;
                if ((readPtr == buffer.length) && (buffer.length > 0)) {
                    getLog().info(String.format("Readed from InputStream: %s", Decoder.bytes2Ascii(buffer)));
                    this.locked = true;
                    lastByteReading(endWaitTime);
                }
                getLog().debug(String.format("Return from is.read 0x%08X", result));
                return result;
            }
        }

        protected void releaseReadLock() {
            synchronized (readLock) {
                if (buffer.length == 0) {
                    getLog().info("Bufer is empty ReadLock NOT released");
                    return;
                }
                locked = false;
                readLock.notifyAll();
                getLog().info("ReadLock released");
            }
        }

        public synchronized void setData(String string, long endWaitTime) {
            getLog().info(String.format("Set InputStream buffer: %s timeout: %d", string, endWaitTime));
            buffer = string != null ? Decoder.ascii2Bytes(string) : new byte[0];
            readPtr = 0;
            this.endWaitTime = endWaitTime;
            synchronized (readLock) {
                this.locked = true;
                getLog().info("ReadLock set");
            }
        }
    }

    public class MBusTestOutputStream extends OutputStream {

        private int ptr;
        private byte[] expected = new byte[0];

        private boolean isOK() {
            return ptr == expected.length;
        }

        public void setData(String data) {
            getLog().info(String.format("Set OutputStream buffer: %s", data));
            ptr = 0;
            expected = data != null ? Decoder.ascii2Bytes(data) : new byte[0];
        }

        @Override
        public synchronized void write(int b) throws IOException {
            getLog().debug(String.format("Write to OutputStream: 0x%02X", b & 0xFF));
            if (ptr > expected.length - 1) {
                ptr++;
                throw new IOException(String.format("%s at outside pos: %d was: 0x%02x ", Decoder.bytes2Ascii(expected), ptr - 1, b & 0xFF));
            }
            if ((b & 0xFF) != (expected[ptr] & 0xFF)) {
                ptr++;
                String exp = Decoder.bytes2Ascii(expected);
                throw new IOException(String.format("%s[%s]%s at pos: %d expected: 0x%02x but was: 0x%02x ", exp.substring(0, ptr * 2 - 2), exp.substring(ptr * 2 - 2, ptr * 2), exp.substring(ptr * 2, exp.length()), ptr - 1, expected[ptr - 1] & 0xFF, b & 0xFF));
            }
            ptr++;
            if (expected.length == ptr) {
                getLog().info(String.format("Written to OutputStream: %s", Decoder.bytes2Ascii(expected)));
                lastByteWriting();
            }
        }
    }
    List<Data> data = new ArrayList<Data>();
    MBusTestInputStream is = new MBusTestInputStream();
    MBusTestOutputStream os = new MBusTestOutputStream();

    public void close() throws IOException {
        os.close();
        is.close();
    }

    protected abstract void lastByteReading(long endWaitTime) throws IOException;

    protected abstract void lastByteWriting() throws IOException;

    public InputStream getInputStream() {
        return is;
    }

    protected abstract Logger getLog();

    public OutputStream getOutputStream() {
        return os;
    }

    public boolean isOK() {
        return data.isEmpty() && is.isOK() && os.isOK();
    }

    public void replay() {
        setNextData(false);
    }

    protected abstract void setNextData(boolean removeFirst);

}
