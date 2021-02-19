/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package net.sf.mbus4j.dataframes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class SelectionOfSlavesTest {

    public SelectionOfSlavesTest() {
    }

    /**
     * Test of matchId method, of class SelectionOfSlaves.
     */
    @Test
    public void testMatchId() {
        System.out.println("matchId");
        SelectionOfSlaves instance = new SelectionOfSlaves((byte) 0xFD);
        instance.setBcdMaskedId(0x12345678);
        assertEquals(true, instance.matchId(12345678));
        instance.setBcdMaskedId(0xFFFFFFFF);
        assertEquals(true, instance.matchId(12345678));
        instance.setBcdMaskedId(0x12FFFF78);
        assertEquals(true, instance.matchId(12345678));
    }

    /**
     * Test of matchId method, of class SelectionOfSlaves.
     */
    @Test
    public void testMatchAll() {
        System.out.println("matchAll");
        SelectionOfSlaves instance = new SelectionOfSlaves((byte) 0xFD);
        instance.setBcdMaskedId(0x00000001);
        assertEquals(false, instance.matchId(3));
        instance.setBcdMaskedId(0xFFFFFFFF);
        assertEquals(true, instance.matchId(12345678));
        instance.setBcdMaskedId(0x12FFFF78);
        assertEquals(true, instance.matchId(12345678));
    }

}
