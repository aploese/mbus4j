package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;

public class ByteDataBlock extends DataBlock {

    private byte value = 0;

    public ByteDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public ByteDataBlock(DataFieldCode dif, Vif vif) {
       super(dif, vif);
    }

    @Override
    public String getValueAsString() {
        switch (getDataFieldCode()) {
            case _8_BIT_INTEGER :
            return Byte.toString(value);
            case _2_DIGIT_BCD :
                return String.format("%02d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }
    }

    /**
     * @return the value
     */
    public byte getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte value) {
        this.value = value;
    }
}
