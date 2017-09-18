package com.bm.classify.core.helper;

import java.io.File;

import com.bm.classify.ClassifyConstants;
import com.bm.classify.ClassifyEnvironment;

/**
 * Hilfsklasse die die Pfade zur SVM learn und SVM classify enthaelt.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMEnviroment implements ClassifyConstants {

	private final File svmLearn;

	private final File svmClassify;

	private final ClassifyEnvironment environment;

	/**
	 * Constructor.
	 * 
	 * @param tempDirPath
	 *            - der pfad des temp directories
	 * @param environment
	 *            - die umgebung wie LINUX, WINDOWS, MAC,..F
	 */
	public SVMEnviroment(File tempDirPath, ClassifyEnvironment environment) {
		this.environment = environment;
		this.svmLearn = new File(tempDirPath.getAbsolutePath() + "/"
				+ environment.getLearn_name());
		this.svmClassify = new File(tempDirPath.getAbsolutePath() + "/"
				+ environment.getClassify_name());
	}

	/**
	 * Liefert den pfad fuer SVM learn.
	 * 
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 * @return - pfad fuer SVM learn.
	 */
	public File getSVMLearn() {
		return this.svmLearn;
	}

	/**
	 * Liefert den pfad fuer SVM classify.
	 * 
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 * @return - pfad fuer SVM classify.
	 */
	public File getSVMClassify() {
		return this.svmClassify;
	}

	/**
	 * Ueberprueft ob die benoetigten lern und classify dateien noch existieren.
	 * 
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 * @return - true wenn noch existent
	 */
	public boolean isValid() {
		boolean isValid = true;
		isValid = isValid && this.svmClassify.exists();
		isValid = isValid && this.svmLearn.exists();
		return isValid;
	}

	/**
	 * Retruns the environment.
	 * 
	 * @return the environment
	 */
	public ClassifyEnvironment getEnvironment() {
		return environment;
	}

}
