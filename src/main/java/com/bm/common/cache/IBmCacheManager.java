package com.bm.common.cache;

import java.util.Set;

/**
 * Organisiert den cache.
 * 
 * @author Daniel Wiese
 * 
 * @param <K>
 * @param <V>
 */
public interface IBmCacheManager<K, V> {

	/**
	 * Loescht alle elemente aus dem chache.
	 */
	void clear();

	/**
	 * Fuegt ein element in den cache hinzu.
	 * 
	 * @param key -
	 *            der frame key
	 * @param value -
	 *            das frame
	 */
	void put(K key, V value);

	/**
	 * True wenn das element im cache ist.
	 * 
	 * @param key
	 *            true wenn das element im cache ist.
	 * @return true wenn im chache
	 */
	boolean contains(K key);

	/**
	 * Holt ein Element wieder aus dem Cache.
	 * 
	 * @author Daniel Wiese
	 * @since 13.06.2006
	 * @param key -
	 *            der frame key
	 * @return ein element oder null
	 */
	V get(K key);

	/**
	 * Returns the kayset of all keys in the cache. Kays of experied entries are
	 * also returned.
	 * 
	 * @return the list off all keys.
	 */
	Set<K> keySet();

}