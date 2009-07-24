/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import java.math.BigDecimal;

/**
 *
 * @author aploese
 */
public class BigDecimalDataBlock extends DataBlock {

    BigDecimal value;

        BigDecimalDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public BigDecimalDataBlock(DataBlock dataBlock) {
        super(dataBlock);
    }



    @Override
    public String getValueAsString() {
        return value != null ? value.toPlainString() : null;
    }

}
