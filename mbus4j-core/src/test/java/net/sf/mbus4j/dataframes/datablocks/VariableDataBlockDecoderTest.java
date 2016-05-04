package net.sf.mbus4j.dataframes.datablocks;

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
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.decoder.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author aploese
 */
public class VariableDataBlockDecoderTest {

    private VariableDataBlockDecoder instance;

    public VariableDataBlockDecoderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new VariableDataBlockDecoder();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    private void doTest(String hexData, String expected) {
        int bytesLeft = hexData.length() / 2;
        instance.init(new UserDataResponse());
        for (byte b : Decoder.ascii2Bytes(hexData)) {
            instance.addByte(b, --bytesLeft);
        }
        assertEquals(VariableDataBlockDecoder.DecodeState.RESULT_AVAIL, instance.getState());
        assertEquals(expected, instance.getDataBlock().toString());
    }

    @Test
    public void test_REACTIVE_ENERGY_KVARH_E_0__Import() {
        doTest("8610FB82730100000000000", "dataType = 48 Bit Integer\n"
                + "description = Reactive Energy Multiplicative correction factor 0.001\n"
                + "value = 1 * 0.001 [kVARh]\n"
                + "tariff = 1\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_REACTIVE_ENERGY_KVARH_E_0__Export() {
        doTest("8610FB82F33C0100000000000", "dataType = 48 Bit Integer\n"
                + "description = Reactive Energy Multiplicative correction factor 0.001 Accumulation of abs value only if negative contributions\n"
                + "value = 1 * 0.001 [kVARh]\n"
                + "tariff = 1\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_CYCLES_OF_EEPROM() {
        doTest("0CFC2000000000", "dataType = 8 digit BCD\n"
                + "description = Speicherzyklen im EEPROM\n"
                + "value = 00000000\ntariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_REACTIVE_POWER_VAR_E__0_PHASE_L1() {
        doTest("04FB94F5FC0100000000", "dataType = 32 Bit Integer\n"
                + "description = Reactive Power Multiplicative correction factor 0.1 Phase L1\n"
                + "value = 0 * 0.1 [VAR]\n"
                + "tariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_PHASE_U_I_E__1_DEGREE__PHASE_L1() {
        doTest("02FBABFC010100", "dataType = 16 Bit Integer\n"
                + "description = Phase U/I Phase L1\n"
                + "value = 1 * 0.1 [°]\n"
                + "tariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_COMPLEX_POWER() {
        doTest("04FBD4F5FC0101000000", "dataType = 32 Bit Integer\n"
                + "description = Complex Power Multiplicative correction factor 0.1 Phase L1\n"
                + "value = 1 * 0.1 * 0.1 [VA]\n"
                + "tariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n"
                + "");
    }

    @Test
    public void test_VOLTAGE_PHASE_L3_L1() {
        doTest("02FDC8FC070100", "dataType = 16 Bit Integer\n"
                + "description = Voltage Phase L3 - L1\n"
                + "value = 1 * 100 [mV]\n"
                + "tariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_FREQUENCY() {
        doTest("02FB2EF401", "dataType = 16 Bit Integer\n"
                + "description = Frequency\n"
                + "value = 500 * 0.1 [Hz]\n"
                + "tariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }

    @Test
    public void test_CURRENT_PLASE_L1() {
        doTest("03FDD9FC01010000", "dataType = 24 Bit Integer\n"
                + "description = Current Phase L1\n"
                + "value = 1 [mA]\n"
                + "tariff = 0\n"
                + "storageNumber = 0\n"
                + "functionField = Instantaneous value\n"
                + "subUnit = 0\n");
    }
    
    @Ignore
    @Test
    public void test_COS_PHI() {
        doTest("01FFE10101", "");
    }
    
    public void test_Template() {
        doTest("", "");
    }

}
