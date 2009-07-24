package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.AsciiVif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;


public class StringDataBlock extends DataBlock {

    private String data;

    StringDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    public StringDataBlock(DataBlock dataBlock) {
        super(dataBlock);
    }

    public StringDataBlock(DataFieldCode dfc, Vif vif) {
        super(dfc, vif);
    }

    @Override
    public String getValueAsString() {
        return data;
    }

    public void setValue(String data) {
        this.data = data;
    }

    public String getValue() {
        return data;
    }

}
