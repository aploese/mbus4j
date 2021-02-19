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
