package com.bm.classify.textclassification;

import java.util.Map;

import junit.framework.TestCase;

/**
 * Junit test.
 * 
 * @author Daniel Wiese
 * @since 21.07.2006
 */
public class DictionaryMakerTest extends TestCase {

    /**
     * Junit test case.
     * 
     * @author Daniel Wiese
     * @since 21.07.2006
     * @throws Exception -
     *             in error case
     */
    public void testTermCreation_sipleText() throws Exception {
        DictionaryMaker toTest = new DictionaryMaker();
        toTest.insertDokument("Das ist ein einfacher text.", false);
        assertEquals(toTest.getAnzahlDokumente(), 1);
        Map<String, Integer> worte = toTest.getTermsAndIDF();
        assertEquals(worte.size(), 5);
        assertEquals(worte.get("das"), Integer.valueOf(1));
        assertEquals(worte.get("ist"), Integer.valueOf(1));
        assertEquals(worte.get("ein"), Integer.valueOf(1));
        assertEquals(worte.get("einfacher"), Integer.valueOf(1));
        assertEquals(worte.get("text"), Integer.valueOf(1));
        
        toTest.insertDokument("Das ist ein einfacher text.", false);
        assertEquals(toTest.getAnzahlDokumente(), 2);
        worte = toTest.getTermsAndIDF();
        assertEquals(worte.size(), 5);
        assertEquals(worte.get("das"), Integer.valueOf(2));
        assertEquals(worte.get("ist"), Integer.valueOf(2));
        assertEquals(worte.get("ein"), Integer.valueOf(2));
        assertEquals(worte.get("einfacher"), Integer.valueOf(2));
        assertEquals(worte.get("text"), Integer.valueOf(2));

    }

}
