package com.bm.common.util;

/**
 * Moving window - gibt die summe der werte im window wieder. Laufzeit (O(n)).
 * 
 * @author Daniel Wiese
 * 
 */
public class MovingWindow {

	private final IIntervall intervall;

	private int currentEndPos;
	private int currentStartPos;
	private int value = 0;
	private boolean firstTime = true;

	/**
	 * Constructor.
	 * 
	 * @param intervall
	 *            das intervall ueber das das window gebildet werden soll.
	 * @param windowSize
	 *            die groesse des fensters.
	 */
	public MovingWindow(
			IIntervall intervall,
			int windowSize) {
		this.intervall = intervall;
		this.currentStartPos = intervall.getStartIndex();
		this.currentEndPos = intervall.getStartIndex() + windowSize - 1;
	}

	/**
	 * Ob es ein nachstes fenster gibt.
	 * 
	 * @return true wenn ja
	 */
	public boolean hasNext() {
		return this.currentEndPos < this.intervall.getEndIndex();
	}

	/**
	 * Geht zum nachsten fenster und gibt die summe des festers aus. Beim ersten
	 * aufruf wird die summe des ersten frames wirdergegeben, beim zewiten die
	 * zweite, etc..
	 * 
	 * @return die summe der werte im window
	 */
	public int next() {
		if (firstTime) {
			firstTimeCalc();
			firstTime = false;
		} else {
			value -= intervall.getValue(currentStartPos);
			currentStartPos++;
			value += intervall.getValue(++currentEndPos);
		}
		return value;
	}

	private void firstTimeCalc() {
		value = 0;
		for (int i = currentStartPos; i <= currentEndPos; i++) {
			value += intervall.getValue(i);
		}
	}

	/**
	 * Die jetzinge window position.
	 * @return jetzinge window position.
	 */
	public int getCurrentStartIndex() {
		return currentStartPos;
		
	}
	
	/**
	 * Die jetzinge window position.
	 * @return jetzinge window position.
	 */
	public int getCurrentEndIndex() {
		return currentEndPos;
		
	}

}
