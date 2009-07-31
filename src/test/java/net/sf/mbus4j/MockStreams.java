/*
 * mbus4j - Open source drivers for mbus protocol (www.mbus.com) - http://mbus4j.sourceforge.net/
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
package net.sf.mbus4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.sf.mbus4j.decoder.PacketParser;

import org.slf4j.Logger;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
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
                    // last byte readed
                    try {
                        Thread.sleep(endWaitTime);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    getLog().info(String.format("Readed from InputStream: %s", PacketParser.bytes2Ascii(buffer)));
                    this.locked = true;
                    dataReaded();
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
            buffer = string != null ? PacketParser.ascii2Bytes(string) : new byte[0];
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
            expected = data != null ? PacketParser.ascii2Bytes(data) : new byte[0];
        }

        @Override
        public synchronized void write(int b) throws IOException {
            getLog().trace(String.format("Write to OutputStream: 0x%02X", b & 0xFF));
            if (ptr > expected.length - 1) {
                ptr++;
                Assert.fail(String.format("%s at outside pos: %d was: 0x%02x ", PacketParser.bytes2Ascii(expected), ptr - 1, b & 0xFF));
            }
            if ((b & 0xFF) != (expected[ptr] & 0xFF)) {
                ptr++;
                String exp = PacketParser.bytes2Ascii(expected);
                Assert.fail(String.format("%s[%s]%s at pos: %d expected: 0x%02x but was: 0x%02x ", exp.substring(0, ptr * 2 - 2), exp.substring(ptr * 2 - 2, ptr * 2), exp.substring(ptr * 2, exp.length()), ptr - 1, expected[ptr - 1] & 0xFF, b & 0xFF));
            }
            ptr++;
            if (expected.length == ptr) {
                getLog().info(String.format("Written to OutputStream: %s", PacketParser.bytes2Ascii(expected)));
                dataWritten();
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

    protected abstract void dataReaded();

    protected abstract void dataWritten();

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
