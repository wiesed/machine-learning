package com.bm.classify.sampling.filter.impl;

import java.util.Set;

import com.bm.classify.sampling.Sampleable;
import com.bm.classify.sampling.filter.IFilter;

/**
 * Diese Klasse holt die ausgewaehlten beispiele heraus.
 * 
 * @author Daniel Wiese
 * @since 18.08.2006
 * @param <T> -
 *            der typ (untertyp vom sampleable)
 */
public final class SelectedFilter<T> implements IFilter<T> {

    private final Set<Integer> selectedTrueExamples;

    private final Set<Integer> selectedFalseExamples;

    private int index = -1;

    private int posCount = -1;

    private int negCount = -1;

    /**
     * Constructor.
     * 
     * @param selectedTrueExamples -
     *            ein set mit den indexen der positiven beispielen, die
     *            ausgewahlt werden sollen
     * @param selectedFalseExamples -
     *            ein set mit den indexen der negativen beispielen, die
     *            ausgewahlt werden sollen
     */
    public SelectedFilter(Set<Integer> selectedTrueExamples, Set<Integer> selectedFalseExamples) {
        this.selectedFalseExamples = selectedFalseExamples;
        this.selectedTrueExamples = selectedTrueExamples;
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
        final Integer selectionTest = new Integer(this.index);
        if (this.selectedFalseExamples.contains(selectionTest)
                || this.selectedTrueExamples.contains(selectionTest)) {
            if (object instanceof Sampleable) {
                final Sampleable akt = (Sampleable) object;

                if (akt.isPositiveExample()) {
                    this.posCount++;
                } else {
                    this.negCount++;
                }
            }
            // das objekt wurde ausgewaehlt
            return true;
        } else {
            return false;
        }
    }

    /**
     * Anzahl der tatsaechlich ausgewahlten negativen beispiele.
     * @return Returns the negCount.
     */
    public int getNegCount() {
        return negCount;
    }

    /**
     * Anzahl der tatsaechlich ausgewahlten positiven beispiele.
     * @return Returns the posCount.
     */
    public int getPosCount() {
        return posCount;
    }
}