package net.sf.mbus4j.serial;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jssc.SerialNativeInterface;

/**
 *
 * @author scream3r
 */
public class SerialStreamFactory implements Closeable {

    private class SerialInputStream extends InputStream {

        private byte[] buffer = new byte[0];
        private int buffPos;

        @Override
        public void close() throws SerialPortException {
                SerialStreamFactory.this.close();
        }

        @Override
        public int read() throws IOException {
            if (buffer.length <= buffPos) {
                int inByteCount;
                while ((inByteCount = getInputBufferBytesCount()) <= 0) {
                    try {
                        Thread.sleep(10000 / baudrate); // at least 10 bits are needed per byte Starbit + 8 Databits + 1 Stopbit
                    } catch (InterruptedException ex) {

                    }
                    if (!portOpened) {
                        return -1;
                    }
                }
                buffer = serialInterface.readBytes(portHandle, inByteCount);
                buffPos = 0;
            }
            return buffer[buffPos++] & 0xFF;
        }

    }

    private class SerialOutputStream extends OutputStream {

        @Override
        public void close() throws SerialPortException {
                SerialStreamFactory.this.close();
        }

        @Override
        public void write(int b) throws SerialPortException {
            checkPortOpened("SerialOutputStream.close()");
            serialInterface.writeBytes(portHandle, new byte[]{(byte) b});
        }

        @Override
        public void write(byte[] b) throws SerialPortException {
            serialInterface.writeBytes(portHandle, b);
        }
    }

    private SerialInputStream is;
    private SerialOutputStream os;
    private int baudrate = 9600;

    public SerialInputStream getInputStream() {
        if (is == null) {
            is = new SerialInputStream();
        }
        return is;
    }

    public SerialOutputStream getOutputStream() {
        if (os == null) {
            os = new SerialOutputStream();
        }
        return os;
    }

    private SerialNativeInterface serialInterface;
    private long portHandle;
    private String portName;
    private boolean portOpened = false;

    public enum Baudrate {

        B_110(110),
        B_300(300),
        B_600(600),
        B_1200(1200),
        B_4800(4800),
        B_9600(9600),
        B_14400(14400),
        B_19200(19200),
        B_38400(38400),
        B_57600(57600),
        B_115200(115200),
        B_128000(128000),
        B_230400(230400),
        B_256000(256000),
        B_460800(460800),
        B_921600(921600);

        final int value;

        private Baudrate(int baudrate) {
            this.value = baudrate;
        }

    }

    public enum DataBits {

        DB_5(5),
        DB_6(6),
        DB_7(7),
        DB_8(8);
        final int value;

        private DataBits(int dataBits) {
            this.value = dataBits;
        }

    }

    public enum StopBits {

        SB_1(0),
        SB_1_5(1),
        SB_2(2);

        final int value;

        private StopBits(int stopBits) {
            this.value = stopBits;
        }

    }

    public enum Parity {

        NONE(0),
        ODD(1),
        EVEN(2),
        MARK(3),
        SPACE(4);
        final int value;

        private Parity(int value) {
            this.value = value;
        }

    }

    public static final int PURGE_RXABORT = 0x0002;
    public static final int PURGE_RXCLEAR = 0x0008;
    public static final int PURGE_TXABORT = 0x0001;
    public static final int PURGE_TXCLEAR = 0x0004;

    public static final int MASK_RXCHAR = 1;
    public static final int MASK_RXFLAG = 2;
    public static final int MASK_TXEMPTY = 4;
    public static final int MASK_CTS = 8;
    public static final int MASK_DSR = 16;
    public static final int MASK_RLSD = 32;
    public static final int MASK_BREAK = 64;
    public static final int MASK_ERR = 128;
    public static final int MASK_RING = 256;

    //since 0.8 ->
    public static final int FLOWCONTROL_NONE = 0;
    public static final int FLOWCONTROL_RTSCTS_IN = 1;
    public static final int FLOWCONTROL_RTSCTS_OUT = 2;
    public static final int FLOWCONTROL_XONXOFF_IN = 4;
    public static final int FLOWCONTROL_XONXOFF_OUT = 8;
    //<- since 0.8

    //since 0.8 ->
    public static final int ERROR_FRAME = 0x0008;
    public static final int ERROR_OVERRUN = 0x0002;
    public static final int ERROR_PARITY = 0x0004;
    //<- since 0.8

    //since 2.6.0 ->
    private static final int PARAMS_FLAG_IGNPAR = 1;
    private static final int PARAMS_FLAG_PARMRK = 2;
    //<- since 2.6.0

    public SerialStreamFactory(String portName) {
        this.portName = portName;
        serialInterface = new SerialNativeInterface();
    }

    /**
     * Getting port name under operation
     *
     * @return Method returns port name under operation as a String
     */
    public String getPortName() {
        return portName;
    }

    /**
     * Getting port state
     *
     * @return Method returns true if port is open, otherwise false
     */
    public boolean isOpened() {
        return portOpened;
    }

    /**
     * Port opening
     * <br><br>
     * <b>Note: </b>If port busy <b>TYPE_PORT_BUSY</b> exception will be thrown.
     * If port not found <b>TYPE_PORT_NOT_FOUND</b> exception will be thrown.
     *
     * @return If the operation is successfully completed, the method returns
     * true
     *
     * @throws SerialPortException
     */
    public boolean open() throws SerialPortException {
        if (portOpened) {
            throw new SerialPortException(portName, "openPort()", SerialPortException.Type.PORT_ALREADY_OPENED);
        }
        if (portName != null) {
            boolean useTIOCEXCL = (System.getProperty(SerialNativeInterface.PROPERTY_JSSC_NO_TIOCEXCL) == null
                    && System.getProperty(SerialNativeInterface.PROPERTY_JSSC_NO_TIOCEXCL.toLowerCase()) == null);
            portHandle = serialInterface.openPort(portName, useTIOCEXCL);//since 2.3.0 -> (if JSSC_NO_TIOCEXCL defined, exclusive lock for serial port will be disabled)
        } else {
            throw new SerialPortException(portName, "openPort()", SerialPortException.Type.NULL_NOT_PERMITTED);//since 2.1.0 -> NULL port name fix
        }
        if (portHandle == SerialNativeInterface.ERR_PORT_BUSY) {
            throw new SerialPortException(portName, "openPort()", SerialPortException.Type.PORT_BUSY);
        } else if (portHandle == SerialNativeInterface.ERR_PORT_NOT_FOUND) {
            throw new SerialPortException(portName, "openPort()", SerialPortException.Type.PORT_NOT_FOUND);
        } else if (portHandle == SerialNativeInterface.ERR_PERMISSION_DENIED) {
            throw new SerialPortException(portName, "openPort()", SerialPortException.Type.PERMISSION_DENIED);
        } else if (portHandle == SerialNativeInterface.ERR_INCORRECT_SERIAL_PORT) {
            throw new SerialPortException(portName, "openPort()", SerialPortException.Type.INCORRECT_SERIAL_PORT);
        }
        portOpened = true;
        return true;
    }

    public boolean setParams(Baudrate baudRate, DataBits dataBits, StopBits stopBits, Parity parity, boolean setRTS, boolean setDTR) throws SerialPortException {
        return setParams(baudRate.value, dataBits, stopBits, parity, setRTS, setDTR);
    }
    /**
     * Setting the parameters of port
     *
     * @param baudRate data transfer rate
     * @param dataBits number of data bits
     * @param stopBits number of stop bits
     * @param parity parity
     * @param setRTS initial state of RTS line(ON/OFF)
     * @param setDTR initial state of DTR line(ON/OFF)
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false
     *
     * @throws SerialPortException
     *
     * @since 0.8
     */
    public boolean setParams(int baudRate, DataBits dataBits, StopBits stopBits, Parity parity, boolean setRTS, boolean setDTR) throws SerialPortException {
        checkPortOpened("setParams()");
        int flags = 0;
        if (System.getProperty(SerialNativeInterface.PROPERTY_JSSC_IGNPAR) != null || System.getProperty(SerialNativeInterface.PROPERTY_JSSC_IGNPAR.toLowerCase()) != null) {
            flags |= PARAMS_FLAG_IGNPAR;
        }
        if (System.getProperty(SerialNativeInterface.PROPERTY_JSSC_PARMRK) != null || System.getProperty(SerialNativeInterface.PROPERTY_JSSC_PARMRK.toLowerCase()) != null) {
            flags |= PARAMS_FLAG_PARMRK;
        }
        this.baudrate = baudRate;
        return serialInterface.setParams(portHandle, baudRate, dataBits.value, stopBits.value, parity.value, setRTS, setDTR, flags);
    }

    /**
     * Purge of input and output buffer. Required flags shall be sent to the
     * input. Variables with prefix
     * <b>"PURGE_"</b>, for example <b>"PURGE_RXCLEAR"</b>. Sent parameter
     * "flags" is additive value, so addition of flags is allowed. For example,
     * if input or output buffer shall be purged, parameter <b>"PURGE_RXCLEAR |
     * PURGE_TXCLEAR"</b>.
     * <br><b>Note: </b>some devices or drivers may not support this function
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false.
     *
     * @throws SerialPortException
     */
    public boolean purgePort(int flags) throws SerialPortException {
        checkPortOpened("purgePort()");
        return serialInterface.purgePort(portHandle, flags);
    }

    /**
     * Change RTS line state. Set "true" for switching ON and "false" for
     * switching OFF RTS line
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false
     *
     * @throws SerialPortException
     */
    public boolean setRTS(boolean enabled) throws SerialPortException {
        checkPortOpened("setRTS()");
        return serialInterface.setRTS(portHandle, enabled);
    }

    /**
     * Change DTR line state. Set "true" for switching ON and "false" for
     * switching OFF DTR line
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false
     *
     * @throws SerialPortException
     */
    public boolean setDTR(boolean enabled) throws SerialPortException {
        checkPortOpened("setDTR()");
        return serialInterface.setDTR(portHandle, enabled);
    }

    /**
     * Get count of bytes in input buffer
     *
     * @return Count of bytes in input buffer or -1 if error occured
     *
     * @throws SerialPortException
     *
     * @since 0.8
     */
    public int getInputBufferBytesCount() throws SerialPortException {
        checkPortOpened("getInputBufferBytesCount()");
        return serialInterface.getBuffersBytesCount(portHandle)[0];
    }

    /**
     * Get count of bytes in output buffer
     *
     * @return Count of bytes in output buffer or -1 if error occured
     *
     * @throws SerialPortException
     *
     * @since 0.8
     */
    public int getOutputBufferBytesCount() throws SerialPortException {
        checkPortOpened("getOutputBufferBytesCount()");
        return serialInterface.getBuffersBytesCount(portHandle)[1];
    }

    /**
     * Set flow control mode. For required mode use variables with prefix
     * <b>"FLOWCONTROL_"</b>. Example of hardware flow control mode(RTS/CTS):
     * setFlowControlMode(FLOWCONTROL_RTSCTS_IN | FLOWCONTROL_RTSCTS_OUT);
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false
     *
     * @throws SerialPortException
     *
     * @since 0.8
     */
    public boolean setFlowControlMode(int mask) throws SerialPortException {
        checkPortOpened("setFlowControlMode()");
        return serialInterface.setFlowControlMode(portHandle, mask);
    }

    /**
     * Get flow control mode
     *
     * @return Mask of setted flow control mode
     *
     * @throws SerialPortException
     *
     * @since 0.8
     */
    public int getFlowControlMode() throws SerialPortException {
        checkPortOpened("getFlowControlMode()");
        return serialInterface.getFlowControlMode(portHandle);
    }

    /**
     * Send Break singnal for setted duration
     *
     * @param duration duration of Break signal
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false
     *
     * @throws SerialPortException
     *
     * @since 0.8
     */
    public boolean sendBreak(int duration) throws SerialPortException {
        checkPortOpened("sendBreak()");
        return serialInterface.sendBreak(portHandle, duration);
    }

    private int[][] waitEvents() {
        return serialInterface.waitEvents(portHandle);
    }

    /**
     * Check port opened (since jSSC-0.8 String "EMPTY" was replaced with
     * "portName" variable)
     *
     * @param methodName method name
     *
     * @throws SerialPortException
     */
    private void checkPortOpened(String methodName) throws SerialPortException {
        if (!portOpened) {
            throw new SerialPortException(portName, methodName, SerialPortException.Type.PORT_NOT_OPENED);
        }
    }

    /**
     * Getting lines status. Lines status is sent as 0 – OFF and 1 - ON
     *
     * @return Method returns the array containing information about lines in
     * following order:
     * <br><b>element 0</b> - <b>CTS</b> line state</br>
     * <br><b>element 1</b> - <b>DSR</b> line state</br>
     * <br><b>element 2</b> - <b>RING</b> line state</br>
     * <br><b>element 3</b> - <b>RLSD</b> line state</br>
     *
     * @throws SerialPortException
     */
    public int[] getLinesStatus() throws SerialPortException {
        checkPortOpened("getLinesStatus()");
        return serialInterface.getLinesStatus(portHandle);
    }

    /**
     * Get state of CTS line
     *
     * @return If line is active, method returns true, otherwise false
     *
     * @throws SerialPortException
     */
    public boolean isCTS() throws SerialPortException {
        checkPortOpened("isCTS()");
        if (serialInterface.getLinesStatus(portHandle)[0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get state of DSR line
     *
     * @return If line is active, method returns true, otherwise false
     *
     * @throws SerialPortException
     */
    public boolean isDSR() throws SerialPortException {
        checkPortOpened("isDSR()");
        if (serialInterface.getLinesStatus(portHandle)[1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get state of RING line
     *
     * @return If line is active, method returns true, otherwise false
     *
     * @throws SerialPortException
     */
    public boolean isRING() throws SerialPortException {
        checkPortOpened("isRING()");
        if (serialInterface.getLinesStatus(portHandle)[2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get state of RLSD line
     *
     * @return If line is active, method returns true, otherwise false
     *
     * @throws SerialPortException
     */
    public boolean isRLSD() throws SerialPortException {
        checkPortOpened("isRLSD()");
        if (serialInterface.getLinesStatus(portHandle)[3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Close port. This method deletes event listener first, then closes the
     * port
     *
     * @return If the operation is successfully completed, the method returns
     * true, otherwise false
     *
     * @throws SerialPortException
     */
    @Override
    public void close() throws SerialPortException {
        checkPortOpened("closePort()");
        boolean returnValue = serialInterface.closePort(portHandle);
        if (returnValue) {
            portOpened = false;
        }
    }

}
