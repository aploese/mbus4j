package net.sf.mbus4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author aploese
 */
@Deprecated
public abstract class Connection implements Serializable {
    public static final int DEFAULT_SPEED = 2400;

    private int bitPerSecond;

    private int responseTimeOutOffset;
    
    public Connection() {
        super();
    }


    public Connection(int bitPerSecond, int responseTimeOutOffset) {
        this.bitPerSecond = bitPerSecond;
        this.responseTimeOutOffset = responseTimeOutOffset;
    }

    /**
     * @return the bitPerSecond
     */
    public int getBitPerSecond() {
        return bitPerSecond;
    }

    public abstract String getName();
    
    
    /**
     * @param bitPerSecond the bitPerSecond to set
     */
    public void setBitPerSecond(int bitPerSecond) {
        this.bitPerSecond = bitPerSecond;
    }

    /**
     * @return the responseTimeOutOffset
     */
    public int getResponseTimeOutOffset() {
        return responseTimeOutOffset;
    }

    /**
     * @param responseTimeOutOffset the responseTimeOutOffset to set
     */
    public void setResponseTimeOutOffset(int responseTimeOutOffset) {
        this.responseTimeOutOffset = responseTimeOutOffset;
    }

    private static final long serialVersionUID = -1;
    private static final int version = 1;

    // Serialization for saveDataSource
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeInt(bitPerSecond);
        out.writeInt(responseTimeOutOffset);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();
        switch (ver) {
            case 1:
                readObjectVer1(in);
                break;
        }
    }

    private void readObjectVer1(ObjectInputStream in) throws IOException {
        bitPerSecond = in.readInt();
        responseTimeOutOffset = in.readInt();
    }

}
