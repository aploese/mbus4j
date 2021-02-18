package net.sf.mbus4j.master.devices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import de.ibapl.spsw.logging.TimeStampLogging;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserDataFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.master.MBusMaster;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

@Disabled
public class Test_SBC_Electricity_0x24 {

    MBusMaster master;
    DeviceId deviceId;

    @BeforeEach
    public void setUp() throws Exception {
        master = new MBusMaster();
        ServiceLoader<SerialPortSocketFactory> loader = ServiceLoader.load(SerialPortSocketFactory.class);
        Iterator<SerialPortSocketFactory> iterator = loader.iterator();
        assertTrue(iterator.hasNext(), "NO implementation of SerialPortSocketFactory available - add a provider for that to the test dependencies");
        SerialPortSocketFactory serialPortSocketFactory = iterator.next();
        assertTrue(iterator.hasNext(), "More than one implementation of SerialPortSocketFactory available - fix the test dependencies");

        SerialPortSocket serialPortSocket = serialPortSocketFactory.createSerialPortSocket("/dev/ttyUSB0");
//		SerialPortSocket serialPortSocket = new Ser2NetProvider("localhost", 4001);
        master.setSerialPort(LoggingSerialPortSocket.wrapWithHexOutputStream(serialPortSocket, new FileOutputStream("/tmp/test_SBC.txt", false), false, TimeStampLogging.FROM_OPEN));
        master.setResponseTimeoutOffset(0);
        master.open();
        deviceId = new DeviceId((byte) 0x24, MBusMedium.ELECTRICITY, 110011, "SBC", (byte) 0x03);
    }

    @AfterEach
    public void tearDown() throws Exception {
        master.close();
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

    @Test
    public void testREQ_UDR2_Primary_Addressing_Times() throws IOException {
        UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCB, deviceId.address);
        assertEquals(deviceId.address, udr.getAddress());
        assertEquals(deviceId.version, udr.getVersion());
        assertEquals(deviceId.identNumber, udr.getIdentNumber());
        assertEquals(deviceId.manufacturer, udr.getManufacturer());
        assertEquals(deviceId.medium, udr.getMedium());
        assertEquals(6, udr.getDataBlockCount(), "udr_0 DataBlockCount: " + udr.getDataBlockCount() + "\n" + udr);
        assertTrue(udr.isLastPackage(), "udr_0" + udr);
        System.err.println(udr);
    }

    @Test
    public void testREQ_UDR2_Secondary_Addressing_Times() throws IOException {
        UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCB, deviceId);
        assertEquals(6, udr.getDataBlockCount(), "udr_0 DataBlockCount: " + udr.getDataBlockCount() + "\n" + udr);
        assertTrue(udr.isLastPackage(), "udr_0" + udr);
        System.err.println(udr);
    }

    @Disabled
    @Test
    public void testREQ_UDR2_100_Times() throws IOException {
        for (int i = 0; i < 100; i++) {
            UserDataResponse udr = master.sendRequestUserData(SendUserDataFrame.DEFAULT_FCB, SendUserDataFrame.DEFAULT_FCB, deviceId.address);
            assertEquals(6, udr.getDataBlockCount(), "udr_0 DataBlockCount: " + udr.getDataBlockCount() + "\n" + udr);
            assertTrue(udr.isLastPackage(), "udr_0" + udr);
            System.err.println("INDEX: " + i + ">\n" + udr + "<INDEX " + i);
            assertTrue(udr.isLastPackage(), "UDR:" + udr);
        }
    }

    @Test
    public void testSearchPrimary() throws IOException {
        final LinkedList<DeviceId> result = new LinkedList<>();
        master.searchDevicesByPrimaryAddress((d) -> {
            assertEquals(deviceId.identNumber, d.identNumber);
            assertEquals(deviceId.manufacturer, d.manufacturer);
            assertEquals(deviceId.address, d.address);
            assertEquals(deviceId.medium, d.medium);
            assertEquals(deviceId.version, d.version);
            result.add(d);
        });
        assertEquals(1, result.size());
    }

    @Test
    public void testSearchSecondary() throws IOException {
        final LinkedList<DeviceId> result = new LinkedList<>();
        master.searchDevicesBySecondaryAddressing((d) -> {
            assertEquals(deviceId.identNumber, d.identNumber);
            assertEquals(deviceId.manufacturer, d.manufacturer);
            assertEquals(deviceId.address, d.address);
            assertEquals(deviceId.medium, d.medium);
            assertEquals(deviceId.version, d.version);
            result.add(d);
        });
        assertEquals(1, result.size());
    }

}
