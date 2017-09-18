package com.bm.common.cache;

/**
 * Enthaelt die verscheidenen cache configurationen.
 * 
 * @author wiesed
 * 
 */
public enum CacheCofiguration {

	/**
	 * cache name="defaultCache" maxElementsInMemory="100000" eternal="false"
	 * overflowToDisk="false" timeToIdleSeconds="18000"
	 * timeToLiveSeconds="86400" memoryStoreEvictionPolicy="LRU".
	 */
	DEFAULT_CACHE("defaultCache"),

	/**
	 * In diesem cahce werden die vor kurzem gekauften wkn's gespeichert cache
	 * name="shortTermCache" maxElementsInMemory="100" eternal="false"
	 * timeToIdleSeconds="5" timeToLiveSeconds="5" overflowToDisk="true"
	 * diskPersistent="false" diskExpiryThreadIntervalSeconds="120"
	 * memoryStoreEvictionPolicy="LRU".
	 */
	SHORT_TERM_CACHE("shortTermCache"),

	/**
	 * In diesem cahce werden die aktien listen der geblocken aktien gespeicher
	 * was eine simulation der potenziellen kaufe erleichtert, lebensdauer 24
	 * stunden cache name="blockedStocksHistoryCache" maxElementsInMemory="300"
	 * eternal="false" overflowToDisk="false" timeToIdleSeconds="86000"
	 * timeToLiveSeconds="86400" memoryStoreEvictionPolicy="LRU".
	 */
	BLOCKED_STOCKS_HISTORY_CACHE("blockedStocksHistoryCache");

	private final String configName;

	private CacheCofiguration(String configName) {
		this.configName = configName;
	}

	/**
	 * Retruns the configName.
	 * 
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

}
