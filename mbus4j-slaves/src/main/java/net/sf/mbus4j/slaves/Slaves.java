package net.sf.mbus4j.slaves;

/*
 * #%L
 * mbus4j-slaves
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
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.provider.SerialPortSocketFactoryImpl;
import java.io.UnsupportedEncodingException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import net.sf.mbus4j.Connection;
import net.sf.mbus4j.SerialPortConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.sf.mbus4j.decoder.DecoderListener;
import net.sf.mbus4j.log.LogUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Slaves implements JSONSerializable {

    public static Slaves readJsonStream(InputStream is, SerialPortSocketFactory serialPortSocketFactory)
            throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        Slaves result = new Slaves(serialPortSocketFactory);
        result.fromJSON(JSONObject.fromObject(sb.toString()));

        return result;
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();

        result.accumulate(conn.getJsonFieldName(), conn.toJSON(jsonSerializeType));

        JSONArray jsonSlaves = new JSONArray();

        for (Slave s : slaves) {
            jsonSlaves.add(s.toJSON(jsonSerializeType));
        }

        result.accumulate("devices", jsonSlaves);

        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        conn = Connection.createFromJSON(json, serialPortSocketFactory);

        JSONArray jsonSlaves = json.getJSONArray("devices");

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

    public void writeJsonStream(OutputStream os) throws UnsupportedEncodingException, IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        JSONObject json = toJSON(JsonSerializeType.SLAVE_CONFIG);
        String text = json.toString(1);
        osw.write(text, 0, text.length());
        osw.flush();
        osw.close();
    }

    public Connection getConnection() {
        return conn;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    private class RequestHandler
            implements Callable<Frame> {

        private final Frame request;
        private final Slave slave;

        RequestHandler(Frame request, Slave slave) {
            this.request = request;
            this.slave = slave;
        }

        @Override
        public Frame call()
                throws Exception {
            try {
                LOG.log(Level.FINEST, "req dispatching: {0}", request);

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

                    LOG.log(Level.FINE, "req dispatched: ", result);
                } else {
                    LOG.fine("req dispatched no result");
                }

                return result;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Call", ex);
                throw ex;
            }
        }
    }

    private class StreamListener
            implements Runnable, DecoderListener {

        ThreadPoolExecutor tpe
                = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

//    List<Future<Frame>> l = new ArrayList<Future<Frame>>();
        @Override
        public void run() {
            try {
                int theData;
                Decoder parser = new Decoder(this);
                LOG.info("Wait for data to process");

                try {
                    while (!isClosed()) {
                        if ((theData = conn.getInputStream().read()) == -1) {
                            LOG.finest("Thread interrupted or EOF on waiting occured");
                        } else {
                            if (LOG.isLoggable(Level.FINEST)) {
                                LOG.finest(String.format("Data received 0x%02x", theData));
                            }

                            try {
                                parser.addByte((byte) theData);
                            } catch (Exception e) {
                                LOG.log(Level.SEVERE, "Error during createPackage()", e);
                            }
                        }
                    }

                    LOG.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "run()", e);
                } catch (Exception e) {
                    LOG.log(Level.INFO, "finished waiting for packages", e);
                }
            } finally {
                tpe.shutdownNow();
            }
        }

        private boolean isClosed() {
            return conn == null ? true : conn.getConnState().equals(Connection.State.CLOSED) || conn.getConnState().equals(Connection.State.CLOSING);
        }

        @Override
        public void success(Frame frame) {
            if (frame != null) {
                if (LOG.isLoggable(Level.FINEST)) {
                    LOG.log(Level.FINEST, "Frame parsed ... will process: ", frame);
                } else {
                    LOG.fine("Frame parsed ... will process");
                }

                for (Slave slave : slaves) {
                    if (slave.willHandleRequest(frame)) {
                        LOG.log(Level.FINE, "Frame will be handled by slave {}", slave.slaveIdToString());
                        tpe.submit(new RequestHandler(frame, slave));
                    }
                }
            }
        }

    }
    private static Logger LOG = LogUtils.getSlaveLogger();

    public static void main(String[] args)
            throws Exception {
        Slaves app = new Slaves(SerialPortSocketFactoryImpl.singleton());
        int timeout = 0;
        ScriptEngineManager scriptManager = new ScriptEngineManager();
        ScriptEngine js = scriptManager.getEngineByExtension("js");

        Bindings bindings = js.createBindings();
        bindings.put("app", app);

        InputStream is = Slaves.class.getResourceAsStream("/acw-test-slaves.js");
        Reader in = new InputStreamReader(is);
        Boolean result = (Boolean) js.eval(in, bindings);

        SerialPortConnection sc = new SerialPortConnection(SerialPortSocketFactoryImpl.singleton(), args[0]);
        app.setConnection(sc);
        app.open();
        try {
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
        } finally {
            try {
                app.close();
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "main", ex);
            }
        }
    }
    private Connection conn;
    private List<Slave> slaves = new ArrayList<Slave>();
    private Encoder encoder = new Encoder();
    private Thread t;
    private StreamListener streamListener = new StreamListener();
    private final SerialPortSocketFactory serialPortSocketFactory;
    
    public Slaves(SerialPortSocketFactory serialPortSocketFactory) {
        super();
        this.serialPortSocketFactory = serialPortSocketFactory;
    }

    public boolean addSlave(Slave slave) {
        return slaves.add(slave);
    }

    public boolean removeSlave(Slave slave) {
        return slaves.remove(slave);
    }

    public void close() throws InterruptedException, IOException {
        if (conn != null) {
            conn.close();
            Thread.sleep(100);
            t.interrupt();
        }
    }

    private void send(Frame frame)
            throws IOException {
        conn.getOutputStrteam().write(encoder.encode(frame));
        conn.getOutputStrteam().flush();
    }

    public void open() throws IOException {
        conn.open();
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
    }

    public int slaveIndexOf(Slave s) {
        return slaves.indexOf(s);
    }
}
