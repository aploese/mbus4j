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
}
