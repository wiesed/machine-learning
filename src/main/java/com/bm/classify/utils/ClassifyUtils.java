package com.bm.classify.utils;

import java.util.List;

import com.bm.classify.core.ISVMVector;
import com.bm.classify.core.SVMFileModel;
import com.bm.classify.io.SVMVectorDataWriter;

/**
 * Hilfklasse die einige statische hilfsmethoden zur Verfuegung stellt.
 * 
 * @author Daniel Wiese
 * @since 14.08.2006
 */
public final class ClassifyUtils {

    /**
     * Privater Constructor.
     */
    private ClassifyUtils() {
        // intetinally left blank
    }
    
    /**
	 * Schreibt die vektoren aufs dateisystem> damit diese von der SVM benutzt
	 * werden koennen.
	 * 
	 * @author Daniel Wiese
	 * @since 06.08.2006
	 * @param fileModel -
	 *            das file model (enthault nach dem schreiben den dateinamen)
	 * @param svmVectors -
	 *            die menge der vektoren die auf die platte geschrieben werden
	 *            sollen
	 */
	public static void writeVectorsToFileSystem(final SVMFileModel fileModel,
			List<? extends ISVMVector> svmVectors) {
		final SVMVectorDataWriter dataWriter = new SVMVectorDataWriter(
				fileModel);
		dataWriter.open();
		// write all news vectors to file
		for (ISVMVector current : svmVectors) {
			dataWriter.writeDataToSvmFile(current);
		}
		dataWriter.closeFile();
	}

}
