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

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 *
 * Problem 1. Schreiben als Masterstream, wenn nix ist ??? Ex ? Assert geht
 * nicht 2. Masterstream wenn letztes Byte gelesen, dann lesen oder timeout
 * triggern und nicht das lesen des letzten Bytes verzögern, das bringt nix
 * ....
 *
 */
public class MasterStreams extends MockConnection {

    private int handledData;
    private boolean dataReaded;

    public MasterStreams(int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
    }

    @Override
    protected synchronized void lastByteReading(final long endWaitTime) throws IOException {
        if (endWaitTime == 0) {
            dataReaded = true;
            if (data.get(0).response == null) {
                setNextData(true);
            }
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(endWaitTime);
                    } catch (InterruptedException ex) {
                    }
                    dataReaded = true;
                    if (data.get(0).response == null) {
                        setNextData(true);
                    }
                }
            }).start();
        }
    }

    @Override
    protected synchronized void lastByteWriting() throws IOException {
        if (!dataReaded) {
            log.severe("Data not readed!");
            throw new IOException("Data not readed!");
        }
        setNextData(true);
    }

    /**
     * Send a request and wait for response
     *
     * @param request
     * @param response
     * @return
     */
    public void sendRequestAndCollectResponse(String request, String response) {
        this.data.add(new Data(request, response));
    }

    public void sendRequestNoAnswer(String request, int waittime, int times) {
        for (int i = 0; i < times; i++) {
            this.data.add(new Data(request, waittime));
        }
    }

    public void sendRequestNoAnswer(String request, int waittime) {
        this.data.add(new Data(request, waittime));
    }

    @Override
    protected synchronized void setNextData(boolean removeFirst) {
        dataReaded = false;
        if (removeFirst) {
            data.remove(0);
        }
        if (data.isEmpty()) {
            notifyAll();
            return;
        }
        getMockOs().setData(data.get(0));
        getMockIs().setData(data.get(0));
        log.info(String.format("Setup data record: %04d data records left: %04d", handledData, data.size() - 1));
        handledData++;
        getMockIs().releaseReadLock();
    }

    @Override
    public String getJsonFieldName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

}
