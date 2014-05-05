package net.sf.mbus4j;

/*
 * #%L
 * mbus4j-core
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
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
 * #L%
 */
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
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private SlaveStreams slaves;

    public SlaveStreamTest() {
    }

    @Test(timeout = 10000)
    public void respondToRequest() throws Exception {
        slaves.respondToRequest("0102", "0201");
        slaves.respondToRequest("0304", "0403");
        slaves.respondToRequest("0506", 1);
        slaves.respondToRequest("0708", "0807");
        slaves.replay();
        assertFalse(slaves.isOK());
        slaves.os.write(0x01);
        slaves.os.write(0x02);
        assertFalse(slaves.isOK());
        assertEquals(0x02, slaves.is.read());
        assertEquals(0x01, slaves.is.read());
        assertFalse(slaves.isOK());
        slaves.os.write(0x03);
        slaves.os.write(0x04);
        assertFalse(slaves.isOK());
        assertEquals(0x04, slaves.is.read());
        assertEquals(0x03, slaves.is.read());
        assertFalse(slaves.isOK());
        slaves.os.write(0x05);
        slaves.os.write(0x06);
        assertFalse(slaves.isOK());
        slaves.os.write(0x07);
        slaves.os.write(0x08);
        assertFalse(slaves.isOK());
        assertEquals(0x08, slaves.is.read());
        assertEquals(0x07, slaves.is.read());
        assertTrue(slaves.isOK());
    }

    @Before
    public void setUp() throws Exception {
        slaves = new SlaveStreams();
        slaves.open();
    }

    @After
    public void tearDown() throws Exception {
        slaves.close();
        slaves = null;
    }
}
