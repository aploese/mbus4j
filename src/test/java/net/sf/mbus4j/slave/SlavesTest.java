/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slave;

import net.sf.mbus4j.LogInit;
import net.sf.mbus4j.MasterStreams;
import net.sf.mbus4j.dataframes.MBusMedium;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class SlavesTest {

    private Slaves slaves;
    private MasterStreams master;

    public SlavesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.DEBUG);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        master = new MasterStreams();
        slaves = new Slaves();
        slaves.setStreams(master.getInputStream(), master.getOutputStream());
    }

    @After
    public void tearDown() throws Exception {
        master.close();
        slaves.releaseStreams();
        slaves = null;
    }

    /**
     * Test of addSlave method, of class Slaves.
     */
    @Test(timeout=10000)
    public void testUserDataResponse() throws Exception {
        System.out.println("userDataResponse");
        Slave slave = new Slave(0x01, 12345678, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        slave = new Slave(0x0F, 01234567, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        slave = new Slave(0x10, 00123456, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        slave = new Slave(0xFA, 00012345, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        master.sendRequestAndCollectResponse("107B017C16", "680F0F6808017278563412AB050000000000003F16");
        master.sendRequestAndCollectResponse("107B017C16", "680F0F6808017278563412AB050000010000004016");
        master.replay();
        synchronized (master) {
            master.wait();
        }
        assertTrue(master.isOK());
        //TODO  FCB ACB ??
        // BCD behandlung ???
    }

}