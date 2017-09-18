package com.bm.data.bo.ai;

import java.io.Serializable;

import com.bm.common.enums.KlassifikationsEnum;

/**
 * Repraesentiert eine klassifizierte boersennachricht.
 * 
 * @author Daniel Wiese
 * 
 */
public interface ISVMNewsLabel extends Serializable {

	/**
	 * Returns the aufgabe.
	 * 
	 * @return Returns the aufgabe.
	 */
	KlassifikationsEnum getAufgabe();

	/**
	 * Returns the label.
	 * 
	 * @return Returns the label.
	 */
	double getLabel();

	/**
	 * Returns the wkn.
	 * 
	 * @return Returns the wkn.
	 */
	int getIdentifier();

	/**
	 * Returns the zeit.
	 * 
	 * @return Returns the zeit.
	 */
	long getZeit();

}