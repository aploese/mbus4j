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
import net.sf.mbus4j.devices.Sender;
import net.sf.mbus4j.log.LogUtils;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    @Test(timeout = 60000)
    @Ignore
    public void testWidcardSearch()
            throws Exception {
        System.out.println("widcardSearch");

        int bcdMaskedId = 0xFFFFFFFF;
        short bcdMaskedMan = (short) 0xFFFF;
        byte bcdMaskedVersion = (byte) 0xFF;
        byte bcdMaskedMedium = (byte) 0xFF;
        slaves.respondToRequest("680B0B6853FD52FFFFFFFFFFFFFFFF9A16", "E5E5");
        slaves.respondToRequest("680B0B6853FD52FFFFFF0FFFFFFFFFAA16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF1FFFFFFFFFBA16", "E5E5");
        slaves.respondToRequest("680B0B6853FD52FFFFFF10FFFFFFFFAB16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF11FFFFFFFFAC16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF12FFFFFFFFAD16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF13FFFFFFFFAE16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF14FFFFFFFFAF16", "E5E5");
        slaves.respondToRequest("680B0B6853FD52FFFF0F14FFFFFFFFBF16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF1F14FFFFFFFFCF16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF2F14FFFFFFFFDF16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF3F14FFFFFFFFEF16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4F14FFFFFFFFFF16", "E5E5");
        slaves.respondToRequest("680B0B6853FD52FFFF4014FFFFFFFFF016", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4114FFFFFFFFF116", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4214FFFFFFFFF216", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4314FFFFFFFFF316", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4414FFFFFFFFF416", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4514FFFFFFFFF516", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4614FFFFFFFFF616", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4714FFFFFFFFF716", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4814FFFFFFFFF816", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF4914FFFFFFFFF916", "E5E5");
        slaves.respondToRequest("680B0B6853FD52FF0F4914FFFFFFFF0916", 3);
        slaves.respondToRequest("680B0B6853FD52FF1F4914FFFFFFFF1916", "E5E5");
        slaves.respondToRequest("680B0B6853FD52FF104914FFFFFFFF0A16", "E5E5");
        slaves.respondToRequest("680B0B6853FD520F104914FFFFFFFF1A16", "E5E5");
        slaves.respondToRequest("680B0B6853FD5200104914FFFFFFFF0B16", 3);
        slaves.respondToRequest("680B0B6853FD5201104914FFFFFFFF0C16", "E5");
        slaves.respondToRequest("105BFD5816", "680F0F680801720110491457100106000000005716");
        slaves.respondToRequest("680B0B6853FD5202104914FFFFFFFF0D16", 3);
        slaves.respondToRequest("680B0B6853FD5203104914FFFFFFFF0E16", 3);
        slaves.respondToRequest("680B0B6853FD5204104914FFFFFFFF0F16", 3);
        slaves.respondToRequest("680B0B6853FD5205104914FFFFFFFF1016", 3);
        slaves.respondToRequest("680B0B6853FD5206104914FFFFFFFF1116", 3);
        slaves.respondToRequest("680B0B6853FD5207104914FFFFFFFF1216", 3);
        slaves.respondToRequest("680B0B6853FD5208104914FFFFFFFF1316", "E5");
        slaves.respondToRequest("105BFD5816", "680F0F68080172081049146745010600000000A316");
        slaves.respondToRequest("680B0B6853FD5209104914FFFFFFFF1416", 3);
        slaves.respondToRequest("680B0B6853FD521F104914FFFFFFFF2A16", 3);
        slaves.respondToRequest("680B0B6853FD522F104914FFFFFFFF3A16", 3);
        slaves.respondToRequest("680B0B6853FD523F104914FFFFFFFF4A16", 3);
        slaves.respondToRequest("680B0B6853FD524F104914FFFFFFFF5A16", 3);
        slaves.respondToRequest("680B0B6853FD525F104914FFFFFFFF6A16", 3);
        slaves.respondToRequest("680B0B6853FD526F104914FFFFFFFF7A16", 3);
        slaves.respondToRequest("680B0B6853FD527F104914FFFFFFFF8A16", 3);
        slaves.respondToRequest("680B0B6853FD528F104914FFFFFFFF9A16", 3);
        slaves.respondToRequest("680B0B6853FD529F104914FFFFFFFFAA16", 3);
        slaves.respondToRequest("680B0B6853FD52FF114914FFFFFFFF0B16", 3);
        slaves.respondToRequest("680B0B6853FD52FF124914FFFFFFFF0C16", 3);
        slaves.respondToRequest("680B0B6853FD52FF134914FFFFFFFF0D16", 3);
        slaves.respondToRequest("680B0B6853FD52FF144914FFFFFFFF0E16", 3);
        slaves.respondToRequest("680B0B6853FD52FF154914FFFFFFFF0F16", 3);
        slaves.respondToRequest("680B0B6853FD52FF164914FFFFFFFF1016", 3);
        slaves.respondToRequest("680B0B6853FD52FF174914FFFFFFFF1116", 3);
        slaves.respondToRequest("680B0B6853FD52FF184914FFFFFFFF1216", 3);
        slaves.respondToRequest("680B0B6853FD52FF194914FFFFFFFF1316", 3);
        slaves.respondToRequest("680B0B6853FD52FF2F4914FFFFFFFF2916", 3);
        slaves.respondToRequest("680B0B6853FD52FF3F4914FFFFFFFF3916", 3);
        slaves.respondToRequest("680B0B6853FD52FF4F4914FFFFFFFF4916", 3);
        slaves.respondToRequest("680B0B6853FD52FF5F4914FFFFFFFF5916", 3);
        slaves.respondToRequest("680B0B6853FD52FF6F4914FFFFFFFF6916", 3);
        slaves.respondToRequest("680B0B6853FD52FF7F4914FFFFFFFF7916", 3);
        slaves.respondToRequest("680B0B6853FD52FF8F4914FFFFFFFF8916", 3);
        slaves.respondToRequest("680B0B6853FD52FF9F4914FFFFFFFF9916", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF5F14FFFFFFFF0F16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF6F14FFFFFFFF1F16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF7F14FFFFFFFF2F16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF8F14FFFFFFFF3F16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFF9F14FFFFFFFF4F16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF15FFFFFFFFB016", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF16FFFFFFFFB116", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF17FFFFFFFFB216", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF18FFFFFFFFB316", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF19FFFFFFFFB416", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF2FFFFFFFFFCA16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF3FFFFFFFFFDA16", "E5");
        slaves.respondToRequest("105BFD5816", "680F0F680801723348103210200102000000006B16");
        slaves.respondToRequest("680B0B6853FD52FFFFFF4FFFFFFFFFEA16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF5FFFFFFFFFFA16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF6FFFFFFFFF0A16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF7FFFFFFFFF1A16", "E5");
        slaves.respondToRequest("105BFD5816", "680F0F68080172103254761020010300000000BB16");
        slaves.respondToRequest("680B0B6853FD52FFFFFF8FFFFFFFFF2A16", 3);
        slaves.respondToRequest("680B0B6853FD52FFFFFF9FFFFFFFFF3A16", 3);
        slaves.replay();
        master.widcardSearch(bcdMaskedId, bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium, Sender.DEFAULT_SEND_TRIES);
        assertTrue(slaves.isOK());
        log.info("widcardSearch finished");
        assertEquals(4,
                master.deviceCount());
        assertEquals(1,
                master.getDevice(0).getAddress());
        assertEquals(14491001,
                master.getDevice(0).getIdentNumber());
        assertEquals("DBW",
                master.getDevice(0).getManufacturer());
        assertEquals(MBusMedium.HOT_WATER,
                master.getDevice(0).getMedium());
        assertEquals(1,
                master.getDevice(0).getVersion());
        assertEquals(1,
                master.getDevice(1).getAddress());
        assertEquals(14491008,
                master.getDevice(1).getIdentNumber());
        assertEquals("QKG",
                master.getDevice(1).getManufacturer());
        assertEquals(MBusMedium.HOT_WATER,
                master.getDevice(1).getMedium());
        assertEquals(1,
                master.getDevice(1).getVersion());
        assertEquals(1,
                master.getDevice(2).getAddress());
        assertEquals(32104833,
                master.getDevice(2).getIdentNumber());
        assertEquals("H@P",
                master.getDevice(2).getManufacturer());
        assertEquals(MBusMedium.ELECTRICITY,
                master.getDevice(2).getMedium());
        assertEquals(1,
                master.getDevice(2).getVersion());
        assertEquals(1,
                master.getDevice(3).getAddress());
        assertEquals(76543210,
                master.getDevice(3).getIdentNumber());
        assertEquals("H@P",
                master.getDevice(3).getManufacturer());
        assertEquals(MBusMedium.GAS,
                master.getDevice(3).getMedium());
        assertEquals(1,
                master.getDevice(3).getVersion());
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
