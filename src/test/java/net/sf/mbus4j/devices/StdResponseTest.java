/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
 * Copyright (C) 2009  Arne Plöse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.devices;

import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._32_BIT_INTEGER;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._6_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._8_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_KILO_WH_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_KILO_WH_E_1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_MEGA_J_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_MEGA_J_E_1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FABRICATION_NO;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.POWER_KILO_W_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.POWER_W_E_2;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_CBM_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_L_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_L_E_1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_L_E_2;
import static org.junit.Assert.assertEquals;

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
public class StdResponseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    Response instance;

    public StdResponseTest() {
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
        Response r1 = instance.clone();
        r1.chooseAlternative(1, ENERGY_KILO_WH_E_1);
        r1.chooseAlternative(2, VOLUME_L_E_1);
        r1.chooseAlternative(3, POWER_KILO_W_E_0);
        assertEquals(r1.getTag(1), instance.getTag(1).getAlternative());

    }
}
