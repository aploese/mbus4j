/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.dataframes;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 *
 * @author aploese
 */
public interface LongFrame extends Frame, PrimaryAddress, Iterable<DataBlock>  {

    void replaceDataBlock(DataBlock oldDataBlock, DataBlock newDataBlock);

    boolean addDataBlock(DataBlock dataBlock);

    void setLastPackage(boolean isLastPackage);

    DataBlock getLastDataBlock();

}
