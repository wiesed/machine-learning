package com.bm.classify.core.helper;


import com.bm.classify.exception.SVMModelNotValidException;
import com.bm.common.util.JacksonProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.*;
import java.util.*;

/**
 * LinearHyperplane erlaubt eine klassifikation direkt im speicher (falls des ergebniss linear ist).
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public final class LinearHyperplane implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int[] nonZeroPositions;

	private final double[] weigth;

	private final double bias;

	private final int anzahlNonZeroPositions;

	private double userBias = 0;

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Die Hyperebene wird aus einem Mapping Postion->Gewicht erzeugt.
	 *
	 * @param _weights - gewichte
	 * @param _bias    - der bias (veschiebung der hyperebene)
	 */
	public LinearHyperplane(TreeMap<Integer, Double> _weights, double _bias) {
		final Set<Integer> positions = _weights.keySet();

		this.anzahlNonZeroPositions = positions.size();
		this.weigth = new double[anzahlNonZeroPositions];
		this.nonZeroPositions = new int[anzahlNonZeroPositions];

		Iterator<Integer> iter = _weights.keySet().iterator();

		int count = 0;

		while (iter.hasNext()) {
			Integer pos = iter.next();
			this.nonZeroPositions[count] = pos.intValue();
			this.weigth[count] = _weights.get(pos).doubleValue();
			count++;
		}

		bias = _bias;
	}

	/**
	 * Die Hyperebene wird aus eine im classapth iegenden JSON datei erzeugt.
	 *
	 * @param serializedJSONPane der name der json datei (muss im classpath sein)
	 */
	public LinearHyperplane(String serializedJSONPane) {
		try {
			final String hyperpaneAsJson =
					Resources.toString(Resources.getResource("hyperplane/" + serializedJSONPane), Charsets.UTF_8);
			final LinearHyperpaneJson linearHyperpaneJson = JacksonProvider.getMapper().readValue(hyperpaneAsJson,
					LinearHyperpaneJson.class);
			this.nonZeroPositions = linearHyperpaneJson.getNonZeroPositions();
			this.weigth = linearHyperpaneJson.getWeigth();
			this.bias = linearHyperpaneJson.getBias();
			this.userBias = linearHyperpaneJson.getUserBias();
			this.anzahlNonZeroPositions = linearHyperpaneJson.getAnzahlNonZeroPositions();

		} catch (IOException e) {
			throw new RuntimeException("fehlr bei der JSON de-serialiserung");
		}

	}


	/**
	 * Die hyperebene wird aus einer SVM Datei erzeugt.
	 *
	 * @param model - das model (festplatte)
	 * @throws SVMModelNotValidException - im fehlerfall
	 */
	public LinearHyperplane(File model) throws SVMModelNotValidException {
		TreeMap<Integer, Double> weights = new TreeMap<Integer, Double>();

		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(model));
			String line = null;
			boolean svRead = false;

			double tempBias = 0;
			while ((line = lnr.readLine()) != null) {
				line = line.trim();

				if (!svRead) {
					if (line.indexOf("threshold b, each following line is a SV") > -1) {
						StringTokenizer tok = new StringTokenizer(line, " #");

						tempBias = Double.parseDouble(tok.nextToken());

						svRead = true;
					}
				} else { // end if
					this.addLineToWeights(weights, line);
				}
			}
			// end while

			lnr.close();
			// Bias anlegen
			this.bias = tempBias;

			// Vektor erzeugen
			final Set<Integer> positions = weights.keySet();

			this.anzahlNonZeroPositions = positions.size();
			this.weigth = new double[anzahlNonZeroPositions];
			this.nonZeroPositions = new int[anzahlNonZeroPositions];

			Iterator<Integer> iter = weights.keySet().iterator();

			int count = 0;
			while (iter.hasNext()) {
				Integer pos = iter.next();
				this.nonZeroPositions[count] = pos.intValue();
				this.weigth[count] = (weights.get(pos)).doubleValue();

				count++;
			}
		} catch (FileNotFoundException fe) {
			throw new SVMModelNotValidException("Das Model wurde nicht an '" + model.toString()
					+ "' gefunden. " + fe.getMessage());
		} catch (IOException ioe) {
			throw new SVMModelNotValidException("Unerwartete IOException: " + ioe.getMessage());
		}
	}


	/**
	 * Gibt den bias aus.
	 *
	 * @return - der bias
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public double getBias() {
		return bias;
	}

	/**
	 * Berechnet den vorhergesagten Wert - ist sehr schnell, wenn spaerlich
	 * besetzt.
	 *
	 * @param vektor - der vektor
	 * @return - die vorhersage
	 */
	public double getPreditedValue(final TreeMap<Integer, Double> vektor) {
		double back = 0;

		Iterator<Integer> iter = vektor.keySet().iterator();

		while (iter.hasNext()) {
			Integer key = iter.next();

			// Suchen ist es im Vektor
			final double weight = this.getWeigth(key.intValue());
			final Double vecorValue = vektor.get(key);

			if (weight != 0) {
				back += (weight * vecorValue);
			}
		}

		back -= (bias + userBias);

		return back;
	}

	/**
	 * Liefert das Gewicht zu einer einzelnen Position.
	 *
	 * @param pos - die position
	 * @return - des gewicht
	 */
	public double getWeigth(int pos) {
		// Ist es ein nicht-0 Wert
		int posIntern = Arrays.binarySearch(nonZeroPositions, pos);

		if ((posIntern >= 0) && (posIntern < this.anzahlNonZeroPositions)) {
			return weigth[posIntern];
		} else {
			return 0;
		}
	}

	public void setUserBias(double userBias) {
		this.userBias = userBias;
	}

	public double getUserBias() {
		return this.userBias;
	}


	/**
	 * Druckt die ersten 500 Position der Hyperebene.
	 *
	 * @return - gibt die hyperebene aus
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("{b=").append(this.bias);

		for (int count = 0; (count < weigth.length) && (count < 500); count++) {
			buf.append(" ").append(nonZeroPositions[count]).append(":").append(weigth[count]);

			count++;
		}

		buf.append("...}");

		return buf.toString();
	}

	public String asJson() {
		try {
			final LinearHyperpaneJson linearHyperpaneJson =
					new LinearHyperpaneJson(this.nonZeroPositions, this.weigth, this.bias, this.anzahlNonZeroPositions, userBias);
			return JacksonProvider.getMapper().writeValueAsString(linearHyperpaneJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("fehlr bei der JOSn serialiserung");
		}
	}


	/**
	 * Wird bei der Erzeugung aus der SVM-Datei benutzt.
	 *
	 * @param weigthMap - die gewichte
	 * @param line      - die zeile
	 * @throws SVMModelNotValidException
	 */
	private void addLineToWeights(TreeMap<Integer, Double> weigthMap, String line) throws SVMModelNotValidException {
		StringTokenizer tok = new StringTokenizer(line, " ");

		double alphaYi = Double.parseDouble(tok.nextToken());

		while (tok.hasMoreTokens()) {
			String featureValue = tok.nextToken();
			//bei der neuen SVM ist als letztes Zeichen #
			if (!featureValue.trim().equals("#")) {
				if ((featureValue.indexOf(":") <= 0)
						|| ((featureValue.indexOf(":") + 1) >= featureValue.length())) {
					throw new SVMModelNotValidException("Ein FeatuerValuePaar war nicht valide.");
				} else {
					this.addValueToWeights(weigthMap, alphaYi, Integer.valueOf(featureValue
							.substring(0, featureValue.indexOf(":"))), Double
							.parseDouble(featureValue.substring(featureValue.indexOf(":") + 1,
									featureValue.length())));
				}
			}
		}
	}

	/**
	 * Wird bei der Erzeugung aus der SVM-Datei benutzt-
	 *
	 * @param weigthMap - gewichts map
	 * @param alphaYi   - alpha yi
	 * @param pos       - position
	 * @param value     - wert
	 */
	private void addValueToWeights(TreeMap<Integer, Double> weigthMap, double alphaYi, Integer pos, double value) {
		double weightTemp = alphaYi * value;

		if (weightTemp != 0) {
			if (!weigthMap.containsKey(pos)) {
				weigthMap.put(pos, new Double(0));
			}
			// end if

			Double old = weigthMap.get(pos);

			weigthMap.put(pos, new Double(old.doubleValue() + weightTemp));
		}
		// end if
	}

}
