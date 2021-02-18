package net.sf.mbus4j.master;

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
import java.util.Iterator;
import java.util.LinkedList;

import net.sf.mbus4j.MBusAddressing;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.MBusMedium;

/**
 * Request that bundles requests to different devices
 * @author Arne Plöse
 * @param <T>
 */
public class ValuesRequest<T>
        implements Iterable<DeviceRequest<T>> {

    private final LinkedList<DeviceRequest<T>> devices= new LinkedList<>();

    public void add(byte version, int identNumber, String manufacturer, MBusMedium medium, byte address, MBusAddressing addressing, DataBlockLocator<T> value) {
        for (DeviceRequest<T> r : devices) {
        	if (r.deviceId.equalsCheckAddress(version, identNumber, manufacturer, medium, address)) {
        		if (r.addressing != addressing) {
        			throw new IllegalArgumentException("Adressing mismatch");
        		}
        		r.dataBlockLocators.add(value);
        		return;
        	}
        }
        DeviceId deviceId = new DeviceId(version, medium, identNumber, manufacturer, address);
        devices.add(new DeviceRequest<>(deviceId, addressing, value));
    }

    @Override
    public Iterator<DeviceRequest<T>> iterator() {
        return devices.iterator();
    }
    
    
    
}
