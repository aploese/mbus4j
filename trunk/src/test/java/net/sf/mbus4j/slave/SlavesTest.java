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
package net.sf.mbus4j.slave;

import static org.junit.Assert.assertTrue;
import net.sf.mbus4j.LogInit;
import net.sf.mbus4j.MasterStreams;
import net.sf.mbus4j.dataframes.MBusMedium;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SlavesTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogInit.initLog(LogInit.DEBUG);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
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
}
