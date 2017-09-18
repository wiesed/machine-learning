package com.bm.classify.core.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bm.classify.core.ISVMVector;
import com.bm.classify.core.SVMFileModel;
import com.bm.classify.core.exec.SVMLightClassify;
import com.bm.classify.core.result.SinglePrediction;
import com.bm.classify.io.SVMVectorDataWriter;
import com.bm.common.enums.Label;

/**
 * Filtert eine menge von vectoren so, dass nur die positiven/negativen vectoren
 * uebrig bleiben.
 * 
 * @param <T> -
 *            der typ der gefiltert werden soll.
 * @author Daniel Wiese
 * @since 27.08.2006
 */
public class VectorFilterImpl<T extends ISVMVector> implements IVectorFilter<T> {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(VectorFilterImpl.class);

	private static final long TIMEOUT = 3 * 60 * 1000;

	private final File trainedModel;

	private final SVMLightClassify classifier = new SVMLightClassify();

	private final String name;

	private final Label validLabel;

	final List<T> unused = new ArrayList<T>();

	/**
	 * Constructor.
	 * 
	 * @param validLabel -
	 *            welcher label als das richtige angesehen wird
	 * @param trainedModel -
	 *            ein fuer diesen filter task rainiertes model - z.b. ein model
	 *            dass
	 * @param name -
	 *            igendein name des filters (fuer log. meldungen)
	 *            analystenmeldungen korrekt klassifiziert.
	 */
	public VectorFilterImpl(Label validLabel, SVMFileModel trainedModel,
			String name) {
		this.trainedModel = trainedModel.getSvmModel();
		this.name = name;
		this.validLabel = validLabel;
	}

	/**
	 * Liefert die gefilterte vectoren liste.
	 * 
	 * @author Daniel Wiese
	 * @since 27.08.2006
	 * @param all -
	 *            all vectoren
	 * @return - die gefilterten vectoren.
	 * @see com.bm.classify.core.filter.IVectorFilter#getFilteredVectors(List,
	 *      Label)
	 */
	public List<T> getFilteredVectors(List<T> all) {
		this.unused.clear();
		final List<T> back = new ArrayList<T>();
		final SVMFileModel model = new SVMFileModel("VECTOR_FILTER");
		final SVMVectorDataWriter vectorWriter = new SVMVectorDataWriter(model);
		vectorWriter.open();
		for (ISVMVector current : all) {
			vectorWriter.writeDataToSvmFile(current);
		}
		vectorWriter.closeFile();
		model.setSvmModel(this.trainedModel);
		final SinglePrediction[] pred = this.classifier.classify(model, 0,
				TIMEOUT);
		if (pred.length != all.size()) {
			log.error("Der eingabe (Size: " + all.size()
					+ ") und ausgabe (Size " + pred.length
					+ ") vector sind nicht gleich");
			log.error("Trainigs-Datei: "
					+ model.getSvmTrainTestData().getAbsolutePath());
			throw new RuntimeException("Der eingabe (Size: " + all.size()
					+ ") und ausgabe (Size " + pred.length
					+ ") vector sind nicht gleich");
		}

		// die validen vectoren transferieren
		for (int i = 0; i < pred.length; i++) {
			if (pred[i].getLabel() == validLabel) {
				back.add(all.get(i));
			} else {
				this.unused.add(all.get(i));
			}
		}
		log.info("Filter (" + name + ") Eingabe #(" + all.size()
				+ ") --> Ausgabe #(" + back.size() + ")");
		return back;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.bm.classify.core.filter.IVectorFilter#getFilteredOutVectors()
	 */
	public List<T> getFilteredOutVectors() {
		return this.unused;
	}

}
