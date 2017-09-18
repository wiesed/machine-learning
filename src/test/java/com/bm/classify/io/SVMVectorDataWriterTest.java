package com.bm.classify.io;

import com.bm.classify.core.SVMFeatureSet;
import com.bm.classify.core.SVMFileModel;

import junit.framework.TestCase;

/**
 * JUnit test.
 * 
 * @author Daniel Wiese
 * @since 03.06.2006
 */
public class SVMVectorDataWriterTest extends TestCase {

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 03.06.2006
     */
    public void testSVMVectorDataWriter_writeVector_andDelete() {
        final SVMFileModel model = new SVMFileModel("TEST");
        final SVMVectorDataWriter toTest = new SVMVectorDataWriter(model);
        toTest.open();
        SVMFeatureSet vector = new SVMFeatureSet("+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ",
                true);
        for (int i = 0; i < 10; i++) {
            toTest.writeDataToSvmFile(vector);
        }
        toTest.closeFile();
        assertTrue(model.getSvmTrainTestData().exists());
        toTest.delete();
        assertFalse(model.getSvmTrainTestData().exists());
    }

}
