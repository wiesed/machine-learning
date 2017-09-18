package com.bm.train;

/**
 * Beschreibung:.
 * 03.01.17, Time: 09:49.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public enum DocumentType {

	VETRAEGE("texte/NA-Vertrag/"),

	ABRECHNUNG("texte/Abrechnung/"),
	AUFMASS("texte/Aufmaß/"),
	PRUEFPROTOKOLL ("texte/Bescheinigung-Prüfprotokoll/"),
	SPARTENANFRAGE ("texte/Spartenanfrage/"),
	VERMESSUNGSUNTERLAGE ("texte/Vermessungsunterlage/");


	private final String path;

	private DocumentType(String path) {

		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
