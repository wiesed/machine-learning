package com.bm.train;

import com.bm.classify.core.SVMFeatureSet;
import com.bm.classify.core.SVMFileModel;
import com.bm.classify.io.SVMVectorDataWriter;
import com.bm.classify.sampling.Sampler;
import com.bm.common.enums.KlassifikationsEnum;
import com.bm.common.enums.Label;

import java.util.List;

/**
 * Beschreibung: Schreibt ein SVM Model auf die Festplatte.
 * 10.11.16, Time: 14:25.
 *
 * @author wiese.daniel <br>
 *         copyright (C) 2016, SWM Services GmbH
 */
public class ModelWriter {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ModelWriter.class);

	private final Sampler<SVMFeatureSet> biasedRandomSampler;

	public ModelWriter(Sampler<SVMFeatureSet> biasedRandomSampler) {
		this.biasedRandomSampler = biasedRandomSampler;
	}

	public ModelWriter() {
		this.biasedRandomSampler = null;
	}

	public SVMFileModel writeSvmFileModelForTraining(KlassifikationsEnum type, List<SVMFeatureSet> trainFeatureSet) {
		final List<SVMFeatureSet> learnDataSet;
		if (this.biasedRandomSampler != null) {
			final int maximaleAnzahlDaten = trainFeatureSet.size(); // * Math.min(countPositive, countNegative);
			learnDataSet = this.biasedRandomSampler.biasedRandomSample(
					trainFeatureSet, maximaleAnzahlDaten);
		}else{
			learnDataSet = trainFeatureSet;
		}

		final SVMFileModel fileModel = new SVMFileModel(type.toString());
		final SVMVectorDataWriter dataWriter = new SVMVectorDataWriter(fileModel);
		dataWriter.open();
		// write all news vectors to file
		int posVectorCount = 0;
		int negVectorCount = 0;
		for (final SVMFeatureSet current : learnDataSet) {
			dataWriter.writeDataToSvmFile(current);
			if (current.getLabel().equals(Label.POSITIVE)) {
				posVectorCount++;
			} else if (current.getLabel().equals(Label.NEGATIVE)) {
				negVectorCount++;
			}
		}
		dataWriter.closeFile();
		log.info("Eingabe P(" + posVectorCount + ")/ N:(" + negVectorCount + ") Aufgabe ("
				+ type.toString() + "): ");
		return fileModel;
	}
}
