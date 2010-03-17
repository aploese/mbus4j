/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
 */
public class VifeManufacturerSpecific implements Vife {

    private byte vifeValue;

    public VifeManufacturerSpecific(byte vifeValue) {
        this.vifeValue = vifeValue;
    }

    @Override
    public String getLabel() {
        return String.format("0x%02x", vifeValue);
    }

    @Override
    public VifeTypes getVifeType() {
        return VifeTypes.MAN_SPEC;
    }

    /**
     * @return the vifeValue
     */
    public byte getVifeValue() {
        return vifeValue;
    }

    /**
     * @param vifeValue the vifeValue to set
     */
    public void setVifeValue(byte vifeValue) {
        this.vifeValue = vifeValue;
    }

}
