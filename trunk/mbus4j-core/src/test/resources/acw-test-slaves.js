/*
 * #%L
 * mbus4j-core
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
importClass(net.sf.mbus4j.dataframes.datablocks.vif.VifStd);
importClass(net.sf.mbus4j.dataframes.MBusMedium);
importClass(net.sf.mbus4j.slave.acw.AcwHeatMeter);
importClass(net.sf.mbus4j.slave.slb.Cyble);
importClass(net.sf.mbus4j.master.Master);

for (i = 0; i <= Master.LAST_REGULAR_PRIMARY_ADDRESS; i++) {
//for (i = 0; i < 1; i++) {
//  app.addSlave(new AcwHeatMeter(i, i, 0x0B, MBusMedium.StdMedium.HEAT_OUTLET, VifStd.ENERGY_KILO_WH_E_1, VifStd.VOLUME_L_E_2, VifStd.POWER_W_E_2, null, null, null, AcwHeatMeter.State.STANDARD, false, 08772050 + i, 0, 0));
    app.addSlave(new Cyble(i, 100 + i, 0x03, MBusMedium.StdMedium.WATER, 1000 + i, "0000000012", 1, VifStd.VOLUME_CBM_E_0));
}
