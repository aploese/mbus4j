/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import net.sf.mbus4j.dataframes.MBusMedium;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class GenericDeviceTest {


    private GenericDevice instance;
    
    public GenericDeviceTest() {
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
    public void testReadValues() throws Exception {
        System.out.println("readValues");
        Sender sender = null;
        GenericDevice instance = null;
        Map expResult = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

        @Test
    @Ignore
    public void testSerialize() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bos);
        objOut.writeObject(instance);
        objOut.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        System.out.println(String.format("Serialized AcwHeatmeter: (size: %s) %s", bos.size(), bos.toString()));
        GenericDevice result = (GenericDevice) new ObjectInputStream(bis).readObject();
        assertEquals(instance, result);
        System.out.println(instance.toString());
        System.out.println(result.toString());
        assertEquals(instance.isAcd(), result.isAcd());
        assertEquals(instance.isDfc(), result.isDfc());
    }

}