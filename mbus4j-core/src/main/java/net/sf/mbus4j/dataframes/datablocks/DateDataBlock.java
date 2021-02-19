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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class DateDataBlock extends DataBlock {

    // SimpleDateFormat is not thread-safe, so give one to each thread
    private static final ThreadLocal<SimpleDateFormat> ISO_8601 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private Date value;

    public DateDataBlock() {
        super();
    }

    @Deprecated
    public DateDataBlock(Vif vif, Vife... vifes) {
        super(DataFieldCode._16_BIT_INTEGER, vif, vifes);
    }

    /**
     * @return the value
     */
    public Date getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return ISO_8601.get().format(value);
    }

    /**
     * @param value the value to set
     */
    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        JSONObject jsonValue = new JSONObject();
        jsonValue.accumulate("date", ISO_8601.get().format(value));
        json.accumulate("data", jsonValue);
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        try {
            JSONObject jsonValue = json.getJSONObject("data");
            value = ISO_8601.get().parse(jsonValue.getString("date"));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setValue(String text) {
        try {
            value = DateFormat.getDateInstance().parse(text);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
