/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.mbus4j.dataframes.datablocks;

import java.util.ArrayList;
import java.util.List;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.ObjectAction;
import net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix;
import net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public abstract class DataBlock {

    private ObjectAction action;
    private Vif vif;
    /**
     * Extend paramdescription by VIFE value from table 8.4.5
     * @param vife
     */
    private List<Vife> vifes;
    private FunctionField functionField;
    private long storageNumber;
    private int tariff;
    private final DataFieldCode dataFieldCode;
    private short subUnit;

    public DataBlock(DataBlock dataBlock) {
        this(dataBlock.dataFieldCode);
        this.vif = dataBlock.getVif();
        this.vifes = dataBlock.getVifes();
        this.storageNumber = dataBlock.getStorageNumber();
        this.functionField = dataBlock.getFunctionField();
        this.subUnit = dataBlock.getSubUnit();
        this.tariff = dataBlock.getTariff();
    }

    public DataBlock(DataFieldCode dataFieldCode) {
        super();
        this.dataFieldCode = dataFieldCode;
    }

    public DataBlock(DataFieldCode dif, FunctionField functionField, short subUnit, int tariff, long storageNumber, Vif vif, Vife... vifes) {
        super();
        this.dataFieldCode = dif;
        this.functionField = functionField;
        this.subUnit = subUnit;
        this.tariff = tariff;
        this.storageNumber = storageNumber;
        this.vif = vif;
        if (vifes != null && vifes.length > 0) {
            this.vifes = new ArrayList<Vife>(vifes.length);
            for (Vife vife : vifes) {
                this.vifes.add(vife);
            }
        }
    }

    public DataBlock(DataFieldCode dif, Vif vif, Vife... vifes) {
        super();
        this.dataFieldCode = dif;
        this.vif = vif;
        if (vifes != null && vifes.length > 0) {
            this.vifes = new ArrayList<Vife>(vifes.length);
            for (Vife vife : vifes) {
                this.vifes.add(vife);
            }
        }
    }

    public boolean addVife(Vife vife) {
        if (vifes == null) {
            vifes = new ArrayList<Vife>(1);
        }
        return vifes.add(vife);
    }

    /**
     * @return the action
     */
    public ObjectAction getAction() {
        return action;
    }

    public DataFieldCode getDataFieldCode() {
        return dataFieldCode;
    }

    public Integer getExponent() {
        return vif.getExponent();
    }

    public FunctionField getFunctionField() {
        return functionField;
    }

    public String getParamDescr() {
        StringBuilder sb = new StringBuilder();
        if (vif != null) {
            sb.append(vif.getLabel());
        }
        if (vifes != null) {
            for (Vife vife : vifes) {
                sb.append(" ").append(vife);
            }
        }
        return sb.toString();
    }

    public SiPrefix getSiPrefix() {
        return vif.getSiPrefix();
    }

    public long getStorageNumber() {
        return storageNumber;
    }

    /**
     * @return the deviceUnit
     */
    public short getSubUnit() {
        return subUnit;
    }

    public int getTariff() {
        return tariff;
    }

    public UnitOfMeasurement getUnitOfMeasurement() {
        return vif.getUnitOfMeasurement();
    }

    public abstract String getValueAsString();

    public Vif getVif() {
        return vif;
    }

    public List<Vife> getVifes() {
        return vifes;
    }

    /**
     * @param action the action to set
     */
    public void setAction(ObjectAction action) {
        this.action = action;
    }

    public void setFunctionField(FunctionField functionField) {
        this.functionField = functionField;
    }

    public void setObjectAction(ObjectAction action) {
        this.action = action;
    }

    public void setStorageNumber(long storageNumber) {
        this.storageNumber = storageNumber;
    }

    /**
     * @param deviceUnit the deviceUnit to set
     */
    public void setSubUnit(short subUnit) {
        this.subUnit = subUnit;
    }

    public void setTariff(int tariff) {
        this.tariff = tariff;
    }

    public void setVif(Vif vif) {
        this.vif = vif;
    }

    public void toString(StringBuilder sb, String inset) {
        if (action != null) {
            sb.append(inset).append("action = ").append(getAction()).append("\n");
        }
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        if (vif != null) {
            sb.append(inset).append("description = ").append(getParamDescr()).append("\n");
            if (getDataFieldCode().equals(DataFieldCode.NO_DATA) || getDataFieldCode().equals(DataFieldCode.SELECTION_FOR_READOUT)) {
                sb.append(inset).append("unit =");
            } else {
                sb.append(inset).append("value = ").append(getValueAsString());
            }
            if (getUnitOfMeasurement() != null) {
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
        }
        if (functionField != null) {
            sb.append(inset).append("tariff = ").append(getTariff()).append('\n');
            sb.append(inset).append("storageNumber = ").append(getStorageNumber()).append("\n");
            sb.append(inset).append("functionField = ").append(getFunctionField()).append("\n");
            sb.append(inset).append("subUnit = ").append(getSubUnit()).append("\n");
        }
    }
}
