/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j;

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
public class SlaveStreamTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.INFO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private SlaveStreams salves;

    public SlaveStreamTest() {
    }

    @Test(timeout = 10000)
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

    @Before
    public void setUp() {
        salves = new SlaveStreams();
    }

    @After
    public void tearDown() {
        salves = null;
    }
}
