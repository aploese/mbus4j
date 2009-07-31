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
package net.sf.mbus4j.slave;

import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.decoder.PacketParser;
import net.sf.mbus4j.encoder.Encoder;
import net.sf.mbus4j.master.Master;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slaves {

    public static class LogInit {

        public static final String TRACE = "trace";
        public static final String DEBUG = "debug";
        public static final String INFO = "info";
        public static final String WARN = "warn";
        public static final String ERROR = "error";
        public static final String FATAL = "fatal";

        public static synchronized void initLog(String level) {
            Properties props = new Properties();
            props.setProperty("log4j.appender.stdout",
                    "org.apache.log4j.ConsoleAppender");
            props.setProperty("log4j.appender.stdout.Target", "System.out");
            //log4j.appender.stdout=org.apache.log4j.FileAppender
            //log4j.appender.stdout.File=Easy.log
            props.setProperty("log4j.appender.stdout.layout",
                    "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.appender.stdout.layout.ConversionPattern",
                    "%d{ABSOLUTE} %5p %c{1}: %m%n");

            //set log levels - for more verbose logging change 'info' to 'debug' ###
            props.setProperty("log4j.rootLogger", level + ", stdout");
            PropertyConfigurator.configure(props);
        }
    }

    private class RequestHandler implements Callable<Frame> {

        private final Frame request;
        private final Slave slave;

        RequestHandler(Frame request, Slave slave) {
            this.request = request;
            this.slave = slave;
        }

        @Override
        public Frame call() throws Exception {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("req dispatching: " + request);
                }
                Frame result;
                switch (request.getControlCode()) {
                    case CON_ACK:
                        result = null;
                        break;
                    case REQ_UD1:
                        result = slave.handleReqUd1((RequestClassXData) request);
                        break;
                    case REQ_UD2:
                        result = slave.handleReqUd2((RequestClassXData) request);
                        break;
                    case RSP_UD:
                        if (request instanceof SendUserData) {
                            result = slave.handleSendUserData((SendUserData) request);
                        } else if (request instanceof ApplicationReset) {
                            result = slave.handleApplicationReset((ApplicationReset) request);
                        } else {
                            result = null;
                        }
                        break;
                    case SND_NKE:
                        if (request instanceof SendInitSlave) {
                            result = slave.handleSendInitSlave((SendInitSlave) request);
                        } else {
                            result = null;
                        }
                        break;
                    case SND_UD:
                        if (request instanceof SendUserData) {
                            result = slave.handleSendUserData((SendUserData) request);
                        } else if (request instanceof ApplicationReset) {
                            result = slave.handleApplicationReset((ApplicationReset) request);
                        } else if (request instanceof SendUserDataManSpec) {
                            result = slave.handleSendUserDataManSpec((SendUserDataManSpec) request);
                        } else {
                            result = null;
                        }
                        break;
                    default:
                        result = null;

                }
                if (result != null) {
                    send(result);
                    if (log.isDebugEnabled()) {
                        log.debug("req dispatched: " + result);
                    }
                } else {
                    log.debug("req dispatched no result");
                }

                return result;

            } catch (Exception ex) {
                log.error("Call", ex);
                throw ex;
            }
        }
    }

    private class StreamListener implements Runnable {

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
//    List<Future<Frame>> l = new ArrayList<Future<Frame>>();

        @Override
        public void run() {
            try {
                int theData;
                PacketParser parser = new PacketParser();
                log.info("Wait for data to process");
                try {
                    while (!closed) {
                        if ((theData = is.read()) == -1) {
                            if (log.isTraceEnabled()) {
                                log.trace("Thread interrupted or eof on waiting occured");
                            }
                        } else {
                            if (log.isTraceEnabled()) {
                                log.trace(String.format("Data received 0x%02x", theData));
                            }
                            try {
                                Frame frame = parser.addByte((byte) theData);
                                if (frame != null) {
                                    if (log.isDebugEnabled()) {
                                        log.info("Frame parsed ... will process: " + frame);
                                    } else {
                                        log.info("Frame parsed ... will process");
                                    }
                                    for (Slave slave : slaves) {
                                        if (slave.willHandleRequest(frame)) {
                                            log.debug(String.format("Frame will be handled by slave 0x%02x", slave.getAddress()));
                                            tpe.submit(new RequestHandler(frame, slave));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("Error during createPackage()", e);
                            }
                        }
                    }
                    log.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    log.error("run()", e);
                } catch (Exception e) {
                    log.info("finished waiting for packages", e);

                }
            } finally {
                tpe.shutdownNow();
            }
        }
    }
    private static Logger log;

    public static void main(String[] args) throws Exception {
        LogInit.initLog(LogInit.DEBUG);
        Slaves app = new Slaves();
        SerialPort sPort = null;
        int timeout = 0;
        ScriptEngineManager scriptManager = new ScriptEngineManager();
        ScriptEngine js = scriptManager.getEngineByExtension("js");

        Bindings bindings = js.createBindings();
        bindings.put("app", app);
        InputStream is = Slaves.class.getResourceAsStream("/acw-test-slaves.js");
        Reader in = new InputStreamReader(is);
        Boolean result = (Boolean) js.eval(in, bindings);

        sPort = Master.openPort(args[0]);
        app.setStreams(sPort.getInputStream(), sPort.getOutputStream());
        if (args.length > 1) {
            timeout = Integer.parseInt(args[1]);
        }
        try {
            if (timeout > 0) {
                Thread.sleep(timeout);
            } else {
                System.in.read();
            }
        } catch (InterruptedException ex) {
            System.err.print("Error sleep " + ex);
        }
        try {
            app.close();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        }
        sPort.close();
    }
    private InputStream is;
    private OutputStream os;
    private boolean closed = true;
    private List<Slave> slaves = new ArrayList<Slave>();
    private Encoder encoder = new Encoder();
    private Thread t;
    private StreamListener streamListener = new StreamListener();

    public Slaves() {
        super();
        log = LoggerFactory.getLogger(Slaves.class);
    }

    public boolean addSlave(Slave slave) {
        return slaves.add(slave);
    }

    public void close() throws InterruptedException {
        if (closed) {
            return;
        }
        closed = true;
        Thread.sleep(100); //TODO wait?
        t.interrupt();
        t = null;
    }

    public void releaseStreams() throws InterruptedException {
        close();
        is = null;
        os = null;
    }

    private void send(Frame frame) throws IOException {
        os.write(encoder.encode(frame));
        os.flush();
    }

    public void setStreams(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        start();
    }

    public void start() {
        closed = false;
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
    }
}
