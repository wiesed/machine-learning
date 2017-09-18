package com.bm.classify.sampling.filter;



/**
 * Dieses Inteface bestimmt ob ein Objekt vom FlatFile gelesen wird.
 * @param <T> der type, des objektes was auf die validitaet geprueft werden soll.
 */
public interface IFilter<T> {

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
