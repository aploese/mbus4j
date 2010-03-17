/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.slaves;

import gnu.io.SerialPort;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.SerialPortTools;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.encoder.Encoder;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slaves implements JSONSerializable {

    public static Slaves readJsonStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        Slaves result = new Slaves();
        result.fromJSON(JSONObject.fromObject(sb.toString()));
        return result;
    }

    /**
     * @return the serialPortName
     */
    public String getSerialPortName() {
        return serialPortName;
    }

    /**
     * @param serialPort the serialPortName to set
     */
    public void setSerialPortName(String serialPortName) {
        this.serialPortName = serialPortName;
    }

    /**
     * @return the baudrate
     */
    public int getBaudrate() {
        return baudrate;
    }

    /**
     * @param baudrate the baudrate to set
     */
    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        if (serialPortName != null) {
            JSONObject jsonSerialPort = new JSONObject();
            jsonSerialPort.accumulate("portname", serialPortName);
            jsonSerialPort.accumulate("baudrate", baudrate);
            result.accumulate("serialPort", jsonSerialPort);
        }
        JSONArray jsonSlaves = new JSONArray();
        for (Slave s : slaves) {
            jsonSlaves.add(s.toJSON(jsonSerializeType));
        }
        result.accumulate("slaves", jsonSlaves);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (json.containsKey("serialPort")) {
            JSONObject jsonSerialPort = json.getJSONObject("serialPort");
            serialPortName = jsonSerialPort.getString("portname");
            baudrate = jsonSerialPort.getInt("baudrate");
        }
        JSONArray jsonSlaves = json.getJSONArray("slaves");
        for (int i = 0; i < jsonSlaves.size(); i++) {
            Slave s = new Slave();
            s.fromJSON(jsonSlaves.getJSONObject(i));
            addSlave(s);
        }
    }

    public int getSalvesSize() {
        return slaves.size();
    }

    public Slave getSlave(int index) {
        return slaves.get(index);
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
                if (LOG.isTraceEnabled()) {
                    LOG.trace("req dispatching: " + request);
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
                        } else if (request instanceof SelectionOfSlaves) {
                            result = slave.handleSelectionOfSlaves((SelectionOfSlaves) request);
                        } else {
                            result = null;
                        }
                        break;
                    default:
                        result = null;

                }
                if (result != null) {
                    send(result);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("req dispatched: " + result);
                    }
                } else {
                    LOG.debug("req dispatched no result");
                }

                return result;

            } catch (Exception ex) {
                LOG.error("Call", ex);
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
                Decoder parser = new Decoder();
                LOG.info("Wait for data to process");
                try {
                    while (!closed) {
                        if ((theData = is.read()) == -1) {
                            if (LOG.isTraceEnabled()) {
                                LOG.trace("Thread interrupted or EOF on waiting occured");
                            }
                        } else {
                            if (LOG.isTraceEnabled()) {
                                LOG.trace(String.format("Data received 0x%02x", theData));
                            }
                            try {
                                Frame frame = parser.addByte((byte) theData);
                                if (frame != null) {
                                    if (LOG.isTraceEnabled()) {
                                        LOG.trace("Frame parsed ... will process: " + frame);
                                    } else {
                                        LOG.debug("Frame parsed ... will process");
                                    }
                                    for (Slave slave : slaves) {
                                        if (slave.willHandleRequest(frame)) {
                                            LOG.debug(String.format("Frame will be handled by slave %s", slave.slaveIdToString()));
                                            tpe.submit(new RequestHandler(frame, slave));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                LOG.error("Error during createPackage()", e);
                            }
                        }
                    }
                    LOG.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    LOG.error("run()", e);
                } catch (Exception e) {
                    LOG.info("finished waiting for packages", e);

                }
            } finally {
                tpe.shutdownNow();
            }
        }
    }
    private static Logger LOG = LoggerFactory.getLogger(Slaves.class);


    public static void main(String[] args) throws Exception {
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

        sPort = SerialPortTools.openPort(args[0]);
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

    private String serialPortName;
    private int baudrate = SerialPortTools.DEFAULT_BAUDRATE;;
    private InputStream is;
    private OutputStream os;
    private boolean closed = true;
    private List<Slave> slaves = new ArrayList<Slave>();
    private Encoder encoder = new Encoder();
    private Thread t;
    private StreamListener streamListener = new StreamListener();

    public Slaves() {
        super();
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
