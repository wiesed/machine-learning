package com.bm.train;

import com.bm.common.util.JacksonProvider;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

/**
 * Beschreibung: Normalisert einen SVM Vector.
 * 18.10.16, Time: 09:20.
 *
 * @author wiese.daniel <br>
 * copyright (C) 2016, SWM Services GmbH
 */
public class Normalizer {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Normalizer.class);

	private final Map<FeatureType, FeatureStats> features = new HashMap<FeatureType, FeatureStats>();
	private final Map<FeatureType, FeatureStatsJson> featureAsJsonsSerializable = new HashMap<FeatureType, FeatureStatsJson>();


	public String getStatisticsAsJson() {
		featureAsJsonsSerializable.clear();
		List<FeatureStatsJson> featureStatsJsonList = new ArrayList<FeatureStatsJson>();
		for (FeatureStats featureStats : this.features.values()) {
			final FeatureStatsJson statsJson = featureStats.toJsonType();
			featureStatsJsonList.add(statsJson);
			featureAsJsonsSerializable.put(statsJson.getFeatureType(), statsJson);
		}

		try {
			return JacksonProvider.getMapper()
								  .writeValueAsString(featureStatsJsonList);
		} catch (JsonProcessingException e) {
			log.info("Fehler bei der JSOn Serialisierung", e);
			return "";
		}
	}

	/**
	 * In machine learning, we can handle various types of data, e.g. audio signals and pixel values for image data, and
	 * this data can include multiple dimensions. Feature standardization makes the values of each feature in the data
	 * have zero-mean (when subtracting the mean in the enumerator) and unit-variance. This method is widely used for
	 * normalization in many machine learning algorithms (e.g., support vector machines, logistic regression,
	 * and neural networks)[1][citation needed]. This is typically done by calculating standard scores.[2] The general
	 * method of calculation is to determine the distribution mean and standard deviation for each feature. Next we
	 * subtract the mean from each feature. Then we divide the values (mean is already subtracted) of each feature by
	 * its standard deviation.
	 * <p>
	 * <p>
	 * <p>
	 * <p>
	 * {\displaystyle x'={\frac {x-{\bar {x}}}{\sigma }}}
	 * <p>
	 * x' = \frac{x - \bar{x}}{\sigma}
	 *
	 * @param value
	 * @param featureType
	 * @return
	 */
	public double normalizeValue(double value, FeatureType featureType) {
		final FeatureStatsJson featureStats = this.featureAsJsonsSerializable.get(featureType);

		final double result = (value - featureStats.getMean()) / featureStats.getStandardDeviation();
		return result;

		//return normalizeValue(value, featureStats.getMax(), featureStats.getMin());
	}

	/**
	 * Normaliserungszahl nach L1 berechnen
	 */
	public double calculateL1NormaliseDivident(Map<Integer, Double> myFeatures) {
		// alle nicht 0 Werte durchgehen...
		Iterator<Integer> iter = myFeatures.keySet()
										   .iterator();
		double sum = 0;
		while (iter.hasNext()) {
			Integer pos = iter.next();
			Double tfidfAkt = myFeatures.get(pos);
			sum += tfidfAkt;
		} // end while

		// wurzel ziehen
		return Math.sqrt(sum);

	}

	/**
	 * Normaliserungszahl nach L2 berechnen
	 */
	public double calculateL2NormaliseDivident(Map<Integer, Double> myFeatures) {

		// alle nicht 0 Werte durchgehen...
		Iterator<Integer> iter = myFeatures.keySet()
										   .iterator();
		double sum = 0;
		while (iter.hasNext()) {
			Integer pos = iter.next();
			Double tfidfAkt = myFeatures.get(pos);
			sum += tfidfAkt * tfidfAkt;

		} // end while

		// wurzel ziehen
		return Math.sqrt(sum);

	}

	//zi=(xi−min(x) / max(x)−min(x))
	//private double normalizeValue(double value, double max, double min) {
	//	return (value - min) / (max - min);
	//}

}
