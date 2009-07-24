/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slave;

import net.sf.mbus4j.MBusTestInputStream;
import net.sf.mbus4j.MBusTestOutputStream;
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
    private MBusTestInputStream is;
    private MBusTestOutputStream os;

    public SlavesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        is = new MBusTestInputStream();
        os = new MBusTestOutputStream();
        slaves = new Slaves();
        slaves.setStreams(is, os);
    }

    @After
    public void tearDown() throws Exception {
        slaves.releaseStreams();
        slaves = null;
        is.close();
    }

    /**
     * Test of addSlave method, of class Slaves.
     */
    @Test
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
        is.setBytes("107B017C16");
        os.waitFor("680F0F6808017278563412AB050000000000003F16", 1000);
        // Accessnumber ?
        os.clear();
        is.setBytes("107B017C16");
        os.waitFor("680F0F6808017278563412AB050000010000004016", 1000);
        //TODO  FCB ACB ??
        // BCD behandlung ???
    }

    /**
     * Test of close method, of class Slaves.
     */
    @Test
    @Ignore
    public void testClose() throws Exception {
        System.out.println("close");
        Slaves instance = null;
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Slaves.
     */
    @Test
    @Ignore
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        Slaves.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


}