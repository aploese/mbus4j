package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.NotSupportedException;

public class LongDataBlock extends DataBlock {

    private long value;

    public LongDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    @Override
    public String getValueAsString() {
        switch (getDataFieldCode()) {
            case _48_BIT_INTEGER:
            case _64_BIT_INTEGER:
        return Long.toString(value);
            case _8_DIGIT_BCD:
                return String.format("%08d", value);
            case _12_DIGIT_BCD:
                return String.format("%012d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }    }

    /**
     * @return the value
     */
    public long getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }
}
