package net.sf.mbus4j.dataframes.datablocks;

/*
 * #%L
 * mbus4j-core
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        return DateFormat.getDateInstance().format(value);
    }

    /**
     * @param value the value to set
     */
    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        DateFormat df = DateFormat.getDateInstance();
        JSONObject jsonValue = new JSONObject();
        jsonValue.accumulate("date", df.format(value));
        json.accumulate("data", jsonValue);
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        try {
            JSONObject jsonValue = json.getJSONObject("data");
            DateFormat df = DateFormat.getDateInstance();
            value = df.parse(jsonValue.getString("date"));
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
