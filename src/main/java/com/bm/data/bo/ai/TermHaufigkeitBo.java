package com.bm.data.bo.ai;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;


/**
 * Diese klasse berechnet einmalig die term haufigkeit fuer alle aktien.
 *
 * @author Daniel Wiese
 * @since 14.06.2006
 */

public class TermHaufigkeitBo implements ITermFrequency, Serializable {

	public static final int DOCUMENT_COUNT_ID = -1;
	public static final String DOCUMENT_COUNT_TERM = "DOCUMENT_COUNT";
	private static final long serialVersionUID = 1L;
	private int id;

	private String term;

	private int haufigkeit;

	/**
	 * Constructor.
	 */
	public TermHaufigkeitBo() {

	}

	/**
	 * Constructor.
	 *
	 * @param id         - die id (wird fuer die eindeutige sortierung verwendet) - Position im vector.
	 * @param term       -
	 *                   der term
	 * @param haufigkeit -
	 *                   die haufigkeit
	 */
	public TermHaufigkeitBo(int id, String term, int haufigkeit) {
		this.id = id;
		this.term = term;
		this.haufigkeit = haufigkeit;
	}


	/**
	 * Returns the id.
	 *
	 * @return Returns the id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the haufigkeit.
	 *
	 * @return Returns the haufigkeit.
	 */
	public int getHaufigkeit() {
		return this.haufigkeit;
	}

	/**
	 * Sets the haufigkeit.
	 *
	 * @param haufigkeit The haufigkeit to set.
	 */
	public void setHaufigkeit(int haufigkeit) {
		this.haufigkeit = haufigkeit;
	}

	/**
	 * Returns the term.
	 *
	 * @return Returns the term.
	 */
	public String getTerm() {
		return this.term;
	}

	/**
	 * Sets the term.
	 *
	 * @param term The term to set.
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Equal if term is equal.
	 *
	 * @param obj - das zu vergleichende obj
	 * @return true wenn gleich.
	 * @author Daniel Wiese
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 21.07.2006
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null & obj instanceof TermHaufigkeitBo) {
			final EqualsBuilder eq = new EqualsBuilder();
			eq.append(this.id, (((TermHaufigkeitBo) obj).id));
			return eq.isEquals();
		} else {
			return false;
		}

	}

	/**
	 * Hashcode based on term.
	 *
	 * @return hashcode
	 * @author Daniel Wiese
	 * @see java.lang.Object#hashCode()
	 * @since 21.07.2006
	 */
	@Override
	public int hashCode() {
		final HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.id);
		return hcb.toHashCode();
	}

	/**
	 * Comparator.
	 *
	 * @param o - das andere vergleichsobjekt
	 * @return - des ergebniss des vergeleichs
	 * @author Daniel Wiese
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @since 05.08.2006
	 */
	public int compareTo(ITermFrequency o) {
		if (o != null) {
			return this.getTerm().compareTo(o.getTerm());
		} else {
			return -1;
		}
	}

}
