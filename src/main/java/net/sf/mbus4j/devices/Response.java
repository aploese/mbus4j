/*
 * mbus4j - Open source drivers for mbus protocol (http://www.m-bus.com) - http://mbus4j.sourceforge.net
 * Copyright (C) 2009  Arne Plöse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class Response implements Cloneable, Iterable<DataTag> {

    List<DataTag> tags = new ArrayList<DataTag>();
    Frame[] initFrames;

    public DataTag addOptionalTag(DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        return addOptionalTag(vif.getLabel(), difCode, functionField, deviceUnit, tariff, storageNumber, vif, vifes);
    }

    public DataTag addOptionalTag(DataFieldCode difCode, Vif vif) {
        return addOptionalTag(difCode, FunctionField.INSTANTANEOUS_VALUE, 0, 0, 0, vif);
    }

    public DataTag addOptionalTag(String label, DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        final DataTag d = addTag(label, difCode, functionField, deviceUnit, tariff, storageNumber, vif, vifes);
        d.setOptional(true);
        return d;
    }

    public DataTag addTag(DataFieldCode difCode, FunctionField functionField, int deviceUnit, int tariff, int storageNumber, Vif vif, Vife... vifes) {
        return addTag(vif.getLabel(), difCode, functionField, deviceUnit, tariff, storageNumber, vif, vifes);
    }

    public DataTag addTag(DataFieldCode difCode, Vif vif, Vife... vife) {
        return addTag(difCode, FunctionField.INSTANTANEOUS_VALUE, 0, 0, 0, vif, vife);
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

    public DataTag addTag(String label, DataFieldCode difCode, Vif vif, Vife... vife) {
        return addTag(label, difCode, FunctionField.INSTANTANEOUS_VALUE, 0, 0, 0, vif, vife);
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

    /**
     * Make a deep copy of datatags
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Response clone() throws CloneNotSupportedException {
        Response result = (Response) super.clone();
        result.tags = new ArrayList<DataTag>();
        result.tags.addAll(tags);
        return result;
    }

    public void deleteOptionaltag(int i) {
        if (tags.get(i).isOptional()) {
            tags.remove(i);
        } else {
            throw new RuntimeException("Datatag is not optional can not remove");
        }
    }

    public Iterable<DataTag> getDataTagsIterable() {
        return tags;
    }

    public int getDataTagSize() {
        return tags.size();
    }

    public Frame[] getinitFrames() {
        return initFrames;
    }

    public DataTag getTag(int i) {
        return tags.get(i);
    }

    @Override
    public Iterator<DataTag> iterator() {
        return tags.iterator();
    }

    public void setInitFrames(Frame... initFrames) {
        this.initFrames = initFrames;
    }
}
