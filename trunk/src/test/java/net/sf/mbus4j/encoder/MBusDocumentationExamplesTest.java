/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import net.sf.mbus4j.LogInit;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
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
public class MBusDocumentationExamplesTest {
        private PacketParser parser;
        private Encoder instance;

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
        parser = null;
        instance = null;
    }


    @Test
    public void testExample_6_1__0() throws Exception {
        doTest("6.1", 0, ApplicationReset.class);
    }

    @Test
    public void testExample_6_1__1() throws Exception {
        doTest("6.1", 1, SynchronizeAction.class);
    }

    // Deprecated so not implemented...
    public void testExample_6_2__0() throws Exception {
        doTest("6.2", 0, UserDataResponse.class);
    }

    @Test
    public void testExample_6_3__0() throws Exception {
        doTest("6.3", 0, UserDataResponse.class);
    }

    @Test
    public void testExample_6_4_1__0() throws Exception {
        doTest("6.4.1", 0, SetBaudrate.class);
    }

    @Test
    public void testExample_6_4_2__0() throws Exception {
        doTest("6.4.2", 0, SendUserData.class);
    }

    @Test
    public void testExample_6_4_2__1() throws Exception {
        doTest("6.4.2", 1, SendUserData.class);
    }

    @Test
    public void testExample_6_4_2__2() throws Exception {
        doTest("6.4.2", 2, SendUserData.class);
    }

    @Test
    public void testExample_6_4_3__0() throws Exception {
        doTest("6.4.3", 0, SendUserData.class);
    }

    @Test
    public void testExample_6_4_3__1() throws Exception {
        doTest("6.4.3", 1, SendUserData.class);
    }

    @Test
    public void testExample_6_4_3__2() throws Exception {
        doTest("6.4.3", 2, SendUserData.class);
    }

    @Test
    public void testExample_6_5__0() throws Exception {
        doTest("6.5", 0, SendUserData.class);
    }

    @Test
    public void testExample_6_5__1() throws Exception {
        doTest("6.5", 1, SendUserData.class);
    }

    @Test
    public void testExample_6_5__2() throws Exception {
        doTest("6.5", 2, SendUserData.class);
    }

    @Test
    public void testExample_6_5__3() throws Exception {
        doTest("6.5", 3, SendUserData.class);
    }

    @Test
    public void testExample_6_6__0() throws Exception {
        doTest("6.6", 0, GeneralApplicationError.class);
    }

    @Test
    public void testExample_6_7_3__0() throws Exception {
        doTest("6.7.3", 0, UserDataResponse.class);
    }

    public void testRequestClass2Data() throws Exception {
        Frame dv = null;
        for (byte b : PacketParser.ascii2Bytes("107BFE7916")) {
            dv = parser.addByte((byte) b);
        }
        assertEquals("control code = REQ_UD2\nisFcb = true\naddress = 0xFE\n", dv.toString());
    }

    private void doTest(String chapter, int exampleIndex, Class<?> clazz) throws IOException {
        System.out.println(String.format("testPackage chapter %s example: %d ", chapter, exampleIndex));
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../example-%s-%d.txt", chapter, exampleIndex));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = PacketParser.ascii2Bytes(br.readLine());
        for (byte b : data) {
            parser.addByte(b);
        }
        assertEquals("ParserState", PacketParser.DecodeState.EXPECT_START, parser.getState());
        assertNotNull("DataValue not available", parser.getFrame());
        assertEquals(clazz, parser.getFrame().getClass());
        byte[] result = instance.encode(parser.getFrame());
        assertArrayEquals(data, result);
    }

}
