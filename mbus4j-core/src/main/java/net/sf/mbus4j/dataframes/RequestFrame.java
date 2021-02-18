/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.dataframes;

/**
 *
 * @author Arne Plöse
 */
public interface RequestFrame<T extends ResponseFrame> extends Frame, PrimaryAddress {
    
	static final boolean DEFAULT_FCB = true;
	static final boolean DEFAULT_FCV = true;

	boolean isFcb();
	
	boolean isFcv();
	
	void setFcb(boolean fcb);
	
	void setFcv(boolean fcv);

	default void toggleFcb() {
		setFcb(isFcb());
	}
	
}
