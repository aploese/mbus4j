/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slave.acw;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
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
public class AcwHeatMeterTest {
    private AcwHeatMeter instance;

    public AcwHeatMeterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new AcwHeatMeter(0x01, 00000001, 0x09, MBusMedium.StdMedium.HEAT_OUTLET, VifStd.ENERGY_KILO_WH_E_1, VifStd.VOLUME_L_E_2, VifStd.POWER_W_E_2, VifStd.VOLUME_L_E_1, VifStd.VOLUME_L_E_1, VifStd.ENERGY_KILO_WH_E_0, AcwHeatMeter.State.STANDARD, false, 0, 0, 0);
    }

    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of handleApplicationReset method, of class AcwHeatMeter.
     */
    @Test
    public void testHandleApplicationReset() {
        System.out.println("handleApplicationReset");
        ApplicationReset applicationReset = new ApplicationReset(ApplicationReset.TelegramType.ALL, 0x12);
        applicationReset.setAddress((byte)0);
        assertFalse(instance.willHandleRequest(applicationReset));
        applicationReset.setAddress((byte)1);
        assertTrue(instance.willHandleRequest(applicationReset));
        Frame result = instance.handleApplicationReset(applicationReset);
        assertEquals(SingleCharFrame.SINGLE_CHAR_FRAME, result);
        assertEquals(AcwHeatMeter.State.CF50, instance.getState());
    }

}