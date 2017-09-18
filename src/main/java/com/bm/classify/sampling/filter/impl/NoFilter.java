package com.bm.classify.sampling.filter.impl;

import com.bm.classify.sampling.filter.IFilter;

/**
 * Ein filter der alles akzeptiert.
 * @param <T> - der typ
 * @author Daniel Wiese
 * @since 15.08.2006
 */
public class NoFilter<T> implements IFilter<T> {

    /**
     * Prufet ob ein flat file objekt valide ist.
     * 
     * @author Daniel Wiese
     * @since 14.08.2006
     * @param object -
     *            des objekt was auf die validitaet geprueft werden soll.
     * @return - true wenn valid
     * @see com.bm.classify.sampling.Filter#isValid(java.lang.Object)
     */
    public boolean isValid(T object) {
        return true;
    }
    
    /**
     * Prueft ob ein flat file objekt valide ist.
     * 
     * @param akt -
     *            des objekt was auf die validitaet geprueft werden soll.
     * @return - true wenn valid
     * @param index -
     *            der aktuell gelesene index, 0 basierend
     * @author Daniel Wiese
     * @since 18.08.2006
     * @see com.bm.classify.sampling.Filter#isValid(java.lang.Object)
     */
    public boolean isValid(T akt, int index) {
        return true;
    }
}