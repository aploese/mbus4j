/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

import java.io.IOException;
import java.io.InputStream;
import net.sf.mbus4j.decoder.PacketParser;

/**
 *
 * @author aploese
 */
public class MBusTestInputStream extends InputStream {

    byte[] buffer = new byte[0];
    int readPtr;
    private boolean closed;
    
    @Override
    public int read() throws IOException {
      synchronized (this) {
                    if (closed) {
                        return -1;
                    }
            if (readPtr < buffer.length) {
                return buffer[readPtr++];
            } else {
                try {
                    wait();
                    if (closed) {
                        return -1;
                    }
                    return buffer[readPtr++];
                } catch (InterruptedException ex) {
                    return -1;
                }
            }
            
        }
    }

    public void setBytes(String string) {
        synchronized (this) {
            buffer = PacketParser.ascii2Bytes(string);
            readPtr = 0;
            notifyAll();
        }
    }

    @Override
    public void close() {
        synchronized (this) {
        closed = true;
        notifyAll();
        }
    }

}
