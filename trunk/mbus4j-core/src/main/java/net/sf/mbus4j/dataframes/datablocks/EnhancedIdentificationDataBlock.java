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
package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.MBusMedium;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class EnhancedIdentificationDataBlock extends DataBlock {

    private int id;
    private String man;
    private int version;
    private MBusMedium medium;

    public EnhancedIdentificationDataBlock(DataBlock db) {
        super(db);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the man
     */
    public String getMan() {
        return man;
    }

    /**
     * @return the medium
     */
    public MBusMedium getMedium() {
        return medium;
    }

    @Override
    public String getValueAsString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param man the man to set
     */
    public void setMan(String man) {
        this.man = man;
    }

    /**
     * @param medium the medium to set
     */
    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public void toString(StringBuilder sb, String inset) {
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        if (getVif() != null) {
            sb.append(inset).append("description = ").append(getParamDescr()).append("\n");
        }
        sb.append(inset).append(String.format("id = %08d\n", id));
        if (man != null) {
            // avail if long
            sb.append(inset).append("man = ").append(man).append("\n");
            sb.append(inset).append("version = ").append(version).append("\n");
        }
        if (medium != null) {
            sb.append(inset).append("medium = ").append(medium).append("\n");
        }
    }
}
