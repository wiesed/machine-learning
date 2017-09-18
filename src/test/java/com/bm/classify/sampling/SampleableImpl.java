package com.bm.classify.sampling;

/**
 * Testimplementierung des sampleable interfaces.
 * 
 * @author Daniel Wiese
 * @since 18.08.2006
 */
public class SampleableImpl implements Sampleable {


    private final long id;
    private final long time;

    private final boolean isPositive;

    private final boolean isSelectable;

    /**
     * 
     * Constructor.
     * 
     * @param time -
     *            die zeit
     * @param isPositive -
     *            ob es positiv ist
     * @param isSelectable -
     *            ob es auswaehlbar ist.
     */
    public SampleableImpl(long id, long time, boolean isPositive, boolean isSelectable) {
        this.id = id;
        this.time = time;
        this.isPositive = isPositive;
        this.isSelectable = isSelectable;
    }


    /**
     * True wenn ein ein positives beispiel ist.
     * 
     * @author Daniel Wiese
     * @since 18.08.2006
     * @return True wenn ein ein positives beispiel ist
     * @see com.bm.classify.sampling.Sampleable#isPositiveExample()
     */
    public boolean isPositiveExample() {
        return this.isPositive;
    }

    /**
     * True wenn dieses beispiel fuer ein sampling ueberhaubt betrachtet werden
     * darf. Bei fals wird dieses beispiel garantiert ignoriert.
     * 
     * @author Daniel Wiese
     * @since 18.08.2006
     * @return - true , dann wird es evnetuell ausgewaehlt
     * @see com.bm.classify.sampling.Sampleable#isSelectable()
     */
    public boolean isSelectable() {
        return this.isSelectable;
    }

    /**
     * Return the date of the implementation.
     * 
     * @return - the date
     * @author Daniel Wiese
     * @since 18.08.2006
     * @see com.bm.classify.sampling.DateOrder#getSystemTimeInMillis()
     */
    public long getSystemTimeInMillis() {
        return this.time;
    }

    public long getId() {
        return id;
    }
}
