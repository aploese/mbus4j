package net.sf.mbus4j;

/*
 * #%L
 * mbus4j-core
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
/**
 *
 * @author aploese
 */
public class MBusUtils {


    public static String short2Man(short value) {
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
     *
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
            result |= (byte) (Short.parseShort(value.substring(i, i + 1), 16) & 0x0F);
        }
        return result;
    }

    public static long long2Bcd(long l) {
        long result = 0;
        for (int i = 0; i < 16; i++) {
            result |= (l % 10) << (i * 4);
            l /= 10;
        }
        return result;
    }

    public static long bcd2Long(long l) {
        long result = 0;
        for (int i = 0; i < 16; i++) {
            result += (long) ((l % 16) * Math.pow(10, i));
            l >>= 4;
        }
        return result;
    }

    public static int int2Bcd(int i) {
        int result = 0;
        for (int idx = 0; idx < 8; idx++) {
            result |= (i % 10) << (idx * 4);
            i /= 10;
        }
        return result;
    }

    public static int bcd2Int(int i) {
        int result = 0;
        for (int idx = 0; idx < 8; idx++) {
            result += (int) ((i % 16) * Math.pow(10, idx));
            i >>= 4;
        }
        return result;
    }

    public static short short2Bcd(short s) {
        short result = 0;
        for (int i = 0; i < 4; i++) {
            result |= (s % 10) << (i * 4);
            s /= 10;
        }
        return result;
    }

    public static short bcd2Short(short s) {
        short result = 0;
        for (int i = 0; i < 4; i++) {
            result += (short) ((s % 16) * Math.pow(10, i));
            s >>= 4;
        }
        return result;
    }

    public static byte byte2Bcd(byte b) {
        byte result = 0;
        for (int i = 0; i < 2; i++) {
            result |= (b % 10) << (i * 4);
            b /= 10;
        }
        return result;
    }

    public static byte bcd2Byte(byte b) {
        byte result = 0;
        for (int i = 0; i < 2; i++) {
            result += (byte) ((b % 16) * Math.pow(10, i));
            b >>= 4;
        }
        return result;
    }

}
