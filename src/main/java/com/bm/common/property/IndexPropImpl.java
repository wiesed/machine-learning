package com.bm.common.property;

/**
 * Die perperty fuer den buy block index wert.
 * @author Daniel Wiese
 *
 */
public class IndexPropImpl implements IIndexProp {
	
	private final int indexfenster;
	private final double minPunkteGefallen;

	/**
	 * Constructor.
	 * @param indexfenster die groesse des indexfensters
	 * @param minPunkteGefallen um wieviel prozentpunkte der index gefallen ist
	 */
	public IndexPropImpl(int indexfenster, double minPunkteGefallen) {
		this.indexfenster = indexfenster;
		this.minPunkteGefallen = minPunkteGefallen;
	}

	/**
	 * {@inheritDoc}
	 * @see com.bm.common.property.IIndexProp#getIndexfenster()
	 */
	public int getIndexfenster() {
		return indexfenster;
	}

	/**
	 * {@inheritDoc}
	 * @see com.bm.common.property.IIndexProp#getMinPunkteGefallen()
	 */
	public double getMinPunkteGefallen() {
		return minPunkteGefallen;
	}
	
	

}
