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
public class ReadOutDataBlock extends DataBlock {

    public ReadOutDataBlock(DataFieldCode dfc) {
        super(dfc);
    }

    @Override
    public String getValueAsString() {
        return "(selected for read out)";
    }

}
