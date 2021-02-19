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

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class TestEncoder {

    private Encoder instance;

    private String arrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length);
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        instance = new Encoder();
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testSimpleUserDataResponse() throws Exception {
        UserDataResponse udr = new UserDataResponse();
        udr.setAddress((byte) 1);
        udr.setIdentNumber(12345678);
        udr.setManufacturer("AMK");
        udr.setMedium(MBusMedium.OTHER);
        udr.addStatus(UserDataResponse.StatusCode.APPLICATION_NO_ERROR);
        byte[] result = instance.encodeFrame(udr);
        assertEquals("680F0F6808017278563412AB050000000000003F16", arrayToString(result));

    }
}
