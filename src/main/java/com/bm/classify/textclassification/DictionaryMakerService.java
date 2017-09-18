package com.bm.classify.textclassification;

import com.bm.data.bo.ai.ITermFrequency;
import com.bm.data.bo.ai.TermHaufigkeitBo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

/**
 * Beschreibung: Erzeugt ein in Memory dictionary.
 * 08.11.16, Time: 10:35.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public class DictionaryMakerService {

	private static final long serialVersionUID = 1L;

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(DictionaryMakerService.class);

	private final DictionaryMaker dictionaryMaker;

	private int counter;

	public DictionaryMakerService() {
		this.dictionaryMaker = new DictionaryMaker();
	}

	public void addDocumentList(List<String> allDocuments) {
		for (final String akt : allDocuments) {
			addSingleDocument(akt);
		}
	}

	public void addSingleDocument(String akt) {
		counter++;
		this.dictionaryMaker.insertDokument(akt, true);
	}

	public Dictionary createDictionary() {
		final List<ITermFrequency> terme = new ArrayList<ITermFrequency>();

		// Anzahl ablegen
		final TermHaufigkeitBo anzahlDokumente = new TermHaufigkeitBo(
				TermHaufigkeitBo.DOCUMENT_COUNT_ID, TermHaufigkeitBo.DOCUMENT_COUNT_TERM,
				this.dictionaryMaker.getAnzahlDokumente());
		terme.add(anzahlDokumente);

		// Eintragen
		final SortedMap<String, Integer> termsAndIDF = this.dictionaryMaker.getTermsAndIDF();
		final Iterator<String> iter = termsAndIDF.keySet().iterator();
		int vectorPosition = 0;
		while (iter.hasNext()) {

			final String term = iter.next();

			final Integer anzahl = termsAndIDF.get(term);
			if ((vectorPosition % 10000 == 0) || (vectorPosition % 10000 == 0)) {
				log.info("Trage Term (" + term + ") ein Pos: (" + vectorPosition + ")");
			}

			final TermHaufigkeitBo akt = new TermHaufigkeitBo(vectorPosition, term, anzahl);
			terme.add(akt);
			vectorPosition++;
		} // end if -else


		return new Dictionary(terme, this.dictionaryMaker.getAnzahlDokumente());

	}
}
