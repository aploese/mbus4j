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
package net.sf.mbus4j.devices;

import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class DeviceFactory {

    public static GenericDevice createDevice(UserDataResponse udResp, Frame requestFrame) {
            return createGenericDevice(udResp, requestFrame);
    }

    public static GenericDevice createGenericDevice(UserDataResponse udResp, Frame requestFrame) {
        return new GenericDevice(udResp, requestFrame);
    }

    public static GenericDevice createDevice(byte address, String manufacturer, MBusMedium medium, byte version, int identNumber) {
        return createGenericDevice(address, manufacturer, medium, version, identNumber);
    }

    private static GenericDevice createGenericDevice(byte address, String manufacturer, MBusMedium medium, byte version, int identNumber) {
        return new GenericDevice(address, manufacturer, medium, version, identNumber);
    }

}
