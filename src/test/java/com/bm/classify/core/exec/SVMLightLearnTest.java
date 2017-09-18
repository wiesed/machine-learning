package com.bm.classify.core.exec;

import java.io.File;
import java.net.URL;

import com.bm.classify.ClassifyConstants;
import com.bm.classify.core.SVMFileModel;
import com.bm.classify.core.options.SVMKernel;
import com.bm.classify.core.result.LearnResult;
import com.bm.common.util.BmUtil;

import junit.framework.TestCase;
import org.junit.Ignore;

/**
 * Junit test mit einem echten svm train lauf.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMLightLearnTest extends TestCase {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SVMLightLearnTest.class);

	/**
	 * Junit test methode.
	 * 
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void testLearn_happyPath() {
		final SVMLightLearn learner = new SVMLightLearn();
		SVMFileModel model = new SVMFileModel("TEST");
		URL train = Thread.currentThread().getContextClassLoader().getResource(
				"train.dat");
		assertNotNull(train);
		model.setSvmTrainTestData(new File(train.getPath()));
		LearnResult result = learner.learn(model, SVMKernel.LINEAR,
				ClassifyConstants.SVM_LEARN_TIMEOUT);
		assertTrue(result.isSucessfulRun());
		model.deleteModelFile();
		log.debug(result);
	}

	/**
	 * Junit test methode.
	 * 
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void testLearn_trainingDataNull() {
		final SVMLightLearn learner = new SVMLightLearn();
		SVMFileModel model = new SVMFileModel("TEST");
		model.setSvmTrainTestData(null);
		try {
			@SuppressWarnings("unused")
			LearnResult result = learner.learn(model, SVMKernel.LINEAR,
					ClassifyConstants.SVM_LEARN_TIMEOUT);
			fail("Exception expected");
		} catch (RuntimeException e) {
			log.debug("Expected: " + e.getMessage());
		}
	}

	/**
	 * Junit test methode.
	 * 
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void testLearn_trainingDataNotExisting() {
		final SVMLightLearn learner = new SVMLightLearn();
		SVMFileModel model = new SVMFileModel("TEST");
		model.setSvmTrainTestData(new File(BmUtil.getTempDirectory()
				+ File.separatorChar + "donotexist.test"));
		try {
			@SuppressWarnings("unused")
			LearnResult result = learner.learn(model, SVMKernel.LINEAR,
					ClassifyConstants.SVM_LEARN_TIMEOUT);
			fail("Exception expected");
		} catch (RuntimeException e) {
			log.debug("Expected: " + e.getMessage());
		}
	}

}
