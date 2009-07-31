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
package net.sf.mbus4j.dataframes;

/**
 *
 * @author arnep@users.sourceforge.net
 * $Id$
 */
public interface Frame {

    public enum ControlCode {

        SND_NKE,
        SND_UD,
        CON_ACK,
        REQ_UD2,
        REQ_UD1,
        RSP_UD;
    }

    ControlCode getControlCode();
    //TODO GarbageFrame for unparsable stuff ie multiple answers??
    //TODO Timespamps for send/receive tracking
//    long getFirstByteTimeStamp();
//    void setFirstByteTimeStamp(long timestamp);
//    long getLastByteTimeStamp();
//    void setLastByteTimeStamp(long timestamp);
}
