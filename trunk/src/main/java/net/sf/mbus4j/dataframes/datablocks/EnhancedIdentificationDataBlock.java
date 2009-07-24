/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes.datablocks;

import net.sf.mbus4j.dataframes.MBusMedium;

/**
 *
 * @author aploese
 */
public class EnhancedIdentificationDataBlock extends DataBlock {

    private int id;
    private String man;
    private int version;
    private MBusMedium medium;

    public EnhancedIdentificationDataBlock(DataBlock db) {
        super(db);
    }

    @Override
    public String getValueAsString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void toString(StringBuilder sb, String inset) {
        sb.append(inset).append("dataType = ").append(getDataFieldCode()).append("\n");
        if (getVif() != null) {
            sb.append(inset).append("description = ").append(getParamDescr()).append("\n");
        }
        sb.append(inset).append(String.format("id = %08d\n",id));
        if (man != null) {
            // avail if long
        sb.append(inset).append("man = ").append(man).append("\n");
        sb.append(inset).append("version = ").append(version).append("\n");
        }
        if (medium != null) {
            sb.append(inset).append("medium = ").append(medium).append("\n");
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the man
     */
    public String getMan() {
        return man;
    }

    /**
     * @param man the man to set
     */
    public void setMan(String man) {
        this.man = man;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the medium
     */
    public MBusMedium getMedium() {
        return medium;
    }

    /**
     * @param medium the medium to set
     */
    public void setMedium(MBusMedium medium) {
        this.medium = medium;
    }

}
