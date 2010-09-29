/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.master.ui;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
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
        Enumeration<CommPortIdentifier> en = CommPortIdentifier.getPortIdentifiers();
        // iterate through the ports.
        while (en.hasMoreElements()) {
            CommPortIdentifier portId = en.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portComboBox.addItem(portId.getName());
            }
        }
        if (portComboBox.getItemCount() > 0) {
            portComboBox.setSelectedIndex(0);
        }
    }
}
