package net.sf.mbus4j.devices;

/*
 * #%L
 * mbus4j-master
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

import net.sf.mbus4j.dataframes.MBusMedium;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 *
 * @author aploese
 */
public class GenericDeviceTest {

    private GenericDevice instance;

    public GenericDeviceTest() {
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

    /**
     * Test of getMan method, of class GenericDevice.
     */
    @Test
    @Ignore
    public void testGetManufacturer() {
        System.out.println("getManufacturer");

        GenericDevice instance = null;
        String expResult = "";
        String result = instance.getManufacturer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMedium method, of class GenericDevice.
     */
    @Test
    @Ignore
    public void testGetMedium() {
        System.out.println("getMedium");

        GenericDevice instance = null;
        MBusMedium expResult = null;
        MBusMedium result = instance.getMedium();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readValues method, of class GenericDevice.
     */
    @Test
    @Ignore
    public void testReadValues()
            throws Exception {
        System.out.println("readValues");

        Sender sender = null;
        GenericDevice instance = null;
        Map expResult = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    @Ignore
    public void testSerialize()
            throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bos);
        objOut.writeObject(instance);
        objOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        System.out.println(String.format("Serialized AcwHeatmeter: (size: %s) %s",
                bos.size(),
                bos.toString()));

        GenericDevice result = (GenericDevice) new ObjectInputStream(bis).readObject();
        assertEquals(instance, result);
        System.out.println(instance.toString());
        System.out.println(result.toString());
        assertEquals(instance.isAcd(),
                result.isAcd());
        assertEquals(instance.isDfc(),
                result.isDfc());
    }
}
