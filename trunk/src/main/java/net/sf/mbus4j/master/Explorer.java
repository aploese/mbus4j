/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.master;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.logging.Level;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.devices.MBusDevice;
import net.sf.mbus4j.slave.Slaves;
import net.sf.mbus4j.slave.acw.AcwHeatMeter;

/**
 *
 * @author aploese
 */
public class Explorer {

    public Explorer() {
        master = new Master();
        slaves = new Slaves();
    }
    private Master master;
    private Slaves slaves;

    public static void main(String[] args) throws IOException {
        Slaves.LogInit.initLog(Slaves.LogInit.INFO);
        SerialPort masterPort = null;
        SerialPort slavePort = null;
        Explorer expl = new Explorer();
        try {
            if ((args.length > 1) || (args.length == 0)) {
                for (int i = 0; i <= Master.LAST_REGULAR_PRIMARY_ADDRESS; i++) {
                    expl.getSlaves().addSlave(new AcwHeatMeter(i, i, 0x09, MBusMedium.StdMedium.HEAT_OUTLET, VifStd.ENERGY_KILO_WH_E_1, VifStd.VOLUME_L_E_2, VifStd.POWER_W_E_2, VifStd.VOLUME_L_E_1, VifStd.VOLUME_L_E_1, VifStd.ENERGY_KILO_WH_E_0, AcwHeatMeter.State.STANDARD, false, 1000 + i, 0, 0));
                }
                slavePort = Master.openPort(args[1]);
                expl.getSlaves().setStreams(slavePort.getInputStream(), slavePort.getOutputStream());
            }
            if (args.length > 0) {
                masterPort = Master.openPort(args[0]);
                expl.getMaster().setStreams(masterPort.getInputStream(), masterPort.getOutputStream(), masterPort.getBaudRate());
            }
        } catch (NoSuchPortException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortInUseException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (MBusDevice dev : expl.searchDevicesByPrimaryAddress()) {
                System.out.println("DEV:\n " + dev.toString());
            }
        } catch (Exception ex) {
            System.err.print("Error sleep " + ex);
        }
        try {
            expl.getMaster().close();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (masterPort != null) {
            masterPort.close();
        }
        try {
            expl.getSlaves().releaseStreams();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Slaves.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (slavePort != null) {
            slavePort.close();
        }

    }

    /**
     * @return the master
     */
    public Master getMaster() {
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(Master master) {
        this.master = master;
    }

    /**
     * @return the slaves
     */
    public Slaves getSlaves() {
        return slaves;
    }

    /**
     * @param slaves the slaves to set
     */
    public void setSlaves(Slaves slaves) {
        this.slaves = slaves;
    }

    private MBusDevice[] searchDevicesByPrimaryAddress() throws IOException, InterruptedException {
        return master.searchDevicesByPrimaryAddress();
    }
}
