/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author aploese
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

    public void setDataInformationBlock(DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber) {
        this.difCode = difCode;
        this.functionField = functionField;
        this.deviceUnit = deviceUnit;
        this.tariff = tariff;
        this.storageNumber = storageNumber;
    }

    public void setValueInformationBlock(Vif vif, Vife ... vifes) {
        this.vif = vif;
        this.vifes = vifes;
    }

    public DataTag addAlternative() {
        try {
            alternative = (DataTag) clone();
        } catch (CloneNotSupportedException ex) {
            throw  new RuntimeException(ex);
        }
        return alternative;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    /**
     * @return the difCode
     */
    public DataFieldCode getDifCode() {
        return difCode;
    }

    /**
     * @param difCode the difCode to set
     */
    public void setDifCode(DataFieldCode difCode) {
        this.difCode = difCode;
    }

    /**
     * @return the functionField
     */
    public FunctionField getFunctionField() {
        return functionField;
    }

    /**
     * @param functionField the functionField to set
     */
    public void setFunctionField(FunctionField functionField) {
        this.functionField = functionField;
    }

    /**
     * @return the deviceUnit
     */
    public int getDeviceUnit() {
        return deviceUnit;
    }

    /**
     * @param deviceUnit the deviceUnit to set
     */
    public void setDeviceUnit(int deviceUnit) {
        this.deviceUnit = deviceUnit;
    }

    /**
     * @return the tariff
     */
    public int getTariff() {
        return tariff;
    }

    /**
     * @param tariff the tariff to set
     */
    public void setTariff(int tariff) {
        this.tariff = tariff;
    }

    /**
     * @return the storageNumber
     */
    public int getStorageNumber() {
        return storageNumber;
    }

    /**
     * @param storageNumber the storageNumber to set
     */
    public void setStorageNumber(int storageNumber) {
        this.storageNumber = storageNumber;
    }

    /**
     * @return the vif
     */
    public Vif getVif() {
        return vif;
    }

    /**
     * @param vif the vif to set
     */
    public DataTag setVif(Vif vif) {
        this.vif = vif;
        return this;
    }

    /**
     * @return the vifes
     */
    public Vife[] getVifes() {
        return vifes;
    }

    /**
     * @param vifes the vifes to set
     */
    public void setVifes(Vife[] vifes) {
        this.vifes = vifes;
    }

    /**
     * @return the alternative
     */
    public DataTag getAlternative() {
        return alternative;
    }

    /**
     * @param alternative the alternative to set
     */
    public void setAlternative(DataTag alternative) {
        this.alternative = alternative;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }


    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder().append(getDifCode()).append(getFunctionField()).append(getDeviceUnit()).append(getTariff()).append(getStorageNumber()).append(getVif());
        for (Vife vife: vifes) {
            b.append(vife);
        }
        return b.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && DataTag.class.isAssignableFrom(o.getClass())) {
            DataTag dto = (DataTag)o;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("vif", getVif()).toString();
    }

    public DataTag addAlternativeVif(Vif vif) {
        return addAlternative().setVif(vif);
    }

}
