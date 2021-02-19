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

import java.util.Calendar;
import java.util.Date;
import net.sf.mbus4j.MBusUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Stack {

    private int stackPos;
    private byte[] data;

    public void clear() {
        data = null;
        stackPos = -1;
    }

    public void init(int i) {
        data = new byte[i];
        stackPos = 0;
    }

    public boolean isFull() {
        return data.length == stackPos;
    }

    public boolean peekIsTimestampRes1() {
        return (data[stackPos - 4] & 0x40) == 0x40;
    }

    public boolean peekIsTimestampRes2() {
        return (data[stackPos - 3] & 0x40) == 0x40;
    }

    public boolean peekIsTimestampRes3() {
        return (data[stackPos - 3] & 0x20) == 0x20;
    }

    public boolean peekIsTimestampSummertime() {
        return (data[stackPos - 3] & 0x80) == 0x80;
    }

    public boolean peekIsTimestampValid() {
        return (data[stackPos - 4] & 0x80) != 0x80;
    }

    public byte popBcdByte() {
        return (byte) popBcdLong(2);
    }

    /* convert to String (LCD recomendation) */
    public char toLcdDigit(int bcdDigit) {
        switch (bcdDigit) {
            case 0x00:
                return '0';
            case 0x01:
                return '1';
            case 0x02:
                return '2';
            case 0x03:
                return '3';
            case 0x04:
                return '4';
            case 0x05:
                return '5';
            case 0x06:
                return '6';
            case 0x07:
                return '7';
            case 0x08:
                return '8';
            case 0x09:
                return '9';
            case 0x0A:
                return 'A';
            case 0x0B:
                return 'b';
            case 0x0C:
                return 'C';
            case 0x0D:
                return ' ';
            case 0x0E:
                return 'E';
            case 0x0F:
                return '-';
            default:
                throw new RuntimeException("Should never ever happend!");
        }
    }

    //TODO Pop BCD Error???
    public String peekBcdError(int digits) {
        char[] result = new char[digits];
        int resultPos = 0;
        final int floorPos = stackPos - (digits / 2);
        boolean isError = false;

        for (int i = stackPos - 1; i >= floorPos; i--) {
            result[resultPos++] = toLcdDigit((data[i] >> 4) & 0x0F);
            isError |= ((i == (stackPos - 1)) && ((data[i] & 0x0F) == 0x0F));
            result[resultPos++] += toLcdDigit(data[i] & 0x0F);
            isError |= ((data[i] & 0x0F) > 0x09);
        }

        if (isError) {
            return new String(result);
        } else {
            return null;
        }
    }

    public int popBcdInteger(int digits) {
        return (int) popBcdLong(digits);
    }

    public short popBcdShort(int digits) {
        return (short) popBcdLong(digits);
    }

    public long popBcdLong(int digits) {
        long result = 0;
        final int floorPos = stackPos - (digits / 2);
        boolean isNegative = false;

        for (int i = stackPos - 1; i >= floorPos; i--) {
            result *= 10;

            if ((i == (stackPos - 1)) && (((data[i] >> 4) & 0x0F) == 0x0F)) {
                isNegative = true;
            } else {
                result += ((data[i] >> 4) & 0x0F);
            }

            result *= 10;
            result += (data[i] & 0x0F);
        }

        stackPos -= (digits / 2);

        if (isNegative) {
            return -result;
        } else {
            return result;
        }
    }

    public byte popByte() {
        return data[--stackPos];
    }

    public byte[] popBytes() {
        stackPos = 0;

        return data;
    }

    public Date popDate() {
        int val = popShort();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, val & 0x1F);
        cal.set(Calendar.YEAR, 2000 + ((val >> 5) & 0x07) + ((val >> 9) & 0x78));
        cal.set(Calendar.MONTH, ((val >> 8) & 0x0F) - 1);

        return cal.getTime();
    }

    public float popFloat() {
        return Float.intBitsToFloat(popInteger());
    }

    public int popInteger() {
        return ((data[--stackPos] << 24) & 0xFF000000) | ((data[--stackPos] << 16) & 0x00FF0000)
                | ((data[--stackPos] << 8) & 0x0000FF00) | (data[--stackPos] & 0x000000FF);
    }

    public int popInteger(int bytes) {
        int result = 0;

        for (int i = stackPos - 1; i >= (stackPos - bytes); i--) {
            result <<= 8;
            result += (data[i] & 0xFF);
        }

        stackPos -= bytes;

        return result;
    }

    public long popLong() {
        return popLong(8);
    }

    public long popLong(int bytes) {
        long result = 0;

        for (int i = stackPos - 1; i >= (stackPos - bytes); i--) {
            result <<= 8;
            result += (data[i] & 0xFF);
        }

        stackPos -= bytes;

        return result;
    }

    public String popMan() {
        return MBusUtils.short2Man(popShort());
    }

    public short popShort() {
        return (short) (((data[--stackPos] << 8) & 0xFF00) | (data[--stackPos] & 0x00FF));
    }

    public String popString() {
        StringBuilder sb = new StringBuilder();

        for (byte b1 : data) {
            sb.insert(0, (char) b1);
        }

        stackPos = 0;

        return sb.toString();
    }

    public Date popTimeStamp() {
        int val = popInteger(4);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, val & 0x3F);
        cal.set(Calendar.HOUR_OF_DAY, (val >> 8) & 0x1F);
        cal.set(Calendar.DAY_OF_MONTH, (val >> 16) & 0x1F);
        cal.set(Calendar.MONTH, ((val >> 24) & 0x0F) - 1);
        cal.set(Calendar.YEAR, 2000 + ((val >> 21) & 0x07) + ((val >> 25) & 0x78));

        return cal.getTime();
    }

    public void push(byte b) {
        data[stackPos++] = b;
    }
}
