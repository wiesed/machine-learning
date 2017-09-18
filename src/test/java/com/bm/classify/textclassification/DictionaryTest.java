package com.bm.classify.textclassification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.bm.data.bo.ai.ITermFrequency;

/**
 * Junit test fuer das dictionary.
 * 
 * @author Daniel Wiese
 * @since 05.08.2006
 */
public class DictionaryTest extends TestCase {

    /**
     * Junit test method.
     * 
     * @author Daniel Wiese
     * @since 05.08.2006
     */
    public void testConstruction() {

        Dictionary toTest = new Dictionary(this.createTermList("Das ist ein einfacher Test"), 1);
        Dictionary toTest2 = new Dictionary(this.createTermList("Test einfacher ein ist das"), 1);
        assertEquals(toTest.getAnzahlDokumente(), 1);
        // die eindeutige reihenfolge der terme testen
        assertEquals(toTest.getTerm(0), "das");
        assertEquals(toTest.getTerm(1), "ein");
        assertEquals(toTest.getTerm(2), "einfacher");
        assertEquals(toTest.getTerm(3), "ist");
        assertEquals(toTest.getTerm(4), "test");

        assertEquals(toTest2.getTerm(0), "das");
        assertEquals(toTest2.getTerm(1), "ein");
        assertEquals(toTest2.getTerm(2), "einfacher");
        assertEquals(toTest2.getTerm(3), "ist");
        assertEquals(toTest2.getTerm(4), "test");

    }

    /**
     * Junit test method.
     * 
     * @author Daniel Wiese
     * @since 05.08.2006
     */
    public void testTermsAndPosoition() {

        Dictionary toTest = new Dictionary(this.createTermList("Das ist ist ein einfacher Test"), 1);
        assertEquals(toTest.getAnzahlDokumente(), 1);
        // die eindeutige reihenfolge der terme testen
        assertEquals(toTest.getTerm(0), "das");
        assertEquals(toTest.getTerm(1), "ein");
        assertEquals(toTest.getTerm(2), "einfacher");
        assertEquals(toTest.getTerm(3), "ist");
        assertEquals(toTest.getTerm(4), "test");

        assertEquals(toTest.getPosition("das"), Integer.valueOf(0));
        assertEquals(toTest.getPosition("ein"), Integer.valueOf(1));
        assertEquals(toTest.getPosition("einfacher"), Integer.valueOf(2));
        assertEquals(toTest.getPosition("ist"), Integer.valueOf(3));
        assertEquals(toTest.getPosition("test"), Integer.valueOf(4));
        
        //document frequency
        assertEquals(toTest.getDF(3), 2d);
        assertEquals(toTest.getDF(4), 1d);

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
