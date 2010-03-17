/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slaves.ui;

import javax.swing.AbstractListModel;
import net.sf.mbus4j.slaves.Slaves;

/**
 *
 * @author aploese
 */
class SlavesListModel extends  AbstractListModel {

    private Slaves slaves;

    @Override
    public int getSize() {
       return slaves == null ? 0 : slaves.getSalvesSize();
    }

    @Override
    public Object getElementAt(int index) {
       return slaves.getSlave(index);
    }

    public void setSlaves(Slaves slaves) {
        final int oldSize = getSize();
        this.slaves = null;
        if (oldSize > 0) {
            fireIntervalRemoved(this, 0, oldSize);
        }
        this.slaves = slaves;
        if (getSize() > 0) {
            fireIntervalAdded(this, 0, getSize());
        }
    }

    public Slaves getSlaves() {
        return slaves;
    }

}
