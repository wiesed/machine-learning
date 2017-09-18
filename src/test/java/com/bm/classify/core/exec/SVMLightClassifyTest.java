package com.bm.classify.core.exec;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.bm.classify.ClassifyConstants;
import com.bm.classify.core.SVMFileModel;
import com.bm.classify.core.options.SVMKernel;
import com.bm.classify.core.result.ClassifyResult;
import com.bm.classify.core.result.LearnResult;
import com.bm.classify.core.result.SinglePrediction;
import com.bm.common.enums.Label;

import junit.framework.TestCase;
import org.junit.Ignore;

/**
 * Testet svm light classify.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMLightClassifyTest extends TestCase {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(SVMLightClassifyTest.class);
    
    private SVMFileModel trainedModel;

    /**
     * Set up.
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final SVMLightLearn learner = new SVMLightLearn();
        this.trainedModel = new SVMFileModel("TEST");
        URL train = Thread.currentThread().getContextClassLoader().getResource("train.dat");
        assertNotNull(train);
        this.trainedModel.setSvmTrainTestData(new File(train.getPath()));
        LearnResult result = learner.learn(this.trainedModel, SVMKernel.LINEAR,
                ClassifyConstants.SVM_LEARN_TIMEOUT);
        assertTrue(result.isSucessfulRun());
    }



    /**
     * Loescht datienen auf der platte
     * @author Daniel Wiese
     * @since 02.06.2006
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.trainedModel.deleteModelFile();
    }



    /**
     * Junit test methode.
     * 
     * @author Daniel Wiese
     * @since 02.06.2006
     */
    public void testClassify_happyPath() {

        URL classifyData = Thread.currentThread().getContextClassLoader().getResource("test.dat");

        // jetzt klassifizieren
        final SVMLightClassify classify = new SVMLightClassify();
        this.trainedModel.setSvmTrainTestData(new File(classifyData.getPath()));
        SinglePrediction[] back = classify.classify(this.trainedModel, 0, ClassifyConstants.SVM_LEARN_TIMEOUT);
        assertNotNull(back);
        assertEquals(back.length, 600);

        final List<Double> realValuesSimulated = new ArrayList<Double>();
        for (SinglePrediction current : back) {
            double toAdd = (current.getLabel() == Label.POSITIVE) ? +1 : -1;
            realValuesSimulated.add(toAdd);
        }

        //klassifizieren mit test, wir tuen so als ob die vorhersagen die echten daten waehren
        ClassifyResult res = classify.classifyAndEvaluate(this.trainedModel, realValuesSimulated, 0,
                ClassifyConstants.SVM_LEARN_TIMEOUT, 0);
        assertEquals(res.getPrecision(), 1.0);
        assertEquals(res.getRecall(), 1.0);
        log.debug(res);

    }
}
