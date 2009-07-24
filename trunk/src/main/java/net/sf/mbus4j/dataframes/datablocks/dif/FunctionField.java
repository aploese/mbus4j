/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks.dif;

/**
 *
 * @author aploese
 */
    public enum FunctionField {

        INSTANTANEOUS_VALUE(0x00, "Instantaneous value"),
        MAXIMUM_VALUE(0x10, "Maximum value"),
        MINIMUM_VALUE(0x20, "Minimum value"),
        VALUE_DURING_ERROR_STATE(0x30, "Value during error state");
        private final String userFriendlyName;
        public final byte code;

        private FunctionField(int code, String name) {
            this.userFriendlyName = name;
            this.code = (byte)code;
        }

        @Override
        public String toString() {
            return userFriendlyName;
        }
    }
