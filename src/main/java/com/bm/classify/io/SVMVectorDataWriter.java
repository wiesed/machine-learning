package com.bm.classify.io;

import com.bm.classify.core.ISVMVector;
import com.bm.classify.core.SVMFileModel;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Schreibt einen SVM Vector auf die Festplatte. Diese Klasse bietet alle fuer
 * den Dateizugriff notwendigen Operationen.
 */
public class SVMVectorDataWriter {
	private static final Logger log = Logger.getLogger(SVMVectorDataWriter.class);
	private final File fileName;
	private BufferedWriter buf = null;

	/**
	 * Standard Constructor.Das Test/Train data verzeichniss wird genommen.
	 *
	 * @param fileModel -
	 *                  des SVM file model
	 */
	public SVMVectorDataWriter(SVMFileModel fileModel) {
		this.fileName = fileModel.getSvmTrainTestData();
	}

	/**
	 * Schlei�t die datei.
	 *
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void closeFile() {
		try {
			buf.close();
		} catch (IOException ex) {
			log.error("Eine Erzeugen der Trainingsdaten nicht moeglich", ex);
			throw new RuntimeException("Eine Erzeugen der Trainingsdaten nicht moeglich");
		}
	}

	/**
	 * Oeffnet die datei zum schrieben.
	 *
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void open() {
		try {
			if (this.fileName.exists()) {
				this.fileName.delete();
			}

			buf = new BufferedWriter(new FileWriter(this.fileName));
		} catch (IOException ex) {
			log.error("Eine Erzeugen der Trainingsdaten nicht moeglich", ex);
			throw new RuntimeException("Eine Erzeugen der Trainingsdaten nicht moeglich");
		}
	}

	/**
	 * Schribt ein SVMFeatureSet auf die platte.
	 *
	 * @param toWrite -
	 *                das SVMFrature Set das geschrieben werden soll.
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void writeDataToSvmFile(ISVMVector toWrite) {
		if (buf == null) {
			log.error("Eine Erzeugen der Trainingsdaten nicht moeglich");

			return;
		}

		try {
			buf.write(toWrite.toSVMString());
			//buf.write(toWrite.toCSVString());
			buf.newLine();
			buf.flush();
		} catch (Exception ex) {
			log.error("Eine Erzeugen der Trainingsdaten nicht moeglich", ex);

		}
	}

	/**
	 * Schriebt direkt einen SVM formattierten string auf die platte.
	 *
	 * @param toWrite -
	 *                der zu schreibende string.
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void writeDataToSvmFile(String toWrite) {
		if (buf == null) {
			log.error("Eine Erzeugen der Trainingsdaten nicht moeglich");

			return;
		}

		try {
			buf.write(toWrite);
			buf.newLine();
			buf.flush();
		} catch (Exception ex) {
			log.error("Eine Erzeugen der Trainingsdaten nicht moeglich", ex);

		}
	}

	/**
	 * Loescht das generierte Train/Test file.
	 */
	public void delete() {
		if (this.fileName.exists()) {
			this.fileName.delete();
		}
	}

}
