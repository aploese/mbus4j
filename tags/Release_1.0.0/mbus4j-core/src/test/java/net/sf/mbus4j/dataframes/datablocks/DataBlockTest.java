/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes.datablocks;

import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.dif.VariableLengthType;
import net.sf.mbus4j.dataframes.datablocks.vif.ObjectAction;
import net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix;
import net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFB;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.dataframes.datablocks.vif.VifTypes;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.dataframes.datablocks.vif.VifePrimary;
import net.sf.mbus4j.json.JsonSerializeType;
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
public class DataBlockTest {
    
    public DataBlockTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCorrectionFactor method, of class DataBlock.
     */
    @Test
    public void testGetCorrectionExponent() {
        System.out.println("getCorrectionFactor");
        IntegerDataBlock instance = new IntegerDataBlock(DataFieldCode._32_BIT_INTEGER, VifPrimary.POWER_MILLI_W_E_2);
        instance.setValue(10000);
        int exponent = instance.getCorrectionExponent(SiPrefix.KILO);
        assertEquals(-4, exponent);
        assertEquals(1.0, Math.pow(10, exponent) * instance.getValue(), 0.0);
    }
    
    @Test
    public void testGetCorrectionExponentWithVife() {
        System.out.println("getCorrectionFactor");
        LongDataBlock instance = new LongDataBlock();
        instance.setDataFieldCode(DataFieldCode._48_BIT_INTEGER);
        instance.setVif(VifFB.REACTIVE_ENERGY_KVARH_E_0);
        instance.addVife(VifePrimary.FACTOR_E__3);
        
        instance.setValue(1);
        int exponent = instance.getCorrectionExponent(SiPrefix.KILO);
        assertEquals(-3, exponent);
        
        instance.addVife(VifePrimary.CONST_E_0);
        double corrConst = instance.getCorrectionConstant();
        assertEquals(1.0, corrConst, 0.0);
        
    }
    
}
