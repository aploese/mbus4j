/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
 */
public enum SiPrefix {
    PICO("p"),
    NANO("n"),
    MICRO("µ"),
    MILLI("m"),
    CENTI("c"),
    DECI("d"),
    ONE(""),
    DECA("D"),
    HECTO("h"),
    KILO("k"),
    MEGA("M"),
    GIGA("G");


    private final String userFriendlyName;

    private SiPrefix(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }


    @Override
    public String toString() {
        return userFriendlyName;
    }

}
