/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes;

/**
 *
 * @author aploese
 */
public interface Frame {

    public enum ControlCode {

        SND_NKE,
        SND_UD,
        CON_ACK,
        REQ_UD2,
        REQ_UD1,
        RSP_UD;
    }

    ControlCode getControlCode();

    //TODO GarbageFrame for unparsable stuff ie multiple answers??
    //TODO Timespamps for send/receive tracking
//    long getFirstByteTimeStamp();
//    void setFirstByteTimeStamp(long timestamp);
//    long getLastByteTimeStamp();
//    void setLastByteTimeStamp(long timestamp);

}
