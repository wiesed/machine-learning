package com.bm.classify.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

/**
 * Junit test case.
 * 
 * @author Daniel Wiese
 * @since Jun 10, 2007
 */
public class SVMFileModelTest extends TestCase {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SVMFileModelTest.class);

	/**
	 * Junit test.
	 * 
	 * @author Daniel Wiese
	 * @since Jun 10, 2007
	 * @throws IOException
	 *             in error case.
	 */
	public void testSVMFileModelNaming() throws IOException {
		final SVMFileModel toTest = new SVMFileModel("TESTAUFGABE");
		File svmModel = toTest.getSvmModel();
		if (svmModel.exists()) {
			svmModel.delete();
		}
		assertTrue(svmModel.createNewFile());

		List<File> existingModelFiles = toTest.getAllExistingModelFiles();
		assertFalse(existingModelFiles.isEmpty());
		for (File file : existingModelFiles) {
			log.info("Modele> " + file.getName());
		}
		svmModel.delete();
	}

}
