/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.AssertionFailedError;

/**
 *
 * @author aploese
 */
public class MasterStreamTest {

    public MasterStreamTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.INFO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private MasterStreams master;

    @Before
    public void setUp() {
        master = new MasterStreams();
    }

    @After
    public void tearDown() {
        master = null;
    }

    @Test(timeout=10000)
     public void sendRequestAndCollectResponse() throws Exception {
        master.sendRequestAndCollectResponse("0102", "0201");
        master.sendRequestAndCollectResponse("0304", "0403");
        master.sendRequestAndCollectResponse("0506", 100);
        master.sendRequestAndCollectResponse("0708", "0807");
        assertFalse(master.isOK());
        master.replay();
        assertFalse(master.isOK());
        assertEquals(0x01, master.is.read());
        assertEquals(0x02, master.is.read());
        assertFalse(master.isOK());
        master.os.write(0x02);
        master.os.write(0x01);
        assertFalse(master.isOK());
        assertEquals(0x03, master.is.read());
        assertEquals(0x04, master.is.read());
        assertFalse(master.isOK());
        master.os.write(0x04);
        master.os.write(0x03);
        assertFalse(master.isOK());
        assertEquals(0x05, master.is.read());
        assertEquals(0x06, master.is.read());
        assertFalse(master.isOK());
        assertEquals(0x07, master.is.read());
        assertEquals(0x08, master.is.read());
        assertFalse(master.isOK());
        master.os.write(0x08);
        master.os.write(0x07);
        assertTrue(master.isOK());
    }

     @Test(timeout=10000)
     public void sendRequestAndCollectResponseWriteError() throws Exception {
        master.sendRequestAndCollectResponse("0102", "0201");
        master.replay();
        assertEquals(0x01, master.is.read());
        assertEquals(0x02, master.is.read());
        assertFalse(master.isOK());
        master.os.write(0x02);
        master.os.write(0x01);
        assertTrue(master.isOK());
        try {
            master.os.write(0x01);
            fail("Too many writes");
        } catch (AssertionFailedError ex) {
//           ex.printStackTrace();
        }
        assertFalse(master.isOK());
    }



}