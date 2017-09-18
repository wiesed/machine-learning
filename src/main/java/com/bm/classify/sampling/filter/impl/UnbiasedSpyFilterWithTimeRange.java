package com.bm.classify.sampling.filter.impl;

import com.bm.classify.sampling.Sampleable;
import com.bm.classify.sampling.filter.IFilter;

/**
 * Diese Klasse dient zum "ausspionieren" des Flat files um zu bestimmen wo die
 * Positiven/Negativenb beispile sind (index)
 * 
 * Hiebei werden daten aus einem bestimmten Zeitfenster betrachtet. Die
 * negativen/positiven beispile werden nicht aufgepalten das es sich um eine
 * unbiased sample handlet
 * 
 * @param <T> -
 *            der typ
 */
public class UnbiasedSpyFilterWithTimeRange<T> implements IFilter<T> {

    private final int[] validExamplesIndex;

    private int index = -1;

    private int end = -1;

    private final long startTime;

    private final long endTime;

    /**
     * Constructor.
     * 
     * @param totalSize -
     *            die gesammt groesse des datenmenge (alle daten)
     * @param startTime -
     *            die minimal zugelassene start zeit (inclusiv)
     * @param endTime .
     *            die maximal zugelassene end zeit (inclusiv)
     */
    public UnbiasedSpyFilterWithTimeRange(int totalSize, long startTime, long endTime) {
        this.validExamplesIndex = new int[totalSize];
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

                end++;
                validExamplesIndex[end] = index;

            }
        }
        return false;
    }

    /**
     * Die gefundenen validen beispiele sind in einem array z.B.
     * pos-neg[index]=positionImFile der reuchgabe wert gibt des ende das array
     * an (maximal genutzte index).
     * 
     * @return Returns the end.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Das ergebiss array der beispiele, mit folgendem aufbau z.B.
     * pos-neg[index]=positionImFile.
     * 
     * @return Returns the ExamplesIndex.
     */
    public int[] getValidExamplesIndex() {
        return validExamplesIndex;
    }

}