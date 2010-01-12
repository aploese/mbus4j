/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.slaves.acw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class AcwHeatMeterTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private AcwHeatMeter instance;

    public AcwHeatMeterTest() {
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
    public void testHandleApplicationReset() throws Exception {
        System.out.println("handleApplicationReset");
        ApplicationReset applicationReset = new ApplicationReset(ApplicationReset.TelegramType.ALL, 0x12);
        applicationReset.setAddress((byte) 0);
        assertFalse(instance.willHandleRequest(applicationReset));
        applicationReset.setAddress((byte) 1);
        assertTrue(instance.willHandleRequest(applicationReset));
        Frame result = instance.handleApplicationReset(applicationReset);
        assertEquals(SingleCharFrame.SINGLE_CHAR_FRAME, result);
        assertEquals(AcwHeatMeter.State.CF50, instance.getState());
    }

    @Test
    @Ignore
    public void testSerialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bos);
        objOut.writeObject(instance);
        objOut.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        System.out.println(String.format("Serialized AcwHeatmeter: (size: %s) %s", bos.size(), bos.toString()));
        AcwHeatMeter result = (AcwHeatMeter) new ObjectInputStream(bis).readObject();
        assertEquals(instance, result);
        System.out.println(instance.toString());
        System.out.println(result.toString());
        assertEquals(instance.isAcd(), result.isAcd());
        assertEquals(instance.isDfc(), result.isDfc());
    }




}
