package com.bm.common.enums;

/**
 * Definiert ten Type einer Klassifikationsaufgabe.
 * 
 * @author Daniel Wiese
 * @since 25.07.2006
 */
public enum KlassifikationsEnum implements EnumConverter {

	TEXT_CLASSIFICATION(1, "Textklassifikation"),
	TEXT_CLASSIFICATION_MULTI_LABEL(2, "TextklassifikationMultiLabel");

	private static KlassifikationsEnum[] endUserValues = null;

	private final int value;

	private final String formatString;

	private KlassifikationsEnum(
			final int value,
			final String formatString) {
		this.value = value;
		this.formatString = formatString;
	}

	public String getFormatString() {
		return this.formatString;
	}

	/**
	 * Erzeugt eine String Darstellung.
	 * 
	 * @return String Darstellung.
	 */
	public static String[] getStringValues() {
		final String[] back = new String[values().length];
		for (int i = 0; i < values().length; i++) {
			back[i] = values()[i].toString();
		}
		return back;
	}

	/**
	 * Lifet die enum werte die Sinn machen fuer den ga und produktion.
	 * 
	 * @return die news werte die sinn machen.
	 */
	public static KlassifikationsEnum[] getEndUserValues() {
		if (endUserValues == null) {
			endUserValues = new KlassifikationsEnum[2];
			endUserValues[0] = KlassifikationsEnum.TEXT_CLASSIFICATION;
			endUserValues[1] = KlassifikationsEnum.TEXT_CLASSIFICATION_MULTI_LABEL;
		}
		return endUserValues;
	}

	/**
	 * Zur Konvertierung der Enum in eine Zahl.
	 * 
	 * @return interner Wert
	 */
	public int convert() {
		return this.value;
	}

}
