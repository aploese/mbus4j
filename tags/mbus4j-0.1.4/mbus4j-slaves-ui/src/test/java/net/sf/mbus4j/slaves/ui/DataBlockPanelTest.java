/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slaves.ui;

import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class DataBlockPanelTest {

    private DataBlockPanel dp;

    public DataBlockPanelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dp = new DataBlockPanel();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of commitChanges method, of class DataBlockPanel.
     */
    @Test
    public void testAddNewDataBlock() {
        System.out.println("commitChanges");
        IntegerDataBlock db = new IntegerDataBlock();
        db.setDataFieldCode(DataFieldCode._32_BIT_INTEGER);
        db.setVif(VifPrimary.ENERGY_KILO_J_E_0);
        db.setValue(100);
        dp.setDataBlock(db);

        UserDataResponse udr = new UserDataResponse();
        dp.commitChanges(udr, 0);
        assertNotSame(db, udr.getDataBlock(0));
        assertEquals(db.toString(), udr.getDataBlock(0).toString());
    }

    /**
     * Test of commitChanges method, of class DataBlockPanel.
     */
    @Ignore
    @Test
    public void testSetASCIIDataBlock() {
        System.out.println("commitChanges");
        UserDataResponse udr = new UserDataResponse();

        dp.commitChanges(udr, 0);
        fail("The test case is a prototype.");
    }

    /**
     * Test of commitChanges method, of class DataBlockPanel.
     */
    @Ignore
    @Test
    public void testSetTimeStampDataBlock() {
        System.out.println("commitChanges");
        UserDataResponse udr = new UserDataResponse();
        dp.commitChanges(udr, 0);
        fail("The test case is a prototype.");
    }

        /**
     * Test of commitChanges method, of class DataBlockPanel.
     */
    @Ignore
    @Test
    public void testSetIntegerDataBlock() {
        System.out.println("commitChanges");
        UserDataResponse udr = new UserDataResponse();
        IntegerDataBlock idb = new IntegerDataBlock();
        idb.setDataFieldCode(DataFieldCode._32_BIT_INTEGER);
        dp.commitChanges(udr, 0);
        fail("The test case is a prototype.");
    }

}