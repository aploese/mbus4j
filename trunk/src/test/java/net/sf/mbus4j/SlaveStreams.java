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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
public class SlaveStreams extends MockStreams {

    private final static Logger log = LoggerFactory.getLogger(SlaveStreams.class);

    @Override
    protected synchronized void dataReaded() {
        setNextData(true);
    }

    @Override
    protected synchronized void dataWritten() {
        is.releaseReadLock();
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
        os.setData(data.get(0).request);
        is.setData(data.get(0).response, data.get(0).noResponseWaitTime);
    }
}
