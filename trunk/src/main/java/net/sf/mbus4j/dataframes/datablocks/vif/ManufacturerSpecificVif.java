/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

import net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement;
import net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import java.util.Arrays;

/**
 *
 * @author aploese
 */
public class ManufacturerSpecificVif implements Vif {

    private byte[] vifes;
    final byte vife;

    public ManufacturerSpecificVif(byte b) {
        vife = b;
        vifes = new byte[0];
    }

    public void addVIFE(byte b) {
        vifes = Arrays.copyOf(vifes, vifes.length + 1);
        vifes[vifes.length -1] = b;
    }

    public byte[] getVifes() {
        return vifes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Manufacturer specific coding including VIFE's (VIF == 0x%02X) VIFE's = [", vife));
        for (byte b : vifes) {
            sb.append(String.format("%02X", b));
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurement() {
        return null;
        }
    /**
     * @return the siPrefix
     */
    @Override
    public SiPrefix getSiPrefix() {
        return null;
    }

    @Override
    public Integer getExponent() {
        return null;
    }

        @Override
    public String getLabel() {
        return toString();
    }

    
}
