/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author aploese
 */
public class Response implements Cloneable, Iterable<DataTag> {

    List<DataTag> tags = new ArrayList<DataTag>();
    Frame[] initFrames;


    public DataTag addTag(DataFieldCode difCode, Vif vif, Vife ... vife) {
        return addTag(difCode, FunctionField.INSTANTANEOUS_VALUE, 0, 0, 0, vif, vife);
    }

    public DataTag addTag(String label, DataFieldCode difCode, Vif vif, Vife ... vife) {
        return addTag(label, difCode, FunctionField.INSTANTANEOUS_VALUE, 0, 0, 0, vif, vife);
    }

    public DataTag addTag(DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        return addTag(vif.getLabel(), difCode, functionField, deviceUnit, tariff, storageNumber, vif, vifes);
    }

    public DataTag addTag(String label, DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        final DataTag d = new DataTag();
        d.setLabel(label);
        d.setOptional(false);
        d.setDataInformationBlock(difCode, functionField, deviceUnit, tariff, storageNumber);
        d.setValueInformationBlock(vif, vifes);
        tags.add(d);
        return d;
    }

    public DataTag addOptionalTag(DataFieldCode difCode, Vif vif) {
        return addOptionalTag(difCode, FunctionField.INSTANTANEOUS_VALUE, 0, 0, 0, vif);
    }

    public DataTag addOptionalTag(DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        return addOptionalTag(vif.getLabel(), difCode, functionField, deviceUnit, tariff, storageNumber, vif, vifes);
    }

    public DataTag addOptionalTag(String label, DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        final DataTag d = addTag(label, difCode, functionField, deviceUnit, tariff, storageNumber, vif, vifes);
        d.setOptional(true);
        return d;
    }

    public void setInitFrames(Frame ... initFrames) {
        this.initFrames = initFrames;
    }

    public Frame[] getinitFrames() {
        return initFrames;
    }

    /**
     * Make a deep copy of datatags
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Response clone() throws CloneNotSupportedException {
        Response result = (Response)super.clone();
        result.tags = new ArrayList<DataTag>();
        result.tags.addAll(tags);
        return result;
    }

    public DataTag getTag(int i) {
        return tags.get(i);
    }

    public void chooseAlternative(int i, Vif vif) {
        DataTag dt = tags.get(i);
        if (vif.equals(dt.getVif())) {
            dt.setAlternative(null);
            return;
        }
        while (dt.getAlternative() != null) {
            dt = dt.getAlternative();
            if (vif.equals(dt.getVif())) {
                tags.set(i, dt);
                dt.setAlternative(null);
                return;
        }
        }
    }

    public void deleteOptionaltag(int i) {
        if (tags.get(i).isOptional()) {
            tags.remove(i);
        } else {
            throw new RuntimeException("Datatag is not optional can not remove");
        }
    }

    public int getDataTagSize() {
        return tags.size();
    }

    public Iterable<DataTag> getDataTagsIterable() {
        return tags;
    }

    @Override
    public Iterator<DataTag> iterator() {
        return tags.iterator();
    }
}
