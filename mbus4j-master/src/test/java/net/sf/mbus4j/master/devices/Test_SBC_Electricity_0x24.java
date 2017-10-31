package net.sf.mbus4j.master.devices;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.ibapl.spsw.provider.SerialPortSocketFactoryImpl;
import de.ibapl.spsw.ser2net.Ser2NetProvider;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.master.MBusMaster;

public class Test_SBC_Electricity_0x24 {

	MBusMaster master;
	DeviceId deviceId;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		master = new MBusMaster();
		master.setSerialPort(SerialPortSocketFactoryImpl.singleton().createSerialPortSocket("/dev/ttyUSB0"));
//		master.setSerialPort(new Ser2NetProvider("localhost", 4001));
		master.setResponseTimeoutOffset(0);
		master.open();
		deviceId = new DeviceId((byte)0x24, MBusMedium.ELECTRICITY, 00110011, "SBC", (byte)0x03);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSND_NKE() throws IOException {
		SendInitSlave SND_NKE = new SendInitSlave(deviceId.address);
		master.send(SND_NKE, 1);
		master.send(SND_NKE, 1);
		master.send(SND_NKE, 1);
		master.send(SND_NKE, 1);
		master.send(SND_NKE, 1);
	}

}
