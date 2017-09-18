package com.bm.common.cache;

/**
 * The  TimeOrderedKey interface sorgt dafuer, dass objekte, die in den
 * Cache eingefegt werden zeitliche definierte ordnung haben damit diese
 * entprechend evicted werden koennen.
 * 
 * @author Daniel Wiese
 * 
 */
public interface TimeOrderedKey {

	/**
	 * Return the date of the implementation.
	 * 
	 * @return - the date as UTC
	 */
	long getSystemTimeInMillis();

}
