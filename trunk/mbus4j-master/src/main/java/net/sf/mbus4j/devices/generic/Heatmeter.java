package net.sf.mbus4j.devices.generic;

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
