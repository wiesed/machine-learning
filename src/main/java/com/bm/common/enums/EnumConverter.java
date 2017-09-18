package com.bm.common.enums;

/**
 * Ermoegelicht das Konvertieren einer Enum in Integer und zurueck.
 * @author Fabian Bauschulte
 *
 */
public interface EnumConverter {
	/**
	 * Zur Konvertierung der Enum in eine Zahl.
	 * @return interner Wert
	 * @see com.bm.data.bo.enums.EnumConverter#convert()
	 */
	int convert();
}
