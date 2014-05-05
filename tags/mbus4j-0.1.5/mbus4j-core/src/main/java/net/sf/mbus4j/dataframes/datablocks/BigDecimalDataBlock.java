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
import java.math.BigDecimal;
import net.sf.json.JSONObject;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class BigDecimalDataBlock extends DataBlock {

    private BigDecimal value;

    public BigDecimalDataBlock() {
        super();
    }

    @Override
    public String getValueAsString() {
        return getValue() != null ? getValue().toPlainString() : null;
    }

    @Override
    protected void accumulateDatatoJSON(JSONObject json) {
        json.accumulate("data", getValue().toEngineeringString());
    }

    @Override
    public void fromJSON(JSONObject json) {
        super.fromJSON(json);
        setValue(new BigDecimal(json.getString("data")));
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public void setValue(String text) {
        this.value = new BigDecimal(text);
    }

}
