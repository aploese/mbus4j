/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFB;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.dataframes.datablocks.vif.VifePrimary;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class DataBlockTest {

    public DataBlockTest() {
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
