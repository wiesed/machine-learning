package com.bm.classify.sampling;

import com.bm.classify.sampling.filter.impl.SelectedFilter;
import com.bm.classify.sampling.filter.impl.SelectedIdsFilter;
import com.bm.classify.sampling.filter.impl.SelectedIndexFilter;
import com.bm.classify.sampling.filter.impl.SpyFilter;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Diese Klasse kann eine Liste die das Interface Sampleable implementiert hat
 * sampeln (biased). Das sampling kann sowohl im speicher als auch vom
 * datentraeger erfolgen Bei sampling vom datentraeger kann die grundmenge ein
 * vielfaches des arbeitsspeichers sein.
 *
 * @param <T> -
 *            der typ des klasse die gesampled wird (aus jedenfall ein untertyp
 *            vom sampleable.
 * @author Daniel
 */
public class Sampler<T extends Sampleable> {

	private static final Logger log = Logger.getLogger(Sampler.class);

	private final FlatFile<T> flatFile;

	private final Random random = new Random(System.currentTimeMillis());

	/**
	 * Consructor.
	 *
	 * @param flatFile -
	 *                 wenn es sich um einen flat fiel sampler handelt
	 */
	public Sampler(FlatFile<T> flatFile) {
		this.flatFile = flatFile;
	}

	/**
	 * Consructor (fuer einen in Memory sampler).
	 */
	public Sampler() {
		this.flatFile = null;
	}

	/**
	 * @param toSamle        die gasammte datenmenge
	 * @param testTrainRatio 0.1 bedeutet dass 10% der daten zum testen (avaluation verwnedet werden)
	 * @return test und trainingsdaten
	 */
	public TestTrainData<T> uncontrolledTestAndLearnSample(List<T> toSamle, double testTrainRatio) {
		int targetSizeTrain = (int) ((double) toSamle.size() * testTrainRatio);

		final Set<Integer> selectedIndexes = this.ziehenOhneZuruecklegen(targetSizeTrain, toSamle.size());

		final TestTrainData<T> result = new TestTrainData<T>(toSamle.size());

		for (int i = 0; i < toSamle.size(); i++) {
			final T data = toSamle.get(i);
			if (selectedIndexes.contains(i)) {
				result.test.add(data);
			} else {
				result.train.add(data);
			}
		}
		log.info("Total Size: " + toSamle.size());
		log.info("Size Train: " + result.train.size());
		log.info("Size Test: " + result.test.size());

		return result;
	}


	/**
	 * Fuehrt ein sampling im speicher aus, mit einem bias von 0.5.
	 *
	 * @param toSample   -
	 *                   die liste der zu sampelnden objekte
	 * @param sampleSize -die greoesse des gewuenschen ergebnisses.
	 * @return - das sample
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public List<T> biasedRandomSample(List<T> toSample, int sampleSize) {
		return this.contolledRandomSample(toSample, sampleSize, 0.5);
	}

	/**
	 * TODO: Wenn derzeit zu wenige beispiele vorhandne sind wird das sample unbiased!
	 * //TODO: mann sollte die gleichng loesen wenn zu wenige beispiele da sind
	 * Fuehrt ein sampling im Speicher aus. Wenn weniger Beispiele als die
	 * gewuenschte zielmenge vorhanden sind, wird das ergebniss entreechend
	 * reduziert jedoch der bias eingehalten.
	 *
	 * @param toSample        -
	 *                        liste mit den zu sampelnden objekten
	 * @param sampleSize      -
	 *                        die sample groesse (ziel)
	 * @param percentNegative -
	 *                        der bias
	 * @return das sample
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public List<T> contolledRandomSample(List<T> toSample, int sampleSize, double percentNegative) {

		if (percentNegative < 0 || percentNegative > 1) {
			throw new RuntimeException("The percent negative ratio must be between 0..1");
		}

		log.debug("Sample Datenmenge (BIASED)!! Ist:(" + toSample.size() + ") > Zielgroesse("
				+ sampleSize + ")");

		int negativeSize = (int) (sampleSize * percentNegative);
		int positiveSize = sampleSize - negativeSize;
		final List<T> back = new ArrayList<T>();
		// fields to separate the true/and false indexes
		final int[] trueExamplesIndex = new int[toSample.size()];
		final int[] falseExamplesIndex = new int[toSample.size()];

		int endTrue = -1;
		int endFalse = -1;

		// calculate the indexes (for true/false examples)
		for (int i = 0; i < toSample.size(); i++) {
			final Sampleable akt = toSample.get(i);
			if (akt.isPositiveExample()) {
				endTrue++;
				trueExamplesIndex[endTrue] = i;
			} else {
				endFalse++;
				falseExamplesIndex[endFalse] = i;
			}
		}

		log.debug("All-Unsampled (" + toSample.size() + ") +:(" + (endTrue + 1) + ") -:("
				+ (endFalse + 1) + ") ");

		if ((positiveSize > endTrue) || (negativeSize > endFalse)) {
			int newTotalSize = this.getPerfectSampleSize(percentNegative, endTrue + 1, endFalse + 1);
			negativeSize = (int) (newTotalSize * percentNegative);
			positiveSize = newTotalSize - negativeSize - 1;

			log.info("Nicht genuegend Beispiele>Korrigiere sample zize auf +(" + positiveSize + ") / -(" + negativeSize + ")");
		}

		int countPos = 0;
		int countNeg = 0;
		// zuerst die positiven Beispiele Auswaehlen
		final Set<Integer> selectedTrueIndexes = this.ziehenOhneZuruecklegen(positiveSize, endTrue);
		final Iterator<Integer> iterTrue = selectedTrueIndexes.iterator();
		while (iterTrue.hasNext()) {
			final Integer aktIndex = iterTrue.next();
			back.add(toSample.get(trueExamplesIndex[aktIndex.intValue()]));
			countPos++;
		}

		// dann die negativen Beispiele Auswaehlen
		final Set<Integer> selectedFalseIndexes = this.ziehenOhneZuruecklegen(negativeSize, endFalse);
		final Iterator<Integer> iterFalse = selectedFalseIndexes.iterator();
		while (iterFalse.hasNext()) {
			final Integer aktIndex = iterFalse.next();
			back.add(toSample.get(falseExamplesIndex[aktIndex.intValue()]));
			countNeg++;
		}

		log.info("All (" + back.size() + ") +:(" + countPos + ") -:(" + countNeg + ") ");

		return back;
	}


	/**
	 * Erzeugt ein unbiased sample welches auf einen Flat file basiert.
	 *
	 * @param flatfilePattern --
	 *                        Die Klasse des Faltfiles
	 * @param totalSize       --
	 *                        die Gesammtgroesse des Flat files
	 * @param sampleSize      --
	 *                        die gewunschte Saple groesse
	 * @param percentNegative --
	 *                        anzahl der negativen beispiel in Proz
	 * @return -- die gesampelte Liste
	 */
	public List<T> contolledRandomFlatFileSample(Class<T> flatfilePattern, int totalSize,
												 int sampleSize, double percentNegative) {

		if (this.flatFile == null) {
			throw new RuntimeException("Der Sampler wurde nicht als Flat-File sampler erzeugt");
		}

		// wenn der bias negativ ist> dann unbiased sampeln
		if (percentNegative < 0) {
			log.error("Der bias ist negativ");
			throw new RuntimeException("Der bias ist negativ");
		} else {
			// normales biased sample durchfuehren
			if (percentNegative < 0 || percentNegative > 1) {
				throw new RuntimeException("The percent negative ratio must be between 0..1");
			}

			log.debug("Sample Datenmenge (UNBIASED)!! Ist:(" + totalSize + ") > Zielgroesse("
					+ sampleSize + ")");

			final SpyFilter<T> spyFilter = new SpyFilter<T>(totalSize);

			// analys pos/false exmples
			flatFile.readFilteredLines(spyFilter);

			// fields to separate the true/and false indexes
			final int[] trueExamplesIndex = spyFilter.getTrueExamplesIndex();
			final int[] falseExamplesIndex = spyFilter.getFalseExamplesIndex();

			int endTrue = spyFilter.getEndTrue();
			int endFalse = spyFilter.getEndFalse();

			log.info("All-Unsampled (" + totalSize + ") +:(" + (endTrue + 1) + ") -:("
					+ (endFalse + 1) + ") ");

			double newRatio = 0;
			// wenn symple zize -1 ist> dann dynamisch berechnen
			if (percentNegative == -1) {
				newRatio = 1 - ((double) endTrue / (double) endFalse);
			} else {
				newRatio = percentNegative;
			}
			log.info("New Ratio: " + newRatio);
			int negativeSize = (int) (sampleSize * newRatio);
			int positiveSize = sampleSize - negativeSize;

			if (endTrue < positiveSize) {
				log.info("Es koennen maximal (" + endTrue
						+ ")POSITIVE Beispiele gezogen werden (-50 Toleranz)");
				positiveSize = endTrue - 50;
			}

			if (endFalse < negativeSize) {
				log.info("Es koennen maximal (" + endFalse
						+ ")NEGATIVE Beispiele gezogen werden (-50 Toleranz)");
				negativeSize = endFalse - 50;
			}

			// zuerst die positiven Beispiele Auswaehlen
			final Set<Integer> selectedTrueIndexes = this.ziehenOhneZuruecklegen(positiveSize, endTrue);
			final Iterator<Integer> iterTrue = selectedTrueIndexes.iterator();
			final Set<Integer> selectedTrueObjects = new HashSet<Integer>();
			while (iterTrue.hasNext()) {
				final Integer aktIndex = iterTrue.next();
				selectedTrueObjects.add(new Integer((trueExamplesIndex[aktIndex.intValue()])));
			}

			// dann die negativen Beispiele Auswaehlen
			final Set<Integer> selectedFalseIndexes = this.ziehenOhneZuruecklegen(negativeSize, endFalse);
			final Iterator<Integer> iterFalse = selectedFalseIndexes.iterator();
			final Set<Integer> selectedFalseObjects = new HashSet<Integer>();
			while (iterFalse.hasNext()) {
				final Integer aktIndex = iterFalse.next();
				selectedFalseObjects.add(new Integer((falseExamplesIndex[aktIndex.intValue()])));
			}

			// jetzt die ausgewahlten Beispiele einlesen
			final SelectedFilter<T> selectFilter = new SelectedFilter<T>(selectedTrueObjects,
					selectedFalseObjects);

			final List<T> back = flatFile.readFilteredLines(selectFilter);

			log.info("All (" + back.size() + ") +:(" + selectFilter.getPosCount() + ") -:("
					+ selectFilter.getNegCount() + ") ");

			return back;
		}
	}

	/**
	 * Erzeugt ein sample welches auf einen Flat file basiert. Unbiased und
	 * geschwindigkeits optimiert.
	 *
	 * @param totalSize  --
	 *                   die Gesammtgroesse des Flat files
	 * @param sampleSize --
	 *                   die gewunschte Saple groesse
	 * @return -- die gesampelte Liste
	 */
	public List<T> uncontrolledUnbiasedRandomFlatFileSample(int sampleSize, int totalSize) {

		if (this.flatFile == null) {
			throw new RuntimeException("Der Sampler wurde nicht als Flat-File sampler erzeugt");
		}

		log.debug("Sample Datenmenge (UNBIASED)!! Ist:(" + totalSize + ") > Zielgroesse("
				+ sampleSize + ")");

		// eine beliebige meneg von indexen auswahelen
		final Set<Integer> selectedIndexes = this.ziehenOhneZuruecklegen(sampleSize, totalSize);

		// jetzt die ausgewahlten Beispiele einlesen
		final SelectedIndexFilter<T> selectFilter = new SelectedIndexFilter<T>(selectedIndexes);
		final List<T> back = flatFile.readFilteredLinesWithRandomAccess(selectFilter);

		log.info("Result size (" + back.size() + ")");

		return back;
	}

	/**
	 * Erzeugt ein sample welches auf einen Flat file basiert. Unbiased und
	 * geschwindigkeits optimiert. Es wird nur ueber zugelassenen id�s
	 * gesampled.
	 *
	 * @param validIds   die gueltigen id aus denen eine menge gesampled werden soll
	 * @param sampleSize die gewunschte Saple groesse
	 * @return die gesampelte Liste
	 */
	public List<T> uncontrolledUnbiasedIdRandomFlatFileSample(List<Long> validIds, int sampleSize) {

		if (this.flatFile == null) {
			throw new RuntimeException("Der Sampler wurde nicht als Flat-File sampler erzeugt");
		}

		log.debug("Sample Datenmenge (UNBIASED)!! Ist:(" + validIds.size() + ") > Zielgroesse("
				+ sampleSize + ")");

		// eine beliebige menge von indexen auswahelen
		final Set<Integer> selectedIndexes = this.ziehenOhneZuruecklegen(sampleSize, validIds
				.size());

		// gueltige id�s wahlen
		final Set<Long> selectedIds = new HashSet<Long>();
		for (Integer index : selectedIndexes) {
			selectedIds.add(validIds.get(index));
		}

		// jetzt die ausgewahlten Beispiele einlesen
		final SelectedIdsFilter<T> selectFilter = new SelectedIdsFilter<T>(selectedIds);
		final List<T> back = flatFile.readFilteredLinesWithRandomAccess(selectFilter);

		log.info("Result size (" + back.size() + ")");

		return back;
	}

	/**
	 * Fuehrt ein sampling im Speicher aus. Er wird keib bias (angleichung des
	 * verhaelnisses zwichen den pos/neg beispielen durchgefuehrt)
	 *
	 * @param toSample   -
	 *                   liste mit den zu sampelnden objekten
	 * @param sampleSize -
	 *                   die sample groesse (ziel)
	 * @return das sample
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public List<T> unbiasedRandomSample(List<T> toSample, int sampleSize) {

		final List<T> back = new ArrayList<T>();

		log.debug("All-Unsampled (" + toSample.size() + ")");

		int countPos = 0;
		int countNeg = 0;
		// die Beispiele Auswaehlen
		final Set<Integer> selectedTrueIndexes = this
				.ziehenOhneZuruecklegen(sampleSize, toSample.size() - 1);
		final Iterator<Integer> iterTrue = selectedTrueIndexes.iterator();
		while (iterTrue.hasNext()) {
			final Integer aktIndex = iterTrue.next();
			final T akt = toSample.get(aktIndex.intValue());
			if (akt.isPositiveExample()) {
				countPos++;
			} else {
				countNeg++;
			}
			back.add(akt);
		}

		log.debug("All (" + back.size() + ") +:(" + countPos + ") -:(" + countNeg + ") ");

		return back;
	}

	/**
	 * Sch�ttelt/Mixt eine liste so, dass die elemente nachher voellig
	 * ungeordnet sind.
	 *
	 * @param toShuffle -
	 *                  liste die durcheinander geweurfelt werden soll
	 * @return das sample - die unordentliche liste
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public List<T> suffleList(List<T> toShuffle) {

		final List<T> back = new ArrayList<T>(toShuffle);
		for (int i = 0; i < back.size(); i++) {
			// herausfinden mit wem zufallig getauscht werden soll.
			int tauschPos = this.random.nextInt(back.size());
			T obj1 = back.get(i);
			T obj2 = back.get(tauschPos);
			// tauschen
			back.set(i, obj2);
			back.set(tauschPos, obj1);
		}

		return back;
	}

	private int getPerfectSampleSize(double percentNegative, int realCountTrue, int realCountFalse) {
		int total = realCountTrue + realCountFalse;

		boolean resolved = false;
		while (!resolved) {
			int negativeSize = (int) (total * percentNegative);
			int positiveSize = total - negativeSize;

			if (negativeSize < realCountFalse && positiveSize < realCountTrue) {
				resolved = true;
			} else {
				total -= 1;
			}
		}

		return total;
	}

	/**
	 * Zieht eine menge <code>wieOftZiehen</code> von integerwerten zwischen 0
	 * und <code>maxInt</code>.
	 *
	 * @param wieOftZiehen -
	 *                     wie viele int werte man am ende gerne haette
	 * @param maxInt       -
	 *                     die hoachte zahl die in der ergebissmenge sein wird.
	 * @return - eine Menge von Integerwerten (jeder integerwert kommt nur
	 * einmal vor)
	 * @author Daniel Wiese
	 * @since 22.08.2006
	 */
	private Set<Integer> ziehenOhneZuruecklegen(int wieOftZiehen, int maxInt) {
		if (wieOftZiehen > maxInt) {
			throw new RuntimeException("Es soll mehr gezogen Werden (ohne Zurucklegen) ("
					+ wieOftZiehen + ") als Werte (" + maxInt + ") vorhanden sind");
		}
		final Set<Integer> back = new HashSet<Integer>();

		while (back.size() < wieOftZiehen) {
			final Integer aktDraw = new Integer(random.nextInt(maxInt));
			if (!back.contains(aktDraw)) {
				back.add(aktDraw);
			}
		}

		return back;
	}


	public static class TestTrainData<T> {

		public final List<T> train;
		public final List<T> test;

		public TestTrainData(int size) {
			this.train = new ArrayList<T>();
			this.test = new ArrayList<T>();
		}

	}

}