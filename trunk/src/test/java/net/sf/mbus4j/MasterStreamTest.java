/*
 * mbus4j - Open source drivers for mbus protocol (www.mbus.com) - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009  Arne Plöse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class MasterStreamTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.INFO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private MasterStreams master;

    public MasterStreamTest() {
    }

    @Test(timeout = 10000)
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

    @Test(timeout = 10000)
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

    @Before
    public void setUp() {
        master = new MasterStreams();
    }

    @After
    public void tearDown() {
        master = null;
    }
}
