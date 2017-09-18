package com.bm.classify.core.result;

import java.io.Serializable;

/**
 * Enthaelt das zusammengefasste ergebniss einer Klassifikation.
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class ClassifyResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int fn;
	private final int fp;
	private final int tn;
	private final int tp;
	private double accuracy;
	private double errorRate;
	private double f1;
	private double fallout;
	private double genPrecision;
	private double generality;
	private double precision;
	private double recall;

	/**
	 * Constructor.
	 *
	 * @param otherResult ein anderer Result
	 */
	public ClassifyResult(ClassifyResult otherResult) {

		this.tp = otherResult.tp;

		this.tn = otherResult.tn;

		this.fp = otherResult.fp;

		this.fn = otherResult.fn;

		this.generateAllMeasures();
	}

	/**
	 * Constructor.
	 *
	 * @param tp -
	 *           true positive
	 * @param fp -
	 *           false positive
	 * @param tn -
	 *           true negative
	 * @param fn -
	 *           false negative
	 */
	public ClassifyResult(int tp, int fp, int tn, int fn) {
		this.tp = tp;

		this.tn = tn;

		this.fp = fp;

		this.fn = fn;

		this.generateAllMeasures();
	}

	/**
	 * Constructor.
	 *
	 * @param otherResult ein anderer Result
	 */
	public ClassifyResult add(ClassifyResult otherResult) {

		final int newTp = this.tp + otherResult.tp;
		final int newTn = this.tn + otherResult.tn;
		final int newFp = this.fp + otherResult.fp;
		final int newFn = this.fn + otherResult.fn;

		return new ClassifyResult(newTp, newFp, newTn, newFn);
	}

	/**
	 * Returns the accuracy.
	 *
	 * @return Returns the accuracy.
	 */
	public double getAccuracy() {
		return this.accuracy;
	}

	/**
	 * Returns the errorRate.
	 *
	 * @return Returns the errorRate.
	 */
	public double getErrorRate() {
		return this.errorRate;
	}

	/**
	 * Returns the f1.
	 *
	 * @return Returns the f1.
	 */
	public double getF1() {
		return this.f1;
	}

	/**
	 * Returns the fallout.
	 *
	 * @return Returns the fallout.
	 */
	public double getFallout() {
		return this.fallout;
	}

	/**
	 * Returns the fn. Echter label war positiv und die svm vorhersage war falsch (nagative).
	 *
	 * @return Returns the fn.
	 */
	public int getFn() {
		return this.fn;
	}

	/**
	 * Returns the fp. Echter label war nagativ und die svm vorhersage war falsch (positiv).
	 * Das ist beim handeln der Worst case.
	 *
	 * @return Returns the fp.
	 */
	public int getFp() {
		return this.fp;
	}

	/**
	 * Returns the generality.
	 *
	 * @return Returns the generality.
	 */
	public double getGenerality() {
		return this.generality;
	}

	/**
	 * Returns the genPrecision.
	 *
	 * @return Returns the genPrecision.
	 */
	public double getGenPrecision() {
		return this.genPrecision;
	}

	/**
	 * Returns the precision.
	 *
	 * @return Returns the precision.
	 */
	public double getPrecision() {
		return this.precision;
	}

	/**
	 * Returns the recall.
	 *
	 * @return Returns the recall.
	 */
	public double getRecall() {
		return this.recall;
	}

	/**
	 * Returns the tn. SVM und echter Label waren OK (negativ)
	 *
	 * @return Returns the tn.
	 */
	public int getTn() {
		return this.tn;
	}

	/**
	 * Returns the tp. SVM und echter Label waren OK (posotiv).
	 *
	 * @return Returns the tp.
	 */
	public int getTp() {
		return this.tp;
	}

	/**
	 * Berechnet alle masse.
	 *
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void generateAllMeasures() {
		this.generateAccuracy();

		this.generateErrorRate();

		this.generatePrecision();

		this.generateRecall();

		this.generateF1();

		this.generateFallout();

		this.generateGenerality();

		this.generateGeneralityPrecision();
	}

	/**
	 * Generiert die to string methode.
	 *
	 * @return - die to string methode
	 * @author Daniel Wiese
	 * @see java.lang.Object#toString()
	 * @since 02.06.2006
	 */
	public String toString() {
		StringBuffer erg = new StringBuffer();

		erg.append("ErrorRate: ").append(this.getErrorRate()).append("\n");

		erg.append("Accuracy: ").append(this.getAccuracy()).append("\n");

		erg.append("Recall: ").append(this.getRecall()).append("\n");

		erg.append("Precision: ").append(this.getPrecision()).append("\n");

		erg.append("F1: ").append(this.getF1()).append("\n");

		erg.append("Fallout: ").append(this.getFallout()).append("\n");

		erg.append("Generality: ").append(this.getGenerality()).append("\n");

		erg.append("GenPrecision: ").append(this.getGenPrecision()).append("\n");

		erg.append("    +    |   -\n");

		erg.append("--------------\n");

		erg.append("+ | ").append(this.getTp()).append(" | ").append(this.getFp()).append("\n");

		erg.append("- | ").append(this.getFn()).append(" | ").append(this.getTn()).append("\n");

		return erg.toString();
	}

	private double calculateFBeta(double beta, double prec, double rec) {
		double divisor = ((beta * beta * prec) + rec);

		divisor = (divisor == 0) ? 1 : divisor;

		return (((beta * beta) + 1) * prec * rec) / divisor;
	}

	private void generateAccuracy() {
		double divisor = (tp + fp + fn + tn);

		divisor = (divisor == 0) ? 1 : divisor;

		this.accuracy = (tp + tn) / divisor;
	}

	private void generateErrorRate() {
		double divisor = (tp + fp + fn + tn);

		divisor = (divisor == 0) ? 1 : divisor;

		this.errorRate = (fp + fn) / divisor;
	}

	private void generateF1() {
		this.f1 = this.calculateFBeta(1, this.getPrecision(), this.getRecall());
	}

	private void generateFallout() {
		double divisor = (fp + tn);

		divisor = (divisor == 0) ? 1 : divisor;

		this.fallout = (fp) / divisor;
	}

	private void generateGenerality() {
		double divisor = (tp + fp + fn + tn);

		divisor = (divisor == 0) ? 1 : divisor;

		this.generality = (tp + fn) / divisor;
	}

	private void generateGeneralityPrecision() {
		double divisor = ((this.generality * this.recall) + ((1 - this.generality) * this.fallout));

		divisor = (divisor == 0) ? 1 : divisor;

		this.genPrecision = (this.generality * this.recall) / divisor;
	}

	private void generatePrecision() {
		double divisor = (tp + fp);

		divisor = (divisor == 0) ? 1 : divisor;

		this.precision = (tp) / divisor;
	}

	private void generateRecall() {
		double divisor = (tp + fn);

		divisor = (divisor == 0) ? 1 : divisor;

		this.recall = (tp) / divisor;
	}
}
