package net.sf.mbus4j;

/*
 * #%L
 * mbus4j-core
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import de.ibapl.spsw.api.Baudrate;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortException;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.StopBits;
import net.sf.mbus4j.decoder.Decoder;
import net.sf.mbus4j.log.LogUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class MockSerialPortSocket implements SerialPortSocket {

	public class RequestStackException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

	private MBusTestInputStream is;
	private MBusTestOutputStream os;
	private boolean open;
	private RequestStackException currentStackException;
	private Baudrate baudrate;
	private Parity parity;
	private StopBits stopBits;
	private DataBits dataBits;
	private Set<FlowControl> flowControl;

	protected static final Logger log = LogUtils.getMasterLogger();

	class Data {

		final String request;
		final String response;
		final RequestStackException requestStackException;

		/*
		 * We manipulate the stacktrace for better debugging We now 2 method calls away
		 * from the point we want to show first addRequest() second Data() so remove the
		 * last two we are at the point where addRequest() was called...
		 */
		Data(String request) {
			this.response = null;
			this.request = request;
			this.requestStackException = new RequestStackException();
			StackTraceElement[] st = this.requestStackException.getStackTrace();
			this.requestStackException.setStackTrace(Arrays.copyOfRange(st, 2, st.length - 2));
		}

		/*
		 * We manipulate the stacktrace for better debugging We now 2 method calls away
		 * from the point we want to show first addRequest() second Data() so remove the
		 * last two we are at the point where addRequest() was called...
		 */
		Data(String request, String response) {
			this.response = response;
			this.request = request;
			this.requestStackException = new RequestStackException();
			StackTraceElement[] st = this.requestStackException.getStackTrace();
			this.requestStackException.setStackTrace(Arrays.copyOfRange(st, 2, st.length - 2));
		}
	}

	public class MBusTestInputStream extends InputStream {

		byte[] buffer = null;
		int readPtr = -1;

		@Override
		public void close() throws IOException {
			MockSerialPortSocket.this.close();
		}

		@Override
		public int read() throws IOException {
			if (!open) {
				return -1;
			}
			if (os.ptr == -1 || os.ptr < os.expected.length) {
				throw new InterruptedIOException("Read before all bytes written");
			}
			if (readPtr == -1 || readPtr >= buffer.length) {
				throw new InterruptedIOException();
			}
			final int result = buffer[readPtr++] & 0xFF;
			if ((readPtr == buffer.length) && (buffer.length > 0)) {
				log.info(String.format("Readed from InputStream: %s", Decoder.bytes2Ascii(buffer)));
			}
			log.fine(String.format("Return from is.read 0x%08X", result));
			if (readPtr == buffer.length) {
				nextRequest();
			}
			return result;

		}

		@Override
		public int available() {
			if (os.expected == null) {
				return 0;
			}
			if (os.ptr == os.expected.length) {
				return readPtr == -1 ? 0 : buffer.length - readPtr;
			} else {
				return 0;
			}
		}

		private void setData(Data data) {
			if (data == null || data.response == null) {
				log.info("InputStream buffer to null");
				buffer = null;
				readPtr = -1;
			} else {
				log.info(String.format("Set InputStream buffer: %s", data.response));
				buffer = Decoder.ascii2Bytes(data.response);
				readPtr = 0;
			}
		}
	}

	public class MBusTestOutputStream extends OutputStream {

		private int ptr = -1;
		private byte[] expected = null;

		private void setData(Data data) {
			if (data == null || data.request == null) {
				log.info("Set OutputStream buffer to null");
				ptr = -1;
				expected = null;
			} else {
				log.info(String.format("Set OutputStream buffer: %s", data.request));
				ptr = 0;
				expected = Decoder.ascii2Bytes(data.request);
			}
		}

		@Override
		public void write(int b) throws IOException {
			log.fine(String.format("Write to OutputStream: 0x%02X", b & 0xFF));
			if (ptr == -1) {
				throw new IOException("No write expected");
			}
			if (ptr > expected.length - 1) {
				ptr++;
				IOException ioe = new IOException(String.format("%s at outside pos: %d was: 0x%02x ",
						Decoder.bytes2Ascii(expected), ptr - 1, b & 0xFF));
				if (currentStackException != null) {
					ioe.initCause(currentStackException);
				}
				throw ioe;
			}
			if ((b & 0xFF) != (expected[ptr] & 0xFF)) {
				ptr++;
				String exp = Decoder.bytes2Ascii(expected);
				IOException ioe = new IOException(String.format("%s[%s]%s at pos: %d expected: 0x%02x but was: 0x%02x ",
						exp.substring(0, ptr * 2 - 2), exp.substring(ptr * 2 - 2, ptr * 2),
						exp.substring(ptr * 2, exp.length()), ptr - 1, expected[ptr - 1] & 0xFF, b & 0xFF));
				if (currentStackException != null) {
					ioe.initCause(currentStackException);
				}
				throw ioe;
			}
			ptr++;
			if (expected.length == ptr) {
				log.info(String.format("Written to OutputStream: %s", Decoder.bytes2Ascii(expected)));
				if (is.readPtr == -1) {
					nextRequest();
				}
			}
		}

		@Override
		public void close() throws IOException {
			MockSerialPortSocket.this.close();
		}

	}

	LinkedList<Data> data = new LinkedList<>();
	private int overallReadTimeout;
	private int interByteReadTimeout;
	private int overallWriteTimeout;

	@Override
	public void close() throws IOException {
		open = false;
	}

	@Override
	public final void openRaw() throws IOException {
		is = new MBusTestInputStream();
		os = new MBusTestOutputStream();
		open = true;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (open) {
			return is;
		} else {
			throw new SerialPortException(SerialPortException.SERIAL_PORT_SOCKET_CLOSED);
		}
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (open) {
			return os;
		} else {
			throw new SerialPortException(SerialPortException.SERIAL_PORT_SOCKET_CLOSED);
		}
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void openAsIs() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openModem() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openTerminal() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openRaw(Baudrate baudrate, DataBits dataBits, StopBits stopBits, Parity parity,
			Set<FlowControl> flowControls) throws IOException {
		this.baudrate = baudrate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
		this.flowControl = flowControls;
		this.openRaw();
	}

	@Override
	public boolean isClosed() {
		return !open;
	}

	@Override
	public boolean isCTS() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDSR() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIncommingRI() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPortName() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public void setRTS(boolean value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDTR(boolean value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXONChar(char c) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXOFFChar(char c) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public char getXONChar() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getXOFFChar() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void sendBreak(int duration) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendXON() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendXOFF() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInBufferBytesCount() throws IOException {
		return is.available();
	}

	@Override
	public int getOutBufferBytesCount() throws IOException {
		return 0;
	}

	@Override
	public void setBreak(boolean value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFlowControl(Set<FlowControl> flowControls) throws IOException {
		this.flowControl = flowControls;
	}

	@Override
	public void setBaudrate(Baudrate baudrate) throws IOException {
		this.baudrate = baudrate;
	}

	@Override
	public void setDataBits(DataBits dataBits) throws IOException {
		this.dataBits = dataBits;
	}

	@Override
	public void setStopBits(StopBits stopBits) throws IOException {
		this.stopBits = stopBits;
	}

	@Override
	public void setParity(Parity parity) throws IOException {
		this.parity = parity;
	}

	@Override
	public Baudrate getBaudrate() throws IOException {
		return baudrate;
	}

	@Override
	public DataBits getDatatBits() throws IOException {
		return dataBits;
	}

	@Override
	public StopBits getStopBits() throws IOException {
		return stopBits;
	}

	@Override
	public Parity getParity() throws IOException {
		return parity;
	}

	@Override
	public Set<FlowControl> getFlowControl() throws IOException {
		return flowControl;
	}

	@Override
	public int getOverallReadTimeout() throws IOException {
		return overallReadTimeout;
	}

	@Override
	public int getInterByteReadTimeout() throws IOException {
		return interByteReadTimeout;
	}

	@Override
	public void setTimeouts(int interByteReadTimeout, int overallReadTimeout, int overallWriteTimeout)
			throws IOException {
		this.interByteReadTimeout = interByteReadTimeout;
		this.overallReadTimeout = overallReadTimeout;
		this.overallWriteTimeout = overallWriteTimeout;
	}

	private void nextRequest() {
		if (data.isEmpty()) {
			os.setData(null);
			is.setData(null);
			currentStackException = null;
		} else {
			final Data d = data.removeFirst();
			os.setData(d);
			is.setData(d);
			currentStackException = d.requestStackException;
		}
	}

	private void addRequest(Data d) {
		boolean insert = data.isEmpty() && os.ptr == -1 && is.readPtr == -1;
		data.add(d);
		if (insert) {
			nextRequest();
		}
	}

	public void addRequest(String request, String response) {
		addRequest(new Data(request, response));
	}

	public void addRequest(String request, int times) {
		Data d = new Data(request);
		for (int i = 0; i < times; i++) {
			addRequest(d);
		}
	}

	public boolean allRequestsHandled() {
		if (data.isEmpty()) {
			if (currentStackException != null) {
				throw new RuntimeException(currentStackException);
			}
			return is.readPtr == -1 && os.ptr == -1;
		} else {
			if (currentStackException != null) {
				throw new RuntimeException(currentStackException);
			}
			return false;
		}
	}

	@Override
	public int getOverallWriteTimeout() throws IOException {
		return overallWriteTimeout;
	}

}
