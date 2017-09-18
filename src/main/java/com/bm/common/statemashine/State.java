package com.bm.common.statemashine;

import java.util.HashMap;
import java.util.Map;

/**
 * Reprasentiert einen Zustand im automaten.
 * @author Daniel
 *
 * @param <S> der zustands enum
 * @param <T> der signal enum
 */
public class State<S extends Enum<S>, T extends Enum<T>> {

	private Map<T, State<S, T>> transitions = new HashMap<T, State<S, T>>();

	private final S stateEnum;

	private final boolean isEndState;

	/**
	 * Erzeugt einen zustand.
	 * @param state der zustands enum
	 * @param isEndState true wenn endzustand.
	 */
	public State(S state, boolean isEndState) {
		this.stateEnum = state;
		this.isEndState = isEndState;
	}

	/**
	 * Fuelgt eine transition hinzu.
	 * @param transition die transisiton.
	 * @param state der zielzustand.
	 */
	public void addTransition(T transition, State<S, T> state) {
		transitions.put(transition, state);
	}

	/**
	 * Liefert den naechsten zustand.
	 * 
	 * @param transition
	 *            die signal transition
	 * @return den naechsten zustand oder nul;
	 */
	public State<S, T> getNextState(T transition) {
		State<S, T> nextState = null;
		if (transitions.containsKey(transition)) {
			nextState = transitions.get(transition);
		}
		return nextState;

	}

	/**
	 * Gets the stateEnum.
	 * 
	 * @return the stateEnum
	 */
	public S getStateEnum() {
		return stateEnum;
	}

	/**
	 * True wenn endzustand.
	 * 
	 * @return true wenn endzustand
	 */
	public boolean isEndState() {
		return isEndState;
	}

}
