package com.bm.common.cache;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Wrapper um den eh-cache.
 * 
 * @author Daniel Wiese
 * 
 * @param <K>
 *            der key typ
 * @param <V>
 *            der value typ
 */
public class BmCacheManager<K, V> implements IBmCacheManager<K, V> {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(BmCacheManager.class);

	// Create a CacheManager using defaults
	private static final CacheManager manager;

	private final Cache myCache;

	static {
		// vm singelton
		manager = CacheManager.create();
	}

	/**
	 * Constructor.
	 * 
	 * @param config
	 *            die cahce configuration enum die sich auf ehcache.xml bezieht.
	 */
	public BmCacheManager(CacheCofiguration config) {
		try {
			myCache = manager.getCache(config.getConfigName());
		} catch (CacheException e) {
			log.error("Can�t initialize cache", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.common.cache.IBmCacheManager#clear()
	 */
	public void clear() {
		try {
			myCache.removeAll();
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.common.cache.IBmCacheManager#put(K, V)
	 */
	public void put(K key, V value) {
		Element element = new Element(key, value);
		myCache.put(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.common.cache.IBmCacheManager#contains(K)
	 */
	public boolean contains(K key) {
		return myCache.get(key) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.common.cache.IBmCacheManager#get(K)
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		V back = null;
		try {
			final Element element = myCache.get(key);
			if (element != null) {
				back = (V) element.getValue();
			}
		} catch (IllegalStateException e) {
			log.error("The cache is in an illegal state");
		} catch (CacheException e) {
			log.error("The cache is in an illegal state");
		}

		return back;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.common.cache.IBmCacheManager#keySet()
	 */
	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		final Set<K> keys = new HashSet<K>();
		keys.addAll(myCache.getKeys());
		return keys;
	}

}
