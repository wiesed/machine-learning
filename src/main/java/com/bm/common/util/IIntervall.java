package com.bm.common.util;

/**
 * Stellt ein intervall fuer einen moving window dar.
 * @author Daniel Wiese
 *
 */
public interface IIntervall {
	
	/**
	 * Returns the start index which is inclusive.
	 * @return the start index.
	 */
	int getStartIndex();
	
	/**
	 * Returns the end index which is inclusive.
	 * @return the end index.
	 */
	int getEndIndex();
	
	/**
	 * Returns the value in the next position. 
	 * @param pos the position in the index.
	 * @return the value
	 */
	int getValue(int pos);

}
