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

import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class StackTest {

    public StackTest() {
    }

    private Stack stack;

    @BeforeEach
    public void setUp() {
        stack = new Stack();
    }

    @AfterEach
    public void tearDown() {
        stack = null;
    }

    /**
     * Test of peekIsTimestampRes1 method, of class Stack.
     */
    @Test
    @Disabled
    public void testPeekIsTimestampRes1() {
        System.out.println("peekIsTimestampRes1");
        Stack instance = new Stack();
        boolean expResult = false;
        boolean result = instance.peekIsTimestampRes1();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peekIsTimestampRes2 method, of class Stack.
     */
    @Test
    @Disabled
    public void testPeekIsTimestampRes2() {
        System.out.println("peekIsTimestampRes2");
        Stack instance = new Stack();
        boolean expResult = false;
        boolean result = instance.peekIsTimestampRes2();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peekIsTimestampRes3 method, of class Stack.
     */
    @Test
    @Disabled
    public void testPeekIsTimestampRes3() {
        System.out.println("peekIsTimestampRes3");
        Stack instance = new Stack();
        boolean expResult = false;
        boolean result = instance.peekIsTimestampRes3();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peekIsTimestampSummertime method, of class Stack.
     */
    @Test
    @Disabled
    public void testPeekIsTimestampSummertime() {
        System.out.println("peekIsTimestampSummertime");
        Stack instance = new Stack();
        boolean expResult = false;
        boolean result = instance.peekIsTimestampSummertime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of peekIsTimestampValid method, of class Stack.
     */
    @Test
    @Disabled
    public void testPeekIsTimestampValid() {
        System.out.println("peekIsTimestampValid");
        Stack instance = new Stack();
        boolean expResult = false;
        boolean result = instance.peekIsTimestampValid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popBcdByte method, of class Stack.
     */
    @Test
    public void testPopBcdByte() {
        System.out.println("popBcdByte");
        stack.init(1);
        stack.push((byte) 0xF9);
        assertNull(stack.peekBcdError(2));
        assertEquals(-9, stack.popBcdByte());
    }

    /**
     * Test of peekBcdError method, of class Stack.
     */
    @Test
    public void testPeekBcdError() {
        System.out.println("peekBcdError");
        stack.init(4);
        stack.push((byte) 0xA9);
        stack.push((byte) 0xCB);
        stack.push((byte) 0xED);
        stack.push((byte) 0xFF);
        assertEquals("--E CbA9", stack.peekBcdError(8));
        stack.init(1);
        stack.push((byte) 0xFF);
        assertEquals("--", stack.peekBcdError(2));
    }

    /**
     * Test of popBcdInteger method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopBcdInteger() {
        System.out.println("popBcdInteger");
        int digits = 0;
        Stack instance = new Stack();
        int expResult = 0;
        int result = instance.popBcdInteger(digits);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popBcdShort method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopBcdShort() {
        System.out.println("popBcdShort");
        int digits = 0;
        Stack instance = new Stack();
        short expResult = 0;
        short result = instance.popBcdShort(digits);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popBcdLong method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopBcdLong() {
        System.out.println("popBcdLong");
        int digits = 0;
        Stack instance = new Stack();
        long expResult = 0L;
        long result = instance.popBcdLong(digits);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popByte method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopByte() {
        System.out.println("popByte");
        Stack instance = new Stack();
        byte expResult = 0;
        byte result = instance.popByte();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popBytes method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopBytes() {
        System.out.println("popBytes");
        Stack instance = new Stack();
        byte[] expResult = null;
        byte[] result = instance.popBytes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popDate method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopDate() {
        System.out.println("popDate");
        Stack instance = new Stack();
        Date expResult = null;
        Date result = instance.popDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popFloat method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopFloat() {
        System.out.println("popFloat");
        Stack instance = new Stack();
        float expResult = 0.0F;
        float result = instance.popFloat();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popInteger method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopInteger_0args() {
        System.out.println("popInteger");
        Stack instance = new Stack();
        int expResult = 0;
        int result = instance.popInteger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popInteger method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopInteger_int() {
        System.out.println("popInteger");
        int bytes = 0;
        Stack instance = new Stack();
        int expResult = 0;
        int result = instance.popInteger(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popLong method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopLong_0args() {
        System.out.println("popLong");
        Stack instance = new Stack();
        long expResult = 0L;
        long result = instance.popLong();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popLong method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopLong_int() {
        System.out.println("popLong");
        int bytes = 0;
        Stack instance = new Stack();
        long expResult = 0L;
        long result = instance.popLong(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popMan method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopMan() {
        System.out.println("popMan");
        Stack instance = new Stack();
        String expResult = "";
        String result = instance.popMan();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popShort method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopShort() {
        System.out.println("popShort");
        Stack instance = new Stack();
        short expResult = 0;
        short result = instance.popShort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popString method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopString() {
        System.out.println("popString");
        Stack instance = new Stack();
        String expResult = "";
        String result = instance.popString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of popTimeStamp method, of class Stack.
     */
    @Test
    @Disabled
    public void testPopTimeStamp() {
        System.out.println("popTimeStamp");
        Stack instance = new Stack();
        Date expResult = null;
        Date result = instance.popTimeStamp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
