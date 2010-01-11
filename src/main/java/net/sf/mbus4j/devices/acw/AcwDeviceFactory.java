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
package net.sf.mbus4j.devices.acw;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.devices.DeviceFactory;
import net.sf.mbus4j.devices.MBusResponseFramesContainer;
import net.sf.mbus4j.slave.acw.AcwHeatMeter;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class AcwDeviceFactory extends DeviceFactory {

    public static final String ACW = "ACW";

    public static MBusResponseFramesContainer createDevice(UserDataResponse udResp) {
        if (MBusMedium.StdMedium.HEAT_OUTLET.equals(udResp.getMedium()) || MBusMedium.StdMedium.HEAT_INLET.equals(udResp.getMedium()) || MBusMedium.StdMedium.HEAT_COOLING_LOAD_METER.equals(udResp.getMedium())) {
            if (udResp.getVersion() == 0x09 || udResp.getVersion() == 0x0A || udResp.getVersion() == 0x0B || udResp.getVersion() == 0x0F) {
                AcwHeatMeter m = new AcwHeatMeter(udResp);
                return m;
            }
        }
        return createGenericDevice(udResp);
    }
}
