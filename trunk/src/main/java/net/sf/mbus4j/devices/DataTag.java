/*
 * mbus4j - Open source drivers for mbus protocol (www.mbus.com) - http://mbus4j.sourceforge.net/
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
package net.sf.mbus4j.devices;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class DataTag implements Cloneable {

    private DataFieldCode difCode;
    private FunctionField functionField;
    private int deviceUnit;
    private int tariff;
    private int storageNumber;
    private Vif vif;
    private Vife[] vifes;
    private DataTag alternative;
    private boolean optional;
    private String label;

    public DataTag addAlternative() {
        try {
            alternative = (DataTag) clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        return alternative;
    }

    public DataTag addAlternativeVif(Vif vif) {
        return addAlternative().setVif(vif);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && DataTag.class.isAssignableFrom(o.getClass())) {
            DataTag dto = (DataTag) o;
            EqualsBuilder b = new EqualsBuilder().append(getDifCode(), dto.getDifCode()).append(getFunctionField(), dto.getFunctionField()).append(getDeviceUnit(), dto.getDeviceUnit()).append(getTariff(), dto.getTariff()).append(getStorageNumber(), dto.getStorageNumber()).append(getVif(), dto.getVif());
            if (getVifes().length != dto.getVifes().length) {
                return false;
            }
            for (int i = 0; i < getVifes().length; i++) {
                b.append(getVifes()[i], dto.getVifes()[i]);
            }
            return b.isEquals();
        }
        return false;
    }

    /**
     * @return the alternative
     */
    public DataTag getAlternative() {
        return alternative;
    }

    /**
     * @return the deviceUnit
     */
    public int getDeviceUnit() {
        return deviceUnit;
    }

    /**
     * @return the difCode
     */
    public DataFieldCode getDifCode() {
        return difCode;
    }

    /**
     * @return the functionField
     */
    public FunctionField getFunctionField() {
        return functionField;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the storageNumber
     */
    public int getStorageNumber() {
        return storageNumber;
    }

    /**
     * @return the tariff
     */
    public int getTariff() {
        return tariff;
    }

    /**
     * @return the vif
     */
    public Vif getVif() {
        return vif;
    }

    /**
     * @return the vifes
     */
    public Vife[] getVifes() {
        return vifes;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder().append(getDifCode()).append(getFunctionField()).append(getDeviceUnit()).append(getTariff()).append(getStorageNumber()).append(getVif());
        for (Vife vife : vifes) {
            b.append(vife);
        }
        return b.toHashCode();
    }

    public boolean isOptional() {
        return optional;
    }

    /**
     * @param alternative the alternative to set
     */
    public void setAlternative(DataTag alternative) {
        this.alternative = alternative;
    }

    public void setDataInformationBlock(DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber) {
        this.difCode = difCode;
        this.functionField = functionField;
        this.deviceUnit = deviceUnit;
        this.tariff = tariff;
        this.storageNumber = storageNumber;
    }

    /**
     * @param deviceUnit the deviceUnit to set
     */
    public void setDeviceUnit(int deviceUnit) {
        this.deviceUnit = deviceUnit;
    }

    /**
     * @param difCode the difCode to set
     */
    public void setDifCode(DataFieldCode difCode) {
        this.difCode = difCode;
    }

    /**
     * @param functionField the functionField to set
     */
    public void setFunctionField(FunctionField functionField) {
        this.functionField = functionField;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * @param storageNumber the storageNumber to set
     */
    public void setStorageNumber(int storageNumber) {
        this.storageNumber = storageNumber;
    }

    /**
     * @param tariff the tariff to set
     */
    public void setTariff(int tariff) {
        this.tariff = tariff;
    }

    public void setValueInformationBlock(Vif vif, Vife... vifes) {
        this.vif = vif;
        this.vifes = vifes;
    }

    /**
     * @param vif the vif to set
     */
    public DataTag setVif(Vif vif) {
        this.vif = vif;
        return this;
    }

    /**
     * @param vifes the vifes to set
     */
    public void setVifes(Vife[] vifes) {
        this.vifes = vifes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("vif", getVif()).toString();
    }
}
