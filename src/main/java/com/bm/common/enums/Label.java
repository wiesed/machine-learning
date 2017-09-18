package com.bm.common.enums;

/**
 * Repeasentiert eine SVM verhersage als Label.
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public enum Label {

	/**
	 * negative. *
	 */
	NEGATIVE(-1),

	/**
	 * positive. *
	 */
	POSITIVE(+1),

	/**
	 * ungelabeld. *
	 */
	UNLABLED(0);

	private final int labelValue;

	private Label(int value) {
		this.labelValue = value;
	}

	/**
	 * Liefert den int wert des labels.
	 *
	 * @return - der int wert des labels
	 */
	public int toInt() {
		return this.labelValue;
	}

	/**
	 * Transformiert den Intwert in ein Label.
	 *
	 * @param value - der int wer
	 * @return ein label objekt
	 */
	public static Label getFromInt(int value) {
		if (value > 0) {
			return POSITIVE;
		} else {
			if (value < 0) {
				return NEGATIVE;
			} else {
				return UNLABLED;
			}
		}
	}

	public static Label getFromBoolean(boolean positiveExample) {
		if (positiveExample) {
			return POSITIVE;
		} else {
			return NEGATIVE;

		}
	}

	/**
	 * Transformiert den Double in ein Label.
	 *
	 * @param value - der double wert
	 * @return ein label objekt
	 */
	public static Label getFromDouble(double value) {
		if (value > 0) {
			return POSITIVE;
		} else {
			if (value < 0) {
				return NEGATIVE;
			} else {
				return UNLABLED;
			}
		}
	}

}
