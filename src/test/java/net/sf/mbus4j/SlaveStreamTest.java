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
public class SlaveStreamTest {

    public SlaveStreamTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.INFO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private SlaveStreams salves;

    @Before
    public void setUp() {
        salves = new SlaveStreams();
    }

    @After
    public void tearDown() {
        salves = null;
    }

    @Test(timeout=10000)
     public void respondToRequest() throws Exception {
        salves.respondToRequest("0102", "0201");
        salves.respondToRequest("0304", "0403");
        salves.respondToRequest("0506", 1);
        salves.respondToRequest("0708", "0807");
        salves.replay();
        assertFalse(salves.isOK());
        salves.os.write(0x01);
        salves.os.write(0x02);
        assertFalse(salves.isOK());
        assertEquals(0x02, salves.is.read());
        assertEquals(0x01, salves.is.read());
        assertFalse(salves.isOK());
        salves.os.write(0x03);
        salves.os.write(0x04);
        assertFalse(salves.isOK());
        assertEquals(0x04, salves.is.read());
        assertEquals(0x03, salves.is.read());
        assertFalse(salves.isOK());
        salves.os.write(0x05);
        salves.os.write(0x06);
        assertFalse(salves.isOK());
        salves.os.write(0x07);
        salves.os.write(0x08);
        assertFalse(salves.isOK());
        assertEquals(0x08, salves.is.read());
        assertEquals(0x07, salves.is.read());
        assertTrue(salves.isOK());
    }

}