package com.bm.classify.textclassification;

import com.bm.data.bo.ai.ITermFrequency;

/**
 * Hilfsimpelentierung von ITermFrequency.
 * 
 * @author Daniel Wiese
 * @since 05.08.2006
 */
public final class TermFrequencyImpl implements ITermFrequency {

    private final String term;

    private final int haufigkeit;

    /**
     * 
     * Constructor.
     * 
     * @param term -
     *            der trem
     * @param haufigkeit -
     *            die haufigkeit
     */
    public TermFrequencyImpl(String term, int haufigkeit) {
        this.term = term;
        this.haufigkeit = haufigkeit;
    }

    /**
     * Returns the haufigkeit.
     * 
     * @return Returns the haufigkeit.
     * @author Daniel Wiese
     * @since 05.08.2006
     * @see com.bm.classify.textclassification.ITermFrequency#getHaufigkeit()
     */
    public int getHaufigkeit() {
        return this.haufigkeit;
    }

    /**
     * Returns the term.
     * 
     * @return Returns the term.
     * @author Daniel Wiese
     * @since 05.08.2006
     * @see com.bm.classify.textclassification.ITermFrequency#getTerm()
     */
    public String getTerm() {
        return this.term;
    }

    /**
     * Comparator.
     * @return - des ergebniss des vergeleichs
     * @param o - das andere vergleichsobjekt
     * @author Daniel Wiese
     * @since 05.08.2006
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(ITermFrequency o) {
        if (o != null) {
            return this.getTerm().compareTo(o.getTerm());
        } else {
            return -1;
        }
    }

}