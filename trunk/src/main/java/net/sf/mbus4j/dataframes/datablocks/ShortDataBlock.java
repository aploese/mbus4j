package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.NotSupportedException;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;

public class ShortDataBlock extends DataBlock {

    private short value = 0;

    public ShortDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public ShortDataBlock(DataFieldCode dif, Vif vif) {
       super(dif, vif);
    }

    @Override
    public String getValueAsString() {
        switch (getDataFieldCode()) {
            case _16_BIT_INTEGER:
        return Short.toString(value);
            case _4_DIGIT_BCD:
                return String.format("%04d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }    }

    /**
     * @return the value
     */
    public short getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(short value) {
        this.value = value;
    }
}
