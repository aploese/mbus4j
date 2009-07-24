/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

/**
 *
 * @author aploese
 */
public interface SingleCharFrame extends Frame {

    public final static SingleCharFrame SINGLE_CHAR_FRAME = new SingleCharFrameImpl();

    static class SingleCharFrameImpl implements SingleCharFrame {

    @Override
    public ControlCode getControlCode() {
        return ControlCode.CON_ACK;
    }



}


}
