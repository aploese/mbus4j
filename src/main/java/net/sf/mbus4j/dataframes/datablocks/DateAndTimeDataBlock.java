package net.sf.mbus4j.dataframes.datablocks;

import java.text.DateFormat;
import java.util.Date;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

public class DateAndTimeDataBlock extends DataBlock {

    private Date value;
    private boolean valid;
    private boolean summerTime;
    private boolean res1;
    private boolean res2;
    private boolean res3;


    public DateAndTimeDataBlock(DataBlock dataBlock) {
        super(dataBlock);
    }

     public DateAndTimeDataBlock(Vif vif, Vife ... vifes) {
       super(DataFieldCode._32_BIT_INTEGER, vif, vifes);
    }

   @Override
    public String getValueAsString() {
        return DateFormat.getDateTimeInstance().format(value);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    /**
     * @return the summerTime
     */
    public boolean isSummerTime() {
        return summerTime;
    }

    /**
     * @param summerTime the summerTime to set
     */
    public void setSummerTime(boolean summerTime) {
        this.summerTime = summerTime;
    }

    @Override
    public void toString(StringBuilder sb, String inset) {
        if (getAction() != null) {
            sb.append(inset).append("action = ").append(getAction()).append("\n");
        }
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        if (getVif() != null) {
            sb.append(inset).append("description = ").append(getParamDescr()).append("\n");
            if (getUnitOfMeasurement() != null) {
                sb.append(inset).append("unit =");
                if (getExponent() != null) {

                    if (getExponent() > 0) {
                        sb.append(" * 1");
                        for (int i = 0; i < getExponent(); i++) {
                            sb.append("0");
                        }
                    } else if (getExponent() < 0) {
                        sb.append(" * 0.");
                        for (int i = -1; i > getExponent(); i--) {
                            sb.append("0");
                        }
                        sb.append("1");

                    }
                }
                sb.append(" [");
                if (getSiPrefix() != null) {
                    sb.append(getSiPrefix());
                }
                sb.append(getUnitOfMeasurement()).append("]\n");
            } else {
                sb.append("\n");
            }
            sb.append(inset).append("value = ").append(getValueAsString()).append("\n");
        }
        if (getFunctionField() != null) {
        sb.append(inset).append("tariff = ").append(getTariff()).append('\n');
        sb.append(inset).append("storageNumber = ").append(getStorageNumber()).append("\n");
        sb.append(inset).append("functionField = ").append(getFunctionField()).append("\n");
        sb.append(inset).append("subUnit = ").append(getSubUnit()).append("\n");
        }
        sb.append(inset).append("valid = ").append(valid).append("\n");
        sb.append(inset).append("summertime = ").append(summerTime).append("\n");
        sb.append(inset).append("res1 = ").append(res1).append("\n");
        sb.append(inset).append("res2 = ").append(res2).append("\n");
        sb.append(inset).append("res3 = ").append(res3).append("\n");
    }

    /**
     * @return the res1
     */
    public boolean isRes1() {
        return res1;
    }

    /**
     * @param res1 the res1 to set
     */
    public void setRes1(boolean res1) {
        this.res1 = res1;
    }

    /**
     * @return the res2
     */
    public boolean isRes2() {
        return res2;
    }

    /**
     * @param res2 the res2 to set
     */
    public void setRes2(boolean res2) {
        this.res2 = res2;
    }

    /**
     * @return the res3
     */
    public boolean isRes3() {
        return res3;
    }

    /**
     * @param res3 the res3 to set
     */
    public void setRes3(boolean res3) {
        this.res3 = res3;
    }
}
