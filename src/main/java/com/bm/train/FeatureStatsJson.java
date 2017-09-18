package com.bm.train;

/**
 * Beschreibung: Liefert die statitstik Daten als Json-Serialisierung.
 * 24.10.16, Time: 13:52.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public class FeatureStatsJson {

	private FeatureType featureType;
	private double max = 0;
	private double min = 0;
	private double sum = 0;
	private double standardDeviation = 0;
	private double mean = 0;
	private double variance = 0;

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}
}
