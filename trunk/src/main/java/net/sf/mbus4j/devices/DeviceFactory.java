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

import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.devices.acw.AcwDeviceFactory;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class DeviceFactory {

    public static MBusDevice createDevice(UserDataResponse udResp) {
        if (AcwDeviceFactory.ACW.equals(udResp.getManufacturer())) {
            return AcwDeviceFactory.createDevice(udResp);
        } else {
            return createGenericDevice(udResp);
        }
    }

    public static MBusDevice createGenericDevice(UserDataResponse udResp) {
        return new GenericDevice(udResp.getAddress(), udResp.getManufacturer(), udResp.getMedium(), udResp.getVersion(), udResp.getIdentNumber());
    }
}
