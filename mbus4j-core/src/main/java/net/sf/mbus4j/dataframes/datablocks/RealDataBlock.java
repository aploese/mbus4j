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

import net.sf.json.JSONObject;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vife;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class RealDataBlock extends DataBlock {

    private float value;

    public RealDataBlock() {
        super();
    }

    @Deprecated
    public RealDataBlock(DataFieldCode dataFieldCode, Vif vif, Vife... vifes) {
        super(dataFieldCode, vif, vifes);
    }

    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return Float.toString(value);
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        json.accumulate("data", getValue());
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        setValue((float) json.getDouble("data"));
    }

    @Override
    public void setValue(String text) {
        value = Float.parseFloat(text);
    }
}
