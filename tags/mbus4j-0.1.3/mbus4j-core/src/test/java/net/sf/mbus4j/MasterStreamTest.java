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
package net.sf.mbus4j;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private MasterStreams master;

    public MasterStreamTest() {
    }

    @Test(timeout = 120000)
    public void sendRequestAndCollectResponse() throws Exception {
        master.sendRequestAndCollectResponse("0102", "0201");
        master.sendRequestAndCollectResponse("0304", "0403");
        master.sendRequestNoAnswer("0506", 100);
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

    @Test(expected = IOException.class)
    public void writeWithNotingExpected() throws Exception {
        master.sendRequestNoAnswer("0102", 100, 1);
        master.sendRequestAndCollectResponse("01", "02");
        master.replay();
        assertEquals(0x01, master.is.read());
        assertEquals(0x02, master.is.read());
        master.os.write(0x02);
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