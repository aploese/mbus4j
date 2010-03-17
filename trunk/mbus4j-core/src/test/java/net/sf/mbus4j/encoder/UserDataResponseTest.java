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
package net.sf.mbus4j.encoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.decoder.Decoder;

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
    private Decoder parser;
    private Encoder instance;

    public UserDataResponseTest() {
    }

    @Before
    public void setUp() {
        parser = new Decoder();
        instance = new Encoder();
    }

    @After
    public void tearDown() {
        instance = null;
        parser = null;
    }

    @Test
    public void testABB_HEAT_8_3476_0() throws Exception {
        testPackage("ABB", MBusMedium.HEAT, 8, 3476, 0);
    }

    @Test
    public void testACW_HEAT_11_8772050_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 11, 8772050, 0);
    }

    @Test
    public void testACW_HEAT_9_6522360_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 9, 6522360, 0);
    }

    @Test
    public void testEMH_ELECTRICITY_9_332092_0() throws Exception {
        testPackage("EMH", MBusMedium.ELECTRICITY, 9, 332092, 0);
    }

    @Test
    public void testEMU_32_5_701841_0() throws Exception {
        testPackage("EMU", MBusMedium.RESERVED_0X20, 5, 701841, 0);
    }

    @Test
    public void testLUG_HEAT_2_65068549_0() throws Exception {
        testPackage("LUG", MBusMedium.HEAT, 2, 65068549, 0);
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d", manufacturerId, medium.name(), version, identNumber, packetIndex));
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex, String comment) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d-%s", manufacturerId, medium.name(), version, identNumber, packetIndex, comment));
    }

    private void testPackage(final String man, final String deviceName) throws IOException {
        System.out.println("testPackage: " + deviceName);
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s.txt", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = Decoder.ascii2Bytes(br.readLine());
        for (byte b : data) {
            parser.addByte(b);
        }
        assertEquals("ParserState", Decoder.DecodeState.EXPECT_START, parser.getState());
        assertNotNull("DataValue not available", parser.getFrame());
        byte[] result = instance.encode(parser.getFrame());
        assertArrayEquals(data, result);
    }

    @Test
    public void testPAD_WATER_1_12345678_0() throws Exception {
        testPackage("PAD", MBusMedium.WATER, 1, 12345678, 0);
    }

    @Test
    public void testREL_GAS_8_3043269_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 8, 3043269, 0);
    }

    @Test
    public void testREL_GAS_9_5119949_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 9, 5119949, 0);
    }

    @Test
    public void testREL_GAS_9_8077237_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 9, 8077237, 0);
    }

    @Test
    public void testREL_HEAT_COST_ALLOCATOR_64_13131313_0() throws Exception {
        testPackage("REL", MBusMedium.HEAT_COST_ALLOCATOR, 64, 13131313, 0);
    }

    @Test
    public void testSIE_HEAT_1_60109158_0() throws Exception {
        testPackage("SIE", MBusMedium.HEAT, 1, 60109158, 0);
    }

    @Test
    public void testSLB_WATER_2_1309125_0() throws Exception {
        testPackage("SLB", MBusMedium.WATER, 2, 1309125, 0);
    }

    @Test
    public void testSLB_WATER_3_99365425_0() throws Exception {
        testPackage("SLB", MBusMedium.WATER, 3, 99365425, 0);
    }

    @Test
    public void testSPX_HEAT_52_44350175_0_No_Vife() throws Exception {
        testPackage("SPX", MBusMedium.HEAT, 52, 44350175, 0, "No_Vife");
    }

    @Test
    public void testSPX_HEAT_52_54850059_0_With_Vife() throws Exception {
        testPackage("SPX", MBusMedium.HEAT, 52, 54850059, 0, "With_Vife");
    }

    @Test
    public void testTCH_HEAT_1_44830614_0() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 1, 44830614, 0);
    }

    @Test
    public void testTCH_HEAT_38_21519982_0() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 38, 21519982, 0);
    }

    @Test
    public void testTCH_HEAT_38_21519982_1() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 38, 21519982, 1);
    }
}