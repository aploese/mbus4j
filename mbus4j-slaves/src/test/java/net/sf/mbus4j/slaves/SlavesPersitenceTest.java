package net.sf.mbus4j.slaves;

/*
 * #%L
 * mbus4j-slaves
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
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
 * #L%
 */

import net.sf.json.JSONObject;

import net.sf.mbus4j.json.JsonSerializeType;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 *
 * @author Arne Plöse
 */
public class SlavesPersitenceTest {

    public SlavesPersitenceTest() {
    }

    @BeforeClass
    public static void setUpClass()
            throws Exception {
    }

    @AfterClass
    public static void tearDownClass()
            throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWrite()
            throws Exception {
        Slaves slaves = Slaves.readJsonStream(SlavesPersitenceTest.class.getResourceAsStream("slaves.json"), null);

        JSONObject json = slaves.toJSON(JsonSerializeType.SLAVE_CONFIG);

        InputStream is = SlavesPersitenceTest.class.getResourceAsStream("slaves.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        BufferedReader resultStr = new BufferedReader(new StringReader(json.toString(1)));
        int line = 0;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();

        while ((parsedLine != null) && (dataLine != null)) {
            line++;
            assertEquals(String.format("Line %d", line),
                    dataLine,
                    parsedLine);
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }

        br.close();
        resultStr.close();

        Slaves dehydrated = new Slaves(null);
        dehydrated.fromJSON(json);
        assertEquals(slaves.toJSON(JsonSerializeType.SLAVE_CONFIG).toString(1),
                dehydrated.toJSON(JsonSerializeType.SLAVE_CONFIG).toString(1));
        assertEquals(String.format("Length mismatch at line %d Data", line),
                dataLine,
                parsedLine);
    }
}
