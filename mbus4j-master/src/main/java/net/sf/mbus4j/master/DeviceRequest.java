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
