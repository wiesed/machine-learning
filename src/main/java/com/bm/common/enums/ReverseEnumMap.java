package com.bm.common.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Hilfsklasse zum Erzeugen von Enums aus Integerwerten.
 * 
 * @author Fabian Bauschulte
 * 
 * @param <V>
 *            Typ der Enum
 */
public class ReverseEnumMap<V extends Enum<V> & EnumConverter> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<Integer, V> map = new HashMap<Integer, V>();

	/**
	 * Konstruktor. Liest die Enum und speichert alle Werte in einer Map.
	 * 
	 * @param valueType
	 *            zu diesem Typ wird gelesen.
	 */
	public ReverseEnumMap(Class<V> valueType) {
		for (V v : valueType.getEnumConstants()) {
			map.put(v.convert(), v);
		}
	}

	/**
	 * Liefert die Enum zuer Interzahl.
	 * 
	 * @param num
	 *            Integer-Wert der Enum
	 * @return die passende Enum - oder null wenn nicht gefunden.
	 */
	public V get(int num) {
		return map.get(num);
	}
}