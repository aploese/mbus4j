package net.sf.mbus4j.dataframes.datablocks;

import java.text.DateFormat;
import java.util.Date;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

public class DateDataBlock extends DataBlock {

    private Date value;

    public DateDataBlock(DataBlock dataBlock) {
        super(dataBlock);
    }

    public DateDataBlock(Vif vif, Vife ... vifes) {
        super(DataFieldCode._16_BIT_INTEGER, vif, vifes);
    }

    @Override
    public String getValueAsString() {
        return DateFormat.getDateInstance().format(value);
    }

    /**
     * @return the value
     */
    public Date getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Date value) {
        this.value = value;
    }

}
