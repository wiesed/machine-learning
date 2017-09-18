package com.bm.classify.sampling.filter.impl;

import com.bm.classify.sampling.Sampleable;
import com.bm.classify.sampling.filter.IFilter;

/**
 * Diese Klasse dient zum "ausspionieren" des Flat files um zu bestimmen wo
 * die Positiven/Negativen beispilen sind (index).
 * @param <T> - der typ
 */
public final class SpyFilter<T> implements IFilter<T> {

    private final int[] trueExamplesIndex;

    private final int[] falseExamplesIndex;

    private int endTrue = -1;

    private int endFalse = -1;

    private int index = -1;

    /**
     * Constructor.
     * @param totalSize - die geammtgroesse der datenmenge.
     */
    public SpyFilter(int totalSize) {
        this.trueExamplesIndex = new int[totalSize];
        this.falseExamplesIndex = new int[totalSize];
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
                if (akt.isPositiveExample()) {
                    endTrue++;
                    trueExamplesIndex[endTrue] = index;
                } else {
                    endFalse++;
                    falseExamplesIndex[endFalse] = index;
                }
            }
        }
        return false;
    }

    /**
     * Liefert den end index im neg Ergebniss array.
     * @return Returns the endFalse.
     */
    public int getEndFalse() {
        return endFalse;
    }

    /**
     * Liefert den end index im pos Ergebniss array.
     * @return Returns the endTrue.
     */
    public int getEndTrue() {
        return endTrue;
    }

    /**
     * Liefert ein array mit allen negativen beipielen.
     * @return Returns the falseExamplesIndex.
     */
    public int[] getFalseExamplesIndex() {
        return falseExamplesIndex;
    }

    /**
     * Liefert ein array mit allen positiven beipielen.
     * @return Returns the trueExamplesIndex.
     */
    public int[] getTrueExamplesIndex() {
        return trueExamplesIndex;
    }
    
}