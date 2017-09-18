package com.bm.data.bo.ai;

import com.bm.common.enums.KlassifikationsEnum;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Diese klasse berechnet einmalig die volatilitat fuer alle aktien.
 *
 * @author Daniel Wiese
 * @since 14.06.2006
 */
public class NewsKlassifikationLabelBo implements Serializable {

	private static final long serialVersionUID = 1L;


	private final NewsKlassifikationLabelId primaryKey;


	private int label;


	private KlassifikationsEnum klassifikationsType = null;

	/**
	 * Constructor.
	 */
	public NewsKlassifikationLabelBo() {
		this.primaryKey = new NewsKlassifikationLabelId();
	}

//	/**
//	 * Constructor.
//	 *
//	 * @param news -
//	 *            die news fuer die der label ist
//	 * @param type -
//	 *            ter typ der kalssifikationsaufgabe
//	 * @param label -
//	 *            der label (+1 positiv/-1 Nagativ)
//	 */
//	public NewsKlassifikationLabelBo(final NewsBo news, final KlassifikationsEnum type,
//			final int label) {
//		this.primaryKey = new NewsKlassifikationLabelId();
//		this.primaryKey.klassifikationsaufgabe = type.toString();
//		this.primaryKey.datumInMillis = news.getDatumInMillis();
//		this.primaryKey.ueberschrift = news.getUeberschrift();
//		this.primaryKey.wkn = news.getWkn();
//		this.label = label;
//	}

	/**
	 * Returns the label (+1 positiv/-1 Nagativ).
	 *
	 * @return Returns the label.
	 */
	public int getLabel() {
		return this.label;
	}

	/**
	 * Sets the label (+1 positiv/-1 Nagativ).
	 *
	 * @param label The label to set.
	 */
	public void setLabel(final int label) {
		this.label = label;
	}

	/**
	 * Returns the klassifikationsType.
	 *
	 * @return Returns the klassifikationsType.
	 */
	public KlassifikationsEnum getKlassifikationsType() {
		if ((this.klassifikationsType == null)
				&& (this.primaryKey.klassifikationsaufgabe != null)) {
			this.klassifikationsType = KlassifikationsEnum
					.valueOf(this.primaryKey.klassifikationsaufgabe);
		}
		return this.klassifikationsType;
	}

	/**
	 * Sets the klassifikationsType.
	 *
	 * @param klassifikationsType The klassifikationsType to set.
	 */
	public void setKlassifikationsType(final KlassifikationsEnum klassifikationsType) {
		this.klassifikationsType = klassifikationsType;
		this.primaryKey.klassifikationsaufgabe = klassifikationsType.toString();
	}

	/**
	 * Returns the primaryKey.
	 *
	 * @return Returns the primaryKey.
	 */
	public NewsKlassifikationLabelId getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * Liefert true wenn die news label identisch sind.
	 *
	 * @param other -
	 *              the object to compare
	 * @return - true if equal
	 * @author Daniel Wiese
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 18.09.2005
	 */
	@Override
	public boolean equals(final Object other) {
		if (other instanceof NewsKlassifikationLabelBo) {
			final NewsKlassifikationLabelBo otherCast = (NewsKlassifikationLabelBo) other;
			final EqualsBuilder builder = new EqualsBuilder();
			builder.append(this.primaryKey, otherCast.primaryKey);
			return builder.isEquals();

		} else {
			return false;
		}
	}

	/**
	 * Liefer den hash code - gleich be gleichen news label.
	 *
	 * @return the hash code
	 * @author Daniel Wiese
	 * @see java.lang.Object#hashCode()
	 * @since 18.09.2005
	 */
	@Override
	public int hashCode() {
		final HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(this.primaryKey);
		return builder.toHashCode();
	}

	/**
	 * The PK Class from News - wird benutzt, da es sich um einen.
	 * zusammengesetzten Key handelt.
	 *
	 * @author Daniel
	 */

	public static class NewsKlassifikationLabelId implements Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * PK Comonent 1: Die Klassifikationsaufgabe.
		 */
		private String klassifikationsaufgabe;

		/**
		 * PK Comonent 2: Uberschrift der Meldung>Referenz zur NewsPK.
		 */
		private String ueberschrift;

		/**
		 * PK Comonent 3: WKN der Meldung>Referenz zur NewsPK.
		 */
		private int wkn;

		/**
		 * PK Comonent 4: Datum der Meldung>Referenz zur NewsPK.
		 */
		private long datumInMillis;

		/**
		 * Returns the datumInMillis.
		 *
		 * @return Returns the datumInMillis.
		 */
		public long getDatumInMillis() {
			return this.datumInMillis;
		}

		/**
		 * Returns the ueberschrift.
		 *
		 * @return Returns the ueberschrift.
		 */
		public String getUeberschrift() {
			return this.ueberschrift;
		}

		/**
		 * Returns the wkn.
		 *
		 * @return Returns the wkn.
		 */
		public int getWkn() {
			return this.wkn;
		}

		/**
		 * Standard equals-Methode.
		 *
		 * @param other das andere Objekt
		 * @return .
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object other) {
			if ((other != null) && (other instanceof NewsKlassifikationLabelId)) {
				final NewsKlassifikationLabelId otherC = (NewsKlassifikationLabelId) other;
				final EqualsBuilder eq = new EqualsBuilder();
				eq.append(otherC.klassifikationsaufgabe, this.klassifikationsaufgabe);
				eq.append(otherC.datumInMillis, this.datumInMillis);
				eq.append(otherC.ueberschrift, this.ueberschrift);
				eq.append(otherC.wkn, this.wkn);
				return eq.isEquals();
			} else {
				return false;
			}
		}

		/**
		 * HashCode.
		 *
		 * @return hash code.
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final HashCodeBuilder hb = new HashCodeBuilder();
			hb.append(this.klassifikationsaufgabe);
			hb.append(this.ueberschrift);
			hb.append(this.wkn);
			hb.append(this.datumInMillis);
			return hb.toHashCode();
		}

		public String getKlassifikationsaufgabe() {
			return this.klassifikationsaufgabe;
		}

		public void setKlassifikationsaufgabe(final String klassifikationsaufgabe) {
			this.klassifikationsaufgabe = klassifikationsaufgabe;
		}

		public void setUeberschrift(final String ueberschrift) {
			this.ueberschrift = ueberschrift;
		}

		public void setWkn(final int wkn) {
			this.wkn = wkn;
		}

		public void setDatumInMillis(final long datumInMillis) {
			this.datumInMillis = datumInMillis;
		}

	}

}