package com.bm.common.util;

import java.util.List;

/**
 * Mathematische funktionen.
 * 
 * @author Daniel Wiese
 * 
 */
public final class BmMath {

	private BmMath() {
	}

	/**
	 * Berechnet die Standardabweichung eines double Arrays.
	 * 
	 * @param doubleList
	 *            - eine Liste mit double Werten
	 * @param <T>
	 *            typ der Liste
	 * @return Standardabweichung - diestandardabweichung
	 */
	public static <T extends Number> double calculateStandardabweichung(
			final List<T> doubleList) {
		double mittelwert = 0;
		for (final T aktWert : doubleList) {
			mittelwert += aktWert.doubleValue();
		}

		mittelwert = mittelwert / doubleList.size();
		return calculateStandardabweichung(mittelwert, doubleList);
	}

	/**
	 * Berechnet die Standardabweichung eines double Arrays.
	 * 
	 * @param doubleList
	 *            - eine Liste mit double Werten
	 * @param mittelwert
	 *            der mittelwert
	 * @param <T>
	 *            typ der Liste
	 * @return Standardabweichung - diestandardabweichung
	 */
	public static <T extends Number> double calculateStandardabweichung(
			final double mittelwert, final List<T> doubleList) {

		// Standardabweichung vom Mittelwert berechnen
		double deviation = 0.0;

		for (final T aktWert : doubleList) {
			deviation += Math.pow(aktWert.doubleValue() - mittelwert, 2);
		}
		// FEHLER: SIZE-1 !!!
		deviation = (deviation / (doubleList.size()));
		return Math.sqrt(deviation);
	}

	/**
	 * Berechnet die Standardabweichung eines double Arrays.
	 * 
	 * @param doubleList
	 *            - eine Liste mit double Werten
	 * @param anzElemente
	 *            die anzahl der elemente
	 * @param <T>
	 *            typ der Liste
	 * @return Standardabweichung - diestandardabweichung
	 */
	public static <T extends Number> double calculateStandardabweichungVonNull(
			final int anzElemente, final List<T> doubleList) {
		if (doubleList.isEmpty()) {
			return 0d;
		}
		// Standardabweichung vom Mittelwert berechnen
		double deviation = 0.0;

		for (final T aktWert : doubleList) {
			deviation += Math.pow(aktWert.doubleValue(), 2);
		}

		deviation = (deviation / (anzElemente));
		return Math.sqrt(deviation);
	}

	/**
	 * Berechnet die prozentuale abweichung der beiden werte vonenander in
	 * prozent.
	 * 
	 * @param wert1
	 *            der este wert
	 * @param wert2
	 *            der zeite wert
	 * @return die prozentuale diferenz
	 */
	public static double claculateProzAbweichung(double wert1, double wert2) {

		final double wert1_korrigiert = (wert1 < 0 && wert2 < 0) ? -wert1
				: wert1;
		final double wert2_korrigiert = (wert1 < 0 && wert2 < 0) ? -wert2
				: wert2;
		final double diff = Math.max(wert1_korrigiert, wert2_korrigiert)
				- Math.min(wert1_korrigiert, wert2_korrigiert);

		double abwInProz = 0;
		if (diff != 0) {
			double divisor = Math.max(wert1_korrigiert, wert2_korrigiert);
			if (divisor != 0) {
				abwInProz = diff / divisor;
			} else {
				abwInProz = diff;
			}
		}

		return abwInProz;

	}

	/**
	 * Berechnet den durchschnitt aus einem array.
	 * 
	 * @param array
	 *            das array mit den werten
	 * @return der durchschnitt
	 */
	public static double calculateAverage(int[] array) {
		double sum = 0;
		for (int i : array) {
			sum += i;
		}
		return (array.length != 0) ? (sum / array.length) : 0;

	}

	/**
	 * Berechnet den durchschnitt aus einem array.
	 * 
	 * @param list
	 *            das array mit den werten
	 * @return der durchschnitt
	 */
	public static double calculateAverage(List<Double> list) {
		double sum = 0;
		for (double i : list) {
			sum += i;
		}
		return (list.size() != 0) ? (sum / list.size()) : 0;
	}

	/**
	 * Berechnet den durchschnitt aus einem array.
	 * 
	 * @param array
	 *            das array mit den werten
	 * @return der durchschnitt
	 */
	public static double calculateAverage(double[] array) {
		double sum = 0;
		for (double i : array) {
			sum += i;
		}
		return (array.length != 0) ? (sum / array.length) : 0;

	}

	/**
	 * Berechnet den prozentwert (0..1).
	 * 
	 * @param zuBerechnen
	 *            der aktueller wert
	 * @param total
	 *            der toale wert (100%)
	 * @return der prozentwert wischen 0..1
	 */
	public static double calulateProz(int zuBerechnen, int total) {
		return (total != 0) ? (double) zuBerechnen / (double) total : 0d;
	}

}
