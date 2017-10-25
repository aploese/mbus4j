package net.sf.mbus4j.master;

import java.util.LinkedList;

import net.sf.mbus4j.MBusAddressing;
import net.sf.mbus4j.dataframes.DeviceId;
import net.sf.mbus4j.dataframes.UserDataResponse;

public class DeviceRequest<T> {
	public final DeviceId deviceId;
    public final  MBusAddressing addressing;
	public LinkedList<DataBlockLocator<T>> dataBlockLocators = new LinkedList<>();
    public UserDataResponse fullResponse;

	public DeviceRequest(DeviceId deviceId, MBusAddressing addressing, DataBlockLocator<T> dataBlockLocator) {
		this.deviceId = deviceId;
		this.addressing = addressing;
		dataBlockLocators.add(dataBlockLocator);
	}
}
