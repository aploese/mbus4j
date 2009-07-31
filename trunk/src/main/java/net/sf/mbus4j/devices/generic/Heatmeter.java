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
package net.sf.mbus4j.devices.generic;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public interface Heatmeter<ENERGY extends Number, POWER extends Number, VOLUME extends Number, FLOW extends Number, FLOWTEMP extends Number, RETURNTEMP extends Number, TEMPDIFF extends Number> {

    /**
     * @return the energy
     */
    ENERGY getEnergy();

    /**
     * @return the flow
     */
    FLOW getFlow();

    /**
     * @return the flowTemperature
     */
    FLOWTEMP getFlowTemperature();

    /**
     * @return the power
     */
    POWER getPower();

    /**
     * @return the returnTemperature
     */
    RETURNTEMP getReturnTemperature();

    /**
     * @return the temeratureDifference
     */
    TEMPDIFF getTemeratureDifference();

    /**
     * @return the volume
     */
    VOLUME getVolume();

    /**
     * @param energy the energy to set
     */
    void setEnergy(ENERGY energy);

    /**
     * @param flow the flow to set
     */
    void setFlow(FLOW flow);

    /**
     * @param flowTemperature the flowTemperature to set
     */
    void setFlowTemperature(FLOWTEMP flowTemperature);

    /**
     * @param power the power to set
     */
    void setPower(POWER power);

    /**
     * @param returnTemperature the returnTemperature to set
     */
    void setReturnTemperature(RETURNTEMP returnTemperature);

    /**
     * @param temeratureDifference the temeratureDifference to set
     */
    void setTemeratureDifference(TEMPDIFF temeratureDifference);

    /**
     * @param volume the volume to set
     */
    void setVolume(VOLUME volume);
}
