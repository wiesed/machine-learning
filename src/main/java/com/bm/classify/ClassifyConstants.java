package com.bm.classify;

/**
 * Alle Konstanten die zu BM classify gehoeren.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public interface ClassifyConstants {
	/**
	 * Die datiendung fuer die klassify dateien.
	 */
	String SVM_DATEIENDUNG_FUER_SVM_CALSSIFY_DATEIEN = ".pred.dat";

	/**
	 * Der timeout beim lernen.
	 */
	long SVM_LEARN_TIMEOUT = 15 * 60 * 1000;

	/**
	 * Der timeout beim lernen.
	 */
	long SVM_CLASSIFY_TIMEOUT = 10 * 60 * 1000;

	/**
	 * SVM learn.
	 */
	String SVM_ENVIRONMENT_WINDOWS = "svm-windows-6.0.1";

	/**
	 * SVM learn.
	 */
	String SVM_ENVIRONMENT_LINUX = "svm-linux-6.0.1";

	/**
	 * SVM Larn mac ox.
	 */
	String SVM_ENVIRONMENT_MAC_OX = "svm-mac-6.0.1";

	/**
	 * SVM classify.
	 */
	String SVM_CLASSIFY_NAME_WINDOWS = "svm_classify.exe";

	/**
	 * SVM classify.
	 */
	String SVM_CLASSIFY_NAME_LINUX = "svm_classify";

}
