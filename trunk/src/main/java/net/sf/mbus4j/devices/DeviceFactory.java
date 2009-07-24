/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.devices.acw.AcwDeviceFactory;

/**
 *
 * @author aploese
 */
public class DeviceFactory {

    public static MBusDevice createDevice(UserDataResponse udResp) {
        if (AcwDeviceFactory.ACW.equals(udResp.getManufacturer())) {
            return AcwDeviceFactory.createDevice(udResp);
        } else {
            return null;
        }
    }

    public static MBusDevice createGenericDevice(UserDataResponse udResp) {
        return new GenericDevice(udResp.getAddress(), udResp.getManufacturer(), udResp.getMedium(), udResp.getVersion(), udResp.getIdentNumber());
    }


}
