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
import java.util.LinkedList;
import java.util.logging.Logger;

import net.sf.mbus4j.MockSerialPortSocket;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
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
    private MockSerialPortSocket slaves;
    private MBusMaster master;

    public MasterTest() {
    }

    @Before
    public void setUp()
            throws Exception {
        System.out.println("setUp");
        slaves = new MockSerialPortSocket();
        master = new MBusMaster();
        master.setSerialPort(slaves);
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
        slaves.addRequest("680B0B6853FD52FFFFFFFFFFFFFFFF9A16", "E5E5");
        slaves.addRequest("680B0B6853FD52F0FFFFFFFFFFFFFF8B16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F1FFFFFFFFFFFFFF8C16", "E5E5");
        slaves.addRequest("680B0B6853FD5201FFFFFFFFFFFFFF9C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5211FFFFFFFFFFFFFFAC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5221FFFFFFFFFFFFFFBC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5231FFFFFFFFFFFFFFCC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241FFFFFFFFFFFFFFDC16", "E5E5");
        slaves.addRequest("680B0B6853FD5241F0FFFFFFFFFFFFCD16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F1FFFFFFFFFFFFCE16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F2FFFFFFFFFFFFCF16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F3FFFFFFFFFFFFD016", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F4FFFFFFFFFFFFD116", "E5E5");
        slaves.addRequest("680B0B6853FD524104FFFFFFFFFFFFE116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524114FFFFFFFFFFFFF116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524124FFFFFFFFFFFF0116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524134FFFFFFFFFFFF1116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524144FFFFFFFFFFFF2116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524154FFFFFFFFFFFF3116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524164FFFFFFFFFFFF4116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524174FFFFFFFFFFFF5116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524184FFFFFFFFFFFF6116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194FFFFFFFFFFFF7116", "E5E5");
        slaves.addRequest("680B0B6853FD524194F0FFFFFFFFFF6216", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F1FFFFFFFFFF6316", "E5E5");
        slaves.addRequest("680B0B6853FD52419401FFFFFFFFFF7316", "E5E5");
        slaves.addRequest("680B0B6853FD52419401F0FFFFFFFF6416", "E5E5");
        slaves.addRequest("680B0B6853FD5241940100FFFFFFFF7416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940110FFFFFFFF8416", "E5");
        slaves.addRequest("107BFD7816", "680F0F680801720110491457100106000000005716");
        slaves.addRequest("680B0B6853FD5241940120FFFFFFFF9416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940130FFFFFFFFA416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940140FFFFFFFFB416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940150FFFFFFFFC416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940160FFFFFFFFD416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940170FFFFFFFFE416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940180FFFFFFFFF416", "E5");
        slaves.addRequest("107BFD7816", "680F0F68080172081049146745010600000000A316");
        slaves.addRequest("680B0B6853FD5241940190FFFFFFFF0416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F1FFFFFFFF6516", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F2FFFFFFFF6616", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F3FFFFFFFF6716", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F4FFFFFFFF6816", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F5FFFFFFFF6916", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F6FFFFFFFF6A16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F7FFFFFFFF6B16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F8FFFFFFFF6C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F9FFFFFFFF6D16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419411FFFFFFFFFF8316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419421FFFFFFFFFF9316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419431FFFFFFFFFFA316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419441FFFFFFFFFFB316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419451FFFFFFFFFFC316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419461FFFFFFFFFFD316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419471FFFFFFFFFFE316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419481FFFFFFFFFFF316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419491FFFFFFFFFF0316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F2FFFFFFFFFF6416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F3FFFFFFFFFF6516", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F4FFFFFFFFFF6616", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F5FFFFFFFFFF6716", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F6FFFFFFFFFF6816", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F7FFFFFFFFFF6916", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F8FFFFFFFFFF6A16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F9FFFFFFFFFF6B16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F5FFFFFFFFFFFFD216", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F6FFFFFFFFFFFFD316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F7FFFFFFFFFFFFD416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F8FFFFFFFFFFFFD516", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F9FFFFFFFFFFFFD616", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5251FFFFFFFFFFFFFFEC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5261FFFFFFFFFFFFFFFC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5271FFFFFFFFFFFFFF0C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5281FFFFFFFFFFFFFF1C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5291FFFFFFFFFFFFFF2C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F2FFFFFFFFFFFFFF8D16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F3FFFFFFFFFFFFFF8E16", "E5");
        slaves.addRequest("107BFD7816", "680F0F680801723348103210200102000000006B16");
        slaves.addRequest("680B0B6853FD52F4FFFFFFFFFFFFFF8F16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F5FFFFFFFFFFFFFF9016", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F6FFFFFFFFFFFFFF9116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F7FFFFFFFFFFFFFF9216", "E5");
        slaves.addRequest("107BFD7816", "680F0F68080172103254761020010300000000BB16");
        slaves.addRequest("680B0B6853FD52F8FFFFFFFFFFFFFF9316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F9FFFFFFFFFFFFFF9416", master.getSendReTries());
        
        final LinkedList<DeviceId> deviceIds = new LinkedList<>();
        
        master.widcardSearch(bcdMaskedId, bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium, (deviceId) -> {
                deviceIds.add(deviceId);
        });

        log.info("widcardSearch finished");
        assertTrue(slaves.allRequestsHandled());
        assertEquals(4, deviceIds.size());
        
        assertEquals(1, deviceIds.get(0).address);
        assertEquals(14491001, deviceIds.get(0).identNumber);
        assertEquals("DBW", deviceIds.get(0).manufacturer);
        assertEquals(MBusMedium.HOT_WATER, deviceIds.get(0).medium);
        assertEquals(1, deviceIds.get(0).version);
        assertEquals(1, deviceIds.get(1).address);
        assertEquals(14491008, deviceIds.get(1).identNumber);
        assertEquals("QKG", deviceIds.get(1).manufacturer);
        assertEquals(MBusMedium.HOT_WATER, deviceIds.get(1).medium);
        assertEquals(1, deviceIds.get(1).version);
        assertEquals(1, deviceIds.get(2).address);
        assertEquals(32104833, deviceIds.get(2).identNumber);
        assertEquals("H@P", deviceIds.get(2).manufacturer);
        assertEquals(MBusMedium.ELECTRICITY, deviceIds.get(2).medium);
        assertEquals(1, deviceIds.get(2).version);
        assertEquals(1, deviceIds.get(3).address);
        assertEquals(76543210, deviceIds.get(3).identNumber);
        assertEquals("H@P", deviceIds.get(3).manufacturer);
        assertEquals(MBusMedium.GAS, deviceIds.get(3).medium);
        assertEquals(1, deviceIds.get(3).version);
    }

    @Test//(timeout = 60000)
    //  @Ignore
    public void testWidcardSearch_1()            throws Exception {
        System.out.println("widcardSearch");

        int bcdMaskedId = 0xFFFFFFFF;
        short bcdMaskedMan = (short) 0xFFFF;
        byte bcdMaskedVersion = (byte) 0xFF;
        byte bcdMaskedMedium = (byte) 0xFF;
        slaves.addRequest("680B0B6853FD52FFFFFFFFFFFFFFFF9A16", "E5E5");
        slaves.addRequest("680B0B6853FD52F0FFFFFFFFFFFFFF8B16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F1FFFFFFFFFFFFFF8C16", "E5E5");
        slaves.addRequest("680B0B6853FD5201FFFFFFFFFFFFFF9C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5211FFFFFFFFFFFFFFAC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5221FFFFFFFFFFFFFFBC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5231FFFFFFFFFFFFFFCC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241FFFFFFFFFFFFFFDC16", "E5E5");
        slaves.addRequest("680B0B6853FD5241F0FFFFFFFFFFFFCD16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F1FFFFFFFFFFFFCE16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F2FFFFFFFFFFFFCF16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F3FFFFFFFFFFFFD016", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F4FFFFFFFFFFFFD116", "E5E5");
        slaves.addRequest("680B0B6853FD524104FFFFFFFFFFFFE116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524114FFFFFFFFFFFFF116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524124FFFFFFFFFFFF0116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524134FFFFFFFFFFFF1116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524144FFFFFFFFFFFF2116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524154FFFFFFFFFFFF3116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524164FFFFFFFFFFFF4116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524174FFFFFFFFFFFF5116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524184FFFFFFFFFFFF6116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194FFFFFFFFFFFF7116", "E5E5");
        slaves.addRequest("680B0B6853FD524194F0FFFFFFFFFF6216", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F1FFFFFFFFFF6316", "E5E5");
        slaves.addRequest("680B0B6853FD52419401FFFFFFFFFF7316", "E5E5");
        slaves.addRequest("680B0B6853FD52419401F0FFFFFFFF6416", "E5E5");
        slaves.addRequest("680B0B6853FD5241940100FFFFFFFF7416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940110FFFFFFFF8416", "E5");
        slaves.addRequest("107BFD7816", "680F0F680801720110491457100106000000005716");
        slaves.addRequest("680B0B6853FD5241940120FFFFFFFF9416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940130FFFFFFFFA416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940140FFFFFFFFB416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940150FFFFFFFFC416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940160FFFFFFFFD416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940170FFFFFFFFE416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241940180FFFFFFFFF416", "E5");
        slaves.addRequest("107BFD7816", "680F0F68080172081049146745010600000000A316");
        slaves.addRequest("680B0B6853FD5241940190FFFFFFFF0416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F1FFFFFFFF6516", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F2FFFFFFFF6616", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F3FFFFFFFF6716", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F4FFFFFFFF6816", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F5FFFFFFFF6916", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F6FFFFFFFF6A16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F7FFFFFFFF6B16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F8FFFFFFFF6C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419401F9FFFFFFFF6D16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419411FFFFFFFFFF8316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419421FFFFFFFFFF9316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419431FFFFFFFFFFA316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419441FFFFFFFFFFB316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419451FFFFFFFFFFC316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419461FFFFFFFFFFD316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419471FFFFFFFFFFE316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419481FFFFFFFFFFF316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52419491FFFFFFFFFF0316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F2FFFFFFFFFF6416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F3FFFFFFFFFF6516", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F4FFFFFFFFFF6616", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F5FFFFFFFFFF6716", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F6FFFFFFFFFF6816", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F7FFFFFFFFFF6916", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F8FFFFFFFFFF6A16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD524194F9FFFFFFFFFF6B16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F5FFFFFFFFFFFFD216", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F6FFFFFFFFFFFFD316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F7FFFFFFFFFFFFD416", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F8FFFFFFFFFFFFD516", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5241F9FFFFFFFFFFFFD616", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5251FFFFFFFFFFFFFFEC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5261FFFFFFFFFFFFFFFC16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5271FFFFFFFFFFFFFF0C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5281FFFFFFFFFFFFFF1C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD5291FFFFFFFFFFFFFF2C16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F2FFFFFFFFFFFFFF8D16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F3FFFFFFFFFFFFFF8E16", "E5");
        slaves.addRequest("107BFD7816", "680F0F680801723348103210200102000000006B16");
        slaves.addRequest("680B0B6853FD52F4FFFFFFFFFFFFFF8F16", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F5FFFFFFFFFFFFFF9016", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F6FFFFFFFFFFFFFF9116", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F7FFFFFFFFFFFFFF9216", "E5");
        slaves.addRequest("107BFD7816", "680F0F68080172103254761020010300000000BB16");
        slaves.addRequest("680B0B6853FD52F8FFFFFFFFFFFFFF9316", master.getSendReTries());
        slaves.addRequest("680B0B6853FD52F9FFFFFFFFFFFFFF9416", master.getSendReTries());
        
        final LinkedList<DeviceId> deviceIds = new LinkedList<>();
        
        master.widcardSearch(bcdMaskedId, bcdMaskedMan, bcdMaskedVersion, bcdMaskedMedium, (deviceId) -> {
                deviceIds.add(deviceId);
        });
        
        
        
        assertTrue(slaves.allRequestsHandled());
        log.info("widcardSearch finished");
        assertEquals(4, deviceIds.size());
        
        assertEquals(1, deviceIds.get(0).address);
        assertEquals(14491001, deviceIds.get(0).identNumber);
        assertEquals("DBW", deviceIds.get(0).manufacturer);
        assertEquals(MBusMedium.HOT_WATER, deviceIds.get(0).medium);
        assertEquals(1, deviceIds.get(0).version);
        assertEquals(1, deviceIds.get(1).address);
        assertEquals(14491008, deviceIds.get(1).identNumber);
        assertEquals("QKG", deviceIds.get(1).manufacturer);
        assertEquals(MBusMedium.HOT_WATER, deviceIds.get(1).medium);
        assertEquals(1, deviceIds.get(1).version);
        assertEquals(1, deviceIds.get(2).address);
        assertEquals(32104833, deviceIds.get(2).identNumber);
        assertEquals("H@P", deviceIds.get(2).manufacturer);
        assertEquals(MBusMedium.ELECTRICITY, deviceIds.get(2).medium);
        assertEquals(1, deviceIds.get(2).version);
        assertEquals(1, deviceIds.get(3).address);
        assertEquals(76543210, deviceIds.get(3).identNumber);
        assertEquals("H@P", deviceIds.get(3).manufacturer);
        assertEquals(MBusMedium.GAS, deviceIds.get(3).medium);
        assertEquals(1, deviceIds.get(3).version);
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
