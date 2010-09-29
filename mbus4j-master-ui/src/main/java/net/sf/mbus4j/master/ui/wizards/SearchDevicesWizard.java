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
package net.sf.mbus4j.master.ui.wizards;

import java.util.Map;
import net.sf.mbus4j.MBusAddressing;
import net.sf.mbus4j.master.MBusMaster;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardBranchController;
import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author aploese
 */
public class SearchDevicesWizard extends WizardBranchController {

    Wizard searchWizard;

    private MBusMaster master;
    
    public SearchDevicesWizard(MBusMaster master) {
        super(new WizardPage[]{new AddressingWizardPage()});
        this.master = master;
    }

    @Override
    public Wizard getWizardForStep(String step, Map data) {
        
        if (AddressingWizardPage.class.getName().equals(step)) {

        }
        return null;
    }

    public static void main(String[] args) {
        MBusMaster master = new MBusMaster();
        SearchDevicesWizard brancher = new SearchDevicesWizard(master);
        Wizard wizard = brancher.createWizard();
        WizardDisplayer.showWizard(wizard, new java.awt.Rectangle(200, 200, 600, 400) );
    }
}
