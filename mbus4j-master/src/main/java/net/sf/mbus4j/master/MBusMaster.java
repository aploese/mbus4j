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
package net.sf.mbus4j.master;

import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.Speed;
import de.ibapl.spsw.api.StopBits;
import de.ibapl.spsw.api.TimeoutIOException;

import java.io.BufferedInputStream;
import java.io.Closeable;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SelectionOfSlaves;
import net.sf.mbus4j.dataframes.SingleCharFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.decoder.DecodeException;
import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.devices.Sender;
import net.sf.mbus4j.encoder.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Set;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.mbus4j.MBusUtils;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.PrimaryAddress;
import net.sf.mbus4j.dataframes.RequestFrame;
import net.sf.mbus4j.dataframes.ResponseFrame;
import net.sf.mbus4j.dataframes.SendInitSlave;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.log.LogUtils;

/**
 * Handles the MBus devices connected via inputStream/OutputStream.
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 *
 */
public class MBusMaster implements Sender {
	public static final Speed DEFAULT_SPEED = Speed._2400_BPS;
	public static final Set<FlowControl> FLOW_CONTROL = FlowControl.getFC_NONE();
	public static final DataBits DATA_BITS = DataBits.DB_8;
	public static final StopBits STOP_BITS = StopBits.SB_1;
	public static final Parity PARITY = Parity.EVEN;

	private void readGarbage() throws IOException {
		final int avail = inputStream.available();
		if (avail > 0) {
			inputStream.read(new byte[avail]);
		}
	}

	public void requestUserData(ValuesRequest<?> request) throws IOException {
		for (DeviceRequest<?> dr : request) {
			requestUserData(dr);
		}
	}

	private void requestUserData(DeviceRequest<?> dr) throws IOException {
		UserDataResponse udr;
		switch (dr.addressing) {
		case PRIMARY:
			udr = sendRequestUserData(RequestFrame.DEFAULT_FCB, RequestFrame.DEFAULT_FCV, dr.deviceId.address);
			break;
		case SECONDARY:
			udr = sendRequestUserData(RequestFrame.DEFAULT_FCB, RequestFrame.DEFAULT_FCV, dr.deviceId);
			break;
		default:
			throw new RuntimeException("Unknown addressing: " + dr.addressing);
		}
		dr.setFullResponse(udr);
	}

	private class MaskedQueueEntry {

		private final SelectionOfSlaves.WildcardNibbles nibble;
		private final byte value;

		private MaskedQueueEntry(SelectionOfSlaves.WildcardNibbles nibble, byte value) {
			this.nibble = nibble;
			this.value = value;
		}
	}

	private final static Logger log = LogUtils.getMasterLogger();
	private final Encoder encoder = new Encoder();
	private final Decoder parser = new Decoder();
	private SerialPortSocket serialPortSocket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int responseTimeOutOffset;
	private int idleTime;
	private int minSlaveAnswerTime;
	private int maxSlaveAnswerTime;

	public MBusMaster() {
		super();
	}

	private boolean searchDeviceByAddress(byte primaryAddress, Consumer<DeviceId> deviceIdConsumer) throws IOException {
		try {
			final RequestClassXData req = new RequestClassXData(primaryAddress, RequestFrame.DEFAULT_FCB, RequestFrame.DEFAULT_FCV, Frame.ControlCode.REQ_UD2);
			
			UserDataResponse udr = send(req, 1);
			deviceIdConsumer.accept(udr.getDeviceId());
			return true;
		} catch (TimeoutIOException  timeoutIOException) {
			// no device at this address
			return false;
		} //TODO handle DecodeException...
	}

        public boolean isOpen() {
            return serialPortSocket.isOpen();
        }
        
	public void close() throws IOException {
		serialPortSocket.close();
		inputStream = null;
		outputStream = null;
                log.fine("CLOSED");
	}

	/**
	 * Idle time is 33 bit periods see M-Bus doc chaper 5.4 + responseTimeOutOffset
	 *
	 * @return
	 */
	public int getIdleTime() {
		return idleTime;
	}

	protected void calcIdleTimes(Speed speed) {
		minSlaveAnswerTime = (int) Math.round((1000.0 * 11 / speed.value) + responseTimeOutOffset);
		maxSlaveAnswerTime = (int) Math.round((1000.0 * 330 / speed.value) + 50 + responseTimeOutOffset);
		idleTime = (int) Math.round((1000.0 * 33 / speed.value) + responseTimeOutOffset);
	}

	/**
	 * Timeout is 11 bit periods see M-Bus doc chaper 5.4 2 + responseTimeOutOffset
	 *
	 */
	public long getMinAnswerTime() {
		return minSlaveAnswerTime;
	}

	/**
	 * Timeout is 330 bit periods + 50ms see M-Bus doc chaper 5.4 2 +
	 * responseTimeOutOffset
	 *
	 */
	public long getMaxAnswerTime() {
		return maxSlaveAnswerTime;
	}

	public void searchDevicesBySecondaryAddressing(Consumer<DeviceId> deviceIdConsumer) throws IOException {
		widcardSearch(0xFFFFFFFF, (short) 0xFFFF, (byte) 0xFF, (byte) 0xFF, deviceIdConsumer);
	}

	/**
	 * Search by primary address (0..250) 253 is for slave select 254 broadcast
	 * addres to all devices
	 *
	 * @return
	 * @throws java.io.IOException
	 */
	public void searchDevicesByPrimaryAddress(Consumer<DeviceId> deviceIdConsumer) throws IOException {
		searchDevicesByPrimaryAddress((byte) 0, PrimaryAddress.LAST_REGULAR_PRIMARY_ADDRESS, deviceIdConsumer);
	}

	public void searchDevicesByPrimaryAddress(byte first, byte last, Consumer<DeviceId> deviceIdConsumer)
			throws IOException {
		final short fAddr = (short) (first & 0xFF);
		final short lAddr = (short) (last & 0xFF);
		for (short i = fAddr; i <= lAddr; i++) {
			searchDeviceByAddress((byte)i, deviceIdConsumer);
		}
	}

	@Override
	public <T extends ResponseFrame> T send(RequestFrame<T> request, int maxTries) throws IOException {
		for (int tries = 1; tries <= maxTries; tries++) {
			try {
				readGarbage();

				outputStream.write(encoder.encodeFrame(request));
				outputStream.flush();
				return (T) parser.parse(inputStream);

			} catch (TimeoutIOException | DecodeException e) {
				if (tries == maxTries) {
					if (log.isLoggable(Level.FINE)) {
						log.log(Level.FINE, "max tries({0}) reached .. aborting send to: {1}",
								new Object[] { maxTries, request });
					}
					throw e;
				}
			}
		}
		throw new RuntimeException("Should never happen Fall trough send()");
	}

	public SingleCharFrame sendInitSlave(byte address) throws IOException {
		return send(new SendInitSlave(address), getSendReTries());
	}

	public SingleCharFrame sendSetNewAddress(byte address, byte newAddress) throws IOException {
		SendUserData req = new SendUserData();
		req.setAddress(address);
		req.addDataBlock(new ByteDataBlock(DataFieldCode._8_BIT_INTEGER, VifPrimary.BUS_ADDRESS, newAddress));
		return send(req, getSendReTries());
	}

	public UserDataResponse sendRequestUserData(boolean fcb, boolean fcv, byte address) throws IOException {
		return sendRequestUserData(fcb, fcv, null, address);
	}

	public UserDataResponse sendRequestUserData(boolean fcb, boolean fcv, DataBlock sendUserDataDb, byte address)
			throws IOException {
		// Reset FCB Flags if neccesary if device was selecte by secondary Addressing
		// its done
		// Sending an additional SND_NKE to MBusUtils.SLAVE_SELECT_PRIMARY_ADDRESS will
		// deselect device ...
		if (address != PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS) {
			SingleCharFrame singleCharFrame = sendInitSlave(address);
			if (sendUserDataDb != null) {
				sendSendUserData(address, fcb, sendUserDataDb);
			}
		}
		final RequestClassXData req = new RequestClassXData(address, fcb, fcv, Frame.ControlCode.REQ_UD2);
		UserDataResponse result = null;
		ResponseFrame responseFrame = send(req, getSendReTries());
		if (responseFrame instanceof UserDataResponse) {
			result = (UserDataResponse) responseFrame;
		} else if (responseFrame instanceof SingleCharFrame) {
			// Busy
			wait_RSP_UD_Recover();
			result = send(req, getSendReTries());
		} else {
			throw new DecodeException("Expected  RSP_UD or E5!", responseFrame);
		}

		if (result.isDfc()) {
			wait_RSP_UD_Recover();
		}

		if (result.isLastPackage()) {
			return result;
		}
		UserDataResponse udr;
		req.setFcv(true);
		do {
			req.toggleFcb();
			responseFrame = send(req, getSendReTries());
			if (responseFrame instanceof UserDataResponse) {
				udr = (UserDataResponse) responseFrame;
			} else if (responseFrame instanceof SingleCharFrame) {
				// Busy
				wait_RSP_UD_Recover();
				udr = send(req, getSendReTries());
			} else {
				throw new DecodeException("Expected  RSP_UD or E5!", responseFrame);
			}
			if (udr.isDfc()) {
				wait_RSP_UD_Recover();
			}
			result.addAllDataBlocks(udr.getDataBlocks());
		} while (!udr.isLastPackage());
		return result;
	}

	/**
	 * Send one time SND_NKE to the secondary address 
	 * @param bcdMaskedId
	 * @param maskedMan
	 * @param maskedVersion
	 * @param maskedMedium
	 * @return
	 * @throws IOException
	 */
	public int sendSlaveSelect(int bcdMaskedId, short maskedMan, byte maskedVersion, byte maskedMedium)
			throws IOException {
		final SelectionOfSlaves selectionOfSlaves = new SelectionOfSlaves(PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS);
		selectionOfSlaves.setBcdMaskedId(bcdMaskedId);
		selectionOfSlaves.setMaskedMan(maskedMan);
		selectionOfSlaves.setMaskedVersion(maskedVersion);
		selectionOfSlaves.setMaskedMedium(maskedMedium);
		return sendSlaveSelect(selectionOfSlaves);
	}

	private int sendSlaveSelect(SelectionOfSlaves selectionOfSlaves) throws IOException {

		if (log.isLoggable(Level.FINE)) {
			log.fine(String.format("Will select Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X",
					selectionOfSlaves.getBcdMaskedId(), selectionOfSlaves.getMaskedMan(),
					selectionOfSlaves.getMaskedVersion(), selectionOfSlaves.getMaskedMedium()));
		}

		readGarbage();

		outputStream.write(encoder.encodeFrame(selectionOfSlaves));
		outputStream.flush();
		try {
			Thread.sleep(getMaxAnswerTime());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			final byte[] data = new byte[16];
			final int readed = inputStream.read(data);
			if (readed == -1) {
				throw new IOException("port closed");
			} else if (readed == 0) {
				return 0; // By the way this should never happen ... but just in case
			} else if (readed == 1) {
				// we get an e5 (1 slave answered) or garbage (multiple slaves)
				return data[0] == (byte) 0xE5 ? 1 : 2;
			} else {
				return readed;
			}
		} catch (TimeoutIOException e) {
			return 0;
		}
	}

	public Closeable open() throws IOException {
		serialPortSocket.open(DEFAULT_SPEED, DATA_BITS, STOP_BITS, PARITY, FLOW_CONTROL);
		calcIdleTimes(DEFAULT_SPEED);
		serialPortSocket.setTimeouts(100, maxSlaveAnswerTime, maxSlaveAnswerTime);
		inputStream = new BufferedInputStream(serialPortSocket.getInputStream(), 255);
		outputStream = serialPortSocket.getOutputStream();
		return () -> {
			MBusMaster.this.close();
		};
	}

	public void widcardSearch(int bcdMaskedId, short bcdMaskedMan, byte bcdMaskedVersion, byte bcdMaskedMedium,
			Consumer<DeviceId> deviceIdConsumer) throws IOException {

		final SelectionOfSlaves selectionOfSlaves = new SelectionOfSlaves(PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS);
		selectionOfSlaves.setBcdMaskedId(bcdMaskedId);
		selectionOfSlaves.setMaskedMan(bcdMaskedMan);
		selectionOfSlaves.setMaskedMedium(bcdMaskedMedium);
		selectionOfSlaves.setMaskedVersion(bcdMaskedVersion);

		switch (sendSlaveSelect(selectionOfSlaves)) {
		case 0:
			// No devices found
			log.fine(String.format("no slave with bcdMaskedId: 0x%08X", bcdMaskedId));
			return;
		case 1: {
			if (searchDeviceByAddress(PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS, deviceIdConsumer)) {
				return;
			}
		}
		}
		Deque<MaskedQueueEntry> stack = new LinkedList<>();

		SelectionOfSlaves.WildcardNibbles currentNibble = SelectionOfSlaves.WildcardNibbles.ID_0;
		byte currentNibbleValue = 0;
		boolean finished = false;
		while (!finished) {
			selectionOfSlaves.setWildcardNibble(currentNibble, currentNibbleValue);
			switch (sendSlaveSelect(selectionOfSlaves)) {
			case 0:
				log.fine(String.format("no slave with bcdMaskedId: 0x%08X", bcdMaskedId));
				currentNibbleValue++;
				break;
			case 1:
				if (searchDeviceByAddress(PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS, deviceIdConsumer)) {
					// We found exactly one device so no further searching is needed at this nibble.
					currentNibbleValue++;
					break;
				}
				// Collision?!, search further on
			default:
				stack.add(new MaskedQueueEntry(currentNibble, selectionOfSlaves.getWildcardNibble(currentNibble)));
				currentNibbleValue = 0;
				while (!selectionOfSlaves.isWildcardNibble(currentNibble) && !currentNibble.isLast()) {
					currentNibble = currentNibble.next();
				}
				selectionOfSlaves.setWildcardNibble(currentNibble, currentNibbleValue);

			}
			while (currentNibbleValue == 10) {
				if (stack.isEmpty()) {
					return;
				} else {
					selectionOfSlaves.maskWildcardNibble(currentNibble);
					final MaskedQueueEntry nV = stack.pollLast();
					currentNibbleValue = (byte) (nV.value + 1);
					currentNibble = nV.nibble;
				}
			}
		}

	}

	public UserDataResponse sendRequestUserData(boolean fcb, boolean fcv, DeviceId deviceId) throws IOException {
		if (selectDevice(MBusUtils.int2Bcd(deviceId.identNumber), MBusUtils.man2Short(deviceId.manufacturer),
				deviceId.version, MBusUtils.byte2Bcd(deviceId.medium.id))) {
			return sendRequestUserData(fcb, fcv, PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS);
		} else {
			throw new RuntimeException("Can't select device");
		}
	}

	public boolean selectDevice(Integer id, String man, Byte version, MBusMedium medium) throws IOException {
		final int bcdMaskedId = id != null ? MBusUtils.int2Bcd(id) : 0xFFFFFFFF;
		final short maskedMan = man != null ? MBusUtils.short2Bcd(MBusUtils.man2Short(man)) : (short) 0xFFFF;
		final byte maskedVersion = version != null ? MBusUtils.byte2Bcd(version) : (byte) 0xFF;
		final byte maskedMedium = medium != null ? MBusUtils.byte2Bcd(medium.id) : (byte) 0xFF;
		return selectDevice(bcdMaskedId, maskedMan, maskedVersion, maskedMedium);

	}

	public boolean selectDevice(int bcdMaskedId, short maskedMan, byte maskedVersion, byte maskedMedium)
			throws IOException {
		int answers = sendSlaveSelect(bcdMaskedId, maskedMan, maskedVersion, maskedMedium);
		if (answers > 1) {
			log.warning(String.format(
					"Can't select select (too many) Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X",
					bcdMaskedId, maskedMan, maskedVersion, maskedMedium));
		} else if (answers == 0) {
			log.warning(
					String.format("Can't select select (none) Slave: id=0x%08X, man=0x%04X, ver=0x%02X, medium=0x%02X",
							bcdMaskedId, maskedMan, maskedVersion, maskedMedium));
		}
		return answers == 1;
	}

	public boolean selectDevice(DeviceId deviceId) throws IOException {
		return selectDevice(MBusUtils.int2Bcd(deviceId.identNumber), MBusUtils.man2Short(deviceId.manufacturer),
				deviceId.version, (byte) deviceId.medium.getId());
	}

	public boolean setPrimaryAddressOfDevice(DeviceId deviceId, byte newAddress) throws IOException {
		if (selectDevice(deviceId)) {
			final SingleCharFrame f = sendSetNewAddress(PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS, newAddress);
			sendInitSlave(PrimaryAddress.SLAVE_SELECT_PRIMARY_ADDRESS);
			return true;
		} else {
			return false;
		}
	}

	public void setSerialPort(SerialPortSocket serialPortSocket) {
		this.serialPortSocket = serialPortSocket;
	}

	public int getSendReTries() {
		return DEFAULT_SEND_TRIES;
	}

	public void setResponseTimeoutOffset(int responseTimeOutOffset) {
		this.responseTimeOutOffset = responseTimeOutOffset;
		if (serialPortSocket != null && serialPortSocket.isOpen()) {
			try {
				calcIdleTimes(serialPortSocket.getSpeed());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public boolean sendSendUserData(byte address, boolean fcb, DataBlock db) throws IOException {
		SendUserData req = new SendUserData(address, fcb, db);
		return send(req, getSendReTries()) instanceof SingleCharFrame;
	}

	public void wait_RSP_UD_Recover() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
