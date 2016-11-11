package net.sf.mbus4j.slaves.ui;

/*
 * #%L
 * mbus4j-slaves-ui
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
