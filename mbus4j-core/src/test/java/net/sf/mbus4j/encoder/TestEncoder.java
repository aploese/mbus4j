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

import static org.junit.Assert.assertEquals;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;

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
public class TestEncoder {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private Encoder instance;

    private String arrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length);
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
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
        udr.setAddress((byte) 1);
        udr.setIdentNumber(12345678);
        udr.setManufacturer("AMK");
        udr.setMedium(MBusMedium.StdMedium.OTHER);
        udr.addStatus(UserDataResponse.StatusCode.APPLICATION_NO_ERROR);
        byte[] result = instance.encodeFrame(udr);
        assertEquals("680F0F6808017278563412AB050000000000003F16", arrayToString(result));

    }
}
