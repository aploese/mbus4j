/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package net.sf.mbus4j.master;

import java.util.Arrays;
import java.util.LinkedList;

import net.sf.mbus4j.MBusAddressing;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

public class DeviceRequest<T> {

    public final DeviceId deviceId;
    public final MBusAddressing addressing;
    public LinkedList<DataBlockLocator<T>> dataBlockLocators = new LinkedList<>();
    public UserDataResponse fullResponse;

    public DeviceRequest(DeviceId deviceId, MBusAddressing addressing, DataBlockLocator<T> dataBlockLocator) {
        this.deviceId = deviceId;
        this.addressing = addressing;
        dataBlockLocators.add(dataBlockLocator);
    }

    void setFullResponse(UserDataResponse udr) {
        this.fullResponse = udr;
        for (DataBlockLocator<?> dbl : dataBlockLocators) {
            for (DataBlock db : fullResponse) {
                if (db.getDataFieldCode().equals(dbl.getDifCode())
                        && db.getVif().equals(dbl.getVif())
                        && db.getFunctionField().equals(dbl.getFunctionField())
                        && (db.getStorageNumber() == dbl.getStorageNumber())
                        && (db.getSubUnit() == dbl.getDeviceUnit())
                        && (db.getTariff() == dbl.getTariff())
                        && Arrays.equals(db.getVifes(), dbl.getVifes())) {
                    if (dbl.getDb() != null) {
                        throw new RuntimeException("Second DataBlock found! ... DataBlocks are not unique");
                    }
                    dbl.setDb(db);
                    break;
                }
            }

        }
    }
}
