package com.bm.classify.textclassification;

import java.util.ArrayList;
import java.util.List;

import com.bm.classify.core.SVMFeatureSet;
import com.bm.common.enums.Label;

/**
 * Hilfsklasse die aus texten SVM vektoren generiert. Benutzung: mit
 * addText(String text, Label label) weden N vektornen erzeugt. Mit
 * getAllVectors() kann man sich die generierten vektorn geben lassen und mit
 * clear() die datenstruktur resetten.
 * 
 * @author Daniel Wiese
 * @since 05.08.2006
 */
public class Text2SVMVectorGenerator {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(Text2SVMVectorGenerator.class);

    private final Dictionary dictionary;

    private final List<SVMFeatureSet> vectors = new ArrayList<SVMFeatureSet>();

    private final TermExtractor extractor = new TermExtractor();

    /**
     * Constructor.
     * 
     * @param dictionary -
     *            generiert ein dictionary
     */
    public Text2SVMVectorGenerator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Fuegt einen text hinzu, der dann als vektor transformiert wird.
     * 
     * @author Daniel Wiese
     * @since 05.08.2006
     * @param text -
     *            der text der als vector transofrmiert werden soll
     * @param label -
     *            der label
     */
    public void addText(String text, Label label) {
        try {
            final BagOfWords bow = new BagOfWords(this.dictionary, extractor
                    .getTermsAndFrequencies(text, true));
            bow.setLabel(label);
            final SVMFeatureSet svmVector = bow.toSVMFeatureSetTFIDF(true, true);
            this.vectors.add(svmVector);
        } catch (Exception e) {
            log.error("Cannot insert text: (" + text + ") as a vector");
        }

    }

    /**
     * Gibt alle aktuell generierten vektoren zurueck.
     * 
     * @author Daniel Wiese
     * @since 05.08.2006
     * @return alle aktuell generierten vektoren.
     */
    public List<SVMFeatureSet> getAllVectors() {
        return this.vectors;
    }

    /**
     * Loescht die menge aller svm vektoren.
     * 
     * @author Daniel Wiese
     * @since 05.08.2006
     */
    public void clear() {
        this.vectors.clear();
    }

}
