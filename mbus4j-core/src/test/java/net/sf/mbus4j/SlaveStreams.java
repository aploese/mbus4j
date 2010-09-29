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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SlaveStreams extends MockConnection {

    private final static Logger log = LoggerFactory.getLogger(SlaveStreams.class);

    public SlaveStreams() {
        super(115200 / 4, 0);
    }

    @Override
    protected synchronized void lastByteReading(final long endWaitTime) {
        if (endWaitTime == 0) {
            setNextData(true);
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(endWaitTime);
                    } catch (InterruptedException ex) {
                    }
                    setNextData(true);
                }
            }).start();
        }
    }

    @Override
    protected synchronized void lastByteWriting() {
        getMockIs().releaseReadLock();
        if (data.get(0).response == null) {
            setNextData(true);
        }
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    public void respondToRequest(String request, int times) {
        for (int i = 0; i < times; i++) {
            this.data.add(new Data(request, 0));
        }
    }

    /**
     * Wait for a request and respond to it
     * @param request
     * @param response
     * @return
     */
    public SlaveStreams respondToRequest(String request, String response) {
        this.data.add(new Data(request, response));
        return this;
    }

    @Override
    protected synchronized void setNextData(boolean removeFirst) {
        if (removeFirst) {
            data.remove(0);
        }
        if (data.isEmpty()) {
            synchronized (this) {
                notifyAll();
            }
            return;
        }
        getMockOs().setData(data.get(0).request);
        getMockIs().setData(data.get(0).response, data.get(0).noResponseWaitTime);
    }

}
