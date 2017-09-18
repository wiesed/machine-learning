package com.bm.classify.sampling.filter.impl;

import java.util.HashSet;
import java.util.Set;

import com.bm.classify.sampling.filter.IIndexFilter;

/**
 * Der paging filter sorgt dafuer dass ein flatfile in mehreren Pages komplett
 * gelesen werden kann.
 * 
 * @author Daniel Wiese
 * @since 07.09.2006
 * @param <T> -
 *            der flatfile typ der gelesen werden soll
 */
public class PagingFilter<T> implements IIndexFilter<T> {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(PagingFilter.class);

	private final int dataCount;

	private final int pagingSize;

	private int currentPage = 1;

	private final Set<Integer> indexesToLoad = new HashSet<Integer>();

	/**
	 * Constructor.
	 * 
	 * @param dataCount -
	 *            all data count (groesse des flat files)
	 * @param pagingSize -
	 *            wie veiele lines / per page gelesen werden sollen
	 */
	public PagingFilter(int dataCount, int pagingSize) {
		log.debug("Data count: " + dataCount + " Paging size (" + pagingSize
				+ ")");
		this.dataCount = dataCount;
		this.pagingSize = pagingSize;
		this.prepareIndexesToLoad();
	}

	/**
	 * Bereitet den filer vor eine neue Seite zu laden.
	 * 
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 */
	public void nextPage() {
		currentPage++;
		this.prepareIndexesToLoad();
	}

	/**
	 * Liefert true wenn es noch indexe gibt die noch geladen werden sollen.
	 * 
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 * @return true wenn es noch indexe gibt die noch geladen werden sollen
	 */
	public boolean hasNextPage() {
		return (currentPage * pagingSize) < dataCount;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 * @see com.bm.classify.sampling.filter.IIndexFilter#getIndexesToLoad()
	 */
	public Set<Integer> getIndexesToLoad() {
		return this.indexesToLoad;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 * @see com.bm.classify.sampling.filter.IIndexFilter#loadOnlySelsectedIndexes()
	 */
	public boolean loadOnlySelsectedIndexes() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 * @see com.bm.classify.sampling.filter.IFilter#isValid(java.lang.Object,
	 *      int)
	 */
	public boolean isValid(T object, int index) {
		return this.indexesToLoad.contains(Integer.valueOf(index));
	}

	private void prepareIndexesToLoad() {
		this.indexesToLoad.clear();
		int start = (currentPage * pagingSize) - pagingSize;
		int ende = (currentPage * pagingSize);
		log.debug("Preparing to load indexes; Start: (" + start + ") , End: ("
				+ ende + ")");
		for (int i = start; i < ende; i++) {
			this.indexesToLoad.add(Integer.valueOf(i));
		}
	}

}
