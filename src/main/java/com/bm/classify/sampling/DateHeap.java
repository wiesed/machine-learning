/*
 *	Copyright (C) 2005 Siemens AG, Daniel Wiese (Daniel.Wiese@siemens.com)
 */
package com.bm.classify.sampling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Speichert die <code>n</code> neuesten objekte. The DateHeap.java class is
 * responsible for saving for <code>n</code> newest date objects (Objects
 * implementing the DateOrder Interface) .
 * 
 * @param <T> -
 *            the type (must implement the DateOrder Interface)
 * @author Daniel Wiese (Daniel.Wiese@siemens.com)
 */
public class DateHeap<T extends DateOrder> {

    private final int capacity;

    private final PriorityQueue<T> queue;

    /**
     * Default constructor.
     * 
     * @param capacity -
     *            the capacity (how many elements should be stored)
     */
    public DateHeap(int capacity) {
        this.capacity = capacity;
        final DateComarator<T> comp = new DateComarator<T>();
        this.queue = new PriorityQueue<T>(this.capacity, comp);
    }

    /**
     * Adds a date object to the structure The memory consumption is limited to
     * the number of specified elements The implementation provides a O( 2*
     * log(n))=O(log(n)) time for the insertion (n is the capacity).
     * 
     * @param toAdd -
     *            the elements to add
     */
    public void add(T toAdd) {
        this.queue.add(toAdd);
        if (this.queue.size() > this.capacity) {
            this.queue.poll();
        }
    }

    /**
     * Returns the newest date objects (the size will not exeed specified
     * capacity).
     * 
     * @return - the newest date objects
     */
    public List<T> getAllElements() {
        final List<T> back = new ArrayList<T>(this.queue.size());
        for (T akt : this.queue) {
            back.add(akt);
        }
        return back;
    }

    /**
     * The inner class DateComarator is responsible for comparing two dates in
     * ascending order
     * 
     * @author Daniel Wiese (Daniel.Wiese@siemens.com)
     * 
     * @param <D> -
     *            the type must be extended from DateOrder
     */
    private static class DateComarator<D extends DateOrder> implements Comparator<D> {

        /**
         * Comperator method to sort dates
         * 
         * @param o1 -
         *            param 1
         * @param o2 -
         *            param 2
         * @return the coparison result
         * @see java.util.Comparator#compare(D,D)
         */
        public int compare(D o1, D o2) {
            final long thisValue = o1.getSystemTimeInMillis();
            final long anotherVal = o2.getSystemTimeInMillis();
            return (thisValue < anotherVal ? -1 : (thisValue == anotherVal ? 0 : 1));
        }

    }

}
