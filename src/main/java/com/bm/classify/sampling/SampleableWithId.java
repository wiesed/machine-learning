package com.bm.classify.sampling;

/**
 * Ermieglicht ein samplen ueber objekten mit einer id. D.h es koennen z.B. nur
 * bestimmte id's zugelassen werden.
 *
 * @author Daniel Wiese
 * @since 27.08.2006
 */
public interface SampleableWithId extends Sampleable {

	/**
	 * Liefert die id.
	 *
	 * @return - die id
	 * @author Daniel Wiese
	 * @since 27.08.2006
	 */
	Long getID();

}
