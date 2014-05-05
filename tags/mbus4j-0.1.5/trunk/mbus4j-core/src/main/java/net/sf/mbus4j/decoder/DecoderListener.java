/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.decoder;

import net.sf.mbus4j.dataframes.Frame;

/**
 *
 * @author aploese
 */
public interface DecoderListener {

    void success(Frame parsingFrame);

}
