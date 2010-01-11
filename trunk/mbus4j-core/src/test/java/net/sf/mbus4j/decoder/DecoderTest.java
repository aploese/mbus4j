/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.decoder;

import java.util.ArrayList;
import java.util.List;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.datablocks.vif.AsciiVif;
import net.sf.mbus4j.dataframes.datablocks.vif.ManufacturerSpecificVif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFB;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeError;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeStd;
import net.sf.mbus4j.decoder.Decoder.DecodeState;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class DecoderTest {

    public DecoderTest() {
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
     * Test of getVif method, of class Decoder.
     */
    @Test
    public void testGetVif() {
        System.out.println("getVif");
        List<Vif> list = new ArrayList<Vif>();
        for (Vif vif : VifStd.values()) {
            list.add(vif);
        }
        for (Vif vif : VifFB.values()) {
            list.add(vif);
        }
        for (Vif vif : VifFD.values()) {
            list.add(vif);
        }
        list.add(new AsciiVif("ASCII VIF"));
        list.add(new ManufacturerSpecificVif((byte)0x00));
        for (Vif vif : list) {
            Vif v = Decoder.getVif(vif.getLabel(), vif.getUnitOfMeasurement() == null ? null : vif.getUnitOfMeasurement().name(), vif.getSiPrefix() == null ? null : vif.getSiPrefix().name(), vif.getExponent());

            if (!vif.equals(v)) {
                System.out.println("V: " + vif.getClass().getName());
                System.out.println("V: " + v.getClass().getName());
                System.out.println("vif: " + vif.getLabel());
            }
            assertEquals(vif.getLabel() , vif, Decoder.getVif(vif.getLabel(), vif.getUnitOfMeasurement() == null ? null : vif.getUnitOfMeasurement().name(), vif.getSiPrefix() == null ? null : vif.getSiPrefix().name(), vif.getExponent()));
        }
    }

    /**
     * Test of getVife method, of class Decoder.
     */
    @Test
    public void testGetVife() {
        System.out.println("getVife");
        List<Vife> list = new ArrayList<Vife>();
        for (Vife vife : VifeError.values()) {
            list.add(vife);
        }
        for (Vife vife : VifeStd.values()) {
            list.add(vife);
        }
        //TODO VifeObjectAction ???
        for (Vife vife : list) {
            Vife v = Decoder.getVife(vife.getLabel());

            if (!vife.equals(v)) {
                System.out.println("V: " + vife.getClass().getName());
                System.out.println("V: " + vife.getClass().getName());
                System.out.println("vif: " + vife.getLabel());
            }
            assertEquals(vife.getLabel() , vife, Decoder.getVife(vife.getLabel()));
        }
    }

    /**
     * Test of getVife method, of class Decoder.
     */
    @Test
    public void testGetMedium() {
        System.out.println("getMedium");
        List<MBusMedium> list = new ArrayList<MBusMedium>();
        for (MBusMedium medium : MBusMedium.StdMedium.values()) {
            list.add(medium);
        }
        list.add(new MBusMedium.UnknownMBusMedium(0xF0));
        //TODO VifeObjectAction ???
        for (MBusMedium medium : list) {
            MBusMedium m = Decoder.getMBusMedium(medium.getLabel());

            if (!medium.equals(m)) {
                System.out.println("V: " + medium.getClass().getName());
                System.out.println("V: " + medium.getClass().getName());
                System.out.println("vif: " + medium.getLabel());
            }
            assertEquals(medium.getLabel() , medium, Decoder.getMBusMedium(medium.getLabel()));
        }
    }

}