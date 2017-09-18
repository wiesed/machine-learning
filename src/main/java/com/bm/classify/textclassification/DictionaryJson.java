package com.bm.classify.textclassification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Beschreibung:.
 * 23.11.16, Time: 13:40.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public class DictionaryJson {

	private int anzahlDokumente;

	private List<String> terms;

	private List<Integer> termsDf;

	private List<List<Integer>> termsCdf = new ArrayList<List<Integer>>();

	private Map<String, Integer> termsMapPosition = new HashMap<String, Integer>();

	public DictionaryJson() {
	}

	public DictionaryJson(int anzahlDokumente, List<String> terms, List<Integer> termsDf,
						  List<List<Integer>> termsCdf, Map<String, Integer> termsMapPosition) {

		this.anzahlDokumente = anzahlDokumente;
		this.terms = terms;
		this.termsDf = termsDf;
		this.termsCdf = termsCdf;
		this.termsMapPosition = termsMapPosition;
	}

	public int getAnzahlDokumente() {
		return anzahlDokumente;
	}

	public void setAnzahlDokumente(int anzahlDokumente) {
		this.anzahlDokumente = anzahlDokumente;
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	public List<Integer> getTermsDf() {
		return termsDf;
	}

	public void setTermsDf(List<Integer> termsDf) {
		this.termsDf = termsDf;
	}

	public List<List<Integer>> getTermsCdf() {
		return termsCdf;
	}

	public void setTermsCdf(List<List<Integer>> termsCdf) {
		this.termsCdf = termsCdf;
	}

	public Map<String, Integer> getTermsMapPosition() {
		return termsMapPosition;
	}

	public void setTermsMapPosition(Map<String, Integer> termsMapPosition) {
		this.termsMapPosition = termsMapPosition;
	}
}
