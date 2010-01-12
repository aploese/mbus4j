/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;

/**
 *
 * @author aploese
 */
public class SerialPortTools {

    public static SerialPort openPort(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        return openPort(portName, 2400);
    }

    public static SerialPort openPort(String portName, int baudrate) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        // Obtain a CommPortIdentifier object for the port you want to open.
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        SerialPort sPort = (SerialPort) portId.open(SerialPortTools.class.getName(), 30000);
        sPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
        sPort.enableReceiveTimeout(1000);
        sPort.setInputBufferSize(512);
        sPort.setOutputBufferSize(512);
        sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        return sPort;
    }

}
