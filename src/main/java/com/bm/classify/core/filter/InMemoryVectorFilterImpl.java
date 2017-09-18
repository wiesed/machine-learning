package com.bm.classify.core.filter;

import java.util.ArrayList;
import java.util.List;

import com.bm.classify.core.ISVMVector;
import com.bm.common.enums.Label;

/**
 * Macht die filterung ohne die benutzung der svm indem davon ausgegenagen ist,
 * dass der label in den SVM vectoren berits gesetzt ist (was durch einen
 * preprocessing schritt gemacht wurde).
 * 
 * @author Daniel Wiese
 * 
 * @param <T>
 */
public class InMemoryVectorFilterImpl<T extends ISVMVector> implements
		IVectorFilter<T> {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(InMemoryVectorFilterImpl.class);

	private final String name;

	private final Label validLabel;

	final List<T> unused = new ArrayList<T>();

	/**
	 * Consructor.
	 * 
	 * @param validLabel -
	 *            welcher label als das richtige angesehen wird
	 * @param name
	 *            der name des filters (fuer die ausgabe)
	 */
	public InMemoryVectorFilterImpl(Label validLabel, String name) {
		this.name = name;
		this.validLabel = validLabel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.classify.core.filter.IVectorFilter#getFilteredVectors(List,
	 *      Label)
	 */
	public List<T> getFilteredVectors(List<T> all) {
		this.unused.clear();
		final List<T> back = new ArrayList<T>();
		for (T current : all) {
			if (current.getLabel() == validLabel) {
				back.add(current);
			} else {
				unused.add(current);
			}
		}
		log.info("Filter (" + name + ") Eingabe #(" + all.size()
				+ ") --> Ausgabe #(" + back.size() + ")");
		return back;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.classify.core.filter.IVectorFilter#getFilteredOutVectors()
	 */
	public List<T> getFilteredOutVectors() {
		return this.unused;
	}

}
