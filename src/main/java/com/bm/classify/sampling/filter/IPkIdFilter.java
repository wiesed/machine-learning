package com.bm.classify.sampling.filter;

import java.util.Set;

/**
 * Dieses Inteface bestimmt ob ein Objekt vom FlatFile gelesen wird. Bei disem filtertypen werden
 * nur bestimmte id (primary keys) vom Flat file gelesen.
 *
 * @param <T> der type, des objektes was auf die validitaet geprueft werden soll.
 * @author Daniel Wiese
 * @since 27.08.2006
 */
public interface IPkIdFilter<T> extends IFilter<T> {

	/**
	 * Wird hier true zuruckgeliefert, werden nur bestimmte indexe geladen.
	 *
	 * @return - true
	 */
	boolean loadOnlySelsectedIds();

	/**
	 * Wenn <code>loadOnlySelsectedIds()</code> false liefert dann wird diese methode
	 * aufgerufem die bestimmt welche id's im Flat file ueberhaubt geladen werden.
	 *
	 * @return die indexe die geladen werden sollen
	 * @author Daniel Wiese
	 * @since 22.08.2006
	 */
	Set<Long> getIdsToLoad();

}
