/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.sf.mbus4j.SlaveStreams;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.slave.Slaves.LogInit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class MasterTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.DEBUG);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    Logger log = LoggerFactory.getLogger(MasterTest.class);
    private SlaveStreams slaves;
    private Master master;

    public MasterTest() {
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("setUp");
        slaves = new SlaveStreams();
        master = new Master();
        master.setStreams(slaves.getInputStream(), slaves.getOutputStream(), 115200 * 3); // speedup things
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown");
        master.releaseStreams();
        master = null;
        slaves.close();
    }

    /**
     * Test of deselectBySecondaryAddress method, of class Master.
     */
    @Test
    @Ignore
    public void testDeselectBySecondaryAddress() {
        System.out.println("deselectBySecondaryAddress");
        master.deselectBySecondaryAddress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectBySecondaryAddress method, of class Master.
     */
    @Test
    @Ignore
    public void testSelectBySecondaryAddress() {
        System.out.println("selectBySecondaryAddress");
        master.selectBySecondaryAddress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of widcardSearch method, of class Master.
     */
    @Test(timeout = 60000)
    public void testWidcardSearch() throws Exception {
        System.out.println("widcardSearch");
        int leadingBcdDigitsId = 0;
        int maskLength = 7;
        int maskedMan = 0xFFFF;
        int maskedVersion = 0xFF;
        int maskedMedium = 0xFF;
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
        master.widcardSearch(leadingBcdDigitsId, maskLength, maskedMan, maskedVersion, maskedMedium);
        assertTrue(slaves.isOK());
        log.info("widcardSearch finished");
        assertEquals(master.deviceCount(), 4);
        assertEquals(master.getDevice(0).getSecondaryAddress(), 14491001);
        assertEquals(master.getDevice(0).getMan(), "DBW");
        assertEquals(master.getDevice(0).getMedium(), MBusMedium.StdMedium.HOT_WATER);
        assertEquals(master.getDevice(0).getVersion(), 1);
        assertEquals(master.getDevice(1).getSecondaryAddress(), 14491008);
        assertEquals(master.getDevice(1).getMan(), "QKG");
        assertEquals(master.getDevice(1).getMedium(), MBusMedium.StdMedium.HOT_WATER);
        assertEquals(master.getDevice(1).getVersion(), 1);
        assertEquals(master.getDevice(2).getSecondaryAddress(), 32104833);
        assertEquals(master.getDevice(2).getMan(), "H@P");
        assertEquals(master.getDevice(2).getMedium(), MBusMedium.StdMedium.ELECTRICITY);
        assertEquals(master.getDevice(2).getVersion(), 1);
        assertEquals(master.getDevice(3).getSecondaryAddress(), 76543210);
        assertEquals(master.getDevice(3).getMan(), "H@P");
        assertEquals(master.getDevice(3).getMedium(), MBusMedium.StdMedium.GAS);
        assertEquals(master.getDevice(3).getVersion(), 1);
    }
}
