package com.bm.common.statemashine;

/**
 * Callback bevor ein zustan derreicht wird.
 * 
 * @author Daniel
 * 
 * @param <S>
 *            der zustands enum
 * @param <T>
 *            signal enum
 * @param <P>
 *            der parameter typ der uebergeben werden kann.
 */
public interface IStateHandler<S extends Enum<S>, T extends Enum<T>, P extends Object> {

	/**
	 * Wir vor dem erreichen des zustandes aufgerufen.
	 * 
	 * @param upcommingState
	 *            der nachte zustand
	 * @param signal
	 *            das empfangeneSignal
	 * @param param
	 *            der uebergeben parameter (wird durchgereicht)
	 * @param oldstate
	 *            alter Zustand
	 */
	void beforeStateReach(S upcommingState, T signal, P param, S oldstate);

	/**
	 * Wird vor dem erreichen des zustandes aufgerufen.
	 * 
	 * @param upcommingState
	 *            der nachte zustand
	 * @param signal
	 *            das empfangeneSignal
	 * @param param
	 *            der uebergeben parameter (wird durchgereicht)
	 * @param oldstate
	 *            alter Zustand
	 */
	void afterStateReach(S upcommingState, T signal, P param, S oldstate);

}
