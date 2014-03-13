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

import java.util.logging.Level;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import net.sf.mbus4j.SerialPortConnection;
import net.sf.mbus4j.TcpIpConnection;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.Frame.ControlCode;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.devices.GenericDevice;
import net.sf.mbus4j.devices.Sender;
import net.sf.mbus4j.log.LogUtils;
import net.sf.mbus4j.master.MBusMaster;

/**
 * The application's main frame.
 */
public class MBusMasterUiView
        extends FrameView {

    private static final Logger LOG = LogUtils.getMasterLogger();

    public MBusMasterUiView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer
                = new Timer(messageTimeout,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                statusMessageLabel.setText("");
                            }
                        });
        messageTimer.setRepeats(false);

        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");

        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }

        busyIconTimer
                = new Timer(busyAnimationRate,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
                            }
                        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();

                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }

                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        devicesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    splitPane.setRightComponent(null);
                    if (devicesList.getSelectedValue() != null) {
                        devicePane1.setDevice((GenericDevice) devicesList.getSelectedValue());
                        final int divLoc = splitPane.getDividerLocation();
                        splitPane.setRightComponent(devicePane1);
                        splitPane.setDividerLocation(divLoc);
                    }
                }
            }
        });
        setMaster(new MBusMaster());
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MBusMasterUI.getApplication().getMainFrame();
            aboutBox = new MBusMasterUiAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }

        MBusMasterUI.getApplication().show(aboutBox);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        openLastMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        saveAsMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        masterMenu = new javax.swing.JMenu();
        searchPrimaryMenuItem = new javax.swing.JMenuItem();
        searchSecondaryMenuItem = new javax.swing.JMenuItem();
        sendToDeviceMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        splitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        devicesList = new javax.swing.JList();
        devicePane1 = new net.sf.mbus4j.master.ui.DevicePane();
        toolBar = new javax.swing.JToolBar();
        connectionTypeComboBox = new javax.swing.JComboBox();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(MBusMasterUiView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(MBusMasterUiView.class, this);
        openMenuItem.setAction(actionMap.get("openFile")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        fileMenu.add(openMenuItem);

        openLastMenuItem.setAction(actionMap.get("openLast")); // NOI18N
        openLastMenuItem.setName("openLastMenuItem"); // NOI18N
        fileMenu.add(openLastMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        saveAsMenuItem.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        fileMenu.add(saveAsMenuItem);

        saveMenuItem.setAction(actionMap.get("save")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        menuBar.add(fileMenu);

        masterMenu.setText(resourceMap.getString("masterMenu.text")); // NOI18N
        masterMenu.setName("masterMenu"); // NOI18N

        searchPrimaryMenuItem.setAction(actionMap.get("searchDevicesSecondaryAddressing")); // NOI18N
        searchPrimaryMenuItem.setName("addSlaveMenuItem"); // NOI18N
        masterMenu.add(searchPrimaryMenuItem);

        searchSecondaryMenuItem.setAction(actionMap.get("searchBySecondaryAddressing")); // NOI18N
        searchSecondaryMenuItem.setName("searchSecondaryMenuItem"); // NOI18N
        masterMenu.add(searchSecondaryMenuItem);

        sendToDeviceMenuItem.setAction(actionMap.get("searchDevices")); // NOI18N
        sendToDeviceMenuItem.setName("sendToDeviceMenuItem"); // NOI18N
        masterMenu.add(sendToDeviceMenuItem);

        menuBar.add(masterMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1108, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 924, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        splitPane.setDividerLocation(200);
        splitPane.setAutoscrolls(true);
        splitPane.setContinuousLayout(true);
        splitPane.setName("splitPane"); // NOI18N
        splitPane.setOneTouchExpandable(true);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setName("jPanel1"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jSeparator1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        splitPane.setLeftComponent(jPanel1);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        devicesList.setModel(getSlavesModel());
        devicesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        devicesList.setCellRenderer(new net.sf.mbus4j.master.ui.MasterCellRenderer());
        devicesList.setComponentPopupMenu(jPopupMenu1);
        devicesList.setName("devicesList"); // NOI18N
        jScrollPane1.setViewportView(devicesList);

        splitPane.setLeftComponent(jScrollPane1);

        devicePane1.setName("devicePane1"); // NOI18N
        splitPane.setRightComponent(devicePane1);

        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        connectionTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Serial Port", "TCP-IP" }));
        connectionTypeComboBox.setEditor(null);
        connectionTypeComboBox.setName("connectionTypeComboBox"); // NOI18N
        connectionTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionTypeChanged(evt);
            }
        });
        toolBar.add(connectionTypeComboBox);

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jMenuItem1.setAction(actionMap.get("searchPrimary")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jPopupMenu1.add(jMenuItem1);

        setComponent(splitPane);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents

    private void connectionTypeChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionTypeChanged
        if (toolBar.getComponentIndex(connectionToolBar) > -1) {
            connectionToolBar.setVisible(false);
            toolBar.remove(connectionToolBar);
        }
        switch (connectionTypeComboBox.getSelectedIndex()) {
            case -1:
                return;
            case 0:
                connectionToolBar = new SerialPortConnectionToolBar(getMaster());
                break;
            case 1:
                connectionToolBar = new TcpIpConnectionToolBar(getMaster());
                break;
            default:
                throw new RuntimeException("Unknown ConnectionType: " + connectionTypeComboBox.getSelectedItem());
        }
        toolBar.add(connectionToolBar);
    }//GEN-LAST:event_connectionTypeChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox connectionTypeComboBox;
    private net.sf.mbus4j.master.ui.DevicePane devicePane1;
    private javax.swing.JList devicesList;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenu masterMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openLastMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem searchPrimaryMenuItem;
    private javax.swing.JMenuItem searchSecondaryMenuItem;
    private javax.swing.JMenuItem sendToDeviceMenuItem;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private DevicesListModel devicesListModel = new DevicesListModel();
    private File configFile;
    private ConnectionToolBar connectionToolBar;

    private DevicesListModel getSlavesModel() {
        return devicesListModel;
    }

    @Action
    public void openFile() throws IOException {
        JFileChooser chooser = new JFileChooser(configFile);
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith("json");
            }

            @Override
            public String getDescription() {
                return "JSON|*.json";
            }
        });
        JFrame mainFrame = MBusMasterUI.getApplication().getMainFrame();

        int option = chooser.showOpenDialog(mainFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            setConfigFile(chooser.getSelectedFile());
            openLast();
        }
    }

    /**
     * @return the master
     */
    public MBusMaster getMaster() {
        return devicesListModel.getMaster();
    }

    /**
     * @param master the master to set
     */
    public void setMaster(MBusMaster master) {
        this.devicesListModel.setMaster(master);
        if (master.getConnection() instanceof SerialPortConnection) {
            connectionTypeComboBox.setSelectedIndex(0);
        } else if (master.getConnection() instanceof TcpIpConnection) {
            connectionTypeComboBox.setSelectedIndex(1);
        } else {
            connectionTypeComboBox.setSelectedIndex(-1);
        }
    }

    @Action
    public void searchDevicesSecondaryAddressing() {
        try {
            getMaster().clearDevices();
            getMaster().open();
            getMaster().searchDevicesBySecondaryAddressing(1);
            getMaster().close();
            //TODO QAD
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "searchDevicesSecondaryAddressing", ex);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "searchDevicesSecondaryAddressing", ex);
        } finally {
        }
    }

    @Action
    public void sendReqToDevice() {
        try {
            getMaster().open();
            GenericDevice device = (GenericDevice) devicesList.getSelectedValue();
            for (int i = 0; i < 20; i++) {
                ApplicationReset appReset = new ApplicationReset();
                appReset.setAddress(device.getAddress());
                appReset.setFcb(true);
                appReset.setTelegramType(ApplicationReset.TelegramType.ALL);
                appReset.setSubTelegram(i);
                getMaster().send(appReset, Sender.DEFAULT_SEND_TRIES, getMaster().getResponseTimeout());
                RequestClassXData request = new RequestClassXData(true, true, ControlCode.REQ_UD2);
                request.setAddress(device.getAddress());
                Frame f = getMaster().send(request, Sender.DEFAULT_SEND_TRIES, getMaster().getResponseTimeout());
                if (f instanceof UserDataResponse) {
                    ResponseFrameContainer container = new ResponseFrameContainer();
                    container.setSelectFrame(appReset);
                    container.setRequestFrame(request);
                    container.setResponseFrame(f);
                    device.addResponseFrameContainer(container);
                }
            }
            getMaster().close();
            //TODO QAD
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "sendReqToDevice", ex);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "sendReqToDevice", ex);
        } finally {
        }
    }

    @Action(block = Task.BlockingScope.APPLICATION)
    public Task searchDevices() {
        return new SearchDevicesTask(getApplication());
    }

    private class SearchDevicesTask extends org.jdesktop.application.Task<Object, Void> {

        SearchDevicesTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SearchDevicesTask fields, here.
            super(app);
            try {
                getMaster().open();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "SearchDevicesTask", ex);
            }
            SearchDeviceFrame sdf = new SearchDeviceFrame();
            sdf.addCloseListenewr(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        getMaster().close();
                        //TODO QAD
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "SearchDevicesTask.actionPerformed", ex);
                    }
                }
            });
            sdf.setMaster(getMaster());
            sdf.setVisible(true);
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }

    @Action(enabledProperty = "configFileSet")
    public void openLast() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(configFile);
            setMaster(MBusMaster.readJsonStream(in));
            in.close();
            if (devicesListModel.getSize() > 0) {
                devicesList.setSelectedIndex(0);
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "openLast", ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "openLast", ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "openLast close", ex);
            }
        }
    }

    private void setConfigFile(File configFile) {
        final boolean old = isConfigFileSet();
        this.configFile = configFile;
        firePropertyChange("configFileSet", old, isConfigFileSet());
    }

    public boolean isConfigFileSet() {
        return configFile != null;
    }

    @Action
    public void saveAs() {
        JFileChooser chooser = new JFileChooser(configFile);
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith("json");
            }

            @Override
            public String getDescription() {
                return "JSON|*.json";
            }
        });
        JFrame mainFrame = MBusMasterUI.getApplication().getMainFrame();

        int option = chooser.showSaveDialog(mainFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            setConfigFile(chooser.getSelectedFile());
            save();
        }
    }

    @Action(enabledProperty = "configFileSet")
    public void save() {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(configFile);
            getMaster().writeJsonStream(os);
            os.flush();
            os.close();
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "save", ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "save", ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "save close", ex);
            }
        }
    }

    @Action(block = Task.BlockingScope.APPLICATION)
    public Task searchBySecondaryAddressing() {
        return new SearchBySecondaryAddressingTask(getApplication());
    }

    private class SearchBySecondaryAddressingTask extends org.jdesktop.application.Task<Object, Void> {

        SearchBySecondaryAddressingTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SearchBySecondaryAddressingTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            try {
                getMaster().open();
                getMaster().searchDevicesBySecondaryAddressing(1);
                getMaster().close();
                return getMaster(); // return your result
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, "doInBackground", ex);
                return ex;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "doInBackground", ex);
                return ex;
            }
        }

        @Override
        protected void succeeded(Object result) {
            if (result instanceof MBusMaster) {
//                setMaster((MBusMaster)result);
            }
        }
    }

}
