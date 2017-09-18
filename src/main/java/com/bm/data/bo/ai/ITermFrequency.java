package com.bm.data.bo.ai;

/**
 * Interface fuer termhaufigkeiten. Kann sich auch sortieren um eine
 * eindeutige Reihenfolge zu gewaehrleisten.
 * 
 * @author Daniel Wiese
 * @since 05.08.2006
 */
public interface ITermFrequency extends Comparable<ITermFrequency> {

    /**
     * Returns the haufigkeit.
     * 
     * @return Returns the haufigkeit.
     */
    int getHaufigkeit();

    /**
     * Returns the term.
     * 
     * @return Returns the term.
     */
    String getTerm();

}
