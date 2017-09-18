package com.bm.common.util;

import java.util.Arrays;

/**
 * Stellt den Such-Algorithmus fuer Zeit-Array offentlich zur Verfuegung.
 *
 * @author Fabian Bauschulte
 */
public final class BinarySearchInTimeArray {

	private BinarySearchInTimeArray() {

	}

	/**
	 * Uses the binary Search method, if the "toSearch" is contained in the list
	 * the index will be returned otherwise the returned (-(insertion point) -
	 * 1) will be converted: The insertion point is defined as the point at
	 * which the key would be inserted into the list: -the index of the first
	 * element greater than the key, -list.size(), if all elements in the list
	 * are less than the specified key.
	 *
	 * @param array    -
	 *                 the array to search
	 * @param toSeach  -
	 *                 der wert der geschut wird (uhrzeit)
	 * @param isAnfang -
	 *                 ob es der anfang ist
	 * @return - the nearest position
	 */
	public static int findNearestIndexInOrderedTimeLine(long[] array,
														long toSeach, boolean isAnfang) {
		int foundIndex = Arrays.binarySearch(array, toSeach);

		// wenn der index negativ ist , dann wurde es nicht gefunden, die
		// Position gibt den (insertionPoint -1) an
		if (foundIndex < 0) {
			if (isAnfang) {
				// naechstes kleineres oder 0 auswaehlen
				foundIndex = (-foundIndex - 1 < 0) ? 0 : -foundIndex - 1;
			} else {
				// naechstes geoesseres oder array.length-1
				// Fehlerhaft -> ist einen zu lang!
				// foundIndex = (-foundIndex - 1 >= array.length - 1) ?
				// array.length - 1
				// : -foundIndex - 1;
				foundIndex = -foundIndex - 2;
			}
		}

		// Korrektur von doppelten Eintraegen:
		// Es gibt das Problem, dass im falle mehrere gleicher Eintraege
		// nicht
		// sicher ist,
		// welcher gefunden wird. Daher wird definiert, dass immer der der
		// Suche
		// nach Anfang immer das erste,
		// Bei der Suche nach Ende immer das letzte gleiche Element genommen
		// wird.
		// Algorithmus ist O(n) wenn nur gleiche Elemente vorkommen

		if (isAnfang) {
			// Wenn ein kleinere gefunden wird, gibt das letzte gleiche
			// zur�ck
			for (int i = foundIndex - 1; i >= 0; i--) {
				if (array[i] < toSeach) {
					return i + 1;
				}
			}
			// Wenn kein kleinerer gefunden wird, so ist die L�sung 0
			return 0;

		} else {
			// Wenn ein groesseres gefunden wird, gibt das letzte gleiche
			// zur�ck
			for (int i = foundIndex + 1; i < array.length; i++) {
				if (array[i] > toSeach) {
					return i - 1;
				}
			}
			// Wenn kein groeeserer gefunden wird, so ist die L�sung
			// array.length - 1
			return array.length - 1;
		}

	}

	/**
	 * Uses the binary Search method, if the "toSearch" is contained in the list
	 * the index will be returned otherwise the returned (-(insertion point) -
	 * 1) will be converted: The insertion point is defined as the point at
	 * which the key would be inserted into the list: -the index of the first
	 * element greater than the key, -list.size(), if all elements in the list
	 * are less than the specified key.
	 *
	 * @param array    -
	 *                 the array to search
	 * @param toSeach  -
	 *                 der wert der geschut wird (uhrzeit)
	 * @param isAnfang -
	 *                 ob es der anfang ist
	 * @return - the nearest position
	 */
	public static int findNearestIndexInOrderedTimeLine(int[] array,
														int toSeach, boolean isAnfang) {
		int foundIndex = Arrays.binarySearch(array, toSeach);

		// wenn der index negativ ist , dann wurde es nicht gefunden, die
		// Position gibt den (insertionPoint -1) an
		if (foundIndex < 0) {
			if (isAnfang) {
				// naechstes kleineres oder 0 auswaehlen
				foundIndex = (-foundIndex - 1 < 0) ? 0 : -foundIndex - 1;
			} else {
				// naechstes geoesseres oder array.length-1
				// Fehlerhaft -> ist einen zu lang!
				// foundIndex = (-foundIndex - 1 >= array.length - 1) ?
				// array.length - 1
				// : -foundIndex - 1;
				foundIndex = -foundIndex - 2;
			}
		}

		// Korrektur von doppelten Eintraegen:
		// Es gibt das Problem, dass im falle mehrere gleicher Eintr�ge
		// nicht
		// sicher ist,
		// welcher gefunden wird. Daher wird definiert, dass immer der der
		// Suche
		// nach Anfang immer das erste,
		// Bei der Suche nach Ende immer das letzte gleiche Element genommen
		// wird.
		// Algorithmus ist O(n) wenn nur gleiche Elemente vorkommen

		if (isAnfang) {
			// Wenn ein kleinere gefunden wird, gibt das letzte gleiche
			// zur�ck
			for (int i = foundIndex - 1; i >= 0; i--) {
				if (array[i] < toSeach) {
					return i + 1;
				}
			}
			// Wenn kein kleinerer gefunden wird, so ist die L�sung 0
			return 0;

		} else {
			// Wenn ein groesseres gefunden wird, gibt das letzte gleiche
			// zur�ck
			for (int i = foundIndex + 1; i < array.length; i++) {
				if (array[i] > toSeach) {
					return i - 1;
				}
			}
			// Wenn kein groeeserer gefunden wird, so ist die L�sung
			// array.length - 1
			return array.length - 1;
		}

	}

}
