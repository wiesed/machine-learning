package com.bm.data.bo.ai;


import org.apache.commons.lang.builder.HashCodeBuilder;

import com.bm.common.enums.KlassifikationsEnum;
import com.bm.common.enums.ReverseEnumMap;

/**
 * Wenn nachrichten automatisch von der SVM klassifiziert werden, wird das
 * ergebniss in der DB so abgelegt.
 * 
 * @author Daniel Wiese
 * 
 */
public class SVMGeneratedNewsLabel implements ISVMNewsLabel {

	private static final long serialVersionUID = 1L;

	private static final ReverseEnumMap<KlassifikationsEnum> klassifikationsEnumKonverter = new ReverseEnumMap<KlassifikationsEnum>(
			KlassifikationsEnum.class);


	private Long id;

	private Integer identifier;


	private Long zeit;


	private Long newsid;


	private Double label;

	private int aufgabe;

	/**
	 * Consructor.
	 */
	public SVMGeneratedNewsLabel() {

	}

	/**
	 * Consructor with params.
	 * 
	 * @param identifier -
	 *            die identifier
	 * @param newsid -
	 *            die referenz zur newsID
	 * @param zeit -
	 *            die zeit wenn die nachticht erschienen ist (system time in
	 *            millis)
	 * @param label -
	 *            der lable (von der svm generiert)
	 * @param aufgabe -
	 *            die lernaufgabe
	 */
	public SVMGeneratedNewsLabel(int identifier, long newsid, long zeit, double label,
								 KlassifikationsEnum aufgabe) {
		this.identifier = identifier;
		this.newsid = newsid;
		this.label = label;
		this.zeit = zeit;
		this.aufgabe = aufgabe.convert();
	}

	/**
	 * Returns the aufgabe.
	 * 
	 * @return Returns the aufgabe.
	 */
	public KlassifikationsEnum getAufgabe() {
		return klassifikationsEnumKonverter.get(this.aufgabe);
	}

	/**
	 * Sets the aufgabe.
	 * 
	 * @param aufgabe
	 *            The aufgabe to set.
	 */
	public void setAufgabe(KlassifikationsEnum aufgabe) {
		this.aufgabe = aufgabe.convert();
	}

	/**
	 * Returns the label.
	 * 
	 * @return Returns the label.
	 */
	public double getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            The label to set.
	 */
	public void setLabel(Double label) {
		this.label = label;
	}

	/**
	 * Returns the newsid.
	 * 
	 * @return Returns the newsid.
	 */
	public Long getNewsid() {
		return newsid;
	}

	/**
	 * Sets the newsid.
	 * 
	 * @param newsid
	 *            The newsid to set.
	 */
	public void setNewsid(Long newsid) {
		this.newsid = newsid;
	}

	/**
	 * Returns the identifier.
	 * 
	 * @return Returns the identifier.
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier.
	 * 
	 * @param identifier
	 *            The identifier to set.
	 */
	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the zeit.
	 * 
	 * @return Returns the zeit.
	 */
	public long getZeit() {
		return zeit;
	}

	/**
	 * Sets the zeit.
	 * 
	 * @param zeit
	 *            The zeit to set.
	 */
	public void setZeit(Long zeit) {
		this.zeit = zeit;
	}

	/**
	 * Returns the id.
	 * 
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if ((other != null) && (other instanceof SVMGeneratedNewsLabel)) {
			final SVMGeneratedNewsLabel otherC = (SVMGeneratedNewsLabel) other;
			final org.apache.commons.lang.builder.EqualsBuilder eq = new org.apache.commons.lang.builder.EqualsBuilder();
			eq.append(this.id, otherC.id);
			return eq.isEquals();
		} else {
			return false;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final org.apache.commons.lang.builder.HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.id);
		return hcb.toHashCode();
	}

}
