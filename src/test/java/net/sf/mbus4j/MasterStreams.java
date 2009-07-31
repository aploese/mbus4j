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
 * @version $Id$
 */
public class MasterStreams extends MockStreams {

    private static final Logger log = LoggerFactory.getLogger(MasterStreams.class);

    @Override
    protected synchronized void dataReaded() {
        if (data.get(0).response == null) {
            setNextData(true);
        }
    }

    @Override
    protected synchronized void dataWritten() {
        setNextData(true);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    public MockStreams sendRequestAndCollectResponse(String request, long noResponseWaitTime) {
        this.data.add(new Data(request, noResponseWaitTime));
        return this;
    }

    /**
     * Send a request and wait for response
     * @param request
     * @param response
     * @return
     */
    public MockStreams sendRequestAndCollectResponse(String request, String response) {
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
        os.setData(data.get(0).response);
        is.setData(data.get(0).request, data.get(0).noResponseWaitTime);
        is.releaseReadLock();
    }
}
