/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.vif;

/**
 *
 * @author aploese
 */
public enum ObjectAction {
    WRITE(0x00, "write(replace)"),
    ADD_VALUE(0x01, "add value"),
    SUBTRACT_VALUE(0x02, "subtract value"),
    OR(0x03, "or"),
    AND(0x04, "and"),
    XOR(0x05, "xor"),
    AND_NOT(0x06, "and not"),
    CLEAR(0x07, "clear"),
    ADD_ENTRY(0x08, "add entry"),
    DELETE_ENTRY(0x09, "delete entry"),
    RESERVED_0X0A(0x0A, "Reserved 0x0A"),
    FREEZE_DATA(0x0B, "freeze data"),
    ADD_TO_READOUT_LIST(0x0C, "add to readout list"),
    DELETE_FROM_READOUT_LIST(0x0D, "delete from readout list");


    public final int id;
    private final String aname;

    private ObjectAction(int id, String aname) {
        this.id = id;
        this.aname = aname;
    }

    public static ObjectAction valueOf(int id) {
        for (ObjectAction oa : ObjectAction.values()) {
            if (oa.id == id) {
                return oa;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return aname;
    }

}
