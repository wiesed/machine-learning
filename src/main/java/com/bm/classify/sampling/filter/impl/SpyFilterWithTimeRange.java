package com.bm.classify.sampling.filter.impl;

import com.bm.classify.sampling.Sampleable;
import com.bm.classify.sampling.filter.IFilter;

/**
 * Diese Klasse dient zum "ausspionieren" des Flat files um zu bestimmen wo die
 * Positiven/Negativenb beispile sind (index).
 * 
 * Hiebei werden daten aus einem bestimmten Zeitfenster betrachtet
 * 
 * @param <T> - der typ
 */
public final class SpyFilterWithTimeRange<T> implements IFilter<T> {

    private final int[] trueExamplesIndex;

    private final int[] falseExamplesIndex;

    private int endTrue = -1;

    private int endFalse = -1;

    private int index = -1;

    private final long startTime;

    private final long endTime;

    /**
     * Constructor.
     * @param totalSize - die gesammt groesse des datenmenge (alle daten)
     * @param startTime - die minimal zugelassene start zeit (inclusiv)
     * @param endTime  . die maximal zugelassene end zeit (inclusiv)
     */
    public SpyFilterWithTimeRange(int totalSize, long startTime, long endTime) {
        this.trueExamplesIndex = new int[totalSize];
        this.falseExamplesIndex = new int[totalSize];
        this.startTime = startTime;
        this.endTime = endTime;
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
            if (akt.isSelectable() && akt.getSystemTimeInMillis() >= startTime
                    && akt.getSystemTimeInMillis() <= endTime) {
                if (akt.isPositiveExample()) {
                    endTrue++;
                    trueExamplesIndex[endTrue] = index;
                } else {
                    endFalse++;
                    falseExamplesIndex[endFalse] = index;
                }
            }
        }
        
        //liefert immer false damit kein objekt gelesen wird
        //was dann speicherplatz einnimmt.
        return false;
    }

    /**
     * Die gefundenen validen negativen beispiele sind in einem array z.B.
     * neg[index]=positionImFile der reuchgabe wert gibt des ende das array an
     * (maximal genutzte index).
     * 
     * @return Returns the endFalse.
     */
    public int getEndFalse() {
        return endFalse;
    }

    /**
     * Die gefundenen validen positiven beispiele sind in einem array z.B.
     * pos[index]=positionImFile der reuchgabe wert gibt des ende das array an
     * (maximal genutzte index).
     * 
     * @return Returns the endTrue.
     */
    public int getEndTrue() {
        return endTrue;
    }

    /**
     * Das ergebiss array der negativen beispiele, mit folgendem aufbau z.B.
     * neg[index]=positionImFile.
     * @return Returns the falseExamplesIndex.
     */
    public int[] getFalseExamplesIndex() {
        return falseExamplesIndex;
    }

    /**
     * Das ergebiss array der positiven beispiele, mit folgendem aufbau z.B.
     * pos[index]=positionImFile
     * @return Returns the trueExamplesIndex.
     */
    public int[] getTrueExamplesIndex() {
        return trueExamplesIndex;
    }
    
}