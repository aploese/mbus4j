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

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import net.sf.mbus4j.dataframes.UserDataResponse.StatusCode;
import net.sf.mbus4j.slaves.Slave;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jdesktop.application.Action;

/**
 *
 * @author aploese
 */
public class SlavePane
        extends JTabbedPane {

    /** Creates new form BeanForm */
    public SlavePane() {
        initComponents();
        mediumComboBox.removeAllItems();

        for (MBusMedium medium : MBusMedium.values()) {
            mediumComboBox.addItem(medium);
        }

        statusCodesList.setModel(new DefaultListModel());

        for (StatusCode sc : StatusCode.values()) {
            getStatusCodeListModel().addElement(sc);
        }


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deviceDataPanel = new javax.swing.JPanel();
        manufacturerTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        mediumComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        statusCodesList = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        identNumberTextField = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dfcCheckBox = new javax.swing.JCheckBox();
        accessNumberSpinner = new javax.swing.JSpinner();
        acdCheckBox = new javax.swing.JCheckBox();
        versionTextField = new javax.swing.JFormattedTextField();
        addressTextField = new javax.swing.JFormattedTextField();
        signatureTextField = new javax.swing.JFormattedTextField();
        networkSelectedCheckBox = new javax.swing.JCheckBox();

        setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        deviceDataPanel.setName("deviceDataPanel"); // NOI18N

        manufacturerTextField.setName("manufacturerTextField"); // NOI18N

        jLabel3.setText("Version:");
        jLabel3.setName("jLabel3"); // NOI18N

        mediumComboBox.setName("mediumComboBox"); // NOI18N

        jLabel2.setText("Medium:");
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        statusCodesList.setName("statusCodesList"); // NOI18N
        jScrollPane2.setViewportView(statusCodesList);

        jLabel7.setText("AccessNumber:");
        jLabel7.setName("jLabel7"); // NOI18N

        identNumberTextField.setFormatterFactory(createIdentnumberFortmatterFactory());
        identNumberTextField.setName("identNumberTextField"); // NOI18N

        jLabel6.setText("Status:");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel1.setText("Manufacturer:");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel4.setText("Identnumber:");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel8.setText("Signature:");
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel5.setText("Address:");
        jLabel5.setName("jLabel5"); // NOI18N

        dfcCheckBox.setText("Dfc");
        dfcCheckBox.setName("dfcCheckBox"); // NOI18N

        accessNumberSpinner.setName("accessNumberSpinner"); // NOI18N

        acdCheckBox.setText("Acd");
        acdCheckBox.setName("acdCheckBox"); // NOI18N

        versionTextField.setFormatterFactory(createHexFormmatter(2));
        versionTextField.setName("versionTextField"); // NOI18N

        addressTextField.setFormatterFactory(createHexFormmatter(2));
        addressTextField.setName("addressTextField"); // NOI18N

        signatureTextField.setFormatterFactory(createHexFormmatter(4));
        signatureTextField.setName("signatureTextField"); // NOI18N

        networkSelectedCheckBox.setText("Network selected");
        networkSelectedCheckBox.setName("networkSelectedCheckBox"); // NOI18N

        javax.swing.GroupLayout deviceDataPanelLayout = new javax.swing.GroupLayout(deviceDataPanel);
        deviceDataPanel.setLayout(deviceDataPanelLayout);
        deviceDataPanelLayout.setHorizontalGroup(
            deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deviceDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(versionTextField)
                    .addComponent(manufacturerTextField)
                    .addComponent(mediumComboBox, 0, 96, Short.MAX_VALUE)
                    .addComponent(identNumberTextField))
                .addGap(29, 29, 29)
                .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deviceDataPanelLayout.createSequentialGroup()
                        .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(accessNumberSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(signatureTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                    .addGroup(deviceDataPanelLayout.createSequentialGroup()
                        .addComponent(acdCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dfcCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(networkSelectedCheckBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        deviceDataPanelLayout.setVerticalGroup(
            deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deviceDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addGroup(deviceDataPanelLayout.createSequentialGroup()
                        .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(manufacturerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(mediumComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accessNumberSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(deviceDataPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(1, 1, 1))
                            .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(versionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(signatureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(deviceDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(identNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(acdCheckBox)
                            .addComponent(dfcCheckBox)
                            .addComponent(networkSelectedCheckBox))))
                .addContainerGap())
        );

        addTab("Device Data", deviceDataPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner accessNumberSpinner;
    private javax.swing.JCheckBox acdCheckBox;
    private javax.swing.JFormattedTextField addressTextField;
    private javax.swing.JPanel deviceDataPanel;
    private javax.swing.JCheckBox dfcCheckBox;
    private javax.swing.JFormattedTextField identNumberTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField manufacturerTextField;
    private javax.swing.JComboBox mediumComboBox;
    private javax.swing.JCheckBox networkSelectedCheckBox;
    private javax.swing.JFormattedTextField signatureTextField;
    private javax.swing.JList statusCodesList;
    private javax.swing.JFormattedTextField versionTextField;
    // End of variables declaration//GEN-END:variables
    private Slave slave;
    private List<ResponseFramePanel> rfps = new ArrayList<ResponseFramePanel>();

    private AbstractFormatterFactory createIdentnumberFortmatterFactory() {
        MaskFormatter result;

        try {
            result = new MaskFormatter("########");
            result.setPlaceholderCharacter('0');

            return new javax.swing.text.DefaultFormatterFactory(result);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    private AbstractFormatterFactory createHexFormmatter(int digits) {
        StringBuilder sb = new StringBuilder("0x");

        for (int i = 0; i < digits; i++) {
            sb.append("H");
        }

        MaskFormatter result;

        try {
            result = new MaskFormatter(sb.toString());
            result.setPlaceholderCharacter('0');

            return new javax.swing.text.DefaultFormatterFactory(result);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return the slave
     */
    public Slave getSlave() {
        return slave;
    }

    /**
     * @return the slave
     */
    public Slave createSlave() {
        final Slave s = new Slave();
        commitChanges(s);
        return s;
    }

    /**
     * @param slave the slave to set
     */
    public void setSlave(Slave slave) {
        SlaveChangeAspect.unregister(this.slave, this);
        this.slave = slave;
        refreshDeviceData();
        SlaveChangeAspect.register(this.slave, this);

    }

    private void refreshDeviceData() {
        manufacturerTextField.setText(slave.getManufacturer());
        mediumComboBox.setSelectedItem(slave.getMedium());
        versionTextField.setText(String.format("0x%02x",
                slave.getVersion()));
        identNumberTextField.setText(String.format("%08d",
                slave.getIdentNumber()));
        addressTextField.setText(String.format("0x%02x",
                slave.getAddress()));
        accessNumberSpinner.setValue(slave.getAccessnumber());
        signatureTextField.setText(String.format("0x%04x",
                slave.getSignature()));
        acdCheckBox.setSelected(slave.isAcd());
        dfcCheckBox.setSelected(slave.isDfc());
        networkSelectedCheckBox.setSelected(slave.isNetworkSelected());

        statusCodesList.clearSelection();
        for (StatusCode st : slave.getStatus()) {
            final int i = getStatusCodeListModel().indexOf(st);
            statusCodesList.addSelectionInterval(i, i);
        }
        //Frames
        while (this.getTabCount() > 1) {
            this.removeTabAt(1);
        }

        rfps.clear();

        for (ResponseFrameContainer rfc : slave.getResponseFrameContainers()) {
            ResponseFramePanel rfp = new ResponseFramePanel();
            rfp.setResponseFrameContainer(rfc);
            rfps.add(rfp);
            addTab(rfc.getName(), rfp);
        }
    }

    void commitChanges(Slave s) {
        s.setManufacturer(manufacturerTextField.getText());
        s.setMedium((MBusMedium) mediumComboBox.getSelectedItem());
        s.setVersion((byte) Integer.parseInt(versionTextField.getText().substring(2),
                16));
        s.setIdentNumber(Integer.parseInt(identNumberTextField.getText()));
        s.setAddress((byte) Integer.parseInt(addressTextField.getText().substring(2),
                16));
        s.setAccessnumber(((Number) accessNumberSpinner.getValue()).shortValue());
        s.setSignature((short) Integer.parseInt(signatureTextField.getText().substring(2),
                16));
        s.setAcd(acdCheckBox.isSelected());
        s.setDfc(dfcCheckBox.isSelected());
        s.setNetworkSelected(networkSelectedCheckBox.isSelected());

        final Object[] stO = statusCodesList.getSelectedValues();
        final StatusCode[] st = new StatusCode[stO.length];
        for (int i = 0; i < st.length; i++) {
            st[i] = (StatusCode) stO[i];
        }
        s.setStatus(st);
        while (s.getResponseFrameContainerCount() != rfps.size()) {
            if (s.getResponseFrameContainerCount() > rfps.size()) {
                s.removeResponseFrameContainer(s.getResponseFrameContainerCount() - 1);
            } else {
                s.addResponseFrameContainer(new ResponseFrameContainer());
            }
        }

        for (int i = 0; i < rfps.size(); i++) {
            rfps.get(i).commitChanges(s, i);
            setTitleAt(i + 1, rfps.get(i).getFrameName());
        }
    }

    @Action
    public void addStatus() {
    }

    private DefaultListModel getStatusCodeListModel() {
        return (DefaultListModel) statusCodesList.getModel();
    }

    private void responseFrameContainerAdded(ResponseFrameContainer rfc, int i) {
        ResponseFramePanel rfp = new ResponseFramePanel();
        rfp.setResponseFrameContainer(rfc);
        rfps.add(i, rfp);
        insertTab(rfc.getName(), null, rfp, null, i + 1);
    }

    private void responseFrameContainerRemoved(int i) {
        rfps.remove(i);
        removeTabAt(i - 1);
    }

    public ResponseFrameContainer getSelectedResponseFrame() {
        if (getSelectedIndex() < 1) {
            return null;
        } else {
            return rfps.get(getSelectedIndex() -1).getResponseFrameContainer();
        }
    }

    @Aspect("pertarget(target(net.sf.mbus4j.slaves.Slave))")
    public static class SlaveChangeAspect {

        public final static void unregister(Slave slave, SlavePane slp) {
            if (slave != null) {
                SlaveChangeAspect asa = Aspects.aspectOf(SlaveChangeAspect.class, slave);
                if (asa.slp == slp) {
                    asa.slp = null;
                }
            }
        }

        public final static void register(Slave slave, SlavePane slp) {
            if (slave != null) {
                Aspects.aspectOf(SlaveChangeAspect.class, slave).slp = slp;
            }
        }
        public SlavePane slp;

        @Pointcut("execution(* net.sf.mbus4j.slaves.Slave.addResponseFrameContainer(..))")
        public void addResponseFrameContainer() {
        }

        @Pointcut("execution(* net.sf.mbus4j.slaves.Slave.removeResponseFrameContainer(..))")
        public void removeResponseFrameContainer() {
        }

        @Around("removeResponseFrameContainer()")
        public Object removeResponseFrameContainerImpl(ProceedingJoinPoint pjp) throws Throwable {
            if (slp == null) {
                return pjp.proceed();
            }
            final ResponseFrameContainer rfc = (ResponseFrameContainer) pjp.getArgs()[0];
            final Slave s = (Slave) pjp.getTarget();
            final int i = s.responseFrameContainerIndexOf(rfc);
            final Object ret = pjp.proceed();
            slp.responseFrameContainerRemoved(i);
            return ret;
        }

        @Around("addResponseFrameContainer()")
        public Object addResponseFrameContainerImpl(ProceedingJoinPoint pjp) throws Throwable {
            Object ret = pjp.proceed();
            if (slp != null) {
                final ResponseFrameContainer rfc = (ResponseFrameContainer) pjp.getArgs()[0];
                final Slave s = (Slave) pjp.getTarget();
                slp.responseFrameContainerAdded(rfc, s.responseFrameContainerIndexOf(rfc));
            }
            return ret;
        }
    }
}
