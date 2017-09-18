package com.bm.train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Beschreibung:.
 * 20.10.16, Time: 19:15.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public class FeatureStats {

	private final List<Double> allData = new ArrayList<Double>();
	private final FeatureType featureType;

	private double max = 0;
	private double min = Double.MAX_VALUE;
	private double sum = 0;
	private int totalDataCount = 0;
	private double standardDev = -1;

	public FeatureStats(FeatureType featureType) {
		this.featureType = featureType;
	}

	public static void calcPerFeature(Map<FeatureType, FeatureStats> minMaxPerFeature,
									  FeatureType featureType, double value) {
		if (minMaxPerFeature.containsKey(featureType)) {
			final FeatureStats featureStats = minMaxPerFeature.get(featureType);
			featureStats.adjustValues(value);
		} else {
			final FeatureStats featureStats = new FeatureStats(featureType);
			featureStats.adjustValues(value);
			minMaxPerFeature.put(featureType, featureStats);
		}
	}

	public void adjustValues(double value) {

		//step count
		if (value > max) {
			max = value;
		}
		if (value < min) {
			min = value;
		}

		sum += value;
		totalDataCount++;
		allData.add(value);

	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public double getMean() {
		return sum / totalDataCount;
	}

	public double getStdDev() {
		if (standardDev == -1) {
			this.standardDev = Math.sqrt(getVariance());
		}
		return this.standardDev;
	}

	public FeatureStatsJson toJsonType(){
		final FeatureStatsJson featureStatsJson = new FeatureStatsJson();
		featureStatsJson.setFeatureType(this.featureType);
		featureStatsJson.setMax(this.max);
		featureStatsJson.setMin(this.min);
		featureStatsJson.setSum(this.sum);
		featureStatsJson.setStandardDeviation(this.getStdDev());
		featureStatsJson.setMean(this.getMean());
		featureStatsJson.setVariance(this.getVariance());
		return featureStatsJson;
	}

	private double getVariance() {
		double mean = this.getMean();
		double temp = 0;
		for (double value : allData)
			temp += (value - mean) * (value - mean);
		return temp / totalDataCount;
	}


}
