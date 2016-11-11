package net.sf.mbus4j.master;

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
import java.util.logging.Logger;
import net.sf.mbus4j.SlaveStreams;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.log.LogUtils;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class MasterTest {

    @BeforeClass
    public static void setUpClass()
            throws Exception {
    }

    @AfterClass
    public static void tearDownClass()
            throws Exception {
    }

    private static Logger log = LogUtils.getMasterLogger();
    private SlaveStreams slaves;
    private MBusMaster master;

    public MasterTest() {
    }

    @Before
    public void setUp()
            throws Exception {
        System.out.println("setUp");
        slaves = new SlaveStreams();
        master = new MBusMaster();
        master.setConnection(slaves);
        master.open();
    }

    @After
    public void tearDown()
            throws Exception {
        System.out.println("tearDown");
        if (master != null) {
            master.close();
        }
        master = null;
        if (slaves != null) {
            slaves.close();
        }
        slaves = null;
    }

    /*        @Test
     public void testTest() throws Exception {
     int value = 0x12FFFFFF;
     //slabveSelect(value);
     // wenn Ans == 1 return 1 gefunden
     // wenn Ans == 0 return nix gefunden
     int nibblePos;
     while ((nibblePos = getLeftmostMaskedNibble(value)) >= 0) {
     for (int i = 0; i <= 9; i++) {
     int res = exchangeNibbleAtPos(nibblePos, value, i);
     log.error(String.format("VALUE MASKED: %d 0x%08X", nibblePos, res));
     }
     return;
     }



     }
     */
    /**
     * Test of widcardSearch method, of class MBusMaster.
     */
    @Test//(timeout = 60000)
    //  @Ignore
    public void testWidcardSearch() throws Exception {
        System.out.println("widcardSearch");

        int bcdMaskedId = 0xFFFFFFFF;
        short bcdMaskedMan = (short) 0xFFFF;
        byte bcdMaskedVersion = (byte) 0xFF;
        byte bcdMaskedMedium = (byte) 0xFF;
        slaves.respondToRequest("680B0B6853FD52FFFFFFFFFFFFFFFF9A16", "E5E5");
        slaves.respondToRequest("680B0B6853FD52F0FFFFFFFFFFFFFF8B16", 3);
        slaves.respondToRequest("680B0B6853FD52F1FFFFFFFFFFFFFF8C16", "E5E5");
        slaves.respondToRequest("680B0B6853FD5201FFFFFFFFFFFFFF9C16", 3);
        slaves.respondToRequest("680B0B6853FD5211FFFFFFFFFFFFFFAC16", 3);
        slaves.respondToRequest("680B0B6853FD5221FFFFFFFFFFFFFFBC16", 3);
        slaves.respondToRequest("680B0B6853FD5231FFFFFFFFFFFFFFCC16", 3);
        slaves.respondToRequest("680B0B6853FD5241FFFFFFFFFFFFFFDC16", "E5E5");
        slaves.respondToRequest("680B0B6853FD5241F0FFFFFFFFFFFFCD16", 3);
        slaves.respondToRequest("680B0B6853FD5241F1FFFFFFFFFFFFCE16", 3);
        slaves.respondToRequest("680B0B6853FD5241F2FFFFFFFFFFFFCF16", 3);
        slaves.respondToRequest("680B0B6853FD5241F3FFFFFFFFFFFFD016", 3);
        slaves.respondToRequest("680B0B6853FD5241F4FFFFFFFFFFFFD116", "E5E5");
        slaves.respondToRequest("680B0B6853FD524104FFFFFFFFFFFFE116", 3);
        slaves.respondToRequest("680B0B6853FD524114FFFFFFFFFFFFF116", 3);
        slaves.respondToRequest("680B0B6853FD524124FFFFFFFFFFFF0116", 3);
        slaves.respondToRequest("680B0B6853FD524134FFFFFFFFFFFF1116", 3);
        slaves.respondToRequest("680B0B6853FD524144FFFFFFFFFFFF2116", 3);
        slaves.respondToRequest("680B0B6853FD524154FFFFFFFFFFFF3116", 3);
        slaves.respondToRequest("680B0B6853FD524164FFFFFFFFFFFF4116", 3);
        slaves.respondToRequest("680B0B6853FD524174FFFFFFFFFFFF5116", 3);
        slaves.respondToRequest("680B0B6853FD524184FFFFFFFFFFFF6116", 3);
        slaves.respondToRequest("680B0B6853FD524194FFFFFFFFFFFF7116", "E5E5");
        slaves.respondToRequest("680B0B6853FD524194F0FFFFFFFFFF6216", 3);
        slaves.respondToRequest("680B0B6853FD524194F1FFFFFFFFFF6316", "E5E5");
        slaves.respondToRequest("680B0B6853FD52419401FFFFFFFFFF7316", "E5E5");
        slaves.respondToRequest("680B0B6853FD52419401F0FFFFFFFF6416", "E5E5");
        slaves.respondToRequest("680B0B6853FD5241940100FFFFFFFF7416", 3);
        slaves.respondToRequest("680B0B6853FD5241940110FFFFFFFF8416", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F680801720110491457100106000000005716");
        slaves.respondToRequest("680B0B6853FD5241940120FFFFFFFF9416", 3);
        slaves.respondToRequest("680B0B6853FD5241940130FFFFFFFFA416", 3);
        slaves.respondToRequest("680B0B6853FD5241940140FFFFFFFFB416", 3);
        slaves.respondToRequest("680B0B6853FD5241940150FFFFFFFFC416", 3);
        slaves.respondToRequest("680B0B6853FD5241940160FFFFFFFFD416", 3);
        slaves.respondToRequest("680B0B6853FD5241940170FFFFFFFFE416", 3);
        slaves.respondToRequest("680B0B6853FD5241940180FFFFFFFFF416", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F68080172081049146745010600000000A316");
        slaves.respondToRequest("680B0B6853FD5241940190FFFFFFFF0416", 3);
        slaves.respondToRequest("680B0B6853FD52419401F1FFFFFFFF6516", 3);
        slaves.respondToRequest("680B0B6853FD52419401F2FFFFFFFF6616", 3);
        slaves.respondToRequest("680B0B6853FD52419401F3FFFFFFFF6716", 3);
        slaves.respondToRequest("680B0B6853FD52419401F4FFFFFFFF6816", 3);
        slaves.respondToRequest("680B0B6853FD52419401F5FFFFFFFF6916", 3);
        slaves.respondToRequest("680B0B6853FD52419401F6FFFFFFFF6A16", 3);
        slaves.respondToRequest("680B0B6853FD52419401F7FFFFFFFF6B16", 3);
        slaves.respondToRequest("680B0B6853FD52419401F8FFFFFFFF6C16", 3);
        slaves.respondToRequest("680B0B6853FD52419401F9FFFFFFFF6D16", 3);
        slaves.respondToRequest("680B0B6853FD52419411FFFFFFFFFF8316", 3);
        slaves.respondToRequest("680B0B6853FD52419421FFFFFFFFFF9316", 3);
        slaves.respondToRequest("680B0B6853FD52419431FFFFFFFFFFA316", 3);
        slaves.respondToRequest("680B0B6853FD52419441FFFFFFFFFFB316", 3);
        slaves.respondToRequest("680B0B6853FD52419451FFFFFFFFFFC316", 3);
        slaves.respondToRequest("680B0B6853FD52419461FFFFFFFFFFD316", 3);
        slaves.respondToRequest("680B0B6853FD52419471FFFFFFFFFFE316", 3);
        slaves.respondToRequest("680B0B6853FD52419481FFFFFFFFFFF316", 3);
        slaves.respondToRequest("680B0B6853FD52419491FFFFFFFFFF0316", 3);
        slaves.respondToRequest("680B0B6853FD524194F2FFFFFFFFFF6416", 3);
        slaves.respondToRequest("680B0B6853FD524194F3FFFFFFFFFF6516", 3);
        slaves.respondToRequest("680B0B6853FD524194F4FFFFFFFFFF6616", 3);
        slaves.respondToRequest("680B0B6853FD524194F5FFFFFFFFFF6716", 3);
        slaves.respondToRequest("680B0B6853FD524194F6FFFFFFFFFF6816", 3);
        slaves.respondToRequest("680B0B6853FD524194F7FFFFFFFFFF6916", 3);
        slaves.respondToRequest("680B0B6853FD524194F8FFFFFFFFFF6A16", 3);
        slaves.respondToRequest("680B0B6853FD524194F9FFFFFFFFFF6B16", 3);
        slaves.respondToRequest("680B0B6853FD5241F5FFFFFFFFFFFFD216", 3);
        slaves.respondToRequest("680B0B6853FD5241F6FFFFFFFFFFFFD316", 3);
        slaves.respondToRequest("680B0B6853FD5241F7FFFFFFFFFFFFD416", 3);
        slaves.respondToRequest("680B0B6853FD5241F8FFFFFFFFFFFFD516", 3);
        slaves.respondToRequest("680B0B6853FD5241F9FFFFFFFFFFFFD616", 3);
        slaves.respondToRequest("680B0B6853FD5251FFFFFFFFFFFFFFEC16", 3);
        slaves.respondToRequest("680B0B6853FD5261FFFFFFFFFFFFFFFC16", 3);
        slaves.respondToRequest("680B0B6853FD5271FFFFFFFFFFFFFF0C16", 3);
        slaves.respondToRequest("680B0B6853FD5281FFFFFFFFFFFFFF1C16", 3);
        slaves.respondToRequest("680B0B6853FD5291FFFFFFFFFFFFFF2C16", 3);
        slaves.respondToRequest("680B0B6853FD52F2FFFFFFFFFFFFFF8D16", 3);
        slaves.respondToRequest("680B0B6853FD52F3FFFFFFFFFFFFFF8E16", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F680801723348103210200102000000006B16");
        slaves.respondToRequest("680B0B6853FD52F4FFFFFFFFFFFFFF8F16", 3);
        slaves.respondToRequest("680B0B6853FD52F5FFFFFFFFFFFFFF9016", 3);
        slaves.respondToRequest("680B0B6853FD52F6FFFFFFFFFFFFFF9116", 3);
        slaves.respondToRequest("680B0B6853FD52F7FFFFFFFFFFFFFF9216", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F68080172103254761020010300000000BB16");
        slaves.respondToRequest("680B0B6853FD52F8FFFFFFFFFFFFFF9316", 3);
        slaves.respondToRequest("680B0B6853FD52F9FFFFFFFFFFFFFF9416", 3);
        slaves.replay();
        master.widcardSearch(bcdMaskedId, bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium);
        assertTrue(slaves.checkNoDataLeft());
        log.info("widcardSearch finished");
        assertEquals(4, master.deviceCount());
        
        assertEquals(1, master.getDevice(0).getAddress());
        assertEquals(14491001, master.getDevice(0).getIdentNumber());
        assertEquals("DBW", master.getDevice(0).getManufacturer());
        assertEquals(MBusMedium.HOT_WATER, master.getDevice(0).getMedium());
        assertEquals(1, master.getDevice(0).getVersion());
        assertEquals(1, master.getDevice(1).getAddress());
        assertEquals(14491008, master.getDevice(1).getIdentNumber());
        assertEquals("QKG", master.getDevice(1).getManufacturer());
        assertEquals(MBusMedium.HOT_WATER, master.getDevice(1).getMedium());
        assertEquals(1, master.getDevice(1).getVersion());
        assertEquals(1, master.getDevice(2).getAddress());
        assertEquals(32104833, master.getDevice(2).getIdentNumber());
        assertEquals("H@P", master.getDevice(2).getManufacturer());
        assertEquals(MBusMedium.ELECTRICITY, master.getDevice(2).getMedium());
        assertEquals(1, master.getDevice(2).getVersion());
        assertEquals(1, master.getDevice(3).getAddress());
        assertEquals(76543210, master.getDevice(3).getIdentNumber());
        assertEquals("H@P", master.getDevice(3).getManufacturer());
        assertEquals(MBusMedium.GAS, master.getDevice(3).getMedium());
        assertEquals(1, master.getDevice(3).getVersion());
    }

    @Test//(timeout = 60000)
    //  @Ignore
    public void testWidcardSearch_1()            throws Exception {
        System.out.println("widcardSearch");

        int bcdMaskedId = 0xFFFFFFFF;
        short bcdMaskedMan = (short) 0xFFFF;
        byte bcdMaskedVersion = (byte) 0xFF;
        byte bcdMaskedMedium = (byte) 0xFF;
        slaves.respondToRequest("680B0B6853FD52FFFFFFFFFFFFFFFF9A16", "E5E5");
        slaves.respondToRequest("680B0B6853FD52F0FFFFFFFFFFFFFF8B16", 3);
        slaves.respondToRequest("680B0B6853FD52F1FFFFFFFFFFFFFF8C16", "E5E5");
        slaves.respondToRequest("680B0B6853FD5201FFFFFFFFFFFFFF9C16", 3);
        slaves.respondToRequest("680B0B6853FD5211FFFFFFFFFFFFFFAC16", 3);
        slaves.respondToRequest("680B0B6853FD5221FFFFFFFFFFFFFFBC16", 3);
        slaves.respondToRequest("680B0B6853FD5231FFFFFFFFFFFFFFCC16", 3);
        slaves.respondToRequest("680B0B6853FD5241FFFFFFFFFFFFFFDC16", "E5E5");
        slaves.respondToRequest("680B0B6853FD5241F0FFFFFFFFFFFFCD16", 3);
        slaves.respondToRequest("680B0B6853FD5241F1FFFFFFFFFFFFCE16", 3);
        slaves.respondToRequest("680B0B6853FD5241F2FFFFFFFFFFFFCF16", 3);
        slaves.respondToRequest("680B0B6853FD5241F3FFFFFFFFFFFFD016", 3);
        slaves.respondToRequest("680B0B6853FD5241F4FFFFFFFFFFFFD116", "E5E5");
        slaves.respondToRequest("680B0B6853FD524104FFFFFFFFFFFFE116", 3);
        slaves.respondToRequest("680B0B6853FD524114FFFFFFFFFFFFF116", 3);
        slaves.respondToRequest("680B0B6853FD524124FFFFFFFFFFFF0116", 3);
        slaves.respondToRequest("680B0B6853FD524134FFFFFFFFFFFF1116", 3);
        slaves.respondToRequest("680B0B6853FD524144FFFFFFFFFFFF2116", 3);
        slaves.respondToRequest("680B0B6853FD524154FFFFFFFFFFFF3116", 3);
        slaves.respondToRequest("680B0B6853FD524164FFFFFFFFFFFF4116", 3);
        slaves.respondToRequest("680B0B6853FD524174FFFFFFFFFFFF5116", 3);
        slaves.respondToRequest("680B0B6853FD524184FFFFFFFFFFFF6116", 3);
        slaves.respondToRequest("680B0B6853FD524194FFFFFFFFFFFF7116", "E5E5");
        slaves.respondToRequest("680B0B6853FD524194F0FFFFFFFFFF6216", 3);
        slaves.respondToRequest("680B0B6853FD524194F1FFFFFFFFFF6316", "E5E5");
        slaves.respondToRequest("680B0B6853FD52419401FFFFFFFFFF7316", "E5E5");
        slaves.respondToRequest("680B0B6853FD52419401F0FFFFFFFF6416", "E5E5");
        slaves.respondToRequest("680B0B6853FD5241940100FFFFFFFF7416", 3);
        slaves.respondToRequest("680B0B6853FD5241940110FFFFFFFF8416", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F680801720110491457100106000000005716");
        slaves.respondToRequest("680B0B6853FD5241940120FFFFFFFF9416", 3);
        slaves.respondToRequest("680B0B6853FD5241940130FFFFFFFFA416", 3);
        slaves.respondToRequest("680B0B6853FD5241940140FFFFFFFFB416", 3);
        slaves.respondToRequest("680B0B6853FD5241940150FFFFFFFFC416", 3);
        slaves.respondToRequest("680B0B6853FD5241940160FFFFFFFFD416", 3);
        slaves.respondToRequest("680B0B6853FD5241940170FFFFFFFFE416", 3);
        slaves.respondToRequest("680B0B6853FD5241940180FFFFFFFFF416", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F68080172081049146745010600000000A316");
        slaves.respondToRequest("680B0B6853FD5241940190FFFFFFFF0416", 3);
        slaves.respondToRequest("680B0B6853FD52419401F1FFFFFFFF6516", 3);
        slaves.respondToRequest("680B0B6853FD52419401F2FFFFFFFF6616", 3);
        slaves.respondToRequest("680B0B6853FD52419401F3FFFFFFFF6716", 3);
        slaves.respondToRequest("680B0B6853FD52419401F4FFFFFFFF6816", 3);
        slaves.respondToRequest("680B0B6853FD52419401F5FFFFFFFF6916", 3);
        slaves.respondToRequest("680B0B6853FD52419401F6FFFFFFFF6A16", 3);
        slaves.respondToRequest("680B0B6853FD52419401F7FFFFFFFF6B16", 3);
        slaves.respondToRequest("680B0B6853FD52419401F8FFFFFFFF6C16", 3);
        slaves.respondToRequest("680B0B6853FD52419401F9FFFFFFFF6D16", 3);
        slaves.respondToRequest("680B0B6853FD52419411FFFFFFFFFF8316", 3);
        slaves.respondToRequest("680B0B6853FD52419421FFFFFFFFFF9316", 3);
        slaves.respondToRequest("680B0B6853FD52419431FFFFFFFFFFA316", 3);
        slaves.respondToRequest("680B0B6853FD52419441FFFFFFFFFFB316", 3);
        slaves.respondToRequest("680B0B6853FD52419451FFFFFFFFFFC316", 3);
        slaves.respondToRequest("680B0B6853FD52419461FFFFFFFFFFD316", 3);
        slaves.respondToRequest("680B0B6853FD52419471FFFFFFFFFFE316", 3);
        slaves.respondToRequest("680B0B6853FD52419481FFFFFFFFFFF316", 3);
        slaves.respondToRequest("680B0B6853FD52419491FFFFFFFFFF0316", 3);
        slaves.respondToRequest("680B0B6853FD524194F2FFFFFFFFFF6416", 3);
        slaves.respondToRequest("680B0B6853FD524194F3FFFFFFFFFF6516", 3);
        slaves.respondToRequest("680B0B6853FD524194F4FFFFFFFFFF6616", 3);
        slaves.respondToRequest("680B0B6853FD524194F5FFFFFFFFFF6716", 3);
        slaves.respondToRequest("680B0B6853FD524194F6FFFFFFFFFF6816", 3);
        slaves.respondToRequest("680B0B6853FD524194F7FFFFFFFFFF6916", 3);
        slaves.respondToRequest("680B0B6853FD524194F8FFFFFFFFFF6A16", 3);
        slaves.respondToRequest("680B0B6853FD524194F9FFFFFFFFFF6B16", 3);
        slaves.respondToRequest("680B0B6853FD5241F5FFFFFFFFFFFFD216", 3);
        slaves.respondToRequest("680B0B6853FD5241F6FFFFFFFFFFFFD316", 3);
        slaves.respondToRequest("680B0B6853FD5241F7FFFFFFFFFFFFD416", 3);
        slaves.respondToRequest("680B0B6853FD5241F8FFFFFFFFFFFFD516", 3);
        slaves.respondToRequest("680B0B6853FD5241F9FFFFFFFFFFFFD616", 3);
        slaves.respondToRequest("680B0B6853FD5251FFFFFFFFFFFFFFEC16", 3);
        slaves.respondToRequest("680B0B6853FD5261FFFFFFFFFFFFFFFC16", 3);
        slaves.respondToRequest("680B0B6853FD5271FFFFFFFFFFFFFF0C16", 3);
        slaves.respondToRequest("680B0B6853FD5281FFFFFFFFFFFFFF1C16", 3);
        slaves.respondToRequest("680B0B6853FD5291FFFFFFFFFFFFFF2C16", 3);
        slaves.respondToRequest("680B0B6853FD52F2FFFFFFFFFFFFFF8D16", 3);
        slaves.respondToRequest("680B0B6853FD52F3FFFFFFFFFFFFFF8E16", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F680801723348103210200102000000006B16");
        slaves.respondToRequest("680B0B6853FD52F4FFFFFFFFFFFFFF8F16", 3);
        slaves.respondToRequest("680B0B6853FD52F5FFFFFFFFFFFFFF9016", 3);
        slaves.respondToRequest("680B0B6853FD52F6FFFFFFFFFFFFFF9116", 3);
        slaves.respondToRequest("680B0B6853FD52F7FFFFFFFFFFFFFF9216", "E5");
        slaves.respondToRequest("107BFD7816", "680F0F68080172103254761020010300000000BB16");
        slaves.respondToRequest("680B0B6853FD52F8FFFFFFFFFFFFFF9316", 3);
        slaves.respondToRequest("680B0B6853FD52F9FFFFFFFFFFFFFF9416", 3);
        slaves.replay();
        master.widcardSearch(bcdMaskedId, bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium);
        assertTrue(slaves.checkNoDataLeft());
        log.info("widcardSearch finished");
        assertEquals(4, master.deviceCount());
        
        assertEquals(1, master.getDevice(0).getAddress());
        assertEquals(14491001, master.getDevice(0).getIdentNumber());
        assertEquals("DBW", master.getDevice(0).getManufacturer());
        assertEquals(MBusMedium.HOT_WATER, master.getDevice(0).getMedium());
        assertEquals(1, master.getDevice(0).getVersion());
        assertEquals(1, master.getDevice(1).getAddress());
        assertEquals(14491008, master.getDevice(1).getIdentNumber());
        assertEquals("QKG", master.getDevice(1).getManufacturer());
        assertEquals(MBusMedium.HOT_WATER, master.getDevice(1).getMedium());
        assertEquals(1, master.getDevice(1).getVersion());
        assertEquals(1, master.getDevice(2).getAddress());
        assertEquals(32104833, master.getDevice(2).getIdentNumber());
        assertEquals("H@P", master.getDevice(2).getManufacturer());
        assertEquals(MBusMedium.ELECTRICITY, master.getDevice(2).getMedium());
        assertEquals(1, master.getDevice(2).getVersion());
        assertEquals(1, master.getDevice(3).getAddress());
        assertEquals(76543210, master.getDevice(3).getIdentNumber());
        assertEquals("H@P", master.getDevice(3).getManufacturer());
        assertEquals(MBusMedium.GAS, master.getDevice(3).getMedium());
        assertEquals(1, master.getDevice(3).getVersion());
    }

    @Test
    @Ignore
    public void testStdResp()
            throws Exception {
        /*
         System.out.println("testStdResp");
         slaves.respondToRequest("105B005B16", "6847476808007225543699824D0316B03800000C78255436990D7C084449202E747375630A36373031303741543939046D160F3C080413B601000004937F1F0000004413B50100000F1C0CF016");
         slaves.respondToRequest("105B005B16", "6847476808007225543699824D0316B03800000C78255436990D7C084449202E747375630A36373031303741543939046D160F3C080413B601000004937F1F0000004413B50100000F1C0CF016");
         slaves.replay();
         master.searchDevicesByPrimaryAddress(0, 0);
         MBusResponseFramesContainer device = master.getDevice(0);
         Map<ResponseFrame, Map<DataTag, DataBlock>> map = device.readValues(master);
         assertTrue(slaves.isOK());
         assertEquals(1, device.getResponseFrameCount());
         ResponseFrame resp = device.getResponseFrame(0);
         Map<DataTag, DataBlock> valueMap = map.get(resp);
         assertEquals(7, resp.getDataBlockCount());
         for (DataTag dt : resp) {
         DataBlock db = valueMap.get(dt);
         log.info("DATATAG: " + dt.toString());
         log.info("DATABLOCK: " + db.toString());

         assertEquals(dt.getVif(), db.getVif());
         assertEquals(dt.getDeviceUnit(), db.getSubUnit());
         assertEquals(dt.getDifCode(), db.getDataFieldCode());
         assertEquals(dt.getFunctionField(), db.getFunctionField());
         assertEquals(dt.getStorageNumber(), db.getStorageNumber());
         assertEquals(dt.getTariff(), db.getTariff());
         if ((dt.getVifes() != null) && (db.getVifes() != null)){
         org.junit.Assert.assertArrayEquals(dt.getVifes(), db.getVifes().toArray(new Vife[0]));
         } else {
         assertEquals(dt.getVifes(), db.getVifes());
         }

         }
         Set<DataBlock> result =  new HashSet<DataBlock>(valueMap.values());
         for (DataBlock db : result) {
         log.info("RESULT: " + db.toString());
         }
         assertEquals("RESULT SIZE", 7, result.size());
         */
    }

}
