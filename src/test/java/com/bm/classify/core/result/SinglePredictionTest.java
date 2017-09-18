package com.bm.classify.core.result;

import com.bm.common.enums.Label;

import junit.framework.TestCase;

/**
 * Junit test.
 * 
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SinglePredictionTest extends TestCase {

    /**
     * Junit test method.
     * @author Daniel Wiese
     * @since 02.06.2006
     */
    public void testCreatePrediction_labelPositive() {
        final SinglePrediction toTest = new SinglePrediction(0.01);
        assertTrue(toTest.getLabel() == Label.POSITIVE);
    }
    
    /**
     * Junit test method.
     * @author Daniel Wiese
     * @since 02.06.2006
     */
    public void testCreatePrediction_labelNegative() {
        final SinglePrediction toTest = new SinglePrediction(-0.01);
        assertTrue(toTest.getLabel() == Label.NEGATIVE);
    }
    
    /**
     * Junit test method.
     * @author Daniel Wiese
     * @since 02.06.2006
     */
    public void testCreatePrediction_NoLabel() {
        final SinglePrediction toTest = new SinglePrediction(0.0);
        assertTrue(toTest.getLabel() == Label.UNLABLED);
    }
    
    /**
     * Junit test method.
     * @author Daniel Wiese
     * @since 02.06.2006
     */
    public void testCreatePrediction_LabelAndTreshold() {
        SinglePrediction toTest = new SinglePrediction(0.3, -0.2);
        assertTrue(toTest.getLabel() == Label.POSITIVE);
        
        toTest = new SinglePrediction(-0.3, 0.4);
        assertTrue(toTest.getLabel() == Label.POSITIVE);
        
        toTest = new SinglePrediction(-0.3, 0.2);
        assertTrue(toTest.getLabel() == Label.NEGATIVE);
        
        toTest = new SinglePrediction(-0.3, 0.4);
        assertTrue(toTest.getLabel() == Label.POSITIVE);
        
        toTest = new SinglePrediction(-0.3, 0.0);
        assertTrue(toTest.getLabel() == Label.NEGATIVE);
        
        
    }

}
