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
package net.sf.mbus4j;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class MBusUtilsTest {

    public MBusUtilsTest() {
    }

    /**
     * Test of shortBcd2Man method, of class MBusUtils.
     */
    @Test
    public void testShort2Man() {
        System.out.println("shortBcd2Man");
        short value = 1091;
        String expResult = "ABC";
        String result = MBusUtils.short2Man(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of man2BcdShort method, of class MBusUtils.
     */
    @Test
    public void testMan2Short() {
        System.out.println("man2BcdShort");
        String man = "ABC";
        short expResult = 1091;
        short result = MBusUtils.man2Short(man);
        assertEquals(expResult, result);
    }

    /**
     * Test of String2Bcd method, of class MBusUtils.
     */
    @Test
    public void testString2Bcd() {
        System.out.println("String2Bcd");
        String value = "12345678";
        long expResult = 0x12345678L;
        long result = MBusUtils.String2Bcd(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of long2Bcd method, of class MBusUtils.
     */
    @Test
    public void testLong2Bcd() {
        assertEquals(0x1234567890L, MBusUtils.long2Bcd(1234567890L));
    }

    /**
     * Test of bcd2Long method, of class MBusUtils.
     */
    @Test
    public void testBcd2Long() {
        assertEquals(1234567890L, MBusUtils.bcd2Long(0x1234567890L));
    }

    /**
     * Test of int2Bcd method, of class MBusUtils.
     */
    @Test
    public void testInt2Bcd() {
        assertEquals(0x12345678, MBusUtils.int2Bcd(12345678));
    }

    /**
     * Test of bcd2Int method, of class MBusUtils.
     */
    @Test
    public void testBcd2Int() {
        assertEquals(12345678, MBusUtils.bcd2Int(0x12345678));
    }

    /**
     * Test of short2Bcd method, of class MBusUtils.
     */
    @Test
    public void testShort2Bcd() {
        assertEquals(0x1234, MBusUtils.short2Bcd((short) 1234));
    }

    /**
     * Test of bcd2Short method, of class MBusUtils.
     */
    @Test
    public void testBcd2Short() {
        assertEquals(1234, MBusUtils.bcd2Short((short) 0x1234));
    }

    /**
     * Test of byte2Bcd method, of class MBusUtils.
     */
    @Test
    public void testByte2Bcd() {
        assertEquals(0x12, MBusUtils.byte2Bcd((byte) 12));
    }

    /**
     * Test of bcd2Byte method, of class MBusUtils.
     */
    @Test
    public void testBcd2Byte() {
        assertEquals(12, MBusUtils.bcd2Byte((byte) 0x12));
    }
}
