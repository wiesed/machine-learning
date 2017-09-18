package com.bm.classify.sampling;


/**
 * The DateOrder.java class is responsible for
 * order date objects.
 * @author Daniel Wiese (Daniel.Wiese@siemens.com)
 *
 */
public interface DateOrder {

	/**
	 * Return the date of the implementation.
	 * @return - the date
	 */
    long getSystemTimeInMillis();
	
}
