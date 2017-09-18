package com.bm.classify.sampling;

/**
 * Alle Objekte die dieses Interface iplementieren koennen gesampled werden.
 * 
 * @author Daniel Wiese
 * @since 18.08.2006
 */
public interface Sampleable extends DateOrder {

    /**
     * True wenn ein ein positives beispiel ist.
     * 
     * @author Daniel Wiese
     * @since 18.08.2006
     * @return True wenn ein ein positives beispiel ist
     */
    boolean isPositiveExample();

    /**
     * True wenn dieses beispiel fuer ein sampling ueberhaubt betrachtet werden
     * darf. Bei fals wird dieses beispiel garantiert ignoriert.
     * 
     * @author Daniel Wiese
     * @since 18.08.2006
     * @return - true , dann wird es evnetuell ausgewaehlt
     */
    boolean isSelectable();

}