/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.slaves.ui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import net.sf.mbus4j.slaves.Slave;

/**
 *
 * @author aploese
 */
public class SlavesCellRenderer extends JLabel implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Slave) {
            final Slave slave = (Slave) value;
            setText(String.format("%s %s 0x%02X %08d, 0x%02X", slave.getManufacturer(), slave.getMedium().getLabel(), slave.getVersion(), slave.getIdentNumber(), slave.getAddress()));
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
