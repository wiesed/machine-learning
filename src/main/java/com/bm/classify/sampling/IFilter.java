package com.bm.classify.sampling;

import java.util.Set;


/**
 * Dieses Inteface bestimmt ob ein Objekt vom FlatFile gelesen wird.
 * @param <T> der type, des objektes was auf die validitaet geprueft werden soll.
 */
public interface IFilter<T> {
    
		/**
		 * Wird hier true zuruckgeliefert, werden nur bestimmte indexe geladen.
		 * @return - true
		 */
        boolean loadOnlySelsectedIndexes();
	
		/**
         * Wenn <code>loadOnlySelsectedIndexes()</code> false liefert dann wird diese methode
         * aufgerufem die bestimmt welche indexe (0 basirend) im Flat file ueberhaubt geladen werden. 
         * @author Daniel Wiese
         * @since 22.08.2006
         * @return die indexe die geladen werden sollen
         */
        Set<Integer> getIndexesToLoad();

        /**
         * Prufet ob ein flat file objekt valide ist. Es wird der aktuell gelesene index, 0 basierend
         * uebergeben.
         * @author Daniel Wiese
         * @since 14.08.2006
         * @param object - des objekt was auf die validitaet geprueft werden soll.
         * @param index -  der aktuell gelesene index, 0 basierend
         * @return - true wenn valid
         */
        boolean isValid(T object, int index);
 }
