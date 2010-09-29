/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
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
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j.dataframes.datablocks;

import net.sf.json.JSONObject;


/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class LongDataBlock
        extends DataBlock {

    private long value;
    private String bcdError;

    public boolean isBcdError() {
        return bcdError != null;
    }

    public LongDataBlock() {
        super();
    }

    /**
     * @return the value
     */
    public long getValue() {
        if (isBcdError()) {
            throw new IllegalArgumentException("No value BCD Error: " + bcdError);
        } else {
            return value;
        }
    }

    @Override
    public String getValueAsString() {
        if (bcdError != null) {
            return "BCD Error: " + bcdError;
        }

        switch (getDataFieldCode()) {
            case _48_BIT_INTEGER:
            case _64_BIT_INTEGER:
                return Long.toString(value);

            case _8_DIGIT_BCD:
                return String.format("%08d", value);

            case _12_DIGIT_BCD:
                return String.format("%012d", value);

            default:
                throw new RuntimeException("DIF not supported: " + getDataFieldCode());
        }
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        if (!isBcdError()) {
            json.accumulate("data",
                    getValue());
        } else {
            JSONObject jsonBcdError = new JSONObject();
            jsonBcdError.accumulate("bcdErrorCode",
                    getBcdError());
            json.accumulate("data", jsonBcdError);
        }
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);

        if (json.get("data") instanceof JSONObject) {
            JSONObject data = json.getJSONObject("data");

            if (data.containsKey("bcdErrorCode")) {
                bcdError = data.getString("bcdErrorCode");
            } else {
                throw new IllegalArgumentException("Unknown value at data: " + data.toString(1));
            }
        } else {
            setValue(json.getLong("data"));
        }
    }

    /**
     * @return the bcdError
     */
    public String getBcdError() {
        return bcdError;
    }

    /**
     * @param bcdError the bcdError to set
     */
    public void setBcdError(String bcdError) {
        this.bcdError = bcdError;
    }

    @Override
    public void setValue(String text) {
        try {
            value = Long.parseLong(text);
            bcdError = null;
        } catch (NumberFormatException ex) {
            value = 0;
            bcdError = text;
        }
    }
}
