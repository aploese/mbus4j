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
package net.sf.mbus4j;

/**
 *
 * @author aploese
 */
public class MBusConstants {

    public final static int UNCONFIGURED_PRIMARY_ADDRESS = 0x00;
    public final static int FIRST_REGULAR_PRIMARY_ADDRESS = 0x01;
    public final static int LAST_REGULAR_PRIMARY_ADDRESS = 0xFA;// 250
    public final static int SLAVE_SELECT_PRIMARY_ADDRESS = 0xFD; // 253
    public final static int BROADCAST_WITH_ANSWER_PRIMARY_ADDRESS = 0xFE;// 254
    public final static int BROADCAST_NO_ANSWER_PRIMARY_ADDRESS = 0xFF;//255

    public static String int2Man(short value) {
        if (value == -1) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append((char) ((value / 1024) + 64));
        value %= 1024; // 32*32 = 1024
        sb.append((char) ((value / (32)) + 64));
        value %= 32;
        sb.append((char) (value + 64));

        return sb.toString();
    }

    /**
     * convert the man string to 2 byte binary representation
     * @param man
     * @return
     */
    public static short man2Short(String man) {
        if (man == null) {
            return -1;
        } else {
            byte[] bytes = man.getBytes();
            return (short) (bytes[2] - 64 + (bytes[1] - 64) * 32 + (bytes[0] - 64) * 1024);
        }
    }

    public static long String2Bcd(String value) {
        long result = 0;
        for (int i = 0; i < value.length(); i++) {
            result <<= 4;
            result |= (byte)(Short.parseShort(value.substring(i, i + 1), 16) & 0x0F);
        }
        return result;
    }
}
