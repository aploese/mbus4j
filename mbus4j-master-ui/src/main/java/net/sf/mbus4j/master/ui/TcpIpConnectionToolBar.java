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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.sf.mbus4j.TcpIpConnection;
import net.sf.mbus4j.master.MBusMaster;

/**
 *
 * @author aploese
 */
class TcpIpConnectionToolBar extends ConnectionToolBar<TcpIpConnection> {

    private JLabel hostnameLabel;
    private JTextField hostNameTextField;
    private JLabel portLabel;
    private JTextField portTextField;

    public TcpIpConnectionToolBar(MBusMaster master) {
        super(master);
        init();
    }

    @Override
    protected void init() {
        hostnameLabel = new JLabel("Hostname:");
        add(hostnameLabel);
        if (!(getConnection() instanceof TcpIpConnection)) {
            getmBusMaster().setConnection(new TcpIpConnection());
        }
        hostNameTextField = new JTextField(getConnection().getHost());
        hostNameTextField.setColumns(15);
        hostNameTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                getConnection().setHost(hostNameTextField.getText());
            }
        });

        add(hostNameTextField);
        portLabel = new JLabel("Port:");
        add(portLabel);
        portTextField = new JTextField(Integer.toString(getConnection().getPort()));
        portTextField.setColumns(5);
        portTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                getConnection().setPort(Integer.parseInt(portTextField.getText()));
            }
        });
        add(portTextField);
        super.init();
    }

}
