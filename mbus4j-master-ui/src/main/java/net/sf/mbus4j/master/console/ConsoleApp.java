/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package net.sf.mbus4j.master.console;

import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.api.TimeoutIOException;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import de.ibapl.spsw.logging.TimeStampLogging;
import de.ibapl.spsw.ser2net.Ser2NetProvider;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.mbus4j.MBusUtils;
import net.sf.mbus4j.SerialPortConnection;
import net.sf.mbus4j.TcpIpConnection;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.SendUserDataFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.ReadOutDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.json.JsonSerializeType;
import net.sf.mbus4j.log.LogUtils;
import net.sf.mbus4j.master.MBusMaster;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * DOCUMENT ME!
 *
 * @author Arne Plöse
 */
public class ConsoleApp {

    private static Logger LOG = LogUtils.getMasterLogger();

    /**
     * Creates a new Main object.
     */
    public ConsoleApp() {
        super();

    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option opt;
        OptionGroup optg;

        opt = new Option("h", "help", false, "print this help message");
        options.addOption(opt);

        optg = new OptionGroup();
        optg.setRequired(true);

        opt = new Option("p", "port", true, "serial port to use (Serial Mode)");
        opt.setArgName("serial port");
        opt.setType(String.class);
        optg.addOption(opt);

        opt = new Option("i", "tcpip", true, "TCP-IP Hostname:Port or IP-Address:Port to use (IP Mode)");
        opt.setArgName("TCP/IP address");
        opt.setType(String.class);
        optg.addOption(opt);

        options.addOptionGroup(optg);

        opt = new Option("t", "timeout", true, "Response timeout offset in ms");
        opt.setArgName("offset");
        opt.setType(Integer.class);
        options.addOption(opt);

        optg = new OptionGroup();
        optg.setRequired(true);

        opt = new Option("s", "search", false, "search for m-bus devices");
        optg.addOption(opt);

        opt = new Option("u", "read", false, "read User Data response drom device");
        optg.addOption(opt);

        options.addOptionGroup(optg);

        opt = new Option("j", "json", false, "output to stdout as JSON");
        options.addOption(opt);

        optg = new OptionGroup();
        optg.setRequired(true);

        opt = new Option("r", "paddr", false, "use primary adressing");
        optg.addOption(opt);

        opt = new Option("e", "saddr", false, "use secondary Addressing");
        optg.addOption(opt);

        options.addOptionGroup(optg);

        opt = new Option(null, "manufacturer", true, "device manufacturer (for selecting device)");
        opt.setArgName("man");
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(null, "version", true, "device version hex (for selecting device)");
        opt.setArgName("ver");
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(null, "id", true,
                "<device id BCD encoded integer with 8 digits Wildcart is \"F\" (for selecting device)");
        opt.setArgName("id");
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(null, "medium", true, "device medium (for selecting device)");
        opt.setArgName("medium");
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option(null, "address", true, "device primary address (hex)");
        opt.setArgName("primary address");
        opt.setType(String.class);
        options.addOption(opt);

        opt = new Option("l", "logFile", true, "logFile");
        opt.setArgName("log file of serial data");
        opt.setType(String.class);
        options.addOption(opt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);
            LOG.log(Level.SEVERE, "Exception during parsing", ex);
            return;
        }

        if (cmd.hasOption("help") || args.length == 0) {
            printHelp(options);

            return;
        }

        MBusMaster master = new MBusMaster();

        int responseTimeoutOffset = -1;

        if (cmd.hasOption("timeout")) {
            responseTimeoutOffset = Integer.parseInt(cmd.getOptionValue("timeout"));
        }

        SerialPortSocket serialPortSocket = null;

        if (cmd.hasOption("tcpip")) {
            final String tcpAddr = cmd.getOptionValue("tcpip").split(":")[0];
            final int tcpPort = Integer.parseInt(cmd.getOptionValue("tcpip").split(":")[1]);
            if (responseTimeoutOffset == -1) {
                master.setResponseTimeoutOffset(TcpIpConnection.DEFAULT_RESPONSE_TIMEOUT_OFFSET);
            }
            serialPortSocket = new Ser2NetProvider(tcpAddr, tcpPort);
        } else if (cmd.hasOption("port")) {
            if (responseTimeoutOffset == -1) {
                master.setResponseTimeoutOffset(SerialPortConnection.DEFAULT_RESPONSE_TIMEOUT_OFFSET);
            }
            ServiceLoader<SerialPortSocketFactory> loader = ServiceLoader.load(SerialPortSocketFactory.class);
            Iterator<SerialPortSocketFactory> iterator = loader.iterator();
            if (!iterator.hasNext()) {
                LOG.severe("NO implementation of SerialPortSocketFactory available - add a provider for that to the test dependencies");
            }
            SerialPortSocketFactory serialPortSocketFactory = iterator.next();
            if (iterator.hasNext()) {
                StringBuilder sb = new StringBuilder("More than one implementation of SerialPortSocketFactory available - fix the test dependencies\n");
                iterator = loader.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().getClass().getCanonicalName()).append("\n");
                }
                LOG.severe(sb.toString());
            }

            serialPortSocket = serialPortSocketFactory.createSerialPortSocket(cmd.getOptionValue("port"));
        }

        if (cmd.hasOption("logFile")) {
            final FileOutputStream logStream = new FileOutputStream(cmd.getOptionValue("logFile"), false);
            serialPortSocket = LoggingSerialPortSocket.wrapWithHexOutputStream(serialPortSocket, logStream, false,
                    TimeStampLogging.FROM_OPEN);
        }

        master.setSerialPort(serialPortSocket);

        try (Closeable c = master.open()) {
            final LinkedList<DeviceId> deviceIds = new LinkedList<>();
            if (cmd.hasOption("search")) {
                if (cmd.hasOption("paddr")) {
                    master.searchDevicesByPrimaryAddress((devId) -> {
                        System.out.println("Found new device: \n" + devId.toString());
                        deviceIds.add(devId);
                    });
                    for (DeviceId devId : deviceIds) {
                        UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS);
                        System.err.println("NewDevice\n" + udr + "\n\n");
                    }
                } else if (cmd.hasOption("saddr")) {
                    int bcdId = cmd.hasOption("id") ? (int) MBusUtils.String2Bcd(cmd.getOptionValue("id")) : 0xFFFFFFFF;
                    byte version = (byte) Short.parseShort(cmd.getOptionValue("version", "FF"), 16);
                    byte medium = cmd.hasOption("medium")
                            ? (byte) MBusMedium.fromLabel(cmd.getOptionValue("medium")).getId()
                            : (byte) 0xFF;
                    short manufacturer = cmd.hasOption("manufacturer")
                            ? MBusUtils.man2Short(cmd.getOptionValue("manufacturer"))
                            : (short) 0xFFFF;

                    master.widcardSearch(bcdId, manufacturer, version, medium, (devId) -> {
                        System.out.println("Found new device: \n" + devId.toString());
                        deviceIds.add(devId);
                    });
                    for (DeviceId devId : deviceIds) {

                        //	master.sendInitSlave(devId.address);
                        //TODO	master.waitIdleTime();
                        try {
                            ReadOutDataBlock db = new ReadOutDataBlock();
                            db.setDataFieldCode(DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST);
                            master.sendSendUserData(devId.address, SendUserDataFrame.DEFAULT_FCB, db);
                        } catch (TimeoutIOException tio) {
                            LOG.warning("No response to DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST device:" + devId);
                        }

                        UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, devId);
                        System.err.println("Found NewDevice");
                        printUdr(udr, cmd.hasOption("json"));
                    }
                    LOG.info("Reading done - Closing down");
                }
            } else if (cmd.hasOption("read")) {
                UserDataResponse udr = null;
                if (cmd.hasOption("paddr")) {
                    final byte slaveAddress = (byte) Short.parseShort(cmd.getOptionValue("address"));
                    master.sendInitSlave(slaveAddress);
                    master.sendInitSlave(slaveAddress);
                    master.sendInitSlave(slaveAddress);
                    ReadOutDataBlock db = new ReadOutDataBlock();
                    db.setDataFieldCode(DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST);
                    master.sendSendUserData(slaveAddress, SendUserDataFrame.DEFAULT_FCB, db);

                    udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, slaveAddress);
                } else if (cmd.hasOption("saddr")) {
                    if (master.selectDevice(cmd.hasOption("id") ? Integer.valueOf(cmd.getOptionValue("id")) : null,
                            cmd.hasOption("manufacturer") ? cmd.getOptionValue("manufacturer") : null,
                            cmd.hasOption("version") ? (byte) Short.parseShort(cmd.getOptionValue("version")) : null,
                            cmd.hasOption("medium") ? MBusMedium.fromLabel(cmd.getOptionValue("medium")) : null)) {
                        /*
						Thread.sleep(2000);
						master.sendInitSlave(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS);
						Thread.sleep(2000);

						LongDataBlock db = new LongDataBlock();
						db.setDataFieldCode(DataFieldCode._64_BIT_INTEGER);
						db.setVif(VifFD.PARAMETER_SET_IDENTIFICATION);
						db.setValue(0x000000000f180008L);

						master.sendSendUserData(MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS, true, db);
						Thread.sleep(2000);
                         */
                        udr = master.sendRequestUserData(true, true, PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS);
                    } else {
                        throw new RuntimeException("No device found");
                    }
                }
                printUdr(udr, cmd.hasOption("json"));
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error", e);
        }
        LOG.log(Level.INFO, "Finished!");
    }

    private static void printUdr(UserDataResponse udr, boolean printJson) {
        System.err.println("Check for double dbs");
        for (int i = 0; i < udr.getDataBlockCount(); i++) {
            DataBlock db = udr.getDataBlock(i);
            try {
                udr.findDataBlock(db.getDataFieldCode(), db.getParamDescr(), db.getUnitOfMeasurement(),
                        db.getFunctionField(), db.getStorageNumber(), db.getSubUnit(), db.getTariff());
            } catch (Exception e) {
                System.err.println("DB[" + i + "] is double: " + db);
            }
        }
        System.err.println("... done");

        if (printJson) {
            System.out.print(udr.toJSON(JsonSerializeType.ALL).toString(2));
        } else {
            System.out.print(udr.toString());
        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("master-console [Options]", opts);
    }
}
