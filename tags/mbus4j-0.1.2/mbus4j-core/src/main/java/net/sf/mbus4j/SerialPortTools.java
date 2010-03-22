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

/**
 *
 * @author aploese
 */
public class SerialPortTools
{
    public static final int DEFAULT_BAUDRATE = 2400;

    public static SerialPort openPort( String portName )
                               throws NoSuchPortException,
                                      PortInUseException,
                                      UnsupportedCommOperationException,
                                      IOException
    {
        return openPort( portName, DEFAULT_BAUDRATE );
    }

    public static SerialPort openPort( String portName, int baudrate )
                               throws NoSuchPortException,
                                      PortInUseException,
                                      UnsupportedCommOperationException,
                                      IOException
    {
        // Obtain a CommPortIdentifier object for the port you want to open.
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier( portName );

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        SerialPort sPort = (SerialPort) portId.open( SerialPortTools.class.getName(  ),
                                                     30000 );
        sPort.setSerialPortParams( baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN );
        sPort.enableReceiveTimeout( 1000 );
        sPort.setInputBufferSize( 512 );
        sPort.setOutputBufferSize( 512 );
        sPort.setFlowControlMode( SerialPort.FLOWCONTROL_NONE );

        return sPort;
    }
}
