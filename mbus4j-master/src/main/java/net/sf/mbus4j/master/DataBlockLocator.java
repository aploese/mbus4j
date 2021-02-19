/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package net.sf.mbus4j.master;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author Arne Plöse
 */
public class DataBlockLocator<T> {

    private DataFieldCode difCode;
    private FunctionField functionField;
    private int deviceUnit;
    private int tariff;
    private long storageNumber;
    private Vif vif;
    private Vife[] vifes = DataBlock.EMPTY_VIFE;
    private DataBlock db;
    private T reference;

    /**
     * @return the reference
     */
    public T getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(T reference) {
        this.reference = reference;
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
    public long getStorageNumber() {
        return storageNumber;
    }

    /**
     * @param storageNumber the storageNumber to set
     */
    public void setStorageNumber(long storageNumber) {
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
    public void setVif(Vif vif) {
        this.vif = vif;
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
     * @return the db
     */
    public DataBlock getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(DataBlock db) {
        this.db = db;
    }

}
