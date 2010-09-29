/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
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
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author aploese
 */
public class SerialPortConnection extends Connection {

    public static final int DEFAULT_BAUDRATE = 2400;
    public static final int DEFAULT_RESPONSE_TIMEOUT_OFFSET = 50;
    private SerialPort sPort;
    private String portName;

    public SerialPortConnection() {
        super(SerialPortConnection.DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
    }

    public SerialPortConnection(String portName) {
       super(DEFAULT_BAUDRATE, DEFAULT_RESPONSE_TIMEOUT_OFFSET);
       this.portName = portName;
    }

    public SerialPortConnection(String portName, int bitPerSecond, int responseTimeoutOffset) {
        super(bitPerSecond, responseTimeoutOffset);
        this.portName = portName;
    }

    @Override
    public void  open() throws IOException {
        try {
            setConnState(State.OPENING);
            // Obtain a CommPortIdentifier object for the port you want to open.
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
            // Open the port represented by the CommPortIdentifier object. Give
            // the open call a relatively long timeout of 30 seconds to allow
            // a different application to reliquish the port if the user
            // wants to.
            sPort = (SerialPort) portId.open(SerialPortConnection.class.getName(),
                30000);
            sPort.setSerialPortParams(getBitPerSecond(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
            sPort.enableReceiveTimeout(1000);
            sPort.setInputBufferSize(512);
            sPort.setOutputBufferSize(512);
            sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            // load here to sppeddup furher access!!!
            is = sPort.getInputStream();
            os = sPort.getOutputStream();
            setConnState(State.OPEN);
        } catch (UnsupportedCommOperationException ex) {
            throw new RuntimeException(ex);
        } catch (PortInUseException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchPortException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        setConnState(State.CLOSING);
        sPort.close();
        setConnState(State.CLOSED);
    }
        @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = super.toJSON(jsonSerializeType);
        result.accumulate("serialPort", portName);
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        portName = json.getString("serialPort");
    }

    /**
     * @return the portName
     */
    public String getPortName() {
        return portName;
    }

    /**
     * @param portName the portName to set
     */
    public void setPortName(String portName) {
        this.portName = portName;
    }


}
