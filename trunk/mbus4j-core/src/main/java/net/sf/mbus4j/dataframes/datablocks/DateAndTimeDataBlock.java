/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009  Arne Plöse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.dataframes.datablocks;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
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

    public DateAndTimeDataBlock(Vif vif, Vife... vifes) {
        super(DataFieldCode._32_BIT_INTEGER, vif, vifes);
    }

    /**
     * @return the value
     */
    public Date getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return DateFormat.getDateTimeInstance().format(value);
    }

    /**
     * @return the res1
     */
    public boolean isRes1() {
        return res1;
    }

    /**
     * @return the res2
     */
    public boolean isRes2() {
        return res2;
    }

    /**
     * @return the res3
     */
    public boolean isRes3() {
        return res3;
    }

    /**
     * @return the summerTime
     */
    public boolean isSummerTime() {
        return summerTime;
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * @param res1 the res1 to set
     */
    public void setRes1(boolean res1) {
        this.res1 = res1;
    }

    /**
     * @param res2 the res2 to set
     */
    public void setRes2(boolean res2) {
        this.res2 = res2;
    }

    /**
     * @param res3 the res3 to set
     */
    public void setRes3(boolean res3) {
        this.res3 = res3;
    }

    /**
     * @param summerTime the summerTime to set
     */
    public void setSummerTime(boolean summerTime) {
        this.summerTime = summerTime;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Date value) {
        this.value = value;
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

    @Override
    public JSONObject toJSON(boolean isTemplate) {
        JSONObject result = super.toJSON(isTemplate);
        if (!isTemplate) {
            DateFormat df = DateFormat.getDateTimeInstance();
            JSONObject jsonValue = new JSONObject();
            jsonValue.accumulate("timestamp", df.format(value));
            jsonValue.accumulate("summertime", summerTime);
            jsonValue.accumulate("valid", valid);
            jsonValue.accumulate("res1", res1);
            jsonValue.accumulate("res2", res2);
            jsonValue.accumulate("res3", res3);
            result.accumulate("data", jsonValue);
        }
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
            super.fromJSON(json);
        try {
            JSONObject jsonValue = json.getJSONObject("data");
            valid = jsonValue.getBoolean("valid");
            summerTime = jsonValue.getBoolean("summertime");
            res1 = jsonValue.getBoolean("res1");
            res2 = jsonValue.getBoolean("res2");
            res3 = jsonValue.getBoolean("res3");
            DateFormat df = DateFormat.getDateTimeInstance();
            value = df.parse(jsonValue.getString("timestamp"));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
