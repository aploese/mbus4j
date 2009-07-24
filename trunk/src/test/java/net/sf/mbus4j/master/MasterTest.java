/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.master;

import gnu.io.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;
import net.sf.mbus4j.MBusTestInputStream;
import net.sf.mbus4j.MBusTestOutputStream;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.devices.MBusDevice;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class MasterTest {

    private MBusTestInputStream is;
    private MBusTestOutputStream os;
    private Master master;

    public MasterTest() {
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
        master = new Master();
        master.setStreams(is, os, 2400);
    }

    @After
    public void tearDown() {
        master.releaseStreams();
        master = null;
        is.close();
    }

    /**
     * Test of widcardSearch method, of class Master.
     */
    @Test
    public void testWidcardSearch() throws Exception {
        System.out.println("widcardSearch");
        int leadingBcdDigitsId = 0;
        int maskLength = 7;
        int maskedMan = 0xFFFF;
        int maskedVersion = 0xFF;
        int maskedMedium = 0xFF;
        master.widcardSearch(leadingBcdDigitsId, maskLength, maskedMan, maskedVersion, maskedMedium);
        os.waitFor("", 1000);
    }

    /**
     * Test of selectBySecondaryAddress method, of class Master.
     */
    @Test
    public void testSelectBySecondaryAddress() {
        System.out.println("selectBySecondaryAddress");
        master.selectBySecondaryAddress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deselectBySecondaryAddress method, of class Master.
     */
    @Test
    public void testDeselectBySecondaryAddress() {
        System.out.println("deselectBySecondaryAddress");
        master.deselectBySecondaryAddress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}