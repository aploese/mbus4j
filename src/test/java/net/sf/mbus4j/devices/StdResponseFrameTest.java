/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009  Arne Plöse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.devices;

import java.util.Iterator;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import static net.sf.mbus4j.dataframes.datablocks.dif.FunctionField.*;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode.*;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.*;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifeStd.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class StdResponseFrameTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    ResponseFrame instance;

    public StdResponseFrameTest() {
    }

    @Before
    public void setUp() {
        instance = new ResponseFrame() {

            @Override
            public Iterable<DataBlock> getDataBlocksIterable() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getDataBlockCount() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Frame[] getinitFrames() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public DataBlock getDataBlock(int i) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Iterator<DataBlock> iterator() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addTag method, of class StdResponse.
     */
    @Test
    @Ignore
    public void testDateForInResponse() throws Exception {
/*        ResponseFrame rf = new ResponseFrame();
        rf.addTag(_8_DIGIT_BCD, FABRICATION_NO);
        rf.addTag(_16_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, 1, TIMEPOINT_DATE);
        rf.addTag(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, 1, ENERGY_KILO_WH_E_0);
        rf.addTag(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, 0, 0, 1, VOLUME_L_E_1);
        rf.addTag(_32_BIT_INTEGER, MAXIMUM_VALUE, 0,0,1, POWER_KILO_W_E_0, TIMESTAMP_END_LAST_UPPER);
        rf.addTag(_32_BIT_REAL, MAXIMUM_VALUE, 0,0,1, POWER_KILO_W_E_0);
        rf.addTag(_32_BIT_INTEGER, MAXIMUM_VALUE, 0,0,1, VOLUME_FLOW_CBM_PER_H_E_0, TIMESTAMP_END_LAST_UPPER);
        rf.addTag(_32_BIT_REAL, MAXIMUM_VALUE, 0,0,1, VOLUME_FLOW_CBM_PER_H_E_0);
        rf.addTag(_32_BIT_INTEGER, MAXIMUM_VALUE, 0,0,1, FLOW_TEMPERATURE_C_E_0, TIMESTAMP_END_LAST_UPPER);
        rf.addTag(_32_BIT_REAL, MAXIMUM_VALUE, 0,0,1, FLOW_TEMPERATURE_C_E_0);
        assertNull(rf.getTimeStampDtOf(rf.getTag(0)));
        assertNull(rf.getTimeStampDtOf(rf.getTag(1)));
        assertEquals(rf.getTag(1), rf.getTimeStampDtOf(rf.getTag(2)));
        assertEquals(rf.getTag(1), rf.getTimeStampDtOf(rf.getTag(3)));
        assertNull(rf.getTimeStampDtOf(rf.getTag(4)));
        assertEquals(rf.getTag(4), rf.getTimeStampDtOf(rf.getTag(5)));
        assertNull(rf.getTimeStampDtOf(rf.getTag(6)));
        assertEquals(rf.getTag(6), rf.getTimeStampDtOf(rf.getTag(7)));
        assertNull(rf.getTimeStampDtOf(rf.getTag(8)));
        assertEquals(rf.getTag(8), rf.getTimeStampDtOf(rf.getTag(9)));
*/    }

}
