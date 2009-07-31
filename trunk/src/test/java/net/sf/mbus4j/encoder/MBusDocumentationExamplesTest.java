/*
 * mbus4j - Open source drivers for mbus protocol (www.mbus.com) - http://mbus4j.sourceforge.net/
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
package net.sf.mbus4j.encoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class MBusDocumentationExamplesTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.INFO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private PacketParser parser;
    private Encoder instance;

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
            dv = parser.addByte(b);
        }
        assertEquals("control code = REQ_UD2\nisFcb = true\naddress = 0xFE\n", dv.toString());
    }
}
