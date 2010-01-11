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
