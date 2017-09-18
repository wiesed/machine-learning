package com.bm.classify.core;

import com.bm.classify.sampling.Sampleable;
import com.bm.common.enums.Label;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Description: Leichgewichtige Klasse um effektiv einen SVM Vektor zu bauen.
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class SVMFeatureSet implements ISVMVector, Sampleable {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SVMFeatureSet.class);

	/**
	 * Speichert die aktuelle/letze belegte Position *
	 */
	private int aktPos = 0;

	private Label label;

	private final TreeMap<Integer, Double> myFeatures = new TreeMap<Integer, Double>();


	/**
	 * Constructor ohne label.
	 */
	public SVMFeatureSet() {
		this.label = Label.UNLABLED;
	}

	/**
	 * Constructor mit label.
	 *
	 * @param label der label
	 */
	public SVMFeatureSet(Label label) {
		this.label = label;
	}

	/**
	 * Kostruiert das objekt aus einem SVM String.
	 *
	 * @param toParse   -
	 *                  der SVM string
	 * @param withLabel true wenn der String mit label ist
	 */
	public SVMFeatureSet(String toParse, boolean withLabel) {
		if (withLabel) {
			this.parseSVMStringWithLabel(toParse);
		} else {
			this.parseSVMStringWithoutLabel(toParse);
		}
	}

	/**
	 * Liefert true wenn del vektor noch leer ist.
	 *
	 * @return - true wenn leer
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public boolean isEmpty() {
		return aktPos == 0;
	}

	/**
	 * Macht eine deep copy des vektors.
	 *
	 * @return - eine deep copy des vektors
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public SVMFeatureSet copy() {
		SVMFeatureSet back = new SVMFeatureSet(this.label);
		back.myFeatures.clear();
		back.myFeatures.putAll(this.myFeatures);
		return back;
	}

	/**
	 * Fuegt ein feature ans ende des vektors an.
	 *
	 * @param toAddBehind -
	 *                    der wert der an die entsprechende position engefuegt werden
	 *                    soll
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void appendFeature(final double toAddBehind) {
		this.aktPos++;
		this.myFeatures.put(this.aktPos, toAddBehind);
	}

	/**
	 * Fuegt ein feature ans eine angegebene position. Ist diese position
	 * bereits besetzt so wird diese mit dem hier uebergegebenen wert
	 * ueberschrieben.
	 *
	 * @param toAdd    -
	 *                 der wert der an die entsprechende position engefuegt werden
	 *                 soll.
	 * @param position -
	 *                 die position an der das feature angefuegt werden soll.
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void appendFeature(double toAdd, int position) {
		if (position <= 0) {
			throw new RuntimeException(
					"Can't add a value to a vector position (" + position
							+ ") less/equal  0");
		}
		this.myFeatures.put(position, toAdd);
		if (this.aktPos < position) {
			this.aktPos = position;
		}
	}

	/**
	 * Fuegt ein feature ans ende des vektors an allerdings jetzt mit namen und
	 * ausgabe ins debug.
	 *
	 * @param toAddBehind -
	 *                    der wert der an die entsprechende position engefuegt werden
	 *                    soll
	 * @param nameFeature -
	 *                    der name des features
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void appendFeature(final double toAddBehind, String nameFeature) {
		this.aktPos++;
		this.myFeatures.put(this.aktPos, toAddBehind);
		log.debug("Appended: " + nameFeature + "=" + toAddBehind);
	}

	/**
	 * Fuegt ein anderes featureset ans ende an. Hierbei werden vom anderen
	 * featureset nur die werte und nicht das label uebernommen und hinten
	 * angefuegt. Die neuen features werden an des ende dieses vektors gehaengt
	 * (also entsprechend umnumeriert).
	 *
	 * @param toAddBehind -
	 *                    das ander was drangehaengt werden soll
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void appendFeature(final SVMFeatureSet toAddBehind) {
		final Iterator<Integer> iter = toAddBehind.myFeatures.keySet()
															 .iterator();
		while (iter.hasNext()) {
			final int aktPos = iter.next();
			final double value = toAddBehind.myFeatures.get(aktPos);
			this.aktPos++;
			this.myFeatures.put(this.aktPos, value);
		}
	}

	public Label getLabel() {
		return this.label;
	}

	/**
	 * Resettet den Zustand, so als ob es neu konstruiert wurde.
	 */
	public void reset() {
		this.myFeatures.clear();
		this.label = Label.UNLABLED;

	}

	/**
	 * Setzt das label des vektors.
	 *
	 * @param label -
	 *              der label
	 * @author Daniel Wiese
	 * @since 02.06.2006
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	/**
	 * Die to string ausgabe.
	 *
	 * @return - die to string ausgabe.
	 * @author Daniel Wiese
	 * @see java.lang.Object#toString()
	 * @since 02.06.2006
	 */
	public String toString() {
		return myFeatures.toString();
	}

	/**
	 * Erzeigt eine Zeile fuer die SVM.
	 *
	 * @return formattierten String fuer die SVM Light
	 */
	public String toSVMString() {
		final StringBuilder sb = new StringBuilder();

		switch (this.label) {
			case POSITIVE:
				sb.append("+1");
				break;
			case NEGATIVE:
				sb.append("-1");
				break;
			default:
				sb.append("0");
		}

		sb.append(" ");

		final Iterator<Integer> iter = this.myFeatures.keySet()
													  .iterator();
		while (iter.hasNext()) {
			final int aktPos = iter.next();
			final double value = this.myFeatures.get(aktPos);
			sb.append((aktPos));
			sb.append(":");
			sb.append(value);
			sb.append(" ");
		}

		return sb.toString();
	}

	/**
	 * Erzeigt eine Zeile fuer die SVM.
	 *
	 * @return formattierten String fuer die SVM Light
	 */
	public String toCSVString() {
		final StringBuilder sb = new StringBuilder();


		final Iterator<Integer> iter = this.myFeatures.keySet()
													  .iterator();
		while (iter.hasNext()) {
			final int aktPos = iter.next();
			final double value = this.myFeatures.get(aktPos);

			//sb.append((aktPos));
			//sb.append("");
			sb.append(value);
			sb.append(",");
		}

		switch (this.label) {
			case POSITIVE:
				sb.append("1");
				break;
			case NEGATIVE:
				sb.append("0");
				break;
			default:
				//sb.append("0");
		}

		return sb.toString();
	}

	/**
	 * Erzeigt eine Zeile fuer die SVM jedoch ohne einen Label.
	 *
	 * @return formattierten String fuer die SVM Light (ohne label)
	 */
	public String toSVMStringWithoutLabel() {
		final StringBuilder sb = new StringBuilder();
		final Iterator<Integer> iter = this.myFeatures.keySet()
													  .iterator();
		while (iter.hasNext()) {
			final int aktPos = iter.next();
			final double value = this.myFeatures.get(aktPos);

			sb.append((aktPos));
			sb.append(":");
			sb.append(value);
			sb.append(" ");
		}

		return sb.toString();
	}

	/**
	 * Parsed eine Zeile der SVM.
	 *
	 * @param toParse -
	 *                zu parsen
	 * @return formattierten String fuer die SVM Light
	 */
	private void parseSVMStringWithLabel(String toParse) {
		final StringTokenizer st = new StringTokenizer(toParse, " ");

		try {
			if (st.hasMoreTokens()) {
				// 1. Label
				String parsedLabel = st.nextToken();
				if (parsedLabel.startsWith("+")) {
					parsedLabel = parsedLabel
							.substring(1, parsedLabel.length());
				}
				final Integer labelReaded = new Integer(parsedLabel);
				if (labelReaded == 0) {
					this.label = Label.UNLABLED;
				} else if (labelReaded > 0) {
					this.label = Label.POSITIVE;
				} else {
					this.label = Label.NEGATIVE;
				}

				// 2. werte
				while (st.hasMoreTokens()) {
					final StringTokenizer innerTokeniter = new StringTokenizer(
							st.nextToken(), ":");
					final Integer tmp = new Integer(innerTokeniter.nextToken());
					final int pos = tmp.intValue();
					if (pos > this.aktPos) {
						this.aktPos = pos;
					}
					this.myFeatures.put(pos, new Double(innerTokeniter
							.nextToken()));
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("Kann den SVM-String nicht parsen");
		}
	}

	/**
	 * Parsed eine Zeile der SVM
	 *
	 * @param toParse ohne label
	 * @return formattierten String fuer die SVM Light
	 */
	private void parseSVMStringWithoutLabel(String toParse) {
		this.label = Label.UNLABLED;
		final StringTokenizer st = new StringTokenizer(toParse, " ");
		try {

			// 2. werte
			while (st.hasMoreTokens()) {
				final StringTokenizer innerTokeniter = new StringTokenizer(st
						.nextToken(), ":");
				final Integer tmp = new Integer(innerTokeniter.nextToken());
				final int pos = tmp.intValue();
				if (pos > this.aktPos) {
					this.aktPos = pos;
				}
				this.myFeatures
						.put(pos, new Double(innerTokeniter.nextToken()));
			}

		} catch (Exception e) {
			throw new RuntimeException("Kann den SVM-String nicht parsen");
		}
	}

	/**
	 * Die equals methode.
	 *
	 * @param other das andere svm vektor
	 * @return true wenn gleich
	 * @author Daniel Wiese
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 02.06.2006
	 */
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof SVMFeatureSet) {
			final SVMFeatureSet otherCast = (SVMFeatureSet) other;
			final EqualsBuilder eq = new EqualsBuilder();
			eq.append(this.getLabel(), otherCast.getLabel());
			eq.append(this.toSVMString(), otherCast.toSVMString());
			return eq.isEquals();
		} else {
			return false;
		}
	}

	/**
	 * Returns the hash code.
	 *
	 * @return den hash code
	 * @author Daniel Wiese
	 * @see java.lang.Object#hashCode()
	 * @since 03.06.2006
	 */
	@Override
	public int hashCode() {
		final HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.getLabel());
		hcb.append(this.toSVMString());
		return hcb.toHashCode();
	}

	/**
	 * Gibt alle aktuellen features aus.
	 *
	 * @return Returns the myFeatures.
	 */
	public Map<Integer, Double> getMyFeatures() {
		return myFeatures;
	}

	/**
	 * Gibt alle aktuellen features aus.
	 *
	 * @return Returns the myFeatures.
	 */
	public TreeMap<Integer, Double> getMyFeaturesAstreeMap() {
		return myFeatures;
	}

	/**
	 * True wenn positiv.
	 *
	 * @return True wenn ein ein positives beispiel ist
	 * @see com.bm.classify.sampling.Sampleable#isPositiveExample()
	 */
	public boolean isPositiveExample() {
		return this.label.equals(Label.POSITIVE);
	}

	/**
	 * Ob es vom sampler ausgewahlt werden kann.
	 *
	 * @return - true , dann wird es evnetuell ausgewaehlt
	 * @see com.bm.classify.sampling.Sampleable#isSelectable()
	 */
	public boolean isSelectable() {
		return true;
	}

	/**
	 * Ein SVM vector hat keine zeitliche order.
	 *
	 * @return - the date
	 * @see com.bm.classify.sampling.DateOrder#getSystemTimeInMillis()
	 */
	public long getSystemTimeInMillis() {
		return 0;
	}
}