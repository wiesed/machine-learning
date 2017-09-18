package com.bm.classify.sampling.filter.impl;

import java.util.List;

import com.bm.classify.sampling.DateHeap;
import com.bm.classify.sampling.Sampleable;
import com.bm.classify.sampling.filter.IFilter;

/**
 * Diese Klasse dient zum "ausspionieren" des Flat files um zu bestimmen wo die
 * Positiven/Negativenb beispiele sind.
 * 
 * Hiebei wird vorher spezifiziert vieviele pos/neg beispiele man haben moechte.
 * Es wird dafuer gesorgt dass stets die neuesten daten genommen werden. Am ende
 * sind N der neusten beispiele bekannt. Weiterhin werden nur Daten in einem
 * bestimmten Zeitraum betrachtet.
 * 
 * @param <T> -
 *            der typ des flat files (untertyp von Sampleable)
 */
public class ActualDataFilter<T extends Sampleable> implements IFilter<T> {

	private final DateHeap<T> trueExamples;

	private final DateHeap<T> falseExamples;

	private final long startTime;

	private final long endTime;

	/**
	 * Constructor.
	 * 
	 * @param posSize -
	 *            anzahl der positiven daten
	 * @param negSize -
	 *            anzahl der negativen daten
	 * @param startTime -
	 *            die untere schranke (inclusiv) des Zeitraums
	 * @param endTime -
	 *            die obere schranke (inclusiv) des Zeitraums
	 */
	public ActualDataFilter(int posSize, int negSize, long startTime,
			long endTime) {
		this.trueExamples = new DateHeap<T>(posSize);
		this.falseExamples = new DateHeap<T>(negSize);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Prueft ob ein flat file objekt valide ist.
	 * 
	 * @param akt -
	 *            des objekt was auf die validitaet geprueft werden soll.
	 * @return - true wenn valid
	 * @param index -
	 *            der aktuell gelesene index, 0 basierend
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 * @see com.bm.classify.sampling.Filter#isValid(java.lang.Object)
	 */
	public boolean isValid(T akt, int index) {

		if (akt.isSelectable() && akt.getSystemTimeInMillis() >= startTime
				&& akt.getSystemTimeInMillis() <= endTime) {
			if (akt.isPositiveExample()) {
				this.trueExamples.add(akt);
			} else {
				this.falseExamples.add(akt);
			}

		}
		return false;
	}

	/**
	 * Liefert die gefundenen negativen beispiele.
	 * 
	 * @return die gefundenen negativen beispiele.
	 */
	public List<T> getFalseExamples() {
		return this.falseExamples.getAllElements();
	}

	/**
	 * Liefert die gefundenen positiven beispiele.
	 * 
	 * @return die gefundenen positiven beispiele.
	 */
	public List<T> getTrueExamples() {
		return this.trueExamples.getAllElements();
	}

}
