package com.bm.classify.textclassification;

import java.util.Map;

import junit.framework.TestCase;

/**
 * Junit test.
 * 
 * @author Daniel Wiese
 * @since 21.07.2006
 */
public class TermExtractorTest extends TestCase {
    

    /**
     * Junit test case.
     * 
     * @author Daniel Wiese
     * @since 21.07.2006
     * @throws Exception -
     *             in error case
     */
    public void testTermCreation_sipleText() throws Exception {
        TermExtractor toTest = new TermExtractor();
        TermSet back = toTest.getTermsAndFrequencies("Das ist ein einfacher text.", false);
        Map<String, Integer> worte = back.getSortedMap();
        assertEquals(back.size(), 5);
        assertEquals(worte.get("das"), Integer.valueOf(1));
        assertEquals(worte.get("ist"), Integer.valueOf(1));
        assertEquals(worte.get("ein"), Integer.valueOf(1));
        assertEquals(worte.get("einfacher"), Integer.valueOf(1));
        assertEquals(worte.get("text"), Integer.valueOf(1));

    }
    

    /**
     * Junit test case.
     * 
     * @author Daniel Wiese
     * @since 21.07.2006
     * @throws Exception -
     *             in error case
     */
    public void testTermCreation_Pattern() throws Exception {
        TermExtractor toTest = new TermExtractor();
        TermSet back = toTest.getTermsAndFrequencies("Das ist $% Das BLBLBL (Xetra: BLBLBL O.N. ist (1234567890 ein 37363.38383 <MOT.NYS> <MTL.FSE> einfacher 234545 Iwoer AG (Xetra: Iwoer-DH (Xetra: test. 1971 ", false);
        Map<String, Integer> worte = back.getSortedMap();
        assertEquals(back.size(), 18);
        assertEquals(worte.get("das"), Integer.valueOf(2));
        assertEquals(worte.get("ist"), Integer.valueOf(2));
        assertEquals(worte.get("ein"), Integer.valueOf(1));
       
        //assertEquals(worte.get("euro"), Integer.valueOf(1));
        //log.info("1> "+ worte.get("euro"));
        assertEquals(worte.get("dollar"), Integer.valueOf(1));
        //assertEquals(worte.get("gbp"), Integer.valueOf(1));
        //log.info("1> "+ worte.get("gbp"));
        //assertEquals(worte.get("yen"), Integer.valueOf(1));
        //log.info("1> "+ worte.get("yen"));
        assertEquals(worte.get("prozent"), Integer.valueOf(1));
        assertEquals(worte.get("einfacher"), Integer.valueOf(1));
        assertEquals(worte.get(TermExtractor.FLOATINGZAHL_DELIM.toLowerCase()), Integer.valueOf(1));
        assertEquals(worte.get(TermExtractor.ZAHL_DELIM.toLowerCase()), Integer.valueOf(2));
        assertEquals(worte.get(TermExtractor.JAHR_DELIMITER.toLowerCase()), Integer.valueOf(1));
        assertEquals(worte.get("test"), Integer.valueOf(1));

    }
    
    /**
     * Junit test case.
     * 
     * @author Daniel Wiese
     * @since 21.07.2006
     * @throws Exception -
     *             in error case
     */
    public void testTermCreation_textMitWiederholungen() throws Exception {
        TermExtractor toTest = new TermExtractor();
        TermSet back = toTest.getTermsAndFrequencies("Das ist ein einfacher ein text ist.", false);
        Map<String, Integer> worte = back.getSortedMap();
        assertEquals(back.size(), 5);
        assertEquals(worte.get("das"), Integer.valueOf(1));
        assertEquals(worte.get("ist"), Integer.valueOf(2));
        assertEquals(worte.get("ein"), Integer.valueOf(2));
        assertEquals(worte.get("einfacher"), Integer.valueOf(1));
        assertEquals(worte.get("text"), Integer.valueOf(1));

    }
    
    /**
     * Junit test case.
     * 
     * @author Daniel Wiese
     * @since 21.07.2006
     * @throws Exception -
     *             in error case
     */
    public void testTermCreation_Datum() throws Exception {
        TermExtractor toTest = new TermExtractor();
        TermSet back = toTest.getTermsAndFrequencies("Das ist ein einfacher 2004 2104 1800 1900 1999 20/12/2005 20.13.2005 32.12.2005 ein text ist.", false);
        Map<String, Integer> worte = back.getSortedMap();
        assertEquals(back.size(), 8);
        assertEquals(worte.get("das"), Integer.valueOf(1));
        assertEquals(worte.get("aaajahraaa"), Integer.valueOf(3));
        assertEquals(worte.get("ist"), Integer.valueOf(2));
        assertEquals(worte.get("ein"), Integer.valueOf(2));
        assertEquals(worte.get("einfacher"), Integer.valueOf(1));
        assertEquals(worte.get("text"), Integer.valueOf(1));

    }
    
    /**
     * Junit test case.
     * 
     * @author Daniel Wiese
     * @since 21.07.2006
     * @throws Exception -
     *             in error case
     */
    public void testTermCreation_textMitWiederholungenUndZahlen() throws Exception {
        TermExtractor toTest = new TermExtractor();
        TermSet back = toTest.getTermsAndFrequencies("Das ist ein 56223 'einfacher' kostet 3000.00 $ QW33W ein text ist.", false);
        Map<String, Integer> worte = back.getSortedMap();
        assertEquals(back.size(), 11);
        assertEquals(worte.get(TermExtractor.ZAHL_DELIM.toLowerCase()), Integer.valueOf(2));
        assertEquals(worte.get(TermExtractor.FLOATINGZAHL_DELIM.toLowerCase()), Integer.valueOf(1));
        assertEquals(worte.get("das"), Integer.valueOf(1));
        assertEquals(worte.get("ist"), Integer.valueOf(2));
        assertEquals(worte.get("ein"), Integer.valueOf(2));
        assertEquals(worte.get("einfacher"), Integer.valueOf(1));
        assertEquals(worte.get("text"), Integer.valueOf(1));
        assertEquals(worte.get("qw"), Integer.valueOf(1));

    }

}
