/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import net.sf.mbus4j.dataframes.ResponseFrame;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;

/**
 * Device in master tree
 * @author aploese
 */
public interface ProxyDevice {
    Map<ResponseFrame, Collection<DataBlock>> readValues(Sender sender, ResponseFrame... responseFrames) throws IOException, InterruptedException;
}
