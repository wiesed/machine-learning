package com.bm.common.statemashine;

/**
 * Die zustandsdefinition der state maschine.
 * @author Daniel
 *
 * @param <S> der zustands enum
 * @param <T> der signal enum
 */
public interface IStateMashineStateDef<S extends Enum<S>, T extends Enum<T>> {
	
	/**
	 * Liefert den zustand.
	 * @return den zustand
	 */
	StateBuilder<S, T> getStates();

}
