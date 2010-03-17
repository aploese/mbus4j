/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

/**
 *
 * @author aploese
 */
public enum MBusAddressing {
    PRIMARY("primary"), SECONDARY("secondary");



    private final String label;

    private MBusAddressing(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static MBusAddressing fromLabel(String label) {
        for (MBusAddressing value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }
}
