package com.bm.classify.sampling.filter.impl;

import com.bm.classify.sampling.filter.IPkIdFilter;

import java.util.Set;

/**
 * Diese Klasse holt die ausgewaehlten indexe heraus.
 *
 * @param <T> -
 *            der typ (untertyp vom sampleable)
 * @author Daniel Wiese
 * @since 18.08.2006
 */
public final class SelectedIdsFilter<T> implements IPkIdFilter<T> {

	private final Set<Long> selectedIds;

	/**
	 * Constructor.
	 *
	 * @param selectedIds -
	 *                    ein set mit den indexen den beispielen, die ausgewahlt werden
	 *                    sollen
	 */
	public SelectedIdsFilter(Set<Long> selectedIds) {
		this.selectedIds = selectedIds;
	}

	/**
	 * Prufet ob ein flat file objekt valide ist.
	 *
	 * @param object -
	 *               des objekt was auf die validitaet geprueft werden soll.
	 * @param index  -
	 *               der aktuell gelesene index, 0 basierend
	 * @return - true wenn valid
	 * @author Daniel Wiese
	 * @see com.bm.classify.sampling.Filter#isValid(java.lang.Object)
	 * @since 18.08.2006
	 */
	public boolean isValid(T object, int index) {
		return true;
	}

	/**
	 * Wenn <code>loadOnlySelsectedIds()</code> false liefert dann wird diese methode
	 * aufgerufem die bestimmt welche id's im Flat file ueberhaubt geladen werden.
	 *
	 * @return die indexe die geladen werden sollen
	 * @author Daniel Wiese
	 * @see com.bm.classify.sampling.filter.IPkIdFilter#getIdsToLoad()
	 * @since 22.08.2006
	 */
	public Set<Long> getIdsToLoad() {
		return this.selectedIds;
	}

	/**
	 * Wenn <code>loadOnlySelsectedIds()</code> false liefert dann wird diese methode
	 * aufgerufem die bestimmt welche id�s im Flat file ueberhaubt geladen werden.
	 *
	 * @return die indexe die geladen werden sollen
	 * @author Daniel Wiese
	 * @see com.bm.classify.sampling.filter.IPkIdFilter#loadOnlySelsectedIds()
	 * @since 22.08.2006
	 */
	public boolean loadOnlySelsectedIds() {
		return true;
	}


}