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
package net.sf.mbus4j.slave.acw;

import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._16_BIT_INTEGER;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._2_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._32_BIT_INTEGER;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._32_BIT_REAL;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._4_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._6_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._8_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.FunctionField.INSTANTANEOUS_VALUE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FABRICATION_NO;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FLOW_TEMPERATURE_C_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FLOW_TEMPERATURE_C_E__1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.OPERATING_TIME_D;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.POWER_KILO_W_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.RETURN_TEMPERATURE_C_E__1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.TEMPERATURE_DIFFERENCE_K_E__2;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.TIMEPOINT_DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.TIMEPOINT_TIME_AND_DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_FLOW_CBM_PER_H_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_FLOW_L_PER_H_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifeStd.TIMESTAMP_END_LAST_UPPER;

import java.util.Date;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateAndTimeDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RawDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RealDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeStd;
import net.sf.mbus4j.slave.Slave;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class AcwHeatMeter extends Slave {

    public enum State {

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
    private final boolean isCF50Mode;
    private State state = State.STANDARD;
    private final IntegerDataBlock fabNo;
    private final IntegerDataBlock storageInterval;
    private final IntegerDataBlock[] energy;
    private final IntegerDataBlock[] volume;
    private final IntegerDataBlock[] water1;
    private final IntegerDataBlock[] water2;
    private final IntegerDataBlock[] coolingEnergy;
    private final DateDataBlock[] effectiveDayTimeStamp;
    private final IntegerDataBlock power;
    private final DateAndTimeDataBlock[] maxPowerTimeStamp;
    private final RealDataBlock[] maxPower;
    private final IntegerDataBlock flow;
    private final DateAndTimeDataBlock[] maxFlowTimeStamp;
    private final RealDataBlock[] maxFlow;
    private final ShortDataBlock flowTemp;
    private final DateAndTimeDataBlock[] maxFlowTempTimeStamp;
    private final RealDataBlock[] maxFlowTemp;
    private final ShortDataBlock returnTemp;
    private final IntegerDataBlock tempDiff;
    private final DateAndTimeDataBlock currentTime;
    private final ShortDataBlock operatingTime;
    private final ByteDataBlock firmwareVer;
    private final ByteDataBlock softwareVer;
    private final RawDataBlock manSpecData;
//TODO cf50 std changed

    public AcwHeatMeter(int address, int id, int version, MBusMedium medium, Vif energyVif, Vif volumeVif, Vif powerVif, Vif water1Vif, Vif water2Vif, Vif coolingEnergyVif, State state, boolean isCF50Mode, int fabNo, int softwareVer, int firmwareVer) {
        super(address, id, "ACW", version, medium);
        this.state = state;
        this.isCF50Mode = isCF50Mode;
        this.fabNo = new IntegerDataBlock(_8_DIGIT_BCD, FABRICATION_NO);
        this.fabNo.setValue(fabNo);
        storageInterval = new IntegerDataBlock(_16_BIT_INTEGER, VifFD.STORAGE_INTERVAL_MIN);
        power = new IntegerDataBlock(_6_DIGIT_BCD, powerVif);
        flow = new IntegerDataBlock(_6_DIGIT_BCD, VOLUME_FLOW_L_PER_H_E_0);
        flowTemp = new ShortDataBlock(_4_DIGIT_BCD, FLOW_TEMPERATURE_C_E__1);
        returnTemp = new ShortDataBlock(_4_DIGIT_BCD, RETURN_TEMPERATURE_C_E__1);
        tempDiff = new IntegerDataBlock(_6_DIGIT_BCD, TEMPERATURE_DIFFERENCE_K_E__2);
        currentTime = new DateAndTimeDataBlock(TIMEPOINT_TIME_AND_DATE);
        operatingTime = new ShortDataBlock(_16_BIT_INTEGER, OPERATING_TIME_D);
        this.firmwareVer = new ByteDataBlock(_2_DIGIT_BCD, VifFD.FIRMWARE_VERSION);
        this.firmwareVer.setValue((byte) firmwareVer);
        this.softwareVer = new ByteDataBlock(_2_DIGIT_BCD, VifFD.SOFTWARE_VERSION);
        this.softwareVer.setValue((byte) softwareVer);
        if (water1Vif != null) {
            water1 = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
            water1[0] = new IntegerDataBlock(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, (short) 1, 0, 0, water1Vif);
        } else {
            water1 = null;
        }
        if (water2Vif != null) {
            water2 = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
            water2[0] = new IntegerDataBlock(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, (short) 2, 0, 0, water2Vif);
        } else {
            water2 = null;
        }
        if (coolingEnergyVif != null) {
            coolingEnergy = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
            coolingEnergy[0] = new IntegerDataBlock(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, (short) 0, 0, 0, coolingEnergyVif, VifeStd.ACCUMULATION_ABS_NEGATIVE);
        } else {
            coolingEnergy = null;
        }
        manSpecData = new RawDataBlock(SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET);
        manSpecData.setValue(0x00, 0x00);

        // effective Day
        energy = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
        volume = new IntegerDataBlock[EFFECTIVE_DAY_COUNT + 1];
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
            energy[storagenumber] = new IntegerDataBlock(_32_BIT_INTEGER, energyVif);
            energy[storagenumber].setStorageNumber(storagenumber);
            volume[storagenumber] = new IntegerDataBlock(_32_BIT_INTEGER, volumeVif);
            volume[storagenumber].setStorageNumber(storagenumber);
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
            if (water1Vif != null) {
                water1[storagenumber] = new IntegerDataBlock(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, (short) 1, 0, storagenumber, water1Vif);
            }
            if (water2Vif != null) {
                water2[storagenumber] = new IntegerDataBlock(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, (short) 2, 0, storagenumber, water1Vif);
            }
            if (coolingEnergyVif != null) {
                coolingEnergy[storagenumber] = new IntegerDataBlock(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, (short) 0, 0, storagenumber, coolingEnergyVif, VifeStd.ACCUMULATION_ABS_NEGATIVE);
            }

        }

    }

    private void addDataBlocks(UserDataResponse result) {
        switch (getState()) {
            case STANDARD:
                if (isCF50Mode) {
                    getCF50Resp(result);
                } else {
                    getStdResp(result);
                }
                break;
            case CF50:
                if (isCF50Mode) {
                    getStdResp(result);
                } else {
                    getCF50Resp(result);
                }
                break;
            case EFFECTIVE_DAY_1:
                getEffectiveDayResp(result, 1);
                break;
            case EFFECTIVE_DAY_2:
                getEffectiveDayResp(result, 2);
                break;
            case EFFECTIVE_DAY_3:
                getEffectiveDayResp(result, 3);
                break;
            case EFFECTIVE_DAY_4:
                getEffectiveDayResp(result, 4);
                break;
            case EFFECTIVE_DAY_5:
                getEffectiveDayResp(result, 5);
                break;
            case EFFECTIVE_DAY_6:
                getEffectiveDayResp(result, 6);
                break;
            case EFFECTIVE_DAY_7:
                getEffectiveDayResp(result, 7);
                break;
            case EFFECTIVE_DAY_8:
                getEffectiveDayResp(result, 8);
                break;
            case EFFECTIVE_DAY_9:
                getEffectiveDayResp(result, 9);
                break;
            case EFFECTIVE_DAY_10:
                getEffectiveDayResp(result, 10);
                break;
            case EFFECTIVE_DAY_11:
                getEffectiveDayResp(result, 11);
                break;
            case EFFECTIVE_DAY_12:
                getEffectiveDayResp(result, 12);
                break;
            case EFFECTIVE_DAY_13:
                getEffectiveDayResp(result, 13);
                break;
            case MAX:
                getMaxResponse(result);
                break;
            case ERROR:
                getErrorResponse(result);
                break;
            case EMPTY:
                getEmptyResponse(result);
                break;
            default:

        }
    }

    public void calc() {
        tempDiff.setValue(flowTemp.getValue() - returnTemp.getValue());
    }

    private void getCF50Resp(UserDataResponse result) {
        result.addDataBlock(energy[0]);
        result.addDataBlock(volume[0]);
        result.addDataBlock(power);
        result.addDataBlock(flow);
        result.addDataBlock(flowTemp);
        result.addDataBlock(returnTemp);
        result.addDataBlock(tempDiff);
        result.addDataBlock(currentTime);
        result.addDataBlock(operatingTime);
        result.addDataBlock(manSpecData);
    }

    private void getEffectiveDayResp(UserDataResponse result, int storagenumber) {
        result.addDataBlock(fabNo);
        result.addDataBlock(effectiveDayTimeStamp[storagenumber]);
        result.addDataBlock(energy[storagenumber]);
        result.addDataBlock(volume[storagenumber]);
        result.addDataBlock(maxPowerTimeStamp[storagenumber]);
        result.addDataBlock(maxPower[storagenumber]);
        result.addDataBlock(maxFlowTimeStamp[storagenumber]);
        result.addDataBlock(maxFlow[storagenumber]);
        result.addDataBlock(maxFlowTempTimeStamp[storagenumber]);
        result.addDataBlock(maxFlowTemp[storagenumber]);
        if (water1 != null) {
            result.addDataBlock(water1[storagenumber]);
        }
        if (water2 != null) {
            result.addDataBlock(water2[storagenumber]);
        }
        if (coolingEnergy != null) {
            result.addDataBlock(energy[storagenumber]);
        }
    }

    private void getEmptyResponse(UserDataResponse result) {
        result.addDataBlock(fabNo);
    }

    private void getErrorResponse(UserDataResponse result) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void getMaxResponse(UserDataResponse result) {
        result.addDataBlock(fabNo);
        result.addDataBlock(storageInterval);
        result.addDataBlock(maxPowerTimeStamp[0]);
        result.addDataBlock(maxPower[0]);
        result.addDataBlock(maxFlowTimeStamp[0]);
        result.addDataBlock(maxFlow[0]);
        result.addDataBlock(maxFlowTempTimeStamp[0]);
        result.addDataBlock(maxFlowTemp[0]);
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    private void getStdResp(UserDataResponse result) {
        result.addDataBlock(fabNo);
        result.addDataBlock(energy[0]);
        result.addDataBlock(volume[0]);
        result.addDataBlock(power);
        result.addDataBlock(flow);
        result.addDataBlock(flowTemp);
        result.addDataBlock(returnTemp);
        result.addDataBlock(tempDiff);
        result.addDataBlock(currentTime);
        result.addDataBlock(operatingTime);
        result.addDataBlock(firmwareVer);
        result.addDataBlock(softwareVer);
        if (water1 != null) {
            result.addDataBlock(water1[0]);
        }
        if (water2 != null) {
            result.addDataBlock(water2[0]);
        }
        if (coolingEnergy != null) {
            result.addDataBlock(coolingEnergy[0]);
        }
        result.addDataBlock(manSpecData);
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
            addDataBlocks((UserDataResponse) result);
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
}
