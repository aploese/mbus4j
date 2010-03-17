/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.master;

import net.sf.mbus4j.MBusAddressing;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author aploese
 */
public class ValueRequestPointLocator {

        private MBusAddressing addressing;
        private byte address;
        private String manufacturer;
        private int identnumber;
        private MBusMedium medium;
        private int version;

        private String responseFrameName;

        private DataFieldCode difCode;
        private FunctionField functionField;
        private int deviceUnit;
        private int tariff;
        private long storageNumber;
        private Vif vif;
        private Vife[] vifes;

        private DataBlock db;
        private DataBlock timestampDb;
        private UserDataResponse fullResponse;

    /**
     * @return the addressing
     */
    public MBusAddressing getAddressing() {
        return addressing;
    }

    /**
     * @param addressing the addressing to set
     */
    public void setAddressing(MBusAddressing addressing) {
        this.addressing = addressing;
    }

    /**
     * @return the address
     */
    public byte getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(byte address) {
        this.address = address;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @return the identnumber
     */
    public int getIdentnumber() {
        return identnumber;
    }

    /**
     * @param identnumber the identnumber to set
     */
    public void setIdentnumber(int identnumber) {
        this.identnumber = identnumber;
    }

    /**
     * @return the medium
     */
    public MBusMedium getMedium() {
        return medium;
    }

    /**
     * @param medium the medium to set
     */
    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the responseFrameName
     */
    public String getResponseFrameName() {
        return responseFrameName;
    }

    /**
     * @param responseFrameName the responseFrameName to set
     */
    public void setResponseFrameName(String responseFrameName) {
        this.responseFrameName = responseFrameName;
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

    /**
     * @return the timestampDb
     */
    public DataBlock getTimestampDb() {
        return timestampDb;
    }

    /**
     * @param timestampDb the timestampDb to set
     */
    public void setTimestampDb(DataBlock timestampDb) {
        this.timestampDb = timestampDb;
    }

    /**
     * @return the fullResponse
     */
    public UserDataResponse getFullResponse() {
        return fullResponse;
    }

    /**
     * @param fullResponse the fullResponse to set
     */
    public void setFullResponse(UserDataResponse fullResponse) {
        this.fullResponse = fullResponse;
    }

}
