/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.master.ui;

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
        hostNameTextField = new JTextField(getConnection().getHostname());
        hostNameTextField.setColumns(15);
        hostNameTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                getConnection().setHostname(hostNameTextField.getText());
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
