/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices.generic;

/**
 *
 * @author aploese
 */
public interface Heatmeter<ENERGY extends Number, POWER extends Number, VOLUME extends Number, FLOW extends Number, FLOWTEMP extends Number, RETURNTEMP extends Number, TEMPDIFF extends Number> {

    /**
     * @return the energy
     */
    ENERGY getEnergy();

    /**
     * @param energy the energy to set
     */
    void setEnergy(ENERGY energy);

    /**
     * @return the power
     */
    POWER getPower();

    /**
     * @param power the power to set
     */
    void setPower(POWER power);

    /**
     * @return the volume
     */
    VOLUME getVolume();

    /**
     * @param volume the volume to set
     */
    void setVolume(VOLUME volume);

    /**
     * @return the flow
     */
    FLOW getFlow();

    /**
     * @param flow the flow to set
     */
    void setFlow(FLOW flow);

    /**
     * @return the flowTemperature
     */
    FLOWTEMP getFlowTemperature();

    /**
     * @param flowTemperature the flowTemperature to set
     */
    void setFlowTemperature(FLOWTEMP flowTemperature);

    /**
     * @return the returnTemperature
     */
    RETURNTEMP getReturnTemperature();

    /**
     * @param returnTemperature the returnTemperature to set
     */
    void setReturnTemperature(RETURNTEMP returnTemperature);

    /**
     * @return the temeratureDifference
     */
    TEMPDIFF getTemeratureDifference();

    /**
     * @param temeratureDifference the temeratureDifference to set
     */
    void setTemeratureDifference(TEMPDIFF temeratureDifference);
}
