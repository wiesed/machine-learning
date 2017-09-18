package com.bm.classify.sampling.filter.impl;

import com.bm.classify.sampling.Sampleable;
import com.bm.classify.sampling.filter.IFilter;

/**
 * Diese Klasse dient zum "ausspionieren" des Flat files um zu bestimmen wo
 * die validen beispiele sind (index).
 * @param <T> - der typ
 */
public final class UnbiasedSpyFilter<T> implements IFilter<T> {

    private final int[] examplesIndex;

    private int index = -1;
    private int end = -1;

    /**
     * Constructor.
     * @param totalSize - die geammtgroesse der datenmenge.
     */
    public UnbiasedSpyFilter(int totalSize) {
        this.examplesIndex = new int[totalSize];
    }

    /**
     * Prufet ob ein flat file objekt valide ist.
     * 
     * @param object -
     *            des objekt was auf die validitaet geprueft werden soll.
     * @return - true wenn valid
     * @param index -
     *            der aktuell gelesene index, 0 basierend
     * @author Daniel Wiese
     * @since 18.08.2006
     * @see com.bm.classify.sampling.Filter#isValid(java.lang.Object)
     */
    public boolean isValid(T object, int index) {
        this.index++;
        if (object instanceof Sampleable) {
            final Sampleable akt = (Sampleable) object;
            if (akt.isSelectable()) {
                    end++;
                    examplesIndex[end] = index;
            }
        }
        return false;
    }

    /**
     * Liefert den end index im neg Ergebniss array.
     * @return Returns the endFalse.
     */
    public int getEnd() {
        return end;
    }


    /**
     * Liefert ein array mit allen negativen beipielen.
     * @return Returns the falseExamplesIndex.
     */
    public int[] getExamplesIndex() {
        return examplesIndex;
    }

}