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
package net.sf.mbus4j.decoder;

import net.sf.mbus4j.dataframes.Frame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.json.JSONFactory;

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
public class UserDataResponseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private Decoder instance;

    public UserDataResponseTest() {
    }

    @Before
    public void setUp() {
        instance = new Decoder();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testABB_HEAT_OUTLET_8_3476_0() throws Exception {
        testPackage("ABB", MBusMedium.StdMedium.HEAT_OUTLET, 8, 3476, 0);
    }

    @Test
    public void testACW_HEAT_OUTLET_11_8772050_0() throws Exception {
        testPackage("ACW", MBusMedium.StdMedium.HEAT_OUTLET, 11, 8772050, 0);
    }

    @Test
    public void testACW_HEAT_OUTLET_11_9803784_0() throws Exception {
        testPackage("ACW", MBusMedium.StdMedium.HEAT_OUTLET, 11, 9803784, 0);
    }

    @Test
    public void testACW_HEAT_OUTLET_9_6522360_0() throws Exception {
        testPackage("ACW", MBusMedium.StdMedium.HEAT_OUTLET, 9, 6522360, 0);
    }

    @Test
    public void testEMH_ELECTRICITY_9_332092_0() throws Exception {
        testPackage("EMH", MBusMedium.StdMedium.ELECTRICITY, 9, 332092, 0);
    }

    @Test
    public void testEMU_32_5_701841_0() throws Exception {
        testPackage("EMU", new MBusMedium.UnknownMBusMedium(32), 5, 701841, 0);
    }

    @Test
    public void testLUG_HEAT_OUTLET_2_65068549_0() throws Exception {
        testPackage("LUG", MBusMedium.StdMedium.HEAT_OUTLET, 2, 65068549, 0);
    }

    public void testNew() throws Exception {
        try {
            testPackage("", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("PARSED PACKAGE DATA >>>>");
            System.err.println(instance.getFrame().toString());
            System.err.println("<<<< PARSED PACKAGE DATA");
            throw ex;
        }
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d", manufacturerId, medium.name(), version, identNumber, packetIndex));
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex, String comment) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d-%s", manufacturerId, medium.name(), version, identNumber, packetIndex, comment));
    }

    private void testPackage(final String man, final String deviceName) throws Exception {
        System.out.println("testPackage: " + deviceName);
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s.txt", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = Decoder.ascii2Bytes(br.readLine());
        try {
            for (byte b : data) {
                instance.addByte(b);
            }
        } catch (RuntimeException ex) {
            System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", instance.getFrame().toString()));
            throw ex;
        }
        assertEquals("ParserState", Decoder.DecodeState.EXPECT_START, instance.getState());
        assertNotNull("DataValue not available", instance.getFrame());
        testJSON(instance.getFrame(), man, deviceName);
        BufferedReader resultStr = new BufferedReader(new StringReader(instance.getFrame().toString()));
        int line = 1;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();
        while (parsedLine != null && dataLine != null) {
            line++;
            assertEquals(String.format("Line %d", line), dataLine, parsedLine);
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }
        br.close();
        resultStr.close();

        assertEquals(String.format("Length mismatch at line %d Data", line), dataLine, parsedLine);
    }

    @Test
    public void testPAD_WATER_1_12345678_0() throws Exception {
        testPackage("PAD", MBusMedium.StdMedium.WATER, 1, 12345678, 0);
    }

    @Test
    public void testREL_GAS_8_3043269_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.GAS, 8, 3043269, 0);
    }

    @Test
    public void testREL_GAS_9_5119949_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.GAS, 9, 5119949, 0);
    }

    @Test
    public void testREL_GAS_9_8077237_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.GAS, 9, 8077237, 0);
    }

    @Test
    public void testREL_HEAT_COST_ALLOCATOR_64_13131313_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.HEAT_COST_ALLOCATOR, 64, 13131313, 0);
    }

    @Test
    public void testSIE_HEAT_OUTLET_1_60109158_0() throws Exception {
        testPackage("SIE", MBusMedium.StdMedium.HEAT_OUTLET, 1, 60109158, 0);
    }

    @Test
    public void testSLB_WATER_2_1309125_0() throws Exception {
        testPackage("SLB", MBusMedium.StdMedium.WATER, 2, 1309125, 0);
    }

    @Test
    public void testSLB_WATER_3_99365425_0() throws Exception {
        testPackage("SLB", MBusMedium.StdMedium.WATER, 3, 99365425, 0);
    }

    @Test
    public void testSPX_HEAT_OUTLET_52_44350175_0_No_Vife() throws Exception {
        testPackage("SPX", MBusMedium.StdMedium.HEAT_OUTLET, 52, 44350175, 0, "No_Vife");
    }

    @Test
    public void testSPX_HEAT_OUTLET_52_54850059_0_With_Vife() throws Exception {
        testPackage("SPX", MBusMedium.StdMedium.HEAT_OUTLET, 52, 54850059, 0, "With_Vife");
    }

    @Test
    public void testTCH_HEAT_OUTLET_1_44830614_0() throws Exception {
        testPackage("TCH", MBusMedium.StdMedium.HEAT_OUTLET, 1, 44830614, 0);
    }

    @Test
    public void testTCH_HEAT_OUTLET_38_21519982_0() throws Exception {
        testPackage("TCH", MBusMedium.StdMedium.HEAT_OUTLET, 38, 21519982, 0);
    }

    @Test
    public void testTCH_HEAT_OUTLET_38_21519982_1() throws Exception {
        testPackage("TCH", MBusMedium.StdMedium.HEAT_OUTLET, 38, 21519982, 1);
    }

    private void testJSON(Frame frame, String man, String deviceName) throws Exception {
        JSONObject json = frame.toJSON(false);
//        System.out.println(json.toString(1));
        Frame jsonFrame = JSONFactory.createFrame(json);
        jsonFrame.fromJSON(json);
        assertEquals("JSON Serializing of " + deviceName, frame.toString(), jsonFrame.toString());

        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s-all.json", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        BufferedReader resultStr = new BufferedReader(new StringReader(json.toString(1)));
        int line = 0;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();
        while (parsedLine != null && dataLine != null) {
            line++;
            assertEquals(String.format("Line %d", line), dataLine, parsedLine);
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }
        br.close();
        resultStr.close();

        assertEquals(String.format("Length mismatch at line %d Data", line), dataLine, parsedLine);
   }

}
