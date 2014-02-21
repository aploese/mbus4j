package net.sf.mbus4j.master.console;

/*
 * #%L
 * mbus4j-master-ui
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

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.mbus4j.MBusUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import net.sf.mbus4j.TcpIpConnection;
import net.sf.mbus4j.SerialPortConnection;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.json.JsonSerializeType;
import net.sf.mbus4j.log.LogUtils;
import net.sf.mbus4j.master.MBusMaster;

/**
 * DOCUMENT ME!
 *
 * @author aploese
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

        opt = new Option(null, "id", true, "<device id BCD encoded integer with 8 digits Wildcart is \"F\" (for selecting device)");
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

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            for (String arg : args) {
                if ("-h".equals(arg) || "--help".equals(arg)) {
                    printHelp(options);
                    return;
                }
            }
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            printHelp(options);
            LOG.log(Level.SEVERE, "Exception during parsing", ex);
            return;
        }

        if (cmd.hasOption("help")) {
            printHelp(options);

            return;
        }

        MBusMaster master = new MBusMaster();

        int bitPerSecond = SerialPortConnection.DEFAULT_BAUDRATE;

        int responseTimeoutOffset = -1;

        if (cmd.hasOption("timeout")) {
            responseTimeoutOffset = Integer.parseInt(cmd.getOptionValue("timeout"));
        }

        if (cmd.hasOption("tcpip")) {
            final String tcpAddr = cmd.getOptionValue("tcpip").split(":")[0];
            final int tcpPort = Integer.parseInt(cmd.getOptionValue("tcpip").split(":")[1]);
            if (responseTimeoutOffset == -1) {
                responseTimeoutOffset = TcpIpConnection.DEFAULT_RESPONSE_TIMEOUT_OFFSET;
            }
            TcpIpConnection conn = new TcpIpConnection(tcpAddr, tcpPort, bitPerSecond, responseTimeoutOffset);
            master.setConnection(conn);
        } else if (cmd.hasOption("port")) {
            if (responseTimeoutOffset == -1) {
                responseTimeoutOffset = SerialPortConnection.DEFAULT_RESPONSE_TIMEOUT_OFFSET;
            }
            SerialPortConnection conn = new SerialPortConnection(cmd.getOptionValue("port"), bitPerSecond, responseTimeoutOffset);
            master.setConnection(conn);
        }

        try (Closeable c = master.open()) {
            if (cmd.hasOption("search")) {
                if (cmd.hasOption("paddr")) {
                    master.searchDevicesByPrimaryAddress();
                } else if (cmd.hasOption("saddr")) {
                    int bcdId = cmd.hasOption("id") ? (int) MBusUtils.String2Bcd(cmd.getOptionValue("id")) : 0xFFFFFFFF;
                    byte version = (byte) Short.parseShort(cmd.getOptionValue("version", "FF"), 16);
                    byte medium = cmd.hasOption("medium") ? (byte) MBusMedium.fromLabel(cmd.getOptionValue("medium")).getId() : (byte) 0xFF;
                    short manufacturer = cmd.hasOption("manufacturer") ? MBusUtils.man2Short(cmd.getOptionValue("manufacturer")) : (short) 0xFFFF;
//                    System.out.println("FOUND SLAVES: " + master.sendSlaveSelect(bcdId, manufacturer, version, medium, 1));
                    master.widcardSearch(bcdId, manufacturer, version, medium, 3);
                    Thread.sleep(1000 * 5); // 20 sec
                }
                if (cmd.hasOption("json")) {
                    System.out.print(master.toJSON(JsonSerializeType.ALL).toString(2));
                } else {
                    System.out.print(master.toString());
                }
            } else if (cmd.hasOption("read")) {
                UserDataResponse udr = null;
                if (cmd.hasOption("paddr")) {
                    final byte slaveAddress = (byte) Short.parseShort(cmd.getOptionValue("address"));
                    //    Frame f = master.sendInitSlave(slaveAddress);
                    udr = master.readResponse(slaveAddress);
                } else if (cmd.hasOption("saddr")) {
                    int bcdId = (int) MBusUtils.String2Bcd(cmd.getOptionValue("id"));
                    Byte version = cmd.hasOption("version") ? (byte) Short.parseShort(cmd.getOptionValue("version")) : null;
                    MBusMedium medium = cmd.hasOption("medium") ? MBusMedium.fromLabel(cmd.getOptionValue("medium")) : null;
                    String manufacturer = cmd.hasOption("manufacturer") ? cmd.getOptionValue("manufacturer") : null;
                    udr = master.readResponseBySecondary(bcdId, manufacturer, version, medium, bcdId);
                }
                if (cmd.hasOption("json")) {
                    System.out.print(udr.toJSON(JsonSerializeType.ALL).toString(2));
                } else {
                    System.out.print(udr.toString());
                }
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error", e);
        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("master-console [Options]", opts);
    }
}
