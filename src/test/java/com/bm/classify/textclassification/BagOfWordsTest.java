package com.bm.classify.textclassification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.bm.classify.core.SVMFeatureSet;
import com.bm.common.enums.Label;
import com.bm.data.bo.ai.ITermFrequency;

/**
 * Junit test.
 * 
 * @author Daniel Wiese
 * @since 05.08.2006
 */
public class BagOfWordsTest extends TestCase {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(BagOfWordsTest.class);

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 05.08.2006
     * @throws Exception -
     *             im feheler fall.
     */
    public void testGenerateWortvector_sameForSameTermSets() throws Exception {
        Dictionary dict = new Dictionary(this.createTermList("Das ist ein einfacher Test"), 2);
        final TermExtractor extractor = new TermExtractor();
        final BagOfWords b1 = new BagOfWords(dict, extractor.getTermsAndFrequencies("Das ist ein", false));
        final BagOfWords b2 = new BagOfWords(dict, extractor.getTermsAndFrequencies("ein ist Das", false));
        assertEquals(b1.printToSVMLightTFIDF(true, true), b2.printToSVMLightTFIDF(true, true));
        SVMFeatureSet b1FeatureSet = b1.toSVMFeatureSetTFIDF(true, true);
        assertEquals(b1.printToSVMLightTFIDF(true, true), b1FeatureSet.toSVMString());
        assertEquals(b2.printToSVMLightTFIDF(true, false), b1.toSVMFeatureSetTFIDF(true, false).toSVMString());
        
        b1.setLabel(Label.POSITIVE);
        assertEquals(b1.printToSVMLightTFIDF(true, true), b1.toSVMFeatureSetTFIDF(true, true).toSVMString());
        assertEquals(b1.printToSVMLightTFIDF(true, false), b1.toSVMFeatureSetTFIDF(true, false).toSVMString());
        
        log.info("Vector1: " + b1.printToSVMLightTFIDF(true, true));
        log.info("Vector2: " + b1.toSVMFeatureSetTFIDF(true, true).toSVMString());

    }

    private List<ITermFrequency> createTermList(String text) {
        final List<ITermFrequency> back = new ArrayList<ITermFrequency>();

        DictionaryMaker dictMaker = new DictionaryMaker();
        dictMaker.insertDokument(text, false);
        Map<String, Integer> worte = dictMaker.getTermsAndIDF();
        for (String term : worte.keySet()) {
            back.add(new TermFrequencyImpl(term, worte.get(term)));
        }

        return back;
    }

}
