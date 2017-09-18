package com.bm.classify.core;


/**
 * Interface um eine menge von trainierten modellen zu representieren.
 * Konkrete Implementierungen ist das SVMModelBag
 * @param <T> - der typ der konkreten modelle
 */
public interface ModelBag<T> {
    
    /**
     * True wenn leer - kein model.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @return - true wenn leer.
     */
    boolean isEmpty();
    
    /**
     * Liefert die groesse des bags.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @return - die groesse
     */
    int size();
    
    /**
     * Liefert das model an der entsprechenden position.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @param i - die position
     * @return - das model vom typ T
     */
    T getModel(int i);
    
    /**
     * Hilfsmethode falls nur ein model existiert.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @return - des erste model
     */
    T getFirstModel();

}
