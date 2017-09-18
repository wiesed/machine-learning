package com.bm.classify.sampling.filter.impl;

import java.util.Set;

import com.bm.classify.sampling.filter.IIndexFilter;

/**
 * Diese Klasse holt die ausgewaehlten indexe heraus.
 * 
 * @author Daniel Wiese
 * @since 18.08.2006
 * @param <T> -
 *            der typ (untertyp vom sampleable)
 */
public final class SelectedIndexFilter<T> implements IIndexFilter<T> {

    private final Set<Integer> selected;

    /**
     * Constructor.
     * 
     * @param selected -
     *            ein set mit den indexen den beispielen, die ausgewahlt werden
     *            sollen
     */
    public SelectedIndexFilter(Set<Integer> selected) {
        this.selected = selected;
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
        boolean back = false;
        final Integer selectionTest = Integer.valueOf(index);
        if (this.selected.contains(selectionTest)) {
            back = true;
        }

        return back;
    }

    /**
	 * Wenn <code>loadOnlySelsectedIndexes()</code> false liefert dann wird
	 * diese methode aufgerufem die bestimmt welche indexe (0 basirend) im Flat
	 * file ueberhaubt geladen werden.
	 * 
	 * @author Daniel Wiese
	 * @since 22.08.2006
	 * @return die indexe die geladen werden sollen
	 * 
	 * @see com.bm.classify.sampling.Filter#getIndexesToLoad()
	 */
	public Set<Integer> getIndexesToLoad() {
		return this.selected;
	}

	/**
	 * Wird hier true zuruckgeliefert, werden nur bestimmte indexe geladen.
	 * @return - true
	 * @see com.bm.classify.sampling.Filter#loadOnlySelsectedIndexes()
	 */
	public boolean loadOnlySelsectedIndexes() {
		return true;
	}
}