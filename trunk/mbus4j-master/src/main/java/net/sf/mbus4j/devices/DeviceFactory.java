package net.sf.mbus4j.devices;

/*
 * #%L
 * mbus4j-master
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */

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

    public static GenericDevice createDevice(byte address, String manufacturer, MBusMedium medium, byte version,
            int identNumber) {
        return createGenericDevice(address, manufacturer, medium, version, identNumber);
    }

    private static GenericDevice createGenericDevice(byte address, String manufacturer, MBusMedium medium,
            byte version, int identNumber) {
        return new GenericDevice(address, manufacturer, medium, version, identNumber);
    }
}
