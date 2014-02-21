package net.sf.mbus4j.master.ui;

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

import java.util.Enumeration;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import net.sf.mbus4j.SerialPortConnection;
import net.sf.mbus4j.master.MBusMaster;

/**
 *
 * @author aploese
 */
public class SerialPortConnectionToolBar extends ConnectionToolBar<SerialPortConnection> {

    private JLabel portNameLabel;
    private JComboBox portComboBox;

    SerialPortConnectionToolBar(MBusMaster master) {
        super(master);
        init();
    }

    @Override
    protected void init() {
        portNameLabel = new JLabel("Port:");
        add(portNameLabel);
        portComboBox = new JComboBox();
        add(portComboBox);
        refreshSerialPorts();
        if (!(getConnection() instanceof SerialPortConnection)) {
            getmBusMaster().setConnection(new SerialPortConnection());
        }
        portComboBox.setSelectedItem(getConnection());
        super.init();
    }

    public void refreshSerialPorts() {
        portComboBox.removeAllItems();
        /*TODO
         Enumeration<CommPortIdentifier> en = CommPortIdentifier.getPortIdentifiers();
         // iterate through the ports.
         while (en.hasMoreElements()) {
         CommPortIdentifier portId = en.nextElement();
         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
         portComboBox.addItem(portId.getName());
         }
         }
         */
        if (portComboBox.getItemCount() > 0) {
            portComboBox.setSelectedIndex(0);
        }
    }
}
