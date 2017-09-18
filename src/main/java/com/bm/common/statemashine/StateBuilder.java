package com.bm.common.statemashine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Wird zum bauen des state automaten verwendet.
 * @author Daniel
 *
 * @param <S> der zustands enum
 * @param <T> der signal enum
 */
public class StateBuilder<S extends Enum<S>, T extends Enum<T>> {

	private Set<State<S, T>> states = new HashSet<State<S, T>>();

	private State<S, T> firstState = null;

	private Map<S, State<S, T>> enumStateMap = new HashMap<S, State<S, T>>();

	/**
	 * Fuegt einen zustand hinzu.
	 * @param stateEnum das zustands enum.
	 * @return den zustand
	 */
	public State<S, T> addState(S stateEnum) {
		return this.addState(stateEnum, false);
	}

	/**
	 * Fuegt einen endzustand hinzu.
	 * @param stateEnum de zustands enum.
	 * @return den zustand
	 */
	public State<S, T> addEndState(S stateEnum) {
		return this.addState(stateEnum, true);
	}

	private State<S, T> addState(S stateEnum, boolean isEndState) {
		State<S, T> state = new State<S, T>(stateEnum, isEndState);
		// der erste zustand ist startzustand
		if (firstState == null) {
			this.firstState = state;
		}
		states.add(state);
		enumStateMap.put(stateEnum, state);
		return state;
	}

	/**
	 * Gibt den zustand zum enum zurueck.
	 * @param state den zustand enum
	 * @return den zustand
	 */
	public State<S, T> getState(S state) {
		if (!enumStateMap.containsKey(state)) {
			throw new IllegalStateException("Der zustand (" + state
					+ ") ist nicht definiert");
		}
		return enumStateMap.get(state);
	}

	/**
	 * Gets the firstState.
	 * 
	 * @return the firstState
	 */
	public State<S, T> getFirstState() {
		return firstState;
	}

}
