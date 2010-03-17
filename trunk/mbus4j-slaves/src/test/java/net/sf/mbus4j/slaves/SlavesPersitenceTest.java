/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slaves;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;
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
public class SlavesPersitenceTest {

    public SlavesPersitenceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWrite() throws Exception {
        
        Slaves slaves = Slaves.readJsonStream(SlavesPersitenceTest.class.getResourceAsStream("slaves.json"));

        JSONObject json = slaves.toJSON(JsonSerializeType.SLAVE_CONFIG);

        InputStream is = SlavesPersitenceTest.class.getResourceAsStream("slaves.json");
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
        Slaves dehydrated = new Slaves();
        dehydrated.fromJSON(json);
        assertEquals(slaves.toJSON(JsonSerializeType.SLAVE_CONFIG).toString(1), dehydrated.toJSON(JsonSerializeType.SLAVE_CONFIG).toString(1));
        assertEquals(String.format("Length mismatch at line %d Data", line), dataLine, parsedLine);
    }

}