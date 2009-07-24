/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.decoder;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author aploese
 */
public class Stack {

    private int stackPos;
    private byte[] data;

    public boolean isFull() {
        return data.length == stackPos;
    }

    public long popLong() {
        return popLong(8);
    }
    
    public long popLong(int bytes) {
        long result = 0;
        for (int i = stackPos - 1; i >= stackPos - bytes; i--) {
            result <<= 8;
            result += (data[i] & 0xFF);
        }
        stackPos -= bytes;
        return result;
    }

    public int popBcdInteger(int digits) {
        int result = 0;
        for (int i = stackPos - 1; i >= stackPos - digits / 2; i--) {
            result *= 10;
            result += (data[i] >> 4) & 0x0F;
            result *= 10;
            result += (data[i] & 0x0F);
        }
        stackPos -= digits / 2;
        return result;
    }

    public int popInteger(int bytes) {
        int result = 0;
        for (int i = stackPos - 1; i >= stackPos - bytes; i--) {
            result <<= 8;
            result += (data[i] & 0xFF);
        }
        stackPos -= bytes;
        return result;
    }

    public int popInteger() {
        return (data[--stackPos] << 24) & 0xFF000000 | (data[--stackPos] << 16) & 0x00FF0000 | (data[--stackPos] << 8) & 0x0000FF00 | data[--stackPos] & 0x000000FF;
    }

    public float popFloat() {
        return Float.intBitsToFloat(popInteger());
    }

    public short popBcdShort(int digits) {
        short result = 0;
        for (int i = stackPos - 1; i >= stackPos - digits / 2; i--) {
            result *= 10;
            result += (data[i] >> 4) & 0x0F;
            result *= 10;
            result += (data[i] & 0x0F);
        }
        stackPos -= digits / 2;
        return result;
    }

    public short popShort() {
        return (short)((data[--stackPos] << 8) & 0xFF00 | data[--stackPos] & 0x00FF);
    }

    public byte popBcdByte() {
        return (byte)popBcdShort(2);
    }

    public byte popByte() {
        return data[--stackPos];
    }

    public void clear() {
        data = null;
        stackPos = -1;
    }

    public void init(int i) {
        data = new byte[i];
        stackPos = 0;
    }

    public void push(byte b) {
        data[stackPos++] = b;
    }

    public long popBcdLong(int digits) {
        long result = 0;
        for (int i = stackPos - 1; i >= stackPos - digits / 2; i--) {
            result *= 10;
            result += (data[i] >> 4) & 0x0F;
            result *= 10;
            result += (data[i] & 0x0F);
        }
        stackPos -= digits / 2;
        return result;
    }

    public String popString() {
        StringBuilder sb = new StringBuilder();
        for (byte b1 : data) {
            sb.insert(0, (char) b1);
        }
        stackPos = 0;
        return sb.toString();
    }

    public String popMan() {
        int lWord = popShort();
        if (lWord == -1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append((char) (lWord / 1024 + 64));
        lWord %= 1024; // 32*32 = 1024
        sb.append((char) (lWord / (32) + 64));
        lWord %= 32;
        sb.append((char) (lWord + 64));
        return sb.toString();
    }

    public boolean peekIsTimestampValid(){
       return (data[stackPos - 4] & 0x80) != 0x80;
    }

    public boolean peekIsTimestampSummertime(){
       return (data[stackPos - 3] & 0x80) == 0x80;
    }

    public boolean peekIsTimestampRes1(){
       return (data[stackPos - 4] & 0x40) == 0x40;
    }

    public boolean peekIsTimestampRes2(){
       return (data[stackPos - 3] & 0x40) == 0x40;
    }

    public boolean peekIsTimestampRes3(){
       return (data[stackPos - 3] & 0x20) == 0x20;
    }

    public boolean peekBCD(){
        //TODO daten/fehler holen
        return false;
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

    public byte[] popBytes() {
        stackPos = 0;
        return data;
    }

}
