/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
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
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j.decoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JsonSerializeType;

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
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private Decoder instance;

    private void doTest(String chapter, int exampleIndex, Class<?> clazz) throws Exception {
        System.out.println(String.format("testPackage chapter %s example: %d ", chapter, exampleIndex));
        InputStream is = MBusDocumentationExamplesTest.class.getResourceAsStream(String.format("../example-%s-%d.txt", chapter, exampleIndex));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = Decoder.ascii2Bytes(br.readLine());
        for (byte b : data) {
            instance.addByte(b);
        }
        assertEquals("ParserState", Decoder.DecodeState.EXPECT_START, instance.getState());
        assertNotNull("DataValue not available", instance.getFrame());
        assertEquals(clazz, instance.getFrame().getClass());
        testJSON(instance.getFrame(), chapter, exampleIndex);
//        System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", instance.getDataValue().toString()));
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

    @Before
    public void setUp() {
        instance = new Decoder();
    }

    @After
    public void tearDown() {
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
        for (byte b : Decoder.ascii2Bytes("107BFE7916")) {
            dv = instance.addByte(b);
        }
        assertEquals("control code = REQ_UD2\nisFcb = true\naddress = 0xFE\n", dv.toString());
    }

        private void testJSON(Frame frame, String chapter, int exampleIndex) throws Exception {
        JSONObject json = frame.toJSON(JsonSerializeType.ALL);
//        System.out.println(json.toString(1));
        Frame jsonFrame = JSONFactory.createFrame(json);
        jsonFrame.fromJSON(json);
        assertEquals("JSON Serializing of " + chapter + " " + exampleIndex, frame.toString(), jsonFrame.toString());

        InputStream is = MBusDocumentationExamplesTest.class.getResourceAsStream(String.format("../example-%s-%d.json", chapter, exampleIndex));
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
