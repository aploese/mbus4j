/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.devices;

import java.io.IOException;
import net.sf.mbus4j.dataframes.Frame;

/**
 *
 * @author aploese
 */
public interface Sender {

    Frame send(Frame frame) throws IOException, InterruptedException;

}
