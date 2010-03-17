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
package net.sf.mbus4j.dataframes;

import java.io.Serializable;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public interface MBusResponseFramesContainer extends Iterable<ResponseFrameContainer>, Serializable {

    void setAddress(byte address);

    void setIdentNumber(int identNumber);

    void setVersion(byte version);

    boolean isAcd();

    void setAcd(boolean acd);

    boolean isDfc();

    void setDfc(boolean dfc);

    String getManufacturer();

    void setManufacturer(String manufacturer);

    MBusMedium getMedium();

    void setMedium(MBusMedium medium);

    byte getAddress();

    int getIdentNumber();

    byte getVersion();

    UserDataResponse.StatusCode[] getStatus();

    void setStatus(UserDataResponse.StatusCode[] status);

    short getSignature();

    void setSignature(short signature);

    short getAccessnumber();

    void setAccessnumber(short accessnumber);

    ResponseFrameContainer getResponseFrameContainer(int index);

    ResponseFrameContainer[] getResponseFrameContainers();

    int getResponseFrameContainerCount();

}
