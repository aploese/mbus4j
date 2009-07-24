package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;

public class RawDataBlock extends DataBlock {

    private byte[] value;

    public RawDataBlock(DataFieldCode dataFieldCode) {
        super(dataFieldCode);
    }

    @Override
    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(hex)[");
        if (value != null) {
            for (byte b : value) {
                sb.append(String.format("%02X", b));
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }
    
    @Override
        public void toString(StringBuilder sb, String inset) {
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        sb.append(inset).append("value = ").append(getValueAsString()).append("\n");
    }

    public void setValue(int ... value) {
        this.value = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            this.value[i] = (byte)value[i];
        }
    }

}
