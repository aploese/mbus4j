/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
 */
public interface Vife {

    public final static String FACTOR = "Multiplicative correction factor";
    public final static String CONST = "Additive correction constant";
    public final static String TIMESTAMP_OF_LIMIT_EXCEED = "=> Date(/time) %s of %s %s limit exceed";
    public final static String DURATION_OF_LIMIT_EXCEED = "=> Duration of %s %s limit exceeds %s";
    public final static String DURATION_OF = "=> Duration of %s %s";
    public final static String TIMESTAMP_OF = "=> Date(/time) %s of %s";
    public final static String BEGIN = "begin";
    public final static String END = "end";
    public final static String FIRST = "first";
    public final static String LAST = "last";
    public final static String LOWER = "lower";
    public final static String UPPER = "upper";
}
