package com.bm.common.enums;

/**
 * Definiert ten Type einer Klassifikationsaufgabe.
 * 
 * @author Daniel Wiese
 * @since 25.07.2006
 */
public enum KlassifikationsEnum implements EnumConverter {

	NEGATIVE_NACHRICHT(1, "neg. Nachricht"),
	POSITIVE_NACHRICHT(2, "pos. Nachricht"),
	NICHT_MARKTUEBERSICHT(3, "nur eine Aktie"),
	ANAYLSTENEMPFEHLUNG(4, "Analysten allgemein"),
	SCHROTT(5, "Schrott"),
	ANALYSTEN_ZU_EINER_AKTIE(6, "Analysten eine Aktie"),
	ANAYLST_NEGATIV(7, "neg Analyst"),
	GESCHAEFTSZAHLEN(8, "geschaeftszahlen"),
	ELBA_TYPE(9, "elba");

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
	 * Lifet die enum werte die sinn machen fuer den ga und produktion.
	 * 
	 * @return die news werte die sinn machen.
	 */
	public static KlassifikationsEnum[] getEndUserValues() {
		if (endUserValues == null) {
			endUserValues = new KlassifikationsEnum[4];
			endUserValues[0] = KlassifikationsEnum.NICHT_MARKTUEBERSICHT;
			endUserValues[1] = KlassifikationsEnum.NEGATIVE_NACHRICHT;
			endUserValues[2] = KlassifikationsEnum.ANALYSTEN_ZU_EINER_AKTIE;
			endUserValues[3] = KlassifikationsEnum.GESCHAEFTSZAHLEN;
		}
		return endUserValues;
	}

	/**
	 * Zur Konvertierung der Enum in eine Zahl.
	 * 
	 * @return interner Wert
	 * @see com.bm.data.bo.enums.EnumConverter#convert()
	 */
	public int convert() {
		return this.value;
	}

}
