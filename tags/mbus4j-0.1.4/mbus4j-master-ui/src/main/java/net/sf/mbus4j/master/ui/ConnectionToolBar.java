/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.master.ui;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import net.sf.mbus4j.Connection;
import net.sf.mbus4j.master.MBusMaster;

/**
 *
 * @author aploese
 */
class ConnectionToolBar<T extends Connection> extends JToolBar {
    private final MBusMaster mBusMaster;

    private JLabel bitPerSecondLabel;
    private JTextField bitPerSecondTextField;
    private JLabel responseTimeoutOffsetLabel;
    private JTextField responseTimeoutOffsetTextField;

    public ConnectionToolBar(MBusMaster mBusMaster) {
        super();
        this.mBusMaster = mBusMaster;
    }

    /**
     * @return the mBusMaster
     */
    public MBusMaster getmBusMaster() {
        return mBusMaster;
    }

    protected void init() {
        bitPerSecondLabel = new JLabel("Bit per second:");
        add(bitPerSecondLabel);
        bitPerSecondTextField = new JTextField(Integer.toString(mBusMaster.getConnection().getBitPerSecond()));
        bitPerSecondTextField.setColumns(6);
        add(bitPerSecondTextField);
        responseTimeoutOffsetLabel = new JLabel("Response timeout offset:");
        add(responseTimeoutOffsetLabel);
        responseTimeoutOffsetTextField = new JTextField(Integer.toString(mBusMaster.getConnection().getResponseTimeOutOffset()));
        responseTimeoutOffsetTextField.setColumns(6);
        add(responseTimeoutOffsetTextField);
        setFloatable(false);
    }

    public T getConnection() {
        return (T) getmBusMaster().getConnection();
    }

}
