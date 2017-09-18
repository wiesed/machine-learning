package com.bm.classify.sampling.filter;

import java.util.Set;

/**
 * Dieses Inteface bestimmt ob ein Objekt vom FlatFile gelesen wird. Bei disem fltertypen werden
 * nur bestimmte indices vom Flat file gelesen.
 * @param <T> der type, des objektes was auf die validitaet geprueft werden soll.
 * @author Daniel Wiese
 * @since 27.08.2006
 */
public interface IIndexFilter<T> extends IFilter<T> {
    
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

}
