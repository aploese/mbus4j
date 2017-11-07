package net.sf.mbus4j.master.devices;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import de.ibapl.spsw.logging.TimeStampLogging;
import de.ibapl.spsw.provider.SerialPortSocketFactoryImpl;
import de.ibapl.spsw.ser2net.Ser2NetProvider;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.Frame.ControlCode;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ReadOutDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.ResponseFrame;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.master.MBusMaster;

@Ignore
public class Test_ALG_Electricity_0x04 {

	MBusMaster master;
	DeviceId deviceId;
	int udrRecoverTime = 100;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		master = new MBusMaster();
//		SerialPortSocket serialPortSocket = SerialPortSocketFactoryImpl.singleton().createSerialPortSocket(portName);
		SerialPortSocket serialPortSocket = new Ser2NetProvider("127.0.0.1", 4001);
		master.setSerialPort(LoggingSerialPortSocket.wrapWithHexOutputStream(serialPortSocket, new FileOutputStream("/tmp/test_ALG.txt", false),  false, TimeStampLogging.FROM_OPEN));
		master.setResponseTimeoutOffset(1000);
		master.open();
		deviceId = new DeviceId((byte)0x04, MBusMedium.ELECTRICITY, 10184, "ALG", (byte)0x01);
	}

	@After
	public void tearDown() throws Exception {
		master.close();
	}
	
	

	//TODO
	@Ignore
	@Test
	public void testGlobalReadout_Separate() throws IOException, InterruptedException {
		SendInitSlave SND_NKE = new SendInitSlave(deviceId.address);
		RequestClassXData REQ_UD2 = new RequestClassXData(deviceId.address, true, true, ControlCode.REQ_UD2);
		
		ReadOutDataBlock db = new ReadOutDataBlock();
		db.setDataFieldCode(DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST);

		SendUserData SND_UD = new SendUserData(deviceId.address, db);
				
		master.send(SND_NKE, 1);
		
		master.send(SND_UD, 1);
		
		UserDataResponse udr_0 = master.send(REQ_UD2, 1);
		assertFalse(udr_0.isLastPackage());
		assertFalse(udr_0.isAcd());
		assertTrue(udr_0.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();
		
		UserDataResponse udr_1 = master.send(REQ_UD2, 1);
		assertFalse(udr_1.isLastPackage());
		assertFalse(udr_1.isAcd());
		assertTrue(udr_1.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_2 = master.send(REQ_UD2, 1);
		assertFalse(udr_2.isLastPackage());
		assertFalse(udr_2.isAcd());
		assertTrue(udr_2.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_3 = master.send(REQ_UD2, 1);
		assertFalse(udr_3.isLastPackage());
		assertFalse(udr_3.isAcd());
		assertTrue(udr_3.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_4 = master.send(REQ_UD2, 1);
		assertFalse(udr_4.isLastPackage());
		assertFalse(udr_4.isAcd());
		assertTrue(udr_4.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_5 = master.send(REQ_UD2, 1);
		assertFalse(udr_5.isLastPackage());
		assertFalse(udr_5.isAcd());
		assertTrue(udr_5.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_6 = master.send(REQ_UD2, 1);
		assertFalse(udr_6.isLastPackage());
		assertFalse(udr_6.isAcd());
		assertTrue(udr_6.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_7 = master.send(REQ_UD2, 1);
		assertFalse(udr_7.isLastPackage());
		assertFalse(udr_7.isAcd());
		assertTrue(udr_7.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_8 = master.send(REQ_UD2, 1);
		assertFalse(udr_8.isLastPackage());
		assertFalse(udr_8.isAcd());
		assertTrue(udr_8.isDfc());
		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_9 = master.send(REQ_UD2, 1);
		assertTrue(udr_9.isLastPackage());
		assertFalse(udr_9.isAcd());
		assertFalse(udr_9.isDfc());
	}
	
	@Ignore
	@Test
	public void testGlobalReadout_1() throws IOException, InterruptedException {
		SendInitSlave SND_NKE = new SendInitSlave(deviceId.address);
		
		ReadOutDataBlock db = new ReadOutDataBlock();
		db.setDataFieldCode(DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST);

		SendUserData SND_UD = new SendUserData(deviceId.address, db);
				
		master.send(SND_NKE, 1);
		
		master.send(SND_UD, 1);
		
		UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCB, deviceId.address);
		assertTrue("UDR:" + udr, udr.isLastPackage());
		System.err.println(udr);
		
	}

	@Ignore
	@Test
	public void testTotaEnergyProfile_Separate() throws IOException, InterruptedException {
		SendInitSlave SND_NKE = new SendInitSlave(deviceId.address);
		RequestClassXData REQ_UD2 = new RequestClassXData(deviceId.address, true, true, ControlCode.REQ_UD2);
		
		LongDataBlock db = new LongDataBlock();
		db.setDataFieldCode(DataFieldCode._64_BIT_INTEGER);
		db.setVif(VifFD.PARAMETER_SET_IDENTIFICATION);
		db.setValue(0x000000000f180008L);

		SendUserData SND_UD = new SendUserData(deviceId.address, db);
				
		master.send(SND_NKE, 1);
		
//		Am 30.10.2017 war es das letzte...
//		master.send(SND_UD, 1);		

		UserDataResponse udr_0 = master.send(REQ_UD2, 1);
		assertFalse("udr_0" + udr_0,  udr_0.isLastPackage());
		Thread.sleep(udrRecoverTime);
//		UserDataResponse udr_0_1 = master.send(REQ_UD2, 1);
//		assertFalse(udr_0_1.isLastPackage());
//TODO		assertEquals(udr_0, udr_0_1);
//		Thread.sleep(udrRecoverTime);
		REQ_UD2.toggleFcb();
		
		UserDataResponse udr_1 = master.send(REQ_UD2, 1);
		assertFalse("udr_1" + udr_1, udr_1.isLastPackage());
		Thread.sleep(udrRecoverTime);
//		UserDataResponse udr_1_1 = master.send(REQ_UD2, 1);
//		assertFalse(udr_1_1.isLastPackage());
//		Thread.sleep(udrRecoverTime);
//TODO		assertEquals(udr_1, udr_1_1);
		REQ_UD2.toggleFcb();

		UserDataResponse udr_2 = master.send(REQ_UD2, 1);
		assertTrue("udr_2" + udr_2, udr_2.isLastPackage());
		Thread.sleep(udrRecoverTime);

	}

	@Ignore
	@Test
	public void testGlobalReadout() throws IOException, InterruptedException {
		ReadOutDataBlock db_SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST = new ReadOutDataBlock();
		db_SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST.setDataFieldCode(DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST);

		UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, db_SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST, deviceId.address);
		assertTrue("udr_0" + udr,  udr.isLastPackage());
		assertEquals("udr_0" + udr,  192, udr.getDataBlockCount());
	}
	
	@Ignore
	@Test
	public void testTotaEnergyProfile() throws IOException, InterruptedException {
		
		final LongDataBlock db_PARAMETER_SET_IDENTIFICATION = new LongDataBlock();
		db_PARAMETER_SET_IDENTIFICATION.setDataFieldCode(DataFieldCode._64_BIT_INTEGER);
		db_PARAMETER_SET_IDENTIFICATION.setVif(VifFD.PARAMETER_SET_IDENTIFICATION);
		db_PARAMETER_SET_IDENTIFICATION.setValue(0x000000000f180008L);

		
		UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, db_PARAMETER_SET_IDENTIFICATION, deviceId.address);
		assertTrue("udr_0" + udr,  udr.isLastPackage());
		assertEquals("udr_0" + udr,  45, udr.getDataBlockCount());
		for (int i = 0; i < udr.getDataBlockCount(); i++) {
			DataBlock udr_db = udr.getDataBlock(i);
			try {
				udr.findDataBlock(udr_db.getDataFieldCode(), udr_db.getParamDescr(), udr_db.getUnitOfMeasurement(),
						udr_db.getFunctionField(), udr_db.getStorageNumber(), udr_db.getSubUnit(), udr_db.getTariff());
			} catch (Exception e) {
				System.err.println("DB[" + i + "] is double: " + udr_db);
			}
		}
	}
	
	@Test
	public void testRealTimeProfile() throws IOException, InterruptedException {
		
		final LongDataBlock db_PARAMETER_SET_IDENTIFICATION = new LongDataBlock();
		db_PARAMETER_SET_IDENTIFICATION.setDataFieldCode(DataFieldCode._64_BIT_INTEGER);
		db_PARAMETER_SET_IDENTIFICATION.setVif(VifFD.PARAMETER_SET_IDENTIFICATION);
		db_PARAMETER_SET_IDENTIFICATION.setValue(0x0000001000DFFF90L);

		
		UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, db_PARAMETER_SET_IDENTIFICATION, deviceId.address);
		assertTrue("udr_0" + udr,  udr.isLastPackage());
		assertEquals("udr_0" + udr,  53, udr.getDataBlockCount());
		for (int i = 0; i < udr.getDataBlockCount(); i++) {
			DataBlock udr_db = udr.getDataBlock(i);
			try {
				udr.findDataBlock(udr_db.getDataFieldCode(), udr_db.getParamDescr(), udr_db.getUnitOfMeasurement(),
						udr_db.getFunctionField(), udr_db.getStorageNumber(), udr_db.getSubUnit(), udr_db.getTariff());
			} catch (Exception e) {
				System.err.println("DB[" + i + "] is double: " + udr_db);
			}
		}
	}
	
	@Ignore
	@Test
	public void testPartialProfile() throws IOException, InterruptedException {
		
		final LongDataBlock db_PARAMETER_SET_IDENTIFICATION = new LongDataBlock();
		db_PARAMETER_SET_IDENTIFICATION.setDataFieldCode(DataFieldCode._64_BIT_INTEGER);
		db_PARAMETER_SET_IDENTIFICATION.setVif(VifFD.PARAMETER_SET_IDENTIFICATION);
		db_PARAMETER_SET_IDENTIFICATION.setValue(0x0000002000380020L);

		
		UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCV, db_PARAMETER_SET_IDENTIFICATION, deviceId.address);
		assertTrue("udr_0" + udr,  udr.isLastPackage());
		assertEquals("udr_0" + udr,  14, udr.getDataBlockCount());
		for (int i = 0; i < udr.getDataBlockCount(); i++) {
			DataBlock udr_db = udr.getDataBlock(i);
			try {
				udr.findDataBlock(udr_db.getDataFieldCode(), udr_db.getParamDescr(), udr_db.getUnitOfMeasurement(),
						udr_db.getFunctionField(), udr_db.getStorageNumber(), udr_db.getSubUnit(), udr_db.getTariff());
			} catch (Exception e) {
				System.err.println("DB[" + i + "] is double: " + udr_db);
			}
		}
	}
	
	@Ignore
	@Test
	public void test100Times() throws IOException, InterruptedException {
		SendInitSlave SND_NKE = new SendInitSlave(deviceId.address);

		LongDataBlock db = new LongDataBlock();
		db.setDataFieldCode(DataFieldCode._64_BIT_INTEGER);
		db.setVif(VifFD.PARAMETER_SET_IDENTIFICATION);
		db.setValue(0x000000000f180008L);

		SendUserData SND_UD = new SendUserData(deviceId.address, db);
				
		master.send(SND_NKE, master.getSendReTries());
		
		master.send(SND_UD, master.getSendReTries());

		for (int i = 0; i < 100; i++) {
			UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCB, deviceId.address);
			master.wait_RSP_UD_Recover();
			assertEquals("udr_0" + udr,  45, udr.getDataBlockCount());
			assertTrue("udr_0" + udr,  udr.isLastPackage());
			System.err.println("INDEX: " + i + ">\n" + udr + "<INDEX " + i);
			assertTrue("UDR:" + udr, udr.isLastPackage());
		}
	}

}
