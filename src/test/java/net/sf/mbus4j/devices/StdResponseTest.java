/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode.*;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.*;

/**
 *
 * @author aploese
 */
public class StdResponseTest {

    Response instance;

    public StdResponseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new Response();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addTag method, of class StdResponse.
     */
    @Test
    public void testA() throws Exception {
        System.out.println("A");
        instance.addTag(_8_DIGIT_BCD, FABRICATION_NO);
        instance.addTag(_32_BIT_INTEGER, ENERGY_KILO_WH_E_0).addAlternativeVif(ENERGY_KILO_WH_E_1).addAlternativeVif(ENERGY_MEGA_J_E_0).addAlternativeVif(ENERGY_MEGA_J_E_1);
        instance.addTag(_8_DIGIT_BCD, VOLUME_L_E_0).addAlternativeVif(VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        instance.addTag(_6_DIGIT_BCD, POWER_W_E_2).addAlternativeVif(POWER_KILO_W_E_0);
        Response r1 = (Response)instance.clone();
        r1.chooseAlternative(1, ENERGY_KILO_WH_E_1);
        r1.chooseAlternative(2, VOLUME_L_E_1);
        r1.chooseAlternative(3, POWER_KILO_W_E_0);
        assertEquals(r1.getTag(1), instance.getTag(1).getAlternative());

    }


}