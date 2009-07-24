/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.encoder;

import net.sf.mbus4j.LogInit;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author aploese
 */
public class TestEncoder {
    private Encoder instance;

    private String arrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length);
        for (byte b: bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
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
        instance = new Encoder();
    }

    @After
    public void tearDown() {
        instance = null;
    }


    @Test
    public void testSimpleUserDataResponse() throws Exception {
        UserDataResponse udr = new UserDataResponse();
        udr.setAddress((byte)1);
        udr.setIdentNumber(12345678);
        udr.setManufacturer("AMK");
        udr.setMedium(MBusMedium.StdMedium.OTHER);
        udr.addStatus(UserDataResponse.StatusCode.APPLICATION_NO_ERROR);
        byte[] result =  instance.encodeFrame(udr);
        assertEquals("680F0F6808017278563412AB050000000000003F16", arrayToString(result));

    }
}
