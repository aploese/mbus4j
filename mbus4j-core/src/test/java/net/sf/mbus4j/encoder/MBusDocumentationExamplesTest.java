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
package net.sf.mbus4j.encoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.GeneralApplicationError;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SetBaudrate;
import net.sf.mbus4j.dataframes.SynchronizeAction;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.decoder.Decoder;
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

    private Decoder parser;
    private Encoder instance;

    private void doTest(String chapter, int exampleIndex, Class<?> clazz) throws IOException {
        System.out.println(String.format("testPackage chapter %s example: %d ", chapter, exampleIndex));
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("/net/sf/mbus4j/example-%s-%d.txt", chapter, exampleIndex));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = Decoder.ascii2Bytes(br.readLine());
        Frame f = parser.parse(new ByteArrayInputStream(data));
        assertEquals(Decoder.DecodeState.SUCCESS, parser.getState(), "ParserState");
        assertNotNull(f, "DataValue not available");
        assertEquals(clazz, f.getClass());
        byte[] result = instance.encodeFrame(f);
        assertArrayEquals(data, result);
    }

    @BeforeEach
    public void setUp() {
        parser = new Decoder();
        instance = new Encoder();
    }

    @AfterEach
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
        Frame f = parser.parse(new ByteArrayInputStream(Decoder.ascii2Bytes("107BFE7916")));
        assertEquals("control code = REQ_UD2\nisFcb = true\naddress = 0xFE\n", f.toString());
    }

}
