/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
 */
public enum VifeTypes {

    PRIMARY("primary"),
    OBJECT_ACTION("object action"),
    ERROR("error"),
    MAN_SPEC("manufacturer specific");

    public static VifeTypes fromLabel(String label) {
        for (VifeTypes value : values()) {
            if (value.getLabel().equals(label)) {
                return value;
            }
        }
        return valueOf(label);
    }

    private final String label;

    private VifeTypes(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
