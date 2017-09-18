package com.bm.common.statemashine;

/**
 * Ein zustandsautomat.
 * 
 * @author Daniel
 * @param <S>
 *            der zustands enum
 * @param <T>
 *            der signal enum
 * @param <P>
 *            der parameter der durchgreicht wird
 */
public final class Statemashine<S extends Enum<S>, T extends Enum<T>, P extends Object> {

	private final StateBuilder<S, T> stateDef;

	private final IStateHandler<S, T, P> handler;

	private State<S, T> currentState;

	/**
	 * Factor methode um einen automaten zu erzeugen.
	 * 
	 * @param <S>
	 *            der zustands enum
	 * @param <T>
	 *            der signal enum
	 * @param stateDef
	 *            die zustandsdefinition.
	 * @param handler
	 *            der handler als callback
	 * @return der automat
	 * @param <P> der parameter der durchgreicht wird
	 */
	public static synchronized <S extends Enum<S>, T extends Enum<T>, P extends Object> Statemashine<S, T, P> load(
			final IStateMashineStateDef<S, T> stateDef,
			final IStateHandler<S, T, P> handler) {
		return new Statemashine<S, T, P>(stateDef.getStates(), handler);
	}

	private Statemashine(final StateBuilder<S, T> stateBuilder,
			final IStateHandler<S, T, P> handler) {
		this.stateDef = stateBuilder;
		this.handler = handler;
		this.currentState = this.stateDef.getFirstState();
	}

	public void setCurrentState(final S stateEnum) {
		this.currentState = this.stateDef.getState(stateEnum);

	}

	/**
	 * Sendet ein uebergangsignal an den automaten.
	 * 
	 * @param submitted
	 *            das signal enum
	 * @param param
	 *            der parameter der an das callback durchgreicht wird.
	 */
	public synchronized void signal(final T submitted, final P param) {
		if (this.currentState == null) {
			this.currentState = this.stateDef.getFirstState();
		}

		if (!this.currentState.isEndState()) {
			final State<S, T> nextUpcommingState = this.currentState
					.getNextState(submitted);
			if (nextUpcommingState == null) {
				throw new IllegalStateException("Die transition (" + submitted
						+ ") vom zustand (" + this.currentState.getStateEnum()
						+ ") ist nicht definiert");
			}
			final S stateEnum = nextUpcommingState.getStateEnum();
			this.handler.beforeStateReach(stateEnum, submitted, param,
					this.currentState.getStateEnum());
			this.currentState = nextUpcommingState;
			this.handler.afterStateReach(stateEnum, submitted, param,
					this.currentState.getStateEnum());
		}

	}

	/**
	 * Gibt den aktuellen Zustand zurueck.
	 * 
	 * @return gibt den aktuellen Zustand zurueck.
	 */
	public S getCurrentState() {
		return this.currentState.getStateEnum();
	}

}
