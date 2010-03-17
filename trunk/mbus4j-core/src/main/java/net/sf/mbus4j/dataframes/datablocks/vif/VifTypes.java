/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
 */
public enum VifTypes {
    FB_EXTENTION("extention FB"),
    FD_EXTENTION("extention FD"),
    ASCII("ascii"),
    PRIMARY("primary"),
    MANUFACTURER_SPECIFIC("manufacturer specific");

    public static VifTypes fromLabel(String label) {
        for (VifTypes value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }

    private final String label;

    private VifTypes(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
