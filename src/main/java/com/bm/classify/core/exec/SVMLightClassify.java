package com.bm.classify.core.exec;

import com.bm.classify.ClassifyConstants;
import com.bm.classify.core.SVMFileModel;
import com.bm.classify.core.helper.ClassifierResultCreator;
import com.bm.classify.core.helper.NativeLibraryLoader;
import com.bm.classify.core.helper.SVMEnviroment;
import com.bm.classify.core.helper.SVMOutputStreamReader;
import com.bm.classify.core.result.ClassifyResult;
import com.bm.classify.core.result.LearnResult;
import com.bm.classify.core.result.SinglePrediction;
import com.bm.classify.exception.SVMModelNotValidException;
import com.bm.common.util.BmUtil;
import com.bm.common.util.OsTypes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Startet die klassifizierung mit der SVM.
 *
 * @author Daniel Wiese
 * @version 1.0
 */
public class SVMLightClassify {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SVMLightClassify.class);

	private static final NativeLibraryLoader nativeSVMLoader = new NativeLibraryLoader();

	/**
	 * Constructor.
	 */
	public SVMLightClassify() {

	}

	/**
	 * Klassifiziert und berechnet geleichgzeitig den lernerfolg anhand der
	 * echten label.
	 *
	 * @param fileModel       -
	 *                        enthaelt die orte wo sich die modelle etc. befinden
	 * @param realLabels      -
	 *                        die "richtigen" vorhersagen fuer den test
	 * @param userTreshold    --
	 *                        treshold wird auf jede prediction addiert
	 * @param timeout         -
	 *                        der timeout
	 * @param ignoreThreshold wenn > 0 werden die daten kliere trhahhod igboriert (SVN ist sich unsicher)
	 * @return -- SingleClassifierEvaluationResultObject
	 */
	public ClassifyResult classifyAndEvaluate(SVMFileModel fileModel, List<Double> realLabels,
											  double userTreshold, long timeout, double ignoreThreshold) {

		ClassifyResult back = null;

		final String filenameTest = fileModel.getSvmTrainTestData().getAbsolutePath();
		final String filenameModel = fileModel.getSvmModel().getAbsolutePath();

		List<Double> preditions = this.executeSVMClassifyAndReadResult(filenameTest, filenameModel,
				timeout);

		if (preditions == null || realLabels.size() != preditions.size()) {
			throw new RuntimeException("Die anzahl der vorhersage werte (" + preditions.size()
					+ ") entspricht nicht der anzahl der wirklichen ergenisse ("
					+ realLabels.size() + ")");
		}

		// Berechnung...
		back = this.calculateResult(realLabels, preditions, userTreshold, ignoreThreshold);

		return back;
	}

	/**
	 * Klassifiziert die vorliegende model datei.
	 *
	 * @param fileModel    -
	 *                     enthaelt die orte wo sich die modelle etc. befinden
	 * @param userTreshold --
	 *                     treshold wird auf jede prediction addiert
	 * @param timeout      -
	 *                     der timeout
	 * @return -- SingleClassifierEvaluationResultObject
	 */
	public SinglePrediction[] classify(SVMFileModel fileModel, double userTreshold, long timeout) {

		final String filenameTest = fileModel.getSvmTrainTestData().getAbsolutePath();
		final String filenameModel = fileModel.getSvmModel().getAbsolutePath();

		List<Double> preditions = this.executeSVMClassifyAndReadResult(filenameTest, filenameModel,
				timeout);
		// alls ok, dann los
		SinglePrediction[] labelsArray = new SinglePrediction[preditions.size()];

		// der treshold wird als konstante gesetzt (USer)
		for (int i = 0; i < preditions.size(); i++) {
			SinglePrediction aktLabel = new SinglePrediction(preditions.get(i), userTreshold);
			// neues label zum array hinzufuegen
			labelsArray[i] = aktLabel;
		}

		return labelsArray;
	}

	private List<Double> executeSVMClassifyAndReadResult(String filenameTest, String filenameModel,
														 long timeout) {
		List<Double> preditions = null;
		log.info("Starte klassifizierung..");

		// Datei mit den predittions
		String filenamePredictions = filenameTest
				+ ClassifyConstants.SVM_DATEIENDUNG_FUER_SVM_CALSSIFY_DATEIEN;

		// Prozess starten
		final String command = this.buildCommand(filenameTest, filenameModel, filenamePredictions);
		int code = 0;
		long startTime = System.currentTimeMillis();
		long endeGeschaetzt = startTime + timeout;
		log.debug("SVMlight called with command '" + command + "', please be patient...");

		try {
			if (BmUtil.getOs() == OsTypes.OSTYPE_LINUX) {
				final String filePath = nativeSVMLoader.prepareSVMEnviroment().getSVMClassify()
						.getAbsolutePath();
				Process process_perm = Runtime.getRuntime().exec("chmod +x " + filePath);
				int exitValue = process_perm.waitFor();
				if (exitValue != 0) {
					log.error("Can�t change svm-light classify to executable");
				}
			}

			final Process process = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));
			SVMOutputStreamReader readStdIn = new SVMOutputStreamReader(in);
			SVMOutputStreamReader readStdErr = new SVMOutputStreamReader(error);
			readStdIn.start();
			readStdErr.start();

			boolean notFinished = true;

			while (notFinished) {
				try {
					if (endeGeschaetzt <= System.currentTimeMillis()) {
						// Timeout
						process.destroy();
						code = LearnResult.CODE_TIMEOUT;
						notFinished = false;
						log.debug("Time out trat ein (" + (timeout / 1000) + " sek)!");
					} else {
						code = process.exitValue();
						notFinished = false;
					}
				} catch (IllegalThreadStateException the) {
					// warten
					try {
						Thread.sleep(100);
					} catch (InterruptedException ie) {
						// nichts zu tun
					}

					// end sleep
				}

				// end try-catch
			}

			// end while
			if (code == LearnResult.CODE_TIMEOUT) {
				log.error("Der Timeout ist eingetreten. Abbruch.");
			}

			String errorMsg = readStdErr.getAktString();
			if (errorMsg != null && !errorMsg.equals("")) {
				log.error("Fehlermeldung: '" + errorMsg + "'");
			}

			preditions = this.readPredictions(filenamePredictions);
			error.close();
			in.close();
		} catch (IOException e) {
			log.error("SVMlight:  " + "IOError whlie running SVMlight!", e);
			throw new RuntimeException("SVMlight:  " + "IOError whlie running SVMlight!");

		} catch (Exception e) {
			log.error("SVMlight has returned with code " + code, e);
			throw new RuntimeException("SVMlight has returned with code " + code);
		}

		return preditions;
	}

	private String buildCommand(String filenameTrain, String filenameModel,
								String filenamePrediction) {
		if ((filenameTrain == null) || (filenameModel == null) || (filenamePrediction == null)) {
			throw new RuntimeException("Einer der uebergebenen Parameter war null");
		}

		final SVMEnviroment svmEnviroment = nativeSVMLoader.prepareSVMEnviroment();
		StringBuffer buf = new StringBuffer(svmEnviroment.getSVMClassify().getAbsolutePath());
		buf.append(" ").append(filenameTrain).append(" ").append(filenameModel).append(" ").append(
				filenamePrediction);

		return buf.toString();
	}

	private ClassifyResult calculateResult(List<Double> manualLabels, List<Double> predictedLabels,
										   double userTeshold, double ignoreThreshold) {
		if ((manualLabels == null) || (predictedLabels == null)) {
			log.error("Die real- Lables oder die SVM Predictiond sind null. Abbruch!");
			throw new RuntimeException(
					"Die real- Lables oder die SVM Predictiond sind null. Abbruch!");
		}

		if ((manualLabels.size() != predictedLabels.size()) || (predictedLabels.size() < 1)) {
			log
					.error("Es sind keine Predictions vorhanden oder es sind nicht gleich viele Predictions und Labels vorhanden. Abbruch!");
			throw new RuntimeException(
					"Es sind keine Predictions vorhanden oder es sind nicht gleich viele Predictions und Labels vorhanden. Abbruch!");
		}

		// alls ok, dann los
		SinglePrediction[] predictedLabelsArray = new SinglePrediction[predictedLabels.size()];

		// der treshold wird als konstante gesetzt (USer)
		for (int i = 0; i < predictedLabels.size(); i++) {
			SinglePrediction aktLabel = new SinglePrediction(predictedLabels.get(i), userTeshold);
			// neues label zum array hinzufuegen
			predictedLabelsArray[i] = aktLabel;
		}

		// Precission / Recall masse kalkullieren
		ClassifierResultCreator evaluator = new ClassifierResultCreator();
		final ClassifyResult back = evaluator.generateResult(predictedLabelsArray, manualLabels, ignoreThreshold);

		return back;
	}

	private List<Double> readPredictions(String filenamePredictions)
			throws SVMModelNotValidException {
		List<Double> back = new ArrayList<Double>();

		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(filenamePredictions));
			String line = null;

			while ((line = lnr.readLine()) != null) {
				line = line.trim();
				back.add(new Double(line));
			}

			// end while
			lnr.close();
		} catch (FileNotFoundException fe) {
			throw new SVMModelNotValidException("Das Model wurde nicht an '" + filenamePredictions
					+ "' gefunden. " + fe.getMessage());
		} catch (IOException ioe) {
			throw new SVMModelNotValidException("Unerwartete IOException: " + ioe.getMessage());
		}

		return back;
	}
}
