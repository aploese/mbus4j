/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package net.sf.mbus4j.decoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id: MBusDocumentationExamplesTest.java 18 2010-03-20 16:15:49Z
 * arnep $
 */
public class MBusDocumentationExamplesTest {

    private Decoder instance;

    private void doTest(String chapter, int exampleIndex, Class<?> clazz) throws Exception {
        System.out.println(String.format("testPackage chapter %s example: %d ", chapter, exampleIndex));
        InputStream is = MBusDocumentationExamplesTest.class.getResourceAsStream(String.format("/net/sf/mbus4j/example-%s-%d.txt", chapter, exampleIndex));
        BufferedReader resultStr;
        int line;
        String dataLine;
        String parsedLine;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            Frame f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(br.readLine())));
            assertEquals(Decoder.DecodeState.SUCCESS, instance.getState(), "ParserState");
            assertNotNull(f, "DataValue not available");
            assertEquals(clazz, f.getClass());
            testJSON(f, chapter, exampleIndex);
//        System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", instance.getDataValue().toString()));
            resultStr = new BufferedReader(new StringReader(f.toString()));
            line = 1;
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
            while (parsedLine != null && dataLine != null) {
                line++;
                assertEquals(dataLine, parsedLine, String.format("Line %d", line));
                dataLine = br.readLine();
                parsedLine = resultStr.readLine();
            }
        }
        resultStr.close();

        assertEquals(dataLine, parsedLine, String.format("Length mismatch at line %d Data", line));
    }

    @BeforeEach
    public void setUp() {
        instance = new Decoder();
    }

    @AfterEach
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

    @Test
    public void testRequestClass2Data() throws Exception {
        Frame f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes("107BFE7916")));
        assertEquals("control code = REQ_UD2\nifb = false\nfcv = true\naddress = 0xFE\n", f.toString());
    }

    private void testJSON(Frame frame, String chapter, int exampleIndex) throws Exception {
        JSONObject json = frame.toJSON(JsonSerializeType.ALL);
//        System.out.println(json.toString(1));
        Frame jsonFrame = JSONFactory.createFrame(json);
        jsonFrame.fromJSON(json);
        assertEquals(frame.toString(), jsonFrame.toString(), "JSON Serializing of " + chapter + " " + exampleIndex);

        InputStream is = MBusDocumentationExamplesTest.class.getResourceAsStream(String.format("/net/sf/mbus4j/example-%s-%d.json", chapter, exampleIndex));
        BufferedReader resultStr;
        int line;
        String dataLine;
        String parsedLine;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            resultStr = new BufferedReader(new StringReader(json.toString(4)));
            line = 0;
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
            while (parsedLine != null && dataLine != null) {
                line++;
                //TODO Skiped Test assertEquals(dataLine, parsedLine, String.format("Line %d", line));
                dataLine = br.readLine();
                parsedLine = resultStr.readLine();
            }
        }
        resultStr.close();

//TODO Skiped Test         assertEquals(dataLine, parsedLine, String.format("Length mismatch at line %d Data", line));
    }

}
