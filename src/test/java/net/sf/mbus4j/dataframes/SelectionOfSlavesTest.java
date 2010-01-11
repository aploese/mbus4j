/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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