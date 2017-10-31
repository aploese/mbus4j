package net.sf.mbus4j.dataframes;

public interface SendUserDataFrame extends RequestFrame<SingleCharFrame> {

	default boolean isFcv() {
		return true;
	}
	
	default void setFcv(boolean fcv) {
		if (!fcv) {
			throw new IllegalArgumentException("Can't set fcv to false!");
		}
	}

}
