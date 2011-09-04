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
package net.sf.mbus4j.slaves.ui;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import javax.swing.event.ChangeEvent;
import net.sf.mbus4j.SerialPortConnection;
import net.sf.mbus4j.slaves.Slaves;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import net.sf.mbus4j.slaves.Slave;

/**
 * The application's main frame.
 */
public class SlavesUiView
        extends FrameView {

    private static final Logger LOG = LoggerFactory.getLogger(SlavesUiView.class);

    class SlaveTabChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof SlavePane) {
                SlavePane sp = (SlavePane) e.getSource();
                selectedResponseFrameChangeDetector();
            }
        }
    }

    public SlavesUiView(SingleFrameApplication app) {
        super(app);

        initComponents();

        setSlaves(new Slaves());

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer =
                new Timer(messageTimeout,
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        statusMessageLabel.setText("");
                    }
                });
        messageTimer.setRepeats(false);

        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");

        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }

        busyIconTimer =
                new Timer(busyAnimationRate,
                new ActionListener() {

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

        slaveTabChangeListener = new SlaveTabChangeListener();
        slavesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                    if (splitPane.getRightComponent() != null) {
                        slavePane1.removeChangeListener(slaveTabChangeListener);
                    }

                    setSlaveSelected(slavesList.getSelectedIndex() != -1);

                    if (slavesList.getSelectedIndex() == -1) {
                        if (slavesListModel.getSize() > 0) {
                            slavesList.setSelectedIndex(0);
                        } else {
                            splitPane.setRightComponent(null);
                        }
                    } else {
                        slavePane1.setSlave((Slave) slavesList.getSelectedValue());
                    }
                    if (splitPane.getRightComponent() == null) {
                        splitPane.setRightComponent(slavePane1);
                    }

                    if (splitPane.getRightComponent() != null) {
                        slavePane1.addChangeListener(slaveTabChangeListener);
                    }
                    selectedResponseFrameChangeDetector();
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SlavesUI.getApplication().getMainFrame();
            aboutBox = new SlavesUiAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }

        SlavesUI.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        reopenMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        autoSaveCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        slaveMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
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
        slavesList = new javax.swing.JList();
        slavePane1 = new net.sf.mbus4j.slaves.ui.SlavePane();
        toolBar = new javax.swing.JToolBar();
        portLabel = new javax.swing.JLabel();
        portComboBox = new javax.swing.JComboBox();
        openPortButton = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        rollbackButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        commitButton = new javax.swing.JButton();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(SlavesUiView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(SlavesUiView.class, this);
        reopenMenuItem.setAction(actionMap.get("openLast")); // NOI18N
        reopenMenuItem.setName("reopenMenuItem"); // NOI18N
        fileMenu.add(reopenMenuItem);

        openMenuItem.setAction(actionMap.get("openFile")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        fileMenu.add(openMenuItem);

        jSeparator4.setName("jSeparator4"); // NOI18N
        fileMenu.add(jSeparator4);

        autoSaveCheckBoxMenuItem.setText(resourceMap.getString("autoSaveCheckBoxMenuItem.text")); // NOI18N
        autoSaveCheckBoxMenuItem.setName("autoSaveCheckBoxMenuItem"); // NOI18N
        fileMenu.add(autoSaveCheckBoxMenuItem);

        saveMenuItem.setAction(actionMap.get("save")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        fileMenu.add(saveAsMenuItem);

        jSeparator5.setName("jSeparator5"); // NOI18N
        fileMenu.add(jSeparator5);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        slaveMenu.setText(resourceMap.getString("slaveMenu.text")); // NOI18N
        slaveMenu.setName("slaveMenu"); // NOI18N

        jMenuItem1.setAction(actionMap.get("addSlave")); // NOI18N
        jMenuItem1.setName("addSlaveMenuItem"); // NOI18N
        slaveMenu.add(jMenuItem1);

        jMenuItem4.setAction(actionMap.get("removeSlave")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        slaveMenu.add(jMenuItem4);

        jSeparator6.setName("jSeparator6"); // NOI18N
        slaveMenu.add(jSeparator6);

        jMenuItem2.setAction(actionMap.get("addDataBlock")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        slaveMenu.add(jMenuItem2);

        jSeparator7.setName("jSeparator7"); // NOI18N
        slaveMenu.add(jSeparator7);

        jMenuItem3.setAction(actionMap.get("addResponseFrame")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        slaveMenu.add(jMenuItem3);

        menuBar.add(slaveMenu);

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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 681, Short.MAX_VALUE)
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

        slavesList.setModel(getSlavesModel());
        slavesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        slavesList.setCellRenderer(new SlavesCellRenderer());
        slavesList.setName("slavesList"); // NOI18N
        jScrollPane1.setViewportView(slavesList);

        splitPane.setLeftComponent(jScrollPane1);

        slavePane1.setName("slavePane1"); // NOI18N
        splitPane.setRightComponent(slavePane1);

        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        portLabel.setText(resourceMap.getString("portLabel.text")); // NOI18N
        portLabel.setName("portLabel"); // NOI18N
        toolBar.add(portLabel);

        portComboBox.setModel(new javax.swing.DefaultComboBoxModel());
        portComboBox.setName("portComboBox"); // NOI18N
        refreshPorts();
        toolBar.add(portComboBox);

        openPortButton.setAction(actionMap.get("openPort")); // NOI18N
        openPortButton.setText(resourceMap.getString("openPortButton.text")); // NOI18N
        openPortButton.setFocusable(false);
        openPortButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openPortButton.setName("openPortButton"); // NOI18N
        openPortButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(openPortButton);

        jSeparator2.setName("jSeparator2"); // NOI18N
        toolBar.add(jSeparator2);

        rollbackButton.setText(resourceMap.getString("rollbackButton.text")); // NOI18N
        rollbackButton.setFocusable(false);
        rollbackButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rollbackButton.setName("rollbackButton"); // NOI18N
        rollbackButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(rollbackButton);

        jSeparator3.setName("jSeparator3"); // NOI18N
        toolBar.add(jSeparator3);

        commitButton.setText(resourceMap.getString("commitButton.text")); // NOI18N
        commitButton.setFocusable(false);
        commitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        commitButton.setName("commitButton"); // NOI18N
        commitButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        commitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitChanges(evt);
            }
        });
        toolBar.add(commitButton);

        setComponent(splitPane);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents

    private void commitChanges( java.awt.event.ActionEvent evt )
    {//GEN-FIRST:event_commitChanges
        commitChanges(  );
    }//GEN-LAST:event_commitChanges

    public boolean isConfigFileSet() {
        return configFile != null;
    }

    private boolean slaveSelected = false;
    public boolean isSlaveSelected() {
        return slaveSelected;
    }

    public void setSlaveSelected(boolean b) {
        boolean old = isSlaveSelected();
        this.slaveSelected = b;
        firePropertyChange("slaveSelected", old, isSlaveSelected());
    }

    public boolean isResponseFrameSelected() {
        return (splitPane.getRightComponent() != null) && (slavePane1.getSelectedResponseFrame() != null);
    }

    private boolean responseFrameSelected = false;
    public void selectedResponseFrameChangeDetector() {
        boolean old = responseFrameSelected;
        this.responseFrameSelected = isResponseFrameSelected();
        firePropertyChange("responseFrameSelected", old, responseFrameSelected);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem autoSaveCheckBoxMenuItem;
    private javax.swing.JButton commitButton;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JToggleButton openPortButton;
    private javax.swing.JComboBox portComboBox;
    private javax.swing.JLabel portLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem reopenMenuItem;
    private javax.swing.JButton rollbackButton;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu slaveMenu;
    private net.sf.mbus4j.slaves.ui.SlavePane slavePane1;
    private javax.swing.JList slavesList;
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
    private SlavesListModel slavesListModel = new SlavesListModel(  );
    private ParseBinaryPackageFrame parseBinaryPackageFrame;
    private File configFile;
    private SlaveTabChangeListener slaveTabChangeListener;

    private SlavesListModel getSlavesModel(  )
    {
        return slavesListModel;
    }

    public void refreshPorts(  )
    {
        portComboBox.removeAllItems(  );
        Enumeration<CommPortIdentifier> en = CommPortIdentifier.getPortIdentifiers(  );
        // iterate through the ports.
        while ( en.hasMoreElements(  ) )
        {
            CommPortIdentifier portId = en.nextElement(  );
            if ( portId.getPortType(  ) == CommPortIdentifier.PORT_SERIAL )
            {
                portComboBox.addItem( portId.getName(  ) );
            }
        }

        if ( portComboBox.getItemCount(  ) > 0 )
        {
            portComboBox.setSelectedIndex( 0 );
        }
    }

    @Action
    public void addSlave(  )
    {
        if (parseBinaryPackageFrame == null) {
            parseBinaryPackageFrame = new ParseBinaryPackageFrame();
            parseBinaryPackageFrame.addAddSlaveListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    parseBinaryPackageFrame.commitChanges();
                    getSlaves().addSlave(parseBinaryPackageFrame.createSlave());
//                    setSlaves(getSlaves());
                }
            });
        }
        parseBinaryPackageFrame.setSlave(new Slave(0, 0, "", 0, MBusMedium.OTHER));
        parseBinaryPackageFrame.setVisible(true);
    }

    @Action
    public void openFile(  )
                  throws IOException
    {
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
        JFrame mainFrame = SlavesUI.getApplication(  ).getMainFrame(  );

       int option = chooser.showOpenDialog(mainFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            setConfigFile(chooser.getSelectedFile());
            openLast();
        }
    }

    /**
     * @return the slaves
     */
    public Slaves getSlaves(  )
    {
        return slavesListModel.getSlaves(  );
    }

    /**
     * @param slaves the slaves to set
     */
    public void setSlaves( Slaves slaves )
    {
        this.slavesListModel.setSlaves( slaves );
    }

    @Action
    public void openPort(  )  {
        if ( openPortButton.isSelected(  ) ) {
            try
            {
                if (!(getSlaves().getConnection() instanceof SerialPortConnection)) {
                    getSlaves().setConnection(new SerialPortConnection());
                }
                ((SerialPortConnection)getSlaves().getConnection()).setPortName((String) portComboBox.getSelectedItem(  ));
                getSlaves().open();
            } catch ( Exception ex ) {
                LOG.error( "Can't close serial port", ex );
            }
        } else {
            try {
                getSlaves().close();
            } catch ( IOException ex ) {
                LOG.error( "Can't close connection", ex );
            } catch ( InterruptedException ex ) {
                LOG.error( "Can't close connection", ex );
            }
        }
    }

    private void commitChanges(  )
    {
        slavePane1.commitChanges(slavePane1.getSlave());
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
        JFrame mainFrame = SlavesUI.getApplication(  ).getMainFrame(  );

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
                getSlaves().writeJsonStream(os);
                os.flush();
                os.close();
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(SlavesUiView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SlavesUiView.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(SlavesUiView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

        @Action(enabledProperty = "configFileSet")
    public void openLast() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(configFile);
            setSlaves(Slaves.readJsonStream(in));
            in.close();
            if (slavesListModel.getSize() > 0) {
                slavesList.setSelectedIndex(0);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SlavesUiView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SlavesUiView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SlavesUiView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   }

    private void setConfigFile(File configFile) {
        final boolean old = isConfigFileSet();
        this.configFile = configFile;
        firePropertyChange("configFileSet", old, isConfigFileSet());
    }

        @Action(enabledProperty = "responseFrameSelected")
    public void addDataBlock() {
            slavePane1.getSelectedResponseFrame().getResponseFrame();
    }

        @Action(enabledProperty = "slaveSelected")
    public void addResponseFrame() {
        Slave s = (Slave)slavesList.getSelectedValue();
        s.addResponseFrameContainer(new ResponseFrameContainer());
    }

        @Action(enabledProperty = "slaveSelected")
    public void removeSlave() {
        getSlaves().removeSlave((Slave)slavesList.getSelectedValue());
    }

}
