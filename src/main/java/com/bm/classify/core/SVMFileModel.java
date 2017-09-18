package com.bm.classify.core;

import com.bm.classify.ClassifyConstants;
import com.bm.common.util.BmUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Enthalt die Refernezen zu der Modell-Datei und zu den Test/Trainingsdaten.
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMFileModel {
	private static final String MODELL_PREFIX = "SVM_MODELL";

	private Date creationDate = null;

	private boolean isValid = false;

	private String labelString;

	private File svmModel;

	private File svmTrainTestData;

	private final String lernaufgabe;

	private double xiError;

	private double xiPrecission;

	/**
	 * Constructor.
	 *
	 * @param lernaufgabe -
	 *                    fuer welche aufgabe
	 */
	public SVMFileModel(String lernaufgabe) {
		this.lernaufgabe = lernaufgabe;
	}

	/**
	 * Constructor.
	 */
	public SVMFileModel() {
		this.lernaufgabe = "UNNAMED_MODEL";
	}

	/**
	 * Loescht alle Classify temp dateien.
	 *
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void deleteClassifyTempFile() {
		if (this.getSvmTrainTestData() != null) {
			final String path = this.getSvmTrainTestData()
									.getAbsolutePath()
					+ ClassifyConstants.SVM_DATEIENDUNG_FUER_SVM_CALSSIFY_DATEIEN;
			final File toDelete = new File(path);

			if (toDelete.exists()) {
				toDelete.delete();
			}
		}
	}

	/**
	 * Loescht die model datei.
	 *
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void deleteModelFile() {
		if ((this.getSvmModel() != null) && this.getSvmModel()
												.exists()) {
			this.getSvmModel()
				.delete();
		}
	}

	/**
	 * Loescht die trainings und test daten.
	 *
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void deleteTrainTestFile() {
		if ((this.getSvmTrainTestData() != null)
				&& this.getSvmTrainTestData()
					   .exists()) {
			this.getSvmTrainTestData()
				.delete();
		}
	}

	/**
	 * Returns the creationDate.
	 *
	 * @return Returns the creationDate.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Sets the creationDate.
	 *
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns the isValid.
	 *
	 * @return Returns the isValid.
	 */
	public boolean isValid() {
		return this.isValid;
	}

	/**
	 * Sets the isValid.
	 *
	 * @param isValid The isValid to set.
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * Returns the labelString.
	 *
	 * @return Returns the labelString.
	 */
	public String getLabelString() {
		return this.labelString;
	}

	/**
	 * Sets the labelString.
	 *
	 * @param labelString The labelString to set.
	 */
	public void setLabelString(String labelString) {
		this.labelString = labelString;
	}

	/**
	 * Returns the svmModel.
	 *
	 * @return Returns the svmModel.
	 */
	public File getSvmModel() {
		if (this.svmModel == null) {
			this.svmModel = this.createModelFileName();
		}
		return this.svmModel;
	}

	/**
	 * Sets the svmModel.
	 *
	 * @param svmModel The svmModel to set.
	 */
	public void setSvmModel(File svmModel) {
		this.svmModel = svmModel;
	}

	/**
	 * Returns the svmTrainTestData.
	 *
	 * @return Returns the svmTrainTestData.
	 */
	public File getSvmTrainTestData() {
		if (this.svmTrainTestData == null) {
			this.svmTrainTestData = this.createTrainTestFileName();
		}
		return this.svmTrainTestData;
	}

	/**
	 * Sets the svmTrainTestData.
	 *
	 * @param svmTrainTestData The svmTrainTestData to set.
	 */
	public void setSvmTrainTestData(File svmTrainTestData) {
		this.svmTrainTestData = svmTrainTestData;
	}

	/**
	 * Returns the name of the model.
	 *
	 * @return Returns the wkn.
	 */
	public String getLernaufgabe() {
		return this.lernaufgabe;
	}

	/**
	 * Returns the xiError.
	 *
	 * @return Returns the xiError.
	 */
	public double getXiError() {
		return this.xiError;
	}

	/**
	 * Sets the xiError.
	 *
	 * @param xiError The xiError to set.
	 */
	public void setXiError(double xiError) {
		this.xiError = xiError;
	}

	/**
	 * Returns the xiPrecission.
	 *
	 * @return Returns the xiPrecission.
	 */
	public double getXiPrecission() {
		return this.xiPrecission;
	}

	/**
	 * Sets the xiPrecission.
	 *
	 * @param xiPrecission The xiPrecission to set.
	 */
	public void setXiPrecission(double xiPrecission) {
		this.xiPrecission = xiPrecission;
	}

	/**
	 * Implementierung der Equals methode. Equals besiert auf wkn.
	 *
	 * @param pOther pOther das zu vergleichende objekt
	 * @return --true wenn gleich
	 */
	public boolean equals(Object pOther) {
		if (pOther instanceof SVMFileModel) {
			SVMFileModel lTest = (SVMFileModel) pOther;
			final EqualsBuilder eqb = new EqualsBuilder();
			eqb.append(lTest.getLernaufgabe(), this.getLernaufgabe());
			eqb.append(lTest.getCreationDate(), this.getCreationDate());
			eqb.append(lTest.getSvmModel(), this.getSvmModel());
			eqb.append(lTest.getSvmTrainTestData(), this.getSvmTrainTestData());
			return eqb.isEquals();
		} else {
			return false;
		}
	}

	/**
	 * Generiert einen HashCode auf basis der WKN.
	 *
	 * @return den hash code.
	 */
	public int hashCode() {
		final HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.getLernaufgabe());
		hcb.append(this.getCreationDate());
		hcb.append(this.getSvmModel());
		hcb.append(this.getSvmTrainTestData());
		return hcb.toHashCode();
	}

	/**
	 * Liefert true wenn das model auch auf der Festplatte existert.
	 *
	 * @return -- true if exist
	 */
	public boolean testIfExistsModelOnFileSystem() {
		boolean back = true;
		back = back && this.getSvmModel()
						   .exists();
		return back;
	}

	/**
	 * Erzeugt einen Dateinamen fuer die SVM-Model Datei.
	 *
	 * @return - der svm model dateiname.
	 */
	public File createModelFileName() {
		// Datei erzeugen
		StringBuilder fileName = new StringBuilder();
		fileName.append(BmUtil.getTempDirectory())
				.append("/");
		fileName.append(MODELL_PREFIX);
		fileName.append("_")
				.append(this.lernaufgabe);
		fileName.append("_");
		fileName.append(System.currentTimeMillis());
		fileName.append(".mod");

		final File modelFile = new File(fileName.toString());
		return modelFile;
	}

	/**
	 * Erzeugt einen Dateinamen fuer die SVM-Model Datei.
	 *
	 * @return - der svm model dateiname.
	 */
	public File createTrainTestFileName() {
		// Datei erzeugen
		StringBuilder fileName = new StringBuilder();
		fileName.append(BmUtil.getTempDirectory())
				.append("/");
		fileName.append("TRAIN_TEST");
		fileName.append("_");
		fileName.append(System.currentTimeMillis());
		fileName.append("_name_")
				.append(this.lernaufgabe);
		fileName.append(".dat");

		final File trainTestFile = new File(fileName.toString());
		if (trainTestFile.exists()) {
			throw new RuntimeException("Das SVM-Train/Test-File ("
					+ trainTestFile.getAbsolutePath() + ") existiert bereits");
		}
		return trainTestFile;
	}

	/**
	 * Lidert die liste alle existierenden svm modelen zu der aktuellen
	 * lernaufgabe.
	 *
	 * @return die existierenden svm modelle.
	 * @author Daniel Wiese
	 * @since Jun 10, 2007
	 */
	public List<File> getAllExistingModelFiles() {
		StringBuilder fileName = new StringBuilder();
		fileName.append(MODELL_PREFIX);
		fileName.append("_")
				.append(this.lernaufgabe);

		final List<File> allFound = new ArrayList<File>();
		File temp = new File(BmUtil.getTempDirectory());
		if (temp.exists() && temp.isDirectory()) {
			File[] files = temp.listFiles();
			for (File file : files) {
				if (file.getName()
						.startsWith(fileName.toString())) {
					allFound.add(file);
				}
			}
		}

		return allFound;
	}
}
