/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks;

/**
 *
 * @author aploese
 */
public interface BcdValue {

    public boolean isBcd();

    public boolean isBcdError();

    public String getBcdError();

    public void setBcdError(String bcdError);

}
