/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import java.util.Map;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author aploese
 */
class GenericDevice extends MBusDevice {
    private final String man;
    private final MBusMedium medium;

    public GenericDevice(int primaryAddress, String man, MBusMedium medium, int version, int secondaryAddress) {
        super(primaryAddress, secondaryAddress, version);
        this.man  = man;
        this.medium = medium;
    }

    @Override
    public String getMan() {
        return man;
    }

    @Override
    public MBusMedium getMedium() {
        return medium;
    }

    @Override
    public void readValues(Map<DataTag, DataBlock> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
