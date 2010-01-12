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
package net.sf.mbus4j.slaves.acw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode.*;
import static net.sf.mbus4j.dataframes.datablocks.dif.FunctionField.*;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.*;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifeStd.*;

import java.util.Date;

import java.util.List;
import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.ResponseFrame;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateAndTimeDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RawDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RealDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeStd;
import net.sf.mbus4j.slaves.Slave;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class AcwHeatMeter extends Slave implements MBusResponseFramesContainer, ProxyDevice {

    //TODO isACF from MBusResponseFramesContainer ???
    @Override
    public void setDfc(boolean dfc) {
        super.setDfc(dfc);
    }

    private class AcwHeatMeterResponseFrame extends ResponseFrame {

        private State myState;

        @Override
        public Iterable<DataBlock> getDataBlocksIterable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getDataBlockCount() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Frame[] getinitFrames() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public DataBlock getDataBlock(int i) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Iterator<DataBlock> iterator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    public AcwHeatMeter(UserDataResponse udResp) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setAddress(int address) {
        super.setAddress(address);
    }

    @Override
    public void setVersion(int version) {
        super.setVersion(version);
    }

    @Override
    public boolean isDfc() {
        return super.isDfc();
    }

    @Override
    public int getAddress() {
        return super.getAddress();
    }

    @Override
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public ResponseFrame getResponseFrame(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseFrame[] getResponseFrames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getResponseFrameCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<ResponseFrame> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<ResponseFrame, Collection<DataBlock>> readValues(Sender sender, ResponseFrame... responseFrames) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static enum State {

        STANDARD,
        ERROR,
        EFFECTIVE_DAY_1,
        EFFECTIVE_DAY_2,
        EFFECTIVE_DAY_3,
        EFFECTIVE_DAY_4,
        EFFECTIVE_DAY_5,
        EFFECTIVE_DAY_6,
        EFFECTIVE_DAY_7,
        EFFECTIVE_DAY_8,
        EFFECTIVE_DAY_9,
        EFFECTIVE_DAY_10,
        EFFECTIVE_DAY_11,
        EFFECTIVE_DAY_12,
        EFFECTIVE_DAY_13,
        MAX,
        CF50,
        EMPTY;
    }
    private final static int EFFECTIVE_DAY_COUNT = 13;
    private boolean isCF50Mode;
    private State state;
    private transient IntegerDataBlock fabNo;
    private transient IntegerDataBlock storageInterval;
    private transient IntegerDataBlock[] energy;
    private transient IntegerDataBlock[] volume;
    private transient IntegerDataBlock[] water1;
    private transient IntegerDataBlock[] water2;
    private transient IntegerDataBlock[] coolingEnergy;
    private transient DateDataBlock[] effectiveDayTimeStamp;
    private transient IntegerDataBlock power;
    private transient DateAndTimeDataBlock[] maxPowerTimeStamp;
    private transient RealDataBlock[] maxPower;
    private transient IntegerDataBlock flow;
    private transient DateAndTimeDataBlock[] maxFlowTimeStamp;
    private transient RealDataBlock[] maxFlow;
    private transient ShortDataBlock flowTemp;
    private transient DateAndTimeDataBlock[] maxFlowTempTimeStamp;
    private transient RealDataBlock[] maxFlowTemp;
    private transient ShortDataBlock returnTemp;
    private transient IntegerDataBlock tempDiff;
    private transient DateAndTimeDataBlock currentTime;
    private transient ShortDataBlock operatingTime;
    private transient ByteDataBlock firmwareVer;
    private transient ByteDataBlock softwareVer;
    private transient RawDataBlock manSpecData;
    private transient AcwHeatMeterResponseFrame responseFrames;
//TODO cf50 std changed

    public AcwHeatMeter() {
        super();
        init();
    }

    public AcwHeatMeter(int address, int id, int version, MBusMedium medium, Vif energyVif, Vif volumeVif, Vif powerVif, Vif water1Vif, Vif water2Vif, Vif coolingEnergyVif, State state, boolean isCF50Mode, int fabNo, int softwareVer, int firmwareVer) {
        super(address, id, "ACW", version, medium);
        init();
        this.isCF50Mode = isCF50Mode;
        setFabNo(fabNo);
        setPowerVif(powerVif);
        setFirmwareVersion(firmwareVer);
        setSoftwareVersion(softwareVer);
        if (water1Vif != null) {
            setWater1Vif(water1Vif);
        }
        if (water2Vif != null) {
            setWater2Vif(water2Vif);
        }
        if (coolingEnergyVif != null) {
            setCoolingEnergyVif(coolingEnergyVif);
        }
        setEnergyVif(energyVif);
        setVolumeVif(volumeVif);
    }

    public Vif getCoolingEnergyVif() {
        return coolingEnergy[0].getVif();
    }

    public void init() {
        storageInterval = new IntegerDataBlock(_16_BIT_INTEGER, VifFD.STORAGE_INTERVAL_MIN);
        flow = new IntegerDataBlock(_6_DIGIT_BCD, VOLUME_FLOW_L_PER_H_E_0);
        flowTemp = new ShortDataBlock(_4_DIGIT_BCD, FLOW_TEMPERATURE_C_E__1);
        returnTemp = new ShortDataBlock(_4_DIGIT_BCD, RETURN_TEMPERATURE_C_E__1);
        tempDiff = new IntegerDataBlock(_6_DIGIT_BCD, TEMPERATURE_DIFFERENCE_K_E__2);
        currentTime = new DateAndTimeDataBlock(TIMEPOINT_TIME_AND_DATE);
        operatingTime = new ShortDataBlock(_16_BIT_INTEGER, OPERATING_TIME_D);
        manSpecData = new RawDataBlock(SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET);
        manSpecData.setValue(0x00, 0x00);
        effectiveDayTimeStamp = new DateDataBlock[EFFECTIVE_DAY_COUNT + 1];
        maxPowerTimeStamp = new DateAndTimeDataBlock[EFFECTIVE_DAY_COUNT + 1];
        maxPower = new RealDataBlock[EFFECTIVE_DAY_COUNT + 1];
        maxFlowTimeStamp = new DateAndTimeDataBlock[EFFECTIVE_DAY_COUNT + 1];
        maxFlow = new RealDataBlock[EFFECTIVE_DAY_COUNT + 1];
        maxFlowTempTimeStamp = new DateAndTimeDataBlock[EFFECTIVE_DAY_COUNT + 1];
        maxFlowTemp = new RealDataBlock[EFFECTIVE_DAY_COUNT + 1];
        for (int storagenumber = 0; storagenumber <= EFFECTIVE_DAY_COUNT; storagenumber++) {
            if (storagenumber > 0) {
                effectiveDayTimeStamp[storagenumber] = new DateDataBlock(TIMEPOINT_DATE);
                effectiveDayTimeStamp[storagenumber].setStorageNumber(storagenumber);
            }
            maxPowerTimeStamp[storagenumber] = new DateAndTimeDataBlock(POWER_KILO_W_E_0, TIMESTAMP_END_LAST_UPPER);
            maxPowerTimeStamp[storagenumber].setStorageNumber(storagenumber);
            maxPower[storagenumber] = new RealDataBlock(_32_BIT_REAL, POWER_KILO_W_E_0);
            maxPower[storagenumber].setStorageNumber(storagenumber);
            maxFlowTimeStamp[storagenumber] = new DateAndTimeDataBlock(VOLUME_FLOW_CBM_PER_H_E_0, TIMESTAMP_END_LAST_UPPER);
            maxFlowTimeStamp[storagenumber].setStorageNumber(storagenumber);
            maxFlow[storagenumber] = new RealDataBlock(_32_BIT_REAL, VOLUME_FLOW_CBM_PER_H_E_0);
            maxFlow[storagenumber].setStorageNumber(storagenumber);
            maxFlowTempTimeStamp[storagenumber] = new DateAndTimeDataBlock(FLOW_TEMPERATURE_C_E_0, TIMESTAMP_END_LAST_UPPER);
            maxFlowTempTimeStamp[storagenumber].setStorageNumber(storagenumber);
            maxFlowTemp[storagenumber] = new RealDataBlock(_32_BIT_REAL, FLOW_TEMPERATURE_C_E_0);
            maxFlowTemp[storagenumber].setStorageNumber(storagenumber);
        }
    }

    public void setCoolingEnergyVif(Vif coolingEnergyVif) {
        coolingEnergy = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
        for (int storageNumber = 0; storageNumber <= EFFECTIVE_DAY_COUNT; storageNumber++) {
            coolingEnergy[storageNumber] = new IntegerDataBlock(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, (short) 0, 0, storageNumber, coolingEnergyVif, VifeStd.ACCUMULATION_ABS_NEGATIVE);
        }
    }

    public Vif getEnergyVif() {
        return energy[0].getVif();
    }

    public void setEnergyVif(Vif energyVif) {
        energy = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
        for (int storageNumber = 0; storageNumber <= EFFECTIVE_DAY_COUNT; storageNumber++) {
            energy[storageNumber] = new IntegerDataBlock(_32_BIT_INTEGER, energyVif);
            energy[storageNumber].setStorageNumber(storageNumber);
        }
    }

    public Vif getVolumeVif() {
        return volume[0].getVif();
    }

    public Vif getPowerVif() {
        return power.getVif();
    }

    public void setVolumeVif(Vif volumeVif) {
        volume = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
        for (int storageNumber = 0; storageNumber <= EFFECTIVE_DAY_COUNT; storageNumber++) {
            volume[storageNumber] = new IntegerDataBlock(_32_BIT_INTEGER, volumeVif);
            volume[storageNumber].setStorageNumber(storageNumber);
        }
    }

    public int getFabNo() {
        return fabNo.getValue();
    }

    public void setFabNo(int fabNo) {
        this.fabNo = new IntegerDataBlock(_8_DIGIT_BCD, FABRICATION_NO);
        this.fabNo.setValue(fabNo);
    }

    public int getFirmwareVersion() {
        return firmwareVer.getValue();
    }

    public void setFirmwareVersion(int firmwareVer) {
        this.firmwareVer = new ByteDataBlock(_2_DIGIT_BCD, VifFD.FIRMWARE_VERSION);
        this.firmwareVer.setValue((byte) firmwareVer);
    }

    public void setPowerVif(Vif powerVif) {
        power = new IntegerDataBlock(_6_DIGIT_BCD, powerVif);
    }

    public int getSoftwareVersion() {
        return softwareVer.getValue();
    }

    public void setSoftwareVersion(int softwareVer) {
        this.softwareVer = new ByteDataBlock(_2_DIGIT_BCD, VifFD.SOFTWARE_VERSION);
        this.softwareVer.setValue((byte) softwareVer);
    }

    public Vif getWater1Vif() {
        return water1[0].getVif();
    }

    public void setWater1Vif(Vif water1Vif) {
        water1 = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
        for (int storageNumber = 0; storageNumber <= EFFECTIVE_DAY_COUNT; storageNumber++) {
            water1[storageNumber] = new IntegerDataBlock(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, (short) 1, 0, storageNumber, water1Vif);
        }
    }

    public Vif getWater2Vif() {
        return water2[0].getVif();
    }

    public void setWater2Vif(Vif water2Vif) {
        water2 = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
        for (int storageNumber = 0; storageNumber <= EFFECTIVE_DAY_COUNT; storageNumber++) {
            water2[storageNumber] = new IntegerDataBlock(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, (short) 2, 0, storageNumber, water2Vif);
        }
    }

    private List<DataBlock> getDataBlocks(State s) {
        switch (s) {
            case STANDARD:
                if (isCF50Mode) {
                    return getCF50Resp();
                } else {
                    return getStdResp();
                }
            case CF50:
                if (isCF50Mode) {
                    return getStdResp();
                } else {
                    return getCF50Resp();
                }
            case EFFECTIVE_DAY_1:
                return getEffectiveDayResp(1);
            case EFFECTIVE_DAY_2:
                return getEffectiveDayResp(2);
            case EFFECTIVE_DAY_3:
                return getEffectiveDayResp(3);
            case EFFECTIVE_DAY_4:
                return getEffectiveDayResp(4);
            case EFFECTIVE_DAY_5:
                return getEffectiveDayResp(5);
            case EFFECTIVE_DAY_6:
                return getEffectiveDayResp(6);
            case EFFECTIVE_DAY_7:
                return getEffectiveDayResp(7);
            case EFFECTIVE_DAY_8:
                return getEffectiveDayResp(8);
            case EFFECTIVE_DAY_9:
                return getEffectiveDayResp(9);
            case EFFECTIVE_DAY_10:
                return getEffectiveDayResp(10);
            case EFFECTIVE_DAY_11:
                return getEffectiveDayResp(11);
            case EFFECTIVE_DAY_12:
                return getEffectiveDayResp(12);
            case EFFECTIVE_DAY_13:
                return getEffectiveDayResp(13);
            case MAX:
                return getMaxResponse();
            case ERROR:
                return getErrorResponse();
            case EMPTY:
                return getEmptyResponse();
            default:
                return null;
        }
    }

    public void calc() {
        tempDiff.setValue(flowTemp.getValue() - returnTemp.getValue());
    }

    private List<DataBlock> getCF50Resp() {
        List<DataBlock> result = new ArrayList<DataBlock>(10);
        result.add(energy[0]);
        result.add(volume[0]);
        result.add(power);
        result.add(flow);
        result.add(flowTemp);
        result.add(returnTemp);
        result.add(tempDiff);
        result.add(currentTime);
        result.add(operatingTime);
        result.add(manSpecData);
        return result;
    }

    private List<DataBlock> getEffectiveDayResp(int storagenumber) {
        List<DataBlock> result = new ArrayList<DataBlock>(12);
        result.add(fabNo);
        result.add(effectiveDayTimeStamp[storagenumber]);
        result.add(energy[storagenumber]);
        result.add(volume[storagenumber]);
        result.add(maxPowerTimeStamp[storagenumber]);
        result.add(maxPower[storagenumber]);
        result.add(maxFlowTimeStamp[storagenumber]);
        result.add(maxFlow[storagenumber]);
        result.add(maxFlowTempTimeStamp[storagenumber]);
        result.add(maxFlowTemp[storagenumber]);
        if (water1 != null) {
            result.add(water1[storagenumber]);
        }
        if (water2 != null) {
            result.add(water2[storagenumber]);
        }
        if (coolingEnergy != null) {
            result.add(energy[storagenumber]);
        }
        return result;
    }

    private List<DataBlock> getEmptyResponse() {
        List<DataBlock> result = new ArrayList<DataBlock>(1);
        result.add(fabNo);
        return result;
    }

    private List<DataBlock> getErrorResponse() {
                List<DataBlock> result = new ArrayList<DataBlock>(0);

        //TODO implement no docs from itron/actaris
                return result;
    }

    private List<DataBlock> getMaxResponse() {
        List<DataBlock> result = new ArrayList<DataBlock>(12);
        result.add(fabNo);
        result.add(storageInterval);
        result.add(maxPowerTimeStamp[0]);
        result.add(maxPower[0]);
        result.add(maxFlowTimeStamp[0]);
        result.add(maxFlow[0]);
        result.add(maxFlowTempTimeStamp[0]);
        result.add(maxFlowTemp[0]);
        return result;
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private List<DataBlock> getStdResp() {
        List<DataBlock> result = new ArrayList<DataBlock>(16);
        result.add(fabNo);
        result.add(energy[0]);
        result.add(volume[0]);
        result.add(power);
        result.add(flow);
        result.add(flowTemp);
        result.add(returnTemp);
        result.add(tempDiff);
        result.add(currentTime);
        result.add(operatingTime);
        result.add(firmwareVer);
        result.add(softwareVer);
        if (water1 != null) {
            result.add(water1[0]);
        }
        if (water2 != null) {
            result.add(water2[0]);
        }
        if (coolingEnergy != null) {
            result.add(coolingEnergy[0]);
        }
        result.add(manSpecData);
        return result;
    }

    @Override
    public Frame handleApplicationReset(ApplicationReset applicationReset) {
        Frame result = super.handleApplicationReset(applicationReset);
        switch (applicationReset.getTelegramType()) {
            case ALL:
                switch (applicationReset.getSubTelegram()) {
                    case 0x00:
                        state = State.STANDARD;
                        break;
                    case 0x01:
                        state = State.ERROR;
                        break;
                    case 0x02:
                        state = State.EFFECTIVE_DAY_1;
                        break;
                    case 0x03:
                        state = State.EFFECTIVE_DAY_2;
                        break;
                    case 0x04:
                        state = State.EFFECTIVE_DAY_3;
                        break;
                    case 0x05:
                        state = State.EFFECTIVE_DAY_4;
                        break;
                    case 0x06:
                        state = State.EFFECTIVE_DAY_5;
                        break;
                    case 0x07:
                        state = State.EFFECTIVE_DAY_6;
                        break;
                    case 0x08:
                        state = State.EFFECTIVE_DAY_7;
                        break;
                    case 0x09:
                        state = State.EFFECTIVE_DAY_8;
                        break;
                    case 0x0A:
                        state = State.EFFECTIVE_DAY_9;
                        break;
                    case 0x0B:
                        state = State.EFFECTIVE_DAY_10;
                        break;
                    case 0x0C:
                        state = State.EFFECTIVE_DAY_11;
                        break;
                    case 0x0D:
                        state = State.EFFECTIVE_DAY_12;
                        break;
                    case 0x0E:
                        state = State.EFFECTIVE_DAY_13;
                        break;
                    case 0x10:
                        state = State.MAX;
                        break;
                    case 0x12:
                        state = State.CF50;
                        break;
                    case 0x13:
                        state = State.EMPTY;
                        break;
                    default:
                }
                break;
            default:

        }
        return result;
    }

    @Override
    public Frame handleReqUd2(RequestClassXData request) {
        Frame result = super.handleReqUd2(request);
        if (result instanceof UserDataResponse) {
            currentTime.setValue(new Date());
            ((UserDataResponse)result).addAllDataBlocks(getDataBlocks(getState()));
        }
        return result;
    }

    public void setFlow(float value) {
        flow.setValue(Math.round(value));
        calc();
    }

    public void setFlowTemp(float temp) {
        flowTemp.setValue((short) Math.round(temp * 10));
        calc();
    }

    public void setReturnTemp(float temp) {
        returnTemp.setValue((short) Math.round(temp * 10));
        calc();
    }
    /**
     * track the version of self saved data !!!
     */
    private final static int MY_SERIAL_VERSION = 0;

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(MY_SERIAL_VERSION);
        stream.writeInt(getFabNo());
        stream.writeObject(getEnergyVif());
        stream.writeObject(getVolumeVif());
        stream.writeObject(getPowerVif());
        stream.writeObject(getWater1Vif());
        stream.writeObject(getWater2Vif());
        stream.writeObject(getCoolingEnergyVif());
        stream.writeObject(getState());
        stream.writeInt(getFirmwareVersion());
        stream.writeInt(getSoftwareVersion());
        stream.writeBoolean(isCF50Mode);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        switch (stream.readInt()) {
            case MY_SERIAL_VERSION:
                setFabNo(stream.readInt());
                setEnergyVif((Vif) stream.readObject());
                setVolumeVif((Vif) stream.readObject());
                setPowerVif((Vif) stream.readObject());
                setWater1Vif((Vif) stream.readObject());
                setWater2Vif((Vif) stream.readObject());
                setCoolingEnergyVif((Vif) stream.readObject());
                setState((State) stream.readObject());
                setFirmwareVersion(stream.readInt());
                setSoftwareVersion(stream.readInt());
                isCF50Mode = stream.readBoolean();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        if (o instanceof AcwHeatMeter) {
            AcwHeatMeter other = (AcwHeatMeter) o;
            return getFabNo() == other.getFabNo() &&
                    getSoftwareVersion() == other.getSoftwareVersion() &&
                    getFirmwareVersion() == other.getFirmwareVersion() &&
                    getEnergyVif().equals(other.getEnergyVif()) &&
                    getPowerVif().equals(other.getPowerVif()) &&
                    getVolumeVif().equals(other.getVolumeVif()) &&
                    getState().equals(other.getState()) &&
                    isCF50Mode == other.isCF50Mode &&
                    getWater1Vif() == null ? other.getWater1Vif() == null : getWater1Vif().equals(other.getWater1Vif()) &&
                    getWater2Vif() == null ? other.getWater2Vif() == null : getWater2Vif().equals(other.getWater2Vif()) &&
                    getCoolingEnergyVif() == null ? other.getCoolingEnergyVif() == null : getCoolingEnergyVif().equals(other.getCoolingEnergyVif());
        } else {
            return false;
        }
    }

    public String getName() {
        switch (getVersion()) {
            case 0x09:
                return "CF-ECHO II";
            case 0x0A:
                return "CF-51";
            case 0x0B:
                return "CF-55";
            case 0x0F:
                return "CF-800";
            default:
                return "Unknown device";
        }
    }
}
