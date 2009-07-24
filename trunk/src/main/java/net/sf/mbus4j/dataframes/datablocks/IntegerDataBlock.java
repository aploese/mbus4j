package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

public class IntegerDataBlock extends DataBlock {

    private int value;

    public IntegerDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public IntegerDataBlock(DataFieldCode dif, Vif vif) {
       super(dif, vif);
    }

    public IntegerDataBlock(DataFieldCode dif, FunctionField functionField, short subUnit, int tariff, long storageNumber, Vif vif, Vife ... vifes) {
        super(dif, functionField, subUnit, tariff, storageNumber, vif, vifes);
    }

    @Override
    public String getValueAsString() {
        switch (getDataFieldCode()) {
            case _24_BIT_INTEGER :
            case _32_BIT_INTEGER:
        return Integer.toString(value);
            case _6_DIGIT_BCD:
                return String.format("%06d", value);
            case _8_DIGIT_BCD:
                return String.format("%08d", value);
            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
}
