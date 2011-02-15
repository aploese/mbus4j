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

/**
 *
 * @author aploese
 */
public class MBusUtilsTest {

    public MBusUtilsTest() {
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
        assertEquals(0x1234, MBusUtils.short2Bcd((short)1234));
    }

    /**
     * Test of bcd2Short method, of class MBusUtils.
     */
    @Test
    public void testBcd2Short() {
        assertEquals(1234, MBusUtils.bcd2Short((short)0x1234));
    }
    /**
     * Test of byte2Bcd method, of class MBusUtils.
     */
    @Test
    public void testByte2Bcd() {
        assertEquals(0x12, MBusUtils.byte2Bcd((byte)12));
    }

    /**
     * Test of bcd2Byte method, of class MBusUtils.
     */
    @Test
    public void testBcd2Byte() {
        assertEquals(12, MBusUtils.bcd2Byte((byte)0x12));
    }
}