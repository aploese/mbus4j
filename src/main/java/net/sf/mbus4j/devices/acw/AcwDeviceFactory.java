/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.devices.acw;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.devices.DeviceFactory;
import net.sf.mbus4j.devices.MBusDevice;

/**
 *
 * @author aploese
 */
public class AcwDeviceFactory extends DeviceFactory {

    public static final String ACW = "ACW";

    public static MBusDevice createDevice(UserDataResponse udResp) {
        if (MBusMedium.StdMedium.HEAT_OUTLET.equals(udResp.getMedium()) || MBusMedium.StdMedium.HEAT_INLET.equals(udResp.getMedium()) || MBusMedium.StdMedium.HEAT_COOLING_LOAD_METER.equals(udResp.getMedium())) {
            if (udResp.getVersion() == 0x09 || udResp.getVersion() == 0x0A || udResp.getVersion() == 0x0B || udResp.getVersion() == 0x0F) {
                ACWHeatmeter m = new ACWHeatmeter(udResp);
                return m;
            }
        }
        return createGenericDevice(udResp);
    }
}
