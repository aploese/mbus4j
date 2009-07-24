/*
 *
 * $Id: UserDataResponseTestA.java 407 2009-03-19 08:38:27Z aploese $
 *
 * @author aploese
 *
 */
package net.sf.mbus4j.encoder;

import net.sf.mbus4j.LogInit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.decoder.PacketParser;
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
public class UserDataResponseTest {

    private PacketParser parser;
    private Encoder instance;

    public UserDataResponseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.INFO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        parser = new PacketParser();
        instance = new Encoder();
    }

    @After
    public void tearDown() {
        instance = null;
        parser = null;
    }

    @Test
    public void testABB_HEAT_OUTLET_8_3476_0() throws Exception {
        testPackage("ABB", MBusMedium.StdMedium.HEAT_OUTLET, 8, 3476, 0);
    }

    @Test
    public void testACW_HEAT_OUTLET_9_6522360_0() throws Exception {
        testPackage("ACW", MBusMedium.StdMedium.HEAT_OUTLET, 9, 6522360, 0);
    }

    @Test
    public void testACW_HEAT_OUTLET_11_8772050_0() throws Exception {
        testPackage("ACW", MBusMedium.StdMedium.HEAT_OUTLET, 11, 8772050, 0);
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

    @Test
    public void testPAD_WATER_1_12345678_0() throws Exception {
        testPackage("PAD", MBusMedium.StdMedium.WATER, 1, 12345678, 0);
    }

    @Test
    public void testREL_HEAT_COST_ALLOCATOR_64_13131313_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.HEAT_COST_ALLOCATOR, 64, 13131313, 0);
    }

    @Test
    public void testREL_GAS_9_5119949_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.GAS, 9, 5119949, 0);
    }

    @Test
    public void testREL_GAS_8_3043269_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.GAS, 8, 3043269, 0);
    }

    @Test
    public void testREL_GAS_9_8077237_0() throws Exception {
        testPackage("REL", MBusMedium.StdMedium.GAS, 9, 8077237, 0);
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

    private void testPackage(final String man, final String deviceName) throws IOException {
        System.out.println("testPackage: " + deviceName);
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s.txt", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = PacketParser.ascii2Bytes(br.readLine());
        for (byte b : data) {
            parser.addByte(b);
        }
        assertEquals("ParserState", PacketParser.DecodeState.EXPECT_START, parser.getState());
        assertNotNull("DataValue not available", parser.getFrame());
        byte[] result = instance.encode(parser.getFrame());
        assertArrayEquals(data, result);
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex, String comment) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d-%s", manufacturerId, medium.name(), version, identNumber, packetIndex, comment));
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d", manufacturerId, medium.name(), version, identNumber, packetIndex));
    }


}
