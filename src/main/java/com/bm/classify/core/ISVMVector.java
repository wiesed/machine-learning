package com.bm.classify.core;

import com.bm.common.enums.Label;

/**
 * Das interface repaesentiert einen SVM vector. Ein vektor kann sich drucken und hat
 * einen Label.
 *
 * @author Daniel Wiese
 * @since 20.08.2006
 */
public interface ISVMVector {

	/**
	 * Liefert den SVM vector fuer die SVM light zurueck.
	 *
	 * @return den SVM vector fuer die SVM light
	 * @author Daniel Wiese
	 * @since 20.08.2006
	 */
	String toSVMString();

	/**
	 * Trick: Vektor als CSV Exportieren
	 * @return  .
	 */
	String toCSVString();

	/**
	 * Liefer den Label des vectors.
	 *
	 * @return Den Label des vectors.
	 * @author Daniel Wiese
	 * @since 20.08.2006
	 */
	Label getLabel();

	/**
	 * Setzt den Label des vectors.
	 *
	 * @param label - der label
	 * @author Daniel Wiese
	 * @since 20.08.2006
	 */
	void setLabel(Label label);

}
