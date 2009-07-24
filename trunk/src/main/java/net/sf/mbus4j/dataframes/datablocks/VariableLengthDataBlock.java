/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;

/**
 *
 * @author aploese
 */
public class VariableLengthDataBlock extends DataBlock {

    public VariableLengthDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    @Override
    public String getValueAsString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
