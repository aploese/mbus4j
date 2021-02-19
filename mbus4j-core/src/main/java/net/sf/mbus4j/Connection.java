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
package net.sf.mbus4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Arne Plöse
 */
@Deprecated
public abstract class Connection implements Serializable {

    public static final int DEFAULT_SPEED = 2400;

    private int bitPerSecond;

    private int responseTimeOutOffset;

    public Connection() {
        super();
    }

    public Connection(int bitPerSecond, int responseTimeOutOffset) {
        this.bitPerSecond = bitPerSecond;
        this.responseTimeOutOffset = responseTimeOutOffset;
    }

    /**
     * @return the bitPerSecond
     */
    public int getBitPerSecond() {
        return bitPerSecond;
    }

    public abstract String getName();

    /**
     * @param bitPerSecond the bitPerSecond to set
     */
    public void setBitPerSecond(int bitPerSecond) {
        this.bitPerSecond = bitPerSecond;
    }

    /**
     * @return the responseTimeOutOffset
     */
    public int getResponseTimeOutOffset() {
        return responseTimeOutOffset;
    }

    /**
     * @param responseTimeOutOffset the responseTimeOutOffset to set
     */
    public void setResponseTimeOutOffset(int responseTimeOutOffset) {
        this.responseTimeOutOffset = responseTimeOutOffset;
    }

    private static final long serialVersionUID = -1;
    private static final int version = 1;

    // Serialization for saveDataSource
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeInt(bitPerSecond);
        out.writeInt(responseTimeOutOffset);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();
        switch (ver) {
            case 1:
                readObjectVer1(in);
                break;
        }
    }

    private void readObjectVer1(ObjectInputStream in) throws IOException {
        bitPerSecond = in.readInt();
        responseTimeOutOffset = in.readInt();
    }

}
