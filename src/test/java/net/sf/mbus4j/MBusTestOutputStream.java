/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

import java.io.IOException;
import java.io.OutputStream;
import org.junit.Assert;
import net.sf.mbus4j.decoder.PacketParser;

/**
 *
 * @author aploese
 */
public class MBusTestOutputStream extends OutputStream {

    private byte[] buffer = new byte[255];
    private int ptr;
    private byte[] expected;


    @Override
    public void write(int b) throws IOException {
        buffer[ptr++] = (byte)b;
        if (isDataEqual()) {
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    public String getBytes() {
        StringBuilder sb = new StringBuilder(ptr + 1);
        for (int i = 0; i < ptr; i++ ) {
            sb.append(String.format("%02X", buffer[i]));
        }
        return sb.toString();
    }

    public void waitFor(String data, int timeout) throws InterruptedException {
        expected = PacketParser.ascii2Bytes(data);
        synchronized (this) {
            wait(timeout);
        }
        Assert.assertEquals(data, getBytes());
    }

    private boolean isDataEqual() {
        if (expected == null) {
            return false;
        }

        if (ptr == expected.length ) {
            for (int i = 0; i < expected.length; i++) {
                if (buffer[i] != expected[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
    }
    }

    public void clear() {
        ptr = 0;
        expected = null;
    }
}
