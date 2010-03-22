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
package net.sf.mbus4j.dataframes;

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
public class SelectionOfSlavesTest {

    public SelectionOfSlavesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of matchId method, of class SelectionOfSlaves.
     */
    @Test
    public void testMatchId() {
        System.out.println("matchId");
        SelectionOfSlaves instance = new SelectionOfSlaves((byte)0xFD);
        instance.setBcdId(0x12345678);
        assertEquals(true, instance.matchId(12345678));
        instance.setBcdId(0xFFFFFFFF);
        assertEquals(true, instance.matchId(12345678));
        instance.setBcdId(0x12FFFF78);
        assertEquals(true, instance.matchId(12345678));
    }

    /**
     * Test of toBcd method, of class SelectionOfSlaves.
     */
    @Test
    public void testToBCD() {
        System.out.println("toBcd");
        assertEquals(0x12345678, (int)SelectionOfSlaves.toBcd(12345678, 8));
    }

}