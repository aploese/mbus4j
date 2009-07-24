/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

/**
 *
 * @author aploese
 */
public interface ShortFrame extends Frame, PrimaryAddress {
    void setFcb(boolean fcb);
    boolean isFcb();
    void setFcv(boolean fcv);
    boolean isFcv();
}
