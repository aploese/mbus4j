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
package net.sf.mbus4j.dataframes;

import net.sf.json.JSONObject;
import net.sf.mbus4j.json.JsonSerializeType;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public interface SingleCharFrame extends Frame {

    static class SingleCharFrameImpl implements SingleCharFrame {

        @Override
        public ControlCode getControlCode() {
            return ControlCode.CON_ACK;
        }

        @Override
        public JSONObject toJSON(JsonSerializeType jsonSerializeType) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fromJSON(JSONObject json) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    public final static SingleCharFrame SINGLE_CHAR_FRAME = new SingleCharFrameImpl();
}
