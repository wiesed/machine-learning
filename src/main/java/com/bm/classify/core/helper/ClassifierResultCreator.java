package com.bm.classify.core.helper;

import com.bm.classify.core.result.ClassifyResult;
import com.bm.classify.core.result.SinglePrediction;
import com.bm.classify.utils.LabelTransformerHelper;
import com.bm.common.enums.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Erzeugt aus den einzelnen vorhersagen das ClassifyResult Object.
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class ClassifierResultCreator {

	/**
	 * Constructor.
	 */
	public ClassifierResultCreator() {
	}

	/**
	 * Konstruiert das classify result objekt.
	 *
	 * @param predictedLabels -
	 *                        die vorhergesagten svm label
	 * @param realLabels      -
	 *                        die echten label
	 * @param threshold       der trashhold der zu jeder vorhersage addirt wird
	 * @param filterThreshold ab diesm trashhold werden ergebnisse nicht berucksichtigt (0 = alle beruecksichtigen)
	 * @return - des ergebiss der klassifikation
	 */
	public ClassifyResult generateResult(List<Double> predictedLabels, List<Double> realLabels,
										 double threshold, double filterThreshold) {
		SinglePrediction[] labels = new SinglePrediction[predictedLabels.size()];
		for (int i = 0; i < predictedLabels.size(); i++) {
			final Double rawSVNValue = predictedLabels.get(i);
			labels[i] = new SinglePrediction(rawSVNValue, threshold);
		}

		return this.generateResult(labels, realLabels, filterThreshold);
	}


	/**
	 * Konstruiert das classify result objekt.
	 *
	 * @param predictedLabels     -
	 *                   die vorhergesagten svm label
	 * @param manualLabels -
	 *                   die echten label
	 * @return - des ergebiss der klassifikation
	 */
	public ClassifyResult generateResult(SinglePrediction[] predictedLabels, List<Double> manualLabels,
										 double filterThreshold) {
		int tp = 0;
		int fp = 0;
		int fn = 0;
		int tn = 0;

		final FilteredResultWithThreshold filteredResultWithThreshold = filterByThreshold(filterThreshold, predictedLabels, manualLabels);
		for (int i = 0; i < filteredResultWithThreshold.predicted.size(); i++) {
			if (LabelTransformerHelper.getLabelFromDouble(filteredResultWithThreshold.manualLabels.get(i)) == Label.POSITIVE) {
				if (filteredResultWithThreshold.predicted.get(i).getLabel() == Label.POSITIVE) {
					tp++;
				} else {
					fn++;
				}
			} else {
				if (filteredResultWithThreshold.predicted.get(i).getLabel() == Label.POSITIVE) {
					fp++;
				} else {
					tn++;
				}
			}
			// end if else
		}
		// end for

		return new ClassifyResult(tp, fp, tn, fn);
	}

	private FilteredResultWithThreshold filterByThreshold(double threshold, SinglePrediction[] predictedLabels, List<Double> manualLabels) {
		final FilteredResultWithThreshold result = new FilteredResultWithThreshold();
		for (int i = 0; i < predictedLabels.length; i++) {
			SinglePrediction predictedLabel = predictedLabels[i];
			Double manualLabel = manualLabels.get(i);
			if (threshold > 0) {
				if (predictedLabel.getPredictedValue() < 0 && predictedLabel.getPredictedValue() < (-1 * threshold)) {
					result.predicted.add(predictedLabel);
					result.manualLabels.add(manualLabel);
				}

				if (predictedLabel.getPredictedValue() > 0 && predictedLabel.getPredictedValue() > (threshold)) {
					result.predicted.add(predictedLabel);
					result.manualLabels.add(manualLabel);
				}
			} else {
				result.predicted.add(predictedLabel);
				result.manualLabels.add(manualLabel);
			}
		}

		return result;
	}


	private static class FilteredResultWithThreshold {
		List<SinglePrediction> predicted = new ArrayList<SinglePrediction>();
		List<Double> manualLabels = new ArrayList<Double>();

	}

}
