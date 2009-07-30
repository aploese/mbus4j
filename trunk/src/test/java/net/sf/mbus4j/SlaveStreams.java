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
 * Slave kennt keinen Timeout, er antwortet einfach nicht
 */
public class SlaveStreams extends MockStreams {
    
    private final static Logger log = LoggerFactory.getLogger(SlaveStreams.class);

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
    protected synchronized void dataWritten() {
        is.releaseReadLock();
        if (data.get(0).response == null) {
            setNextData(true);
        }
    }

    @Override
    protected  synchronized void dataReaded() {
        setNextData(true);
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

    @Override
    protected Logger getLog() {
        return log;
    }
}
