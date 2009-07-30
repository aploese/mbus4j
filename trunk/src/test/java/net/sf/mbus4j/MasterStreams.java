/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 *
 * Timeout heisst read darf darf für diese Zeit nix bringen... Senden, dann  Zeitstempel setzen und testen readlock entfernen
 *
 */
public class MasterStreams extends MockStreams {

    private static final Logger log = LoggerFactory.getLogger(MasterStreams.class);

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

    public MockStreams sendRequestAndCollectResponse(String request, long noResponseWaitTime) {
        this.data.add(new Data(request, noResponseWaitTime));
        return this;
    }

    @Override
    protected synchronized void dataWritten() {
        setNextData(true);
    }

    @Override
    protected synchronized void dataReaded() {
        if (data.get(0).response == null) {
            setNextData(true);
        }
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

    @Override
    protected Logger getLog() {
        return log;
    }
}
