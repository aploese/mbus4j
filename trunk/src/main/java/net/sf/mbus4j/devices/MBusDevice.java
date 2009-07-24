/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.devices;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author aploese
 */
public abstract class MBusDevice implements Iterable<Response> {

    public enum WellKnownMan {

        ACW;
    }
    private int primaryAddress;
    private int secondaryAddress;
    private int version;
    private List<Response> resonses;
    private boolean initialized;

    public MBusDevice(int primaryAddress, int secondaryAddress, int version) {
        this.primaryAddress = primaryAddress;
        this.secondaryAddress = secondaryAddress;
        this.version = version;
    }

    public abstract String getMan();

    public abstract MBusMedium getMedium();

    /**
     * @return the primaryAddress
     */
    public int getPrimaryAddress() {
        return primaryAddress;
    }

    /**
     * @return the secondaryAddress
     */
    public int getSecondaryAddress() {
        return secondaryAddress;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public abstract void readValues(Map<DataTag, DataBlock> map);

    @Override
    public Iterator<Response> iterator() {
        return resonses.iterator();
    }
}
