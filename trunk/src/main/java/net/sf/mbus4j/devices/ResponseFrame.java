/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j.devices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;
import net.sf.mbus4j.dataframes.datablocks.vif.VifeStd;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public abstract class ResponseFrame implements Cloneable, Iterable<DataBlock> {
    public static interface MBusResponsesTreeMap extends Map<ResponseFrame, Collection<DataBlock>> {}


    private String name = "Default";

    public abstract Iterable<DataBlock> getDataBlocksIterable();

    public abstract int getDataBlockCount();

    public abstract Frame[] getinitFrames();

    public abstract DataBlock getDataBlock(int i);

    @Override
    public abstract Iterator<DataBlock> iterator();

    /*TODO implement in generic
    public boolean addTagFromUserDataResponse(DataBlock db) {
        final DataTag d = new DataTag();
        if (db.getVif() != null) {
            d.setLabel(db.getVif().getLabel());
        } else {
            d.setLabel(db.getClass().getSimpleName());
        }
        d.setDataInformationBlock(db.getDataFieldCode(), db.getFunctionField(), db.getSubUnit(), db.getTariff(), db.getStorageNumber());
        d.setValueInformationBlock(db.getVif(), db.getVifes() != null ? db.getVifes().toArray(new Vife[0]) : new Vife[0]);
        return tags.add(d);
    }
*/
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Find the Timestamp of dt.
     * Search for 2nd dt with same storagenumber etc and use its Time or
     * DateTime DataBlock to retrieve the date if nothing is found use the
     * DefaultTime
     * @param dt
     * @param dbs
     * @param defaultTime
     * @return
     */
    public Date getTimeStampOf(DataBlock dt, Collection<DataBlock> dbs, Date defaultTime) {
        final DataBlock resultDt = getTimeStampDtOf(dt);
        if (resultDt == null) {
            return defaultTime;
        }
        //TODO resturn value vom Collection
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public DataBlock getTimeStampDtOf(DataBlock dt) {
        switch (dt.getFunctionField()) {
            case INSTANTANEOUS_VALUE:
                return searchForTimeData(dt);
            case MINIMUM_VALUE:
                return searchForTimestamp(dt);
            case MAXIMUM_VALUE:
                return searchForTimestamp(dt);
            case VALUE_DURING_ERROR_STATE:
                return searchForTimestamp(dt);
            default:
                throw new UnsupportedOperationException("Unknown FunctionField" + dt.getFunctionField());
        }
    }

    /**
     * find date or dateTime
     * @param dt
     * @return
     */
    private DataBlock searchForTimeData(DataBlock dt) {
        for (int i = 0; i < getDataBlockCount(); i++) {
            if (getDataBlock(i).getStorageNumber() == dt.getStorageNumber() ) {
                if (getDataBlock(i).getVif().equals(VifStd.TIMEPOINT_DATE) || getDataBlock(i).getVif().equals(VifStd.TIMEPOINT_TIME_AND_DATE)) {
                    if (!getDataBlock(i).equals(dt)) {
                        return getDataBlock(i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * search for vife but vif is same....
     * @param dt
     * @return
     */
    private DataBlock searchForTimestamp(DataBlock dt) {
        if (VifeStd.isTimestampVife(dt.getVifes())) {
            return null;
        }
        for (int i = 0; i < getDataBlockCount(); i++) {
            if (getDataBlock(i).getStorageNumber() == dt.getStorageNumber() ) {
                if (getDataBlock(i).getVif().equals(dt.getVif())) {
                if (VifeStd.isTimestampVife(getDataBlock(i).getVifes())) {
                    if (!getDataBlock(i).equals(dt)) {
                        return getDataBlock(i);
                    }
                }
                }
            }
        }
        return null;
    }
}
