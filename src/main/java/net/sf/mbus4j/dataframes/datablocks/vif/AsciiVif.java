/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

import net.sf.mbus4j.dataframes.datablocks.*;
import net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;

/**
 *
 * @author aploese
 */
public class AsciiVif implements Vif {

    private String value;

    public AsciiVif() {
    }

    public AsciiVif(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurement() {
        return null;
    }

    @Override
    public SiPrefix getSiPrefix() {
        return null;
    }

    @Override
    public Integer getExponent() {
        return null;
    }

    @Override
    public String toString() {
        return value;
    }


    @Override
    public String getLabel() {
        return value;
    }

}
