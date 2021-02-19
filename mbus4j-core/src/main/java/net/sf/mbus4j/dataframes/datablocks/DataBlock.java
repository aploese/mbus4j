/*
 * MBus4J - Drivers for the M-Bus protocol , https://github.com/aploese/mbus4j/
 * Copyright (C) 2009-2021, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package net.sf.mbus4j.dataframes.datablocks;

import java.io.Serializable;
import java.util.Arrays;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.dif.VariableLengthType;
import net.sf.mbus4j.dataframes.datablocks.vif.ObjectAction;
import net.sf.mbus4j.dataframes.datablocks.vif.SiPrefix;
import net.sf.mbus4j.dataframes.datablocks.vif.UnitOfMeasurement;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifAscii;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFB;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import net.sf.mbus4j.dataframes.datablocks.vif.VifManufacturerSpecific;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.dataframes.datablocks.vif.VifTypes;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeError;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeFC;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeManufacturerSpecific;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeObjectAction;
import net.sf.mbus4j.dataframes.datablocks.vif.VifePrimary;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeTypes;
import net.sf.mbus4j.json.JSONSerializable;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public abstract class DataBlock implements Serializable, JSONSerializable, Cloneable {

    public final static Vife[] EMPTY_VIFE = new Vife[0];

    public static Class<? extends DataBlock> getDataBlockClass(Vif vif, Vife[] vifes, DataFieldCode dfc, VariableLengthType variableLengthType) {
        if (vifes != null) {
            for (Vife vife : vifes) {
                if (vife instanceof VifePrimary) {
                    switch ((VifePrimary) vife) {
                        case START_DATE_TIME_OF:
                        case TIMESTAMP_OF_BEGIN_FIRST_LOWER:
                        case TIMESTAMP_OF_END_FIRST_LOWER:
                        case TIMESTAMP_BEGIN_LAST_LOWER:
                        case TIMESTAMP_END_LAST_LOWER:
                        case TIMESTAMP_BEGIN_FIRST_UPPER:
                        case TIMESTAMP_END_FIRST_UPPER:
                        case TIMESTAMP_BEGIN_LAST_UPPER:
                        case TIMESTAMP_END_LAST_UPPER:
                            return DateAndTimeDataBlock.class;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_S:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_MIN:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_H:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_LOWER_D:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_S:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_MIN:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_H:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_LOWER_D:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_S:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_MIN:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_H:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_FIRST_UPPER_D:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_S:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_MIN:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_H:
                            break;
                        case DURATION_OF_LIMIT_EXCEED_LAST_UPPER_D:
                            break;
                        case DURATION_OF_FIRST_S:
                            break;
                        case DURATION_OF_FIRST_MIN:
                            break;
                        case DURATION_OF_FIRST_H:
                            break;
                        case DURATION_OF_FIRST_D:
                            break;
                        case DURATION_OF_LAST_S:
                            break;
                        case DURATION_OF_LAST_MIN:
                            break;
                        case DURATION_OF_LAST_H:
                            break;
                        case DURATION_OF_LAST_D:
                            break;
                        case TIMESTAMP_BEGIN_OF_FIRST:
                        case TIMESTAMP_END_OF_FIRST:
                        case TIMESTAMP_BEGIN_OF_LAST:
                        case TIMESTAMP_END_OF_LAST:
                            return DateAndTimeDataBlock.class;

                        default:
                    }
                } else {
                    vifes = null;
                }
            }
        }

        switch (dfc) {
            case NO_DATA:
                return EmptyDataBlock.class;
            case SELECTION_FOR_READOUT:
                return ReadOutDataBlock.class;
            case SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST:
                return ReadOutDataBlock.class;
            case SPECIAL_FUNCTION_IDLE_FILLER:
                return EmptyDataBlock.class;
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET:
                return RawDataBlock.class;
            case SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS:
                return RawDataBlock.class;
            case VARIABLE_LENGTH:
                switch (variableLengthType) {
                    case STRING:
                        return StringDataBlock.class;
                    case BIG_DECIMAL:
                        return BigDecimalDataBlock.class;
                    default:
                        return VariableLengthDataBlock.class;
                }
            case _12_DIGIT_BCD:
                return LongDataBlock.class;
            case _16_BIT_INTEGER:
                if (VifPrimary.TIMEPOINT_DATE.equals(vif)) {
                    return DateDataBlock.class;
                } else {
                    return ShortDataBlock.class;
                }
            case _24_BIT_INTEGER:
                return IntegerDataBlock.class;
            case _2_DIGIT_BCD:
                return ByteDataBlock.class;
            case _32_BIT_INTEGER:
                if (VifPrimary.TIMEPOINT_TIME_AND_DATE.equals(vif)) {
                    return DateAndTimeDataBlock.class;
                } else {
                    return IntegerDataBlock.class;
                }
            case _32_BIT_REAL:
                return RealDataBlock.class;
            case _48_BIT_INTEGER:
                return LongDataBlock.class;
            case _4_DIGIT_BCD:
                return ShortDataBlock.class;
            case _64_BIT_INTEGER:
                if (VifPrimary.ENHANCED_IDENTIFICATION_RECORD.equals(vif)) {
                    return EnhancedIdentificationDataBlock.class;
                } else {
                    return LongDataBlock.class;
                }
            case _6_DIGIT_BCD:
                return IntegerDataBlock.class;
            case _8_BIT_INTEGER:
                return ByteDataBlock.class;
            case _8_DIGIT_BCD:
                if (VifPrimary.ENHANCED_IDENTIFICATION_RECORD.equals(vif)) {
                    return EnhancedIdentificationDataBlock.class;
                } else {
                    return IntegerDataBlock.class;
                }
            default:
                throw new UnsupportedOperationException("Unknown ControlCode");
        }

    }

    public static Vif getVif(String vifTypeLabel, String vifLabel, UnitOfMeasurement unitOfMeasurement, SiPrefix siPrefix, Integer exponent) {
        VifTypes vifType = VifTypes.fromLabel(vifTypeLabel);
        switch (vifType) {
            case PRIMARY:
                return VifPrimary.assemble(vifLabel, unitOfMeasurement, siPrefix, exponent);
            case FB_EXTENTION:
                return VifFB.assemble(vifLabel, unitOfMeasurement, siPrefix, exponent);
            case FD_EXTENTION:
                return VifFD.assemble(vifLabel, unitOfMeasurement, siPrefix, exponent);
            case ASCII:
                return new VifAscii(vifLabel);
            case MANUFACTURER_SPECIFIC:
                return new VifManufacturerSpecific();
            default: {
                throw new IllegalArgumentException("Unknown vifType: " + vifTypeLabel);
            }
        }
    }

    public static Vif vifFromJSON(JSONObject jsonVif) {
        return getVif(jsonVif.getString("vifType"),
                jsonVif.containsKey("description") ? jsonVif.getString("description") : null,
                jsonVif.containsKey("unitOfMeasurement") ? UnitOfMeasurement.fromLabel(jsonVif.getString("unitOfMeasurement")) : null,
                jsonVif.containsKey("siPrefix") ? SiPrefix.fromLabel(jsonVif.getString("siPrefix")) : null,
                jsonVif.containsKey("exponent") ? jsonVif.getInt("exponent") : null);
    }

    public static Vife getVife(String vifeTypeLabel, String vifeLabel) {
        VifeTypes vifeType = VifeTypes.fromLabel(vifeTypeLabel);
        switch (vifeType) {
            case PRIMARY:
                return VifePrimary.fromLabel(vifeLabel);
            case FC_EXTENSION:
                return VifeFC.fromLabel(vifeLabel);
            case ERROR:
                return VifeError.fromLabel(vifeLabel);
            case OBJECT_ACTION:
                return VifeObjectAction.fromLabel(vifeLabel);
            case MAN_SPEC:
                return new VifeManufacturerSpecific((byte) Integer.parseInt(vifeLabel, 16));
            default:
                throw new IllegalArgumentException(vifeType.getLabel());
        }
    }

    public static Vife[] vifesFromJSON(VifTypes vifType, JSONArray jsonVifes) {
        if (jsonVifes instanceof JSONArray) {
            Vife[] result = new Vife[jsonVifes.size()];
            for (int i = 0; i < jsonVifes.size(); i++) {
                result[i] = getVife(jsonVifes.getJSONObject(i).getString("vifeType"), jsonVifes.getJSONObject(i).getString("name"));
            }
            return result;
        } else {
            return new Vife[0];
        }
    }
    private ObjectAction action;
    private Vif vif;
    /**
     * Extend paramdescription by VIFE value from table 8.4.5
     *
     * @param vife
     */
    private Vife[] vifes = EMPTY_VIFE;
    private FunctionField functionField;
    private long storageNumber;
    private int tariff;
    private DataFieldCode dataFieldCode;
    private short subUnit;

    public DataBlock() {
        super();
    }

    @Deprecated
    public DataBlock(DataFieldCode dif, FunctionField functionField, short subUnit, int tariff, long storageNumber, Vif vif, Vife... vifes) {
        super();
        this.dataFieldCode = dif;
        this.functionField = functionField;
        this.subUnit = subUnit;
        this.tariff = tariff;
        this.storageNumber = storageNumber;
        this.vif = vif;
        if (vifes.length > 0) {
            this.vifes = Arrays.copyOf(vifes, vifes.length);
        }
    }

    @Deprecated
    public DataBlock(DataFieldCode dif, Vif vif, Vife... vifes) {
        super();
        this.dataFieldCode = dif;
        functionField = FunctionField.INSTANTANEOUS_VALUE;
        this.vif = vif;
        if (vifes.length > 0) {
            this.vifes = Arrays.copyOf(vifes, vifes.length);
        }
    }

    public boolean addVife(Vife vife) {
        vifes = Arrays.copyOf(vifes, vifes.length + 1);
        vifes[vifes.length - 1] = vife;
        return true;
    }

    /**
     * @return the action
     */
    public ObjectAction getAction() {
        return action;
    }

    public DataFieldCode getDataFieldCode() {
        return dataFieldCode;
    }

    public Integer getExponent() {
        return vif != null ? vif.getExponent() : null;
    }

    public FunctionField getFunctionField() {
        return functionField;
    }

    public String getParamDescr() {
        StringBuilder sb = new StringBuilder();
        if (vif != null) {
            sb.append(vif.getLabel());
        }
        if (vifes != null) {
            for (Vife vife : vifes) {
                sb.append(", ").append(vife.getLabel());
            }
        }
        return sb.toString();
    }

    public SiPrefix getSiPrefix() {
        return vif == null ? null : vif.getSiPrefix();
    }

    public long getStorageNumber() {
        return storageNumber;
    }

    /**
     * @return the deviceUnit
     */
    public short getSubUnit() {
        return subUnit;
    }

    public int getTariff() {
        return tariff;
    }

    public UnitOfMeasurement getUnitOfMeasurement() {
        return vif != null ? vif.getUnitOfMeasurement() : null;
    }

    public abstract String getValueAsString();

    public Vif getVif() {
        return vif;
    }

    public Vife[] getVifes() {
        return vifes;
    }

    /**
     * @param action the action to set
     */
    public void setAction(ObjectAction action) {
        this.action = action;
    }

    public void setFunctionField(FunctionField functionField) {
        this.functionField = functionField;
    }

    public void setStorageNumber(long storageNumber) {
        this.storageNumber = storageNumber;
    }

    /**
     * @param subUnit the subUnit to set
     */
    public void setSubUnit(short subUnit) {
        this.subUnit = subUnit;
    }

    public void setTariff(int tariff) {
        this.tariff = tariff;
    }

    public void setVif(Vif vif) {
        this.vif = vif;
    }

    public void toString(StringBuilder sb, String inset) {
        if (action != null) {
            sb.append(inset).append("action = ").append(getAction()).append("\n");
        }
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        if (vif != null) {
            sb.append(inset).append("description = ").append(getParamDescr()).append("\n");
            if (getDataFieldCode().equals(DataFieldCode.NO_DATA) || getDataFieldCode().equals(DataFieldCode.SELECTION_FOR_READOUT)) {
                sb.append(inset).append("unit =");
            } else {
                sb.append(inset).append("value = ").append(getValueAsString());
            }
            if (getUnitOfMeasurement() != null) {
                if (getExponent() != null) {

                    if (getExponent() > 0) {
                        sb.append(" * 1");
                        for (int i = 0; i < getExponent(); i++) {
                            sb.append("0");
                        }
                    } else if (getExponent() < 0) {
                        sb.append(" * 0.");
                        for (int i = -1; i > getExponent(); i--) {
                            sb.append("0");
                        }
                        sb.append("1");

                    }
                }

                final Double cf = VifePrimary.getVifeCorrectionFactor(vifes);
                if (!Double.isNaN(cf)) {
                    sb.append(" * ");
                    sb.append(cf);
                }

                sb.append(" [");
                if (getSiPrefix() != null) {
                    sb.append(getSiPrefix());
                }
                sb.append(getUnitOfMeasurement()).append("]\n");
            } else {
                sb.append("\n");
            }
        }
        if (functionField != null) {
            sb.append(inset).append("tariff = ").append(getTariff()).append('\n');
            sb.append(inset).append("storageNumber = ").append(getStorageNumber()).append("\n");
            sb.append(inset).append("functionField = ").append(getFunctionField()).append("\n");
            sb.append(inset).append("subUnit = ").append(getSubUnit()).append("\n");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb, "");
        return sb.toString();
    }

    protected void accumulateDatatoJSON(JSONObject json) {
    }

    @Override
    public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
        JSONObject result = new JSONObject();
        JSONObject drh = new JSONObject();
        JSONObject dib = new JSONObject();
        JSONObject vib = new JSONObject();

        if (getAction() != null) {
            dib.accumulate("action", getAction().getLabel());
        }
        dib.accumulate("dataFieldCode", getDataFieldCode().getLabel());
        if (DataFieldCode.VARIABLE_LENGTH.equals(getDataFieldCode())) {
            if (this instanceof StringDataBlock) {
                dib.accumulate("variableLengthType", VariableLengthType.STRING.getLabel());
            } else {
                throw new UnsupportedOperationException("Varoable datablock:" + this.getClass().getName());
            }
        }
        if (DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET.equals(dataFieldCode) || DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS.equals(dataFieldCode) || DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST.equals(dataFieldCode)) {
            drh.accumulate("dib", dib);
        } else {
            dib.accumulate("functionField", getFunctionField().getLabel());
            dib.accumulate("storageNumber", getStorageNumber());
            dib.accumulate("subUnit", getSubUnit());
            dib.accumulate("tariff", getTariff());
            drh.accumulate("dib", dib);
            JSONObject jsonVif = new JSONObject();
            jsonVif.accumulate("vifType", vif.getVifType().getLabel());
            jsonVif.accumulate("description", vif.getLabel());
            if (vif.getUnitOfMeasurement() != null) {
                jsonVif.accumulate("unitOfMeasurement", vif.getUnitOfMeasurement().getLabel());
            }
            if (vif.getSiPrefix() != null) {
                jsonVif.accumulate("siPrefix", vif.getSiPrefix().getLabel());
            }
            if (vif.getExponent() != null) {
                jsonVif.accumulate("exponent", vif.getExponent());
            }

            vib.accumulate("vif", jsonVif);
            if (getVifes().length > 0) {
                JSONArray jsonVifes = new JSONArray();
                for (Vife value : vifes) {
                    JSONObject jsonVife = new JSONObject();
                    jsonVife.accumulate("vifeType", value.getVifeType().getLabel());
                    jsonVife.accumulate("name", value.getLabel());
                    jsonVifes.add(jsonVife);
                }
                vib.accumulate("vifes", jsonVifes);
            }
            drh.accumulate("vib", vib);
        }
        result.accumulate("drh", drh);
        if ((JsonSerializeType.ALL == jsonSerializeType) || (JsonSerializeType.SLAVE_CONFIG == jsonSerializeType)) {
            accumulateDatatoJSON(result);
        }
        return result;
    }

    @Override
    public void fromJSON(JSONObject json) {
        JSONObject drh = json.getJSONObject("drh");
        JSONObject dib = drh.getJSONObject("dib");
        JSONObject vib = drh.getJSONObject("vib");
        if (dib.get("action") != null) {
            setAction(ObjectAction.fromLabel(dib.getString("action")));
        }

        setDataFieldCode(DataFieldCode.fromLabel(dib.getString("dataFieldCode")));
        if ((DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET == dataFieldCode)
                || (DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS == dataFieldCode)
                || (DataFieldCode.SPECIAL_FUNCTION_GLOBAL_READOUT_REQUEST == dataFieldCode)) {
        } else {
            setFunctionField(FunctionField.fromLabel(dib.getString("functionField")));
            setStorageNumber(dib.getLong("storageNumber"));
            setSubUnit((short) dib.getInt("subUnit"));
            setTariff(dib.getInt("tariff"));
            if (!vib.isNullObject()) {
                JSONObject jsonVif = vib.getJSONObject("vif");
                vif = vifFromJSON(jsonVif);
                if (vib.containsKey("vifes")) {

                    Vife[] parsedVifes = vifesFromJSON(vif.getVifType(), vib.getJSONArray("vifes"));
                    if (parsedVifes.length > 0) {
                        this.vifes = Arrays.copyOf(parsedVifes, parsedVifes.length);
                    } else {
                        vifes = EMPTY_VIFE;
                    }
                } else {
                    vifes = EMPTY_VIFE;
                }
            }
        }
    }

    public abstract void setValue(String text);

    /**
     * @param dataFieldCode the dataFieldCode to set
     */
    public void setDataFieldCode(DataFieldCode dataFieldCode) {
        this.dataFieldCode = dataFieldCode;
    }

    public void clearVifes() {
        vifes = EMPTY_VIFE;
    }

    /* Helper method */
    protected String formatBcdError(String bcdError) {
        if (bcdError == null) {
            return bcdError;
        }
        int length;
        switch (getDataFieldCode()) {
            case _2_DIGIT_BCD:
                length = 2;
                break;
            case _4_DIGIT_BCD:
                length = 4;
                break;
            case _6_DIGIT_BCD:
                length = 6;
                break;
            case _8_DIGIT_BCD:
                length = 8;
                break;
            case _12_DIGIT_BCD:
                length = 12;
                break;
            default:
                throw new RuntimeException("Unknown DFC " + getDataFieldCode());
        }

        if (bcdError.length() == length) {
            return bcdError;
        } else if ((bcdError.length() < length) && (bcdError.length() > 0)) {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length - bcdError.length(); i++) {
                sb.append(' ');
            }
            sb.append(bcdError);
            return sb.toString();
        } else {
            throw new RuntimeException("Bcd is too long: \"" + bcdError + "\"");
        }

    }

    public int getCorrectionExponent(SiPrefix siPrefix) {
        return getSiPrefix().getExponent() - siPrefix.getExponent() + getExponent() + VifePrimary.getVifeCorrectionExponent(vifes);
    }

    public double getCorrectionConstant() {
        return VifePrimary.getVifeCorrectionConstant(vifes);
    }

}
