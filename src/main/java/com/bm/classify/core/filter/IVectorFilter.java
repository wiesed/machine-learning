package com.bm.classify.core.filter;

import java.util.List;

import com.bm.classify.core.ISVMVector;

/**
 * Filtert eine menge von vectoren so, dass nur die positiven/negativen vectoren uebrig bleiben.
 * @author Daniel Wiese
 * @param <T> - der typ der gefiltert werden soll.
 * @since 27.08.2006
 */
public interface IVectorFilter<T extends ISVMVector> {
    
    /**
     * Liefert die gefilterte vectoren liste.
     * @author Daniel Wiese
     * @since 27.08.2006
     * @param all - all vectoren
     * @return - die gefilterten vectoren.
     */
    List<T> getFilteredVectors(List<T> all);
    
    /**
     * After the <code>getFilteredVectors></code> was executed this method contains all
     * unused vectors.
     * @return - the unused (filtered out vectors)
     */
    List<T> getFilteredOutVectors();

}
