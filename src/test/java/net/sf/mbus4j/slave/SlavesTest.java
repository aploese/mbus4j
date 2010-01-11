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
package net.sf.mbus4j.slave;

import static org.junit.Assert.assertTrue;
import net.sf.mbus4j.LogInit;
import net.sf.mbus4j.MasterStreams;
import net.sf.mbus4j.dataframes.MBusMedium;

import net.sf.mbus4j.master.MBusMaster;
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
public class SlavesTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.DEBUG);
        log = LoggerFactory.getLogger(SlavesTest.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private static Logger log;
    private Slaves slaves;
    private MasterStreams master;

    public SlavesTest() {
    }

    @Before
    public void setUp() {
        master = new MasterStreams();
        slaves = new Slaves();
        slaves.setStreams(master.getInputStream(), master.getOutputStream());
    }

    @After
    public void tearDown() throws Exception {
        master.close();
        slaves.releaseStreams();
        slaves = null;
    }

    /**
     * Test of addSlave method, of class Slaves.
     */
    @Test(timeout = 10000)
    public void testUserDataResponse() throws Exception {
        System.out.println("userDataResponse");
        Slave slave = new Slave(0x01, 12345678, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        slave = new Slave(0x0F, 01234567, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        slave = new Slave(0x10, 00123456, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        slave = new Slave(0xFA, 00012345, "AMK", 0, MBusMedium.StdMedium.OTHER);
        slaves.addSlave(slave);
        master.sendRequestAndCollectResponse("107B017C16", "680F0F6808017278563412AB050000000000003F16");
        master.sendRequestAndCollectResponse("107B017C16", "680F0F6808017278563412AB050000010000004016");
        master.replay();
        synchronized (master) {
            master.wait();
        }
        assertTrue(master.isOK());
        //TODO  FCB ACB ??
        // BCD behandlung ???
    }

     /**
     * Test of widcardSearch method, of class MBusMaster.
     */
    @Test(timeout = 60000)
    public void testWidcardSearch() throws Exception {
        System.out.println("widcardSearch");
        slaves.addSlave(new Slave(0x01, 14491001, "DBW", 1, MBusMedium.StdMedium.HOT_WATER));
        slaves.addSlave(new Slave(0x01, 14491008, "QKG", 1, MBusMedium.StdMedium.HOT_WATER));
        slaves.addSlave(new Slave(0x01, 32104833, "H@P", 1, MBusMedium.StdMedium.ELECTRICITY));
        slaves.addSlave(new Slave(0x01, 76543210, "H@P", 1, MBusMedium.StdMedium.GAS));

        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF0FFFFFFFFFAA16", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FFFFFF1FFFFFFFFFBA16", "E5E5");
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF10FFFFFFFFAB16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF11FFFFFFFFAC16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF12FFFFFFFFAD16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF13FFFFFFFFAE16", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FFFFFF14FFFFFFFFAF16", "E5E5");
        master.sendRequestNoAnswer("680B0B6853FD52FFFF0F14FFFFFFFFBF16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF1F14FFFFFFFFCF16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF2F14FFFFFFFFDF16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF3F14FFFFFFFFEF16", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FFFF4F14FFFFFFFFFF16", "E5E5");
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4014FFFFFFFFF016", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4114FFFFFFFFF116", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4214FFFFFFFFF216", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4314FFFFFFFFF316", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4414FFFFFFFFF416", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4514FFFFFFFFF516", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4614FFFFFFFFF616", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4714FFFFFFFFF716", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF4814FFFFFFFFF816", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FFFF4914FFFFFFFFF916", "E5E5");
        master.sendRequestNoAnswer("680B0B6853FD52FF0F4914FFFFFFFF0916", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FF1F4914FFFFFFFF1916", "E5E5");
        master.sendRequestAndCollectResponse("680B0B6853FD52FF104914FFFFFFFF0A16", "E5E5");
        master.sendRequestAndCollectResponse("680B0B6853FD520F104914FFFFFFFF1A16", "E5E5");
        master.sendRequestNoAnswer("680B0B6853FD5200104914FFFFFFFF0B16", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD5201104914FFFFFFFF0C16", "E5");
        master.sendRequestAndCollectResponse("105BFD5816", "680F0F680801720110491457100106000000005716");
        master.sendRequestNoAnswer("680B0B6853FD5202104914FFFFFFFF0D16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD5203104914FFFFFFFF0E16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD5204104914FFFFFFFF0F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD5205104914FFFFFFFF1016", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD5206104914FFFFFFFF1116", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD5207104914FFFFFFFF1216", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD5208104914FFFFFFFF1316", "E5");
        master.sendRequestAndCollectResponse("105BFD5816", "680F0F68080172081049146745010600000000A316");
        master.sendRequestNoAnswer("680B0B6853FD5209104914FFFFFFFF1416", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD521F104914FFFFFFFF2A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD522F104914FFFFFFFF3A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD523F104914FFFFFFFF4A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD524F104914FFFFFFFF5A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD525F104914FFFFFFFF6A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD526F104914FFFFFFFF7A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD527F104914FFFFFFFF8A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD528F104914FFFFFFFF9A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD529F104914FFFFFFFFAA16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF114914FFFFFFFF0B16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF124914FFFFFFFF0C16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF134914FFFFFFFF0D16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF144914FFFFFFFF0E16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF154914FFFFFFFF0F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF164914FFFFFFFF1016", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF174914FFFFFFFF1116", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF184914FFFFFFFF1216", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF194914FFFFFFFF1316", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF2F4914FFFFFFFF2916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF3F4914FFFFFFFF3916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF4F4914FFFFFFFF4916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF5F4914FFFFFFFF5916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF6F4914FFFFFFFF6916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF7F4914FFFFFFFF7916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF8F4914FFFFFFFF8916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FF9F4914FFFFFFFF9916", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF5F14FFFFFFFF0F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF6F14FFFFFFFF1F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF7F14FFFFFFFF2F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF8F14FFFFFFFF3F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFF9F14FFFFFFFF4F16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF15FFFFFFFFB016", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF16FFFFFFFFB116", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF17FFFFFFFFB216", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF18FFFFFFFFB316", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF19FFFFFFFFB416", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF2FFFFFFFFFCA16", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FFFFFF3FFFFFFFFFDA16", "E5");
        master.sendRequestAndCollectResponse("105BFD5816", "680F0F680801723348103210200102000000006B16");
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF4FFFFFFFFFEA16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF5FFFFFFFFFFA16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF6FFFFFFFFF0A16", 100, 3);
        master.sendRequestAndCollectResponse("680B0B6853FD52FFFFFF7FFFFFFFFF1A16", "E5");
        master.sendRequestAndCollectResponse("105BFD5816", "680F0F68080172103254761020010300000000BB16");
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF8FFFFFFFFF2A16", 100, 3);
        master.sendRequestNoAnswer("680B0B6853FD52FFFFFF9FFFFFFFFF3A16", 100, 3);
        master.replay();
        synchronized (master) {
            master.wait();
        }
        assertTrue(master.isOK());
        log.info("widcardSearch finished");
    }


         /**
     * Test of widcardSearch method, of class MBusMaster.
     */
    @Ignore
    @Test(timeout = 60000)
    public void testWidcardSearchWithMaster() throws Exception {
        System.out.println("widcardSearch");
        slaves.addSlave(new Slave(0x01, 14491001, "DBW", 1, MBusMedium.StdMedium.HOT_WATER));
        slaves.addSlave(new Slave(0x01, 14491008, "QKG", 1, MBusMedium.StdMedium.HOT_WATER));
        slaves.addSlave(new Slave(0x01, 32104833, "H@P", 1, MBusMedium.StdMedium.ELECTRICITY));
        slaves.addSlave(new Slave(0x01, 76543210, "H@P", 1, MBusMedium.StdMedium.GAS));
        MBusMaster master = new MBusMaster();
//        master.setStreams(slaves.getInputStream(), slaves.getOutputStream(), 115200 * 3); // speedup things
//TODO        master.getI
        int leadingBcdDigitsId = 0;
        int maskLength = 7;
        short maskedMan = (short)0xFFFF;
        byte maskedVersion = (byte)0xFF;
        byte maskedMedium = (byte)0xFF;
        master.widcardSearch(leadingBcdDigitsId, maskLength, maskedMan, maskedVersion, maskedMedium);

    }
}
