package com.bm.classify.core.helper;

/**
 * Beschreibung:.
 * 26.10.16, Time: 15:05.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public class LinearHyperpaneJson {

	private int[] nonZeroPositions;

	private double[] weigth;

	private double bias;

	private int anzahlNonZeroPositions;

	private double userBias;

	public LinearHyperpaneJson() {
	}

	public LinearHyperpaneJson(int[] nonZeroPositions, double[] weigth, double bias, int anzahlNonZeroPositions, double userBias) {
		this.nonZeroPositions = nonZeroPositions;
		this.weigth = weigth;
		this.bias = bias;
		this.anzahlNonZeroPositions = anzahlNonZeroPositions;
		this.userBias = userBias;
	}

	public int[] getNonZeroPositions() {
		return nonZeroPositions;
	}

	public double[] getWeigth() {
		return weigth;
	}

	public double getBias() {
		return bias;
	}

	public int getAnzahlNonZeroPositions() {
		return anzahlNonZeroPositions;
	}

	public double getUserBias() {
		return userBias;
	}

	public void setNonZeroPositions(int[] nonZeroPositions) {
		this.nonZeroPositions = nonZeroPositions;
	}

	public void setWeigth(double[] weigth) {
		this.weigth = weigth;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public void setAnzahlNonZeroPositions(int anzahlNonZeroPositions) {
		this.anzahlNonZeroPositions = anzahlNonZeroPositions;
	}

	public void setUserBias(double userBias) {
		this.userBias = userBias;
	}
}
