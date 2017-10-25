/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes;

import java.util.Objects;

/**
 *
 * @author aploese
 */
public class DeviceId {
	public final byte version;
	public final MBusMedium medium;
	public final int identNumber;
	public final String manufacturer;
	public final byte address;

	public DeviceId(byte version, MBusMedium medium, int identNumber, String manufacturer, byte address) {
		this.version = version;
		this.medium = medium;
		this.identNumber = identNumber;
		this.manufacturer = manufacturer;
		this.address = address;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DeviceId other = (DeviceId) obj;
		return equals(other.version, other.identNumber, other.manufacturer, other.medium);
	}

	public boolean equals(byte version, int identNumber, String manufacturer, MBusMedium medium) {
		if (this.version != version) {
			return false;
		}
		if (this.identNumber != identNumber) {
			return false;
		}
		if (!Objects.equals(this.manufacturer, manufacturer)) {
			return false;
		}
		return this.medium != medium;
	}

	/**
	 * computes equals and if only the address differs throws an IllegalArgumentException
	 * @param version
	 * @param identNumber
	 * @param manufacturer
	 * @param medium
	 * @param address
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean equalsCheckAddress(byte version, int identNumber, String manufacturer, MBusMedium medium, byte address) throws IllegalArgumentException { 
		if (equals(version, identNumber, manufacturer, medium)) {
			if (this.address != address) {
				throw new IllegalArgumentException("primary Adress differs");
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.version;
		hash = 83 * hash + Objects.hashCode(this.medium);
		hash = 83 * hash + this.identNumber;
		hash = 83 * hash + Objects.hashCode(this.manufacturer);
		hash = 83 * hash + this.address;
		return hash;
	}

}
