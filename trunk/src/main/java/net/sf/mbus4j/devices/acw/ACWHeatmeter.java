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
package net.sf.mbus4j.devices.acw;

import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._16_BIT_INTEGER;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._2_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._32_BIT_INTEGER;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._32_BIT_REAL;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._4_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._6_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode._8_DIGIT_BCD;
import static net.sf.mbus4j.dataframes.datablocks.dif.FunctionField.INSTANTANEOUS_VALUE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_KILO_WH_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_KILO_WH_E_1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_MEGA_J_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.ENERGY_MEGA_J_E_1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FABRICATION_NO;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FLOW_TEMPERATURE_C_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.FLOW_TEMPERATURE_C_E__1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.OPERATING_TIME_D;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.POWER_KILO_W_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.POWER_W_E_2;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.RETURN_TEMPERATURE_C_E__1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.TEMPERATURE_DIFFERENCE_K_E__2;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.TIMEPOINT_DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.TIMEPOINT_TIME_AND_DATE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_CBM_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_FLOW_CBM_PER_H_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_FLOW_L_PER_H_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_L_E_0;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_L_E_1;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifStd.VOLUME_L_E_2;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifeStd.ACCUMULATION_ABS_NEGATIVE;
import static net.sf.mbus4j.dataframes.datablocks.vif.VifeStd.TIMESTAMP_END_LAST_UPPER;

import java.util.Map;

import net.sf.mbus4j.dataframes.ApplicationReset;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.devices.DataTag;
import net.sf.mbus4j.devices.MBusDevice;
import net.sf.mbus4j.devices.Response;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
public class ACWHeatmeter extends MBusDevice {

    public final static Response STD_RESP;
    public final static Response ERROR_RESP = null;
    public final static Response EFFECTIVE_DAY_RESP[];
    public final static Response MAX_VALUES_RESP = null;
    public final static Response CF_50_RESP;
    public final static Response EMPTY_RESP = null;

    static {
        STD_RESP = new Response();
        STD_RESP.setInitFrames(new ApplicationReset(ApplicationReset.TelegramType.ALL, 0), new RequestClassXData(Frame.ControlCode.REQ_UD2));
        STD_RESP.addTag(_8_DIGIT_BCD, FABRICATION_NO);
        STD_RESP.addTag(_32_BIT_INTEGER, ENERGY_KILO_WH_E_0).addAlternativeVif(ENERGY_KILO_WH_E_1).addAlternativeVif(ENERGY_MEGA_J_E_0).addAlternativeVif(ENERGY_MEGA_J_E_1);
        STD_RESP.addTag(_8_DIGIT_BCD, VOLUME_L_E_0).addAlternativeVif(VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        STD_RESP.addTag(_6_DIGIT_BCD, POWER_W_E_2).addAlternativeVif(POWER_KILO_W_E_0);
        STD_RESP.addTag(_6_DIGIT_BCD, VOLUME_FLOW_L_PER_H_E_0);
        STD_RESP.addTag(_4_DIGIT_BCD, FLOW_TEMPERATURE_C_E__1);
        STD_RESP.addTag(_4_DIGIT_BCD, RETURN_TEMPERATURE_C_E__1);
        STD_RESP.addTag(_6_DIGIT_BCD, TEMPERATURE_DIFFERENCE_K_E__2);
        STD_RESP.addTag(_32_BIT_INTEGER, TIMEPOINT_TIME_AND_DATE);
        STD_RESP.addTag(_16_BIT_INTEGER, OPERATING_TIME_D);
        STD_RESP.addTag(_2_DIGIT_BCD, VifFD.FIRMWARE_VERSION);
        STD_RESP.addTag(_2_DIGIT_BCD, VifFD.SOFTWARE_VERSION);
        STD_RESP.addOptionalTag("Water Channel 1", _8_DIGIT_BCD, INSTANTANEOUS_VALUE, 1, 0, 0, VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        STD_RESP.addOptionalTag("Water Channel 2", _8_DIGIT_BCD, INSTANTANEOUS_VALUE, 2, 0, 0, VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        STD_RESP.addOptionalTag("Cooling Energy", _32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, 0, ENERGY_KILO_WH_E_0, ACCUMULATION_ABS_NEGATIVE).addAlternativeVif(ENERGY_KILO_WH_E_1).addAlternativeVif(ENERGY_MEGA_J_E_0).addAlternativeVif(ENERGY_MEGA_J_E_1);
        STD_RESP.addTag(SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET.toString(), SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET, null);

        CF_50_RESP = new Response();
        CF_50_RESP.setInitFrames(new ApplicationReset(ApplicationReset.TelegramType.ALL, 0x12), new RequestClassXData(Frame.ControlCode.REQ_UD2));
        CF_50_RESP.addTag(_32_BIT_INTEGER, ENERGY_KILO_WH_E_0).addAlternativeVif(ENERGY_KILO_WH_E_1).addAlternativeVif(ENERGY_MEGA_J_E_0).addAlternativeVif(ENERGY_MEGA_J_E_1);
        CF_50_RESP.addTag(_8_DIGIT_BCD, VOLUME_L_E_0).addAlternativeVif(VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        CF_50_RESP.addTag(_6_DIGIT_BCD, POWER_W_E_2).addAlternativeVif(POWER_KILO_W_E_0);
        CF_50_RESP.addTag(_6_DIGIT_BCD, VOLUME_FLOW_L_PER_H_E_0);
        CF_50_RESP.addTag(_4_DIGIT_BCD, FLOW_TEMPERATURE_C_E__1);
        CF_50_RESP.addTag(_4_DIGIT_BCD, RETURN_TEMPERATURE_C_E__1);
        CF_50_RESP.addTag(_6_DIGIT_BCD, TEMPERATURE_DIFFERENCE_K_E__2);
        CF_50_RESP.addTag(_32_BIT_INTEGER, TIMEPOINT_TIME_AND_DATE);
        CF_50_RESP.addTag(_16_BIT_INTEGER, OPERATING_TIME_D);
        CF_50_RESP.addTag(SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET.toString(), SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET, null);

        EFFECTIVE_DAY_RESP = new Response[13];
        for (int i = 0; i < 13; i++) {
            EFFECTIVE_DAY_RESP[i] = getEffectiveDayResp(i + 1);
        }

    }

    private static Response getEffectiveDayResp(int storageNumber) {
        Response result = new Response();
        result.setInitFrames(new ApplicationReset(ApplicationReset.TelegramType.ALL, 0x02 + storageNumber), new RequestClassXData(Frame.ControlCode.REQ_UD2));
        result.addTag(_8_DIGIT_BCD, FABRICATION_NO);

        result.addTag(_16_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, storageNumber, TIMEPOINT_DATE);
        result.addTag(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, storageNumber, ENERGY_KILO_WH_E_0).addAlternativeVif(ENERGY_KILO_WH_E_1).addAlternativeVif(ENERGY_MEGA_J_E_0).addAlternativeVif(ENERGY_MEGA_J_E_1);
        result.addTag(_8_DIGIT_BCD, INSTANTANEOUS_VALUE, 0, 0, storageNumber, VOLUME_L_E_0).addAlternativeVif(VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);

        result.addTag(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, storageNumber, POWER_KILO_W_E_0, TIMESTAMP_END_LAST_UPPER).addAlternativeVif(POWER_KILO_W_E_0);
        result.addTag(_32_BIT_REAL, INSTANTANEOUS_VALUE, 0, 0, storageNumber, POWER_KILO_W_E_0);

        result.addTag(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, storageNumber, VOLUME_FLOW_CBM_PER_H_E_0, TIMESTAMP_END_LAST_UPPER);
        result.addTag(_32_BIT_REAL, INSTANTANEOUS_VALUE, 0, 0, storageNumber, VOLUME_FLOW_CBM_PER_H_E_0);

        result.addTag(_32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, storageNumber, FLOW_TEMPERATURE_C_E_0, TIMESTAMP_END_LAST_UPPER);
        result.addTag(_32_BIT_REAL, INSTANTANEOUS_VALUE, 0, 0, storageNumber, FLOW_TEMPERATURE_C_E_0);

        STD_RESP.addOptionalTag("Water Channel 1", _8_DIGIT_BCD, INSTANTANEOUS_VALUE, 1, 0, storageNumber, VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        STD_RESP.addOptionalTag("Water Channel 2", _8_DIGIT_BCD, INSTANTANEOUS_VALUE, 2, 0, storageNumber, VOLUME_L_E_1).addAlternativeVif(VOLUME_L_E_2).addAlternativeVif(VOLUME_CBM_E_0);
        STD_RESP.addOptionalTag("Cooling Energy", _32_BIT_INTEGER, INSTANTANEOUS_VALUE, 0, 0, storageNumber, ENERGY_KILO_WH_E_0, ACCUMULATION_ABS_NEGATIVE).addAlternativeVif(ENERGY_KILO_WH_E_1).addAlternativeVif(ENERGY_MEGA_J_E_0).addAlternativeVif(ENERGY_MEGA_J_E_1);

        result.addTag(SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET.toString(), SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET, null);

        return result;
    }

    public static Frame[] getInitFrames() {
        return STD_RESP.getinitFrames();
    }
    private MBusMedium medium;
    private Response stdResponse;

    public ACWHeatmeter(UserDataResponse udResp) {
        super(udResp.getAddress(), udResp.getIdentNumber(), udResp.getVersion());
        boolean initialized = true;
        medium = udResp.getMedium();
        initialized &= setPower(getPowerFromUdRsp(udResp));
        initialized &= setVolume(getVolumeFromUdRsp(udResp));
        initialized &= setEnergy(getEnergyFromUdRsp(udResp));
        setInitialized(initialized);
    }

    public Vif getEnergy() {
        return STD_RESP.getTag(1).getVif();
    }

    private Vif getEnergyFromUdRsp(UserDataResponse udResp) {
        //TODO implement
        return VifStd.ENERGY_KILO_WH_E_1;
    }

    @Override
    public String getMan() {
        return MBusDevice.WellKnownMan.ACW.name();
    }

    //TODO HEAT_INLET oder HEAT_COOLING_LOAD_METER
    @Override
    public MBusMedium getMedium() {
        return medium;
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

    public Vif getPower() {
        return STD_RESP.getTag(3).getVif();
    }

    private Vif getPowerFromUdRsp(UserDataResponse udResp) {
        //TODO implement
        return VifStd.POWER_KILO_W_E_0;
    }

    /**
     * @return the stdResponse
     */
    public Response getStdResponse() {
        return stdResponse;
    }

    public Vif getVolume() {
        return STD_RESP.getTag(2).getVif();
    }

    private Vif getVolumeFromUdRsp(UserDataResponse udResp) {
        //TODO implement
        return VifStd.VOLUME_L_E_1;
    }

    @Override
    public void readValues(Map<DataTag, DataBlock> map) {
    }

    public boolean setEnergy(Vif energy) {
        //TODO implement
        STD_RESP.chooseAlternative(1, energy);
        return true;
    }

    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

    public boolean setPower(Vif power) {
        //TODO implement
        STD_RESP.chooseAlternative(3, power);
        return true;
    }

    /**
     * @param stdResponse the stdResponse to set
     */
    public void setStdResponse(Response stdResponse) {
        this.stdResponse = stdResponse;
    }

    public boolean setVolume(Vif volume) {
        //TODO implement
        STD_RESP.chooseAlternative(2, volume);
        return true;
    }

    @Override
    public String toString() {
        return String.format("ACWHeatmeter address=0x%02x man=%s medium=%s version=0x%02X (%s) id=%08d energy=%s volume=%s power=%s", getPrimaryAddress() & 0xFF, getMan(), getMedium(), getVersion(), getName(), getSecondaryAddress(), getEnergy(), getVolume(), getPower());
    }
}
