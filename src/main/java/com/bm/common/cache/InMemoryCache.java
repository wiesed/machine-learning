package com.bm.common.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;

/**
 * Cache implementation of an in memory cache. The eviction startegy is time
 * stamp based, every key must implement a TimeOrderdKey interface. Object with
 * oldest key (oldest timestamp) are evited firstly.
 * 
 * @param <T> -
 *            the key-type (must implement the DateOrder Interface)
 * @author Daniel Wiese
 * @param <D> -
 *            the type of the value
 * @since 19.11.2005
 */
public class InMemoryCache<T extends TimeOrderedKey, D> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(InMemoryCache.class);

	private final int capacity;

	private final PriorityQueue<T> queue;

	private final Map<T, D> map;

	/**
	 * Default constructor.
	 * 
	 * @param capacity -
	 *            the capacity (how many elements should be stored)
	 */
	public InMemoryCache(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException(
					"Dia Kapazitaet muss minimum 1 sein");
		}
		this.capacity = capacity;
		final DateComarator<T> comp = new DateComarator<T>();
		this.queue = new PriorityQueue<T>(this.capacity, comp);
		this.map = new HashMap<T, D>(this.capacity);
	}

	/**
	 * Adds a date object to the structure The memory consumption is limited to
	 * the number of specified elements The implementation provides a O( 2*
	 * log(n))=O(log(n)) time for the insertion (n is the capacity).
	 * 
	 * @param key -
	 *            the elements to add
	 * @param value -
	 *            the value to add
	 */
	public synchronized void add(T key, D value) {

		if (key == null || value == null) {
			throw new IllegalArgumentException(
					"Key und Value duerfen nicht null sein");
		}

		this.queue.add(key);
		this.map.put(key, value);
		if (log.isDebugEnabled()) {
			log.debug("(" + this.queue.size() + "/" + capacity
					+ ") Inserting: " + value);
		}
		if (this.queue.size() > this.capacity) {
			T removed = this.queue.poll();
			this.map.remove(removed);
			if (removed == null) {
				log.info("A NULL object was removed>(" + this.queue.size()
						+ "/" + capacity + ") This is normally impossible!!");
				throw new RuntimeException("A NULL object was removed>(" + this.queue.size()
						+ "/" + capacity + ") This is normally impossible!!");
			} else if (log.isDebugEnabled()) {
				log.debug("(" + this.queue.size() + "/" + capacity
						+ ") Removed: "
						+ new Date(removed.getSystemTimeInMillis()));
			}
		}
	}

	/**
	 * Returns the element from the cache or null if element is not in the
	 * cache.
	 * 
	 * @author Daniel Wiese
	 * @since 19.11.2005
	 * @param key -
	 *            the key
	 * @return the element from the cache or null if element is not in the
	 *         cache.
	 */
	public synchronized D get(T key) {
		if (key != null) {
			return this.map.get(key);
		} else {
			return null;
		}
	}

	/**
	 * Returns the newest keys (the size will not exeed specified capacity).
	 * 
	 * @return - the newest date objects
	 */
	public List<T> getAllKeyElements() {
		final List<T> back = new ArrayList<T>(this.queue.size());
		for (T akt : this.queue) {
			back.add(akt);
		}
		return back;
	}

	/**
	 * The inner class DateComarator is responsible for comparing two dates in
	 * ascending order.
	 * 
	 * @author Daniel Wiese (Daniel.Wiese@siemens.com)
	 * 
	 * @param <O> -
	 *            the type must be extended from DateOrder
	 */
	public class DateComarator<O extends TimeOrderedKey> implements
			Comparator<O> {

		/**
		 * Comperator method to sort dates.
		 * 
		 * @param o1 -
		 *            param 1
		 * @param o2 -
		 *            param 2
		 * @return the coparison result
		 */
		public int compare(O o1, O o2) {
			if ((o1 == null) || (o2 == null)) {
				return -1;
			}
			final long thisValue = o1.getSystemTimeInMillis();
			final long anotherVal = o2.getSystemTimeInMillis();
			return (thisValue < anotherVal ? -1 : (thisValue == anotherVal ? 0
					: 1));
		}

	}
}
