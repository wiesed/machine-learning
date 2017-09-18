package com.bm.classify.sampling;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Junit test fuer den Sampler.
 * 
 * @author Daniel Wiese
 * @since 18.08.2006
 */
public class SamplerTest extends TestCase {

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public void testInMemorySample_bised_50_50() {
		List<Sampleable> all = this.generateNObjects(20, true);
		all.addAll(this.generateNObjects(100, false));
		all.addAll(this.generateNObjects(20, true));

		final Sampler<Sampleable> toTest = new Sampler<Sampleable>();
		List<Sampleable> back = toTest.biasedRandomSample(all, 40);
		assertEquals(back.size(), 40);
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public void testInMemorySample_bised_50_50_nichtGenugBsp() {
		//wir haben 40 pos und 15 neg
		List<Sampleable> all = this.generateNObjects(20, true);
		all.addAll(this.generateNObjects(15, false));
		all.addAll(this.generateNObjects(20, true));

		final Sampler<Sampleable> toTest = new Sampler<Sampleable>();
		List<Sampleable> back = toTest.biasedRandomSample(all, 50);
		assertEquals(28, back.size());
		int posCount = 0;
		int negCount = 0;
		for (Sampleable sampleable : back) {
			if (sampleable.isPositiveExample()) {
				posCount++;
			} else {
				negCount++;
			}
		}

		assertEquals(14, posCount);
		assertEquals(14, negCount);
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public void testShuffle() {
		List<Sampleable> all = this.generateNObjects(10, true);
		final Sampler<Sampleable> toTest = new Sampler<Sampleable>();
		List<Sampleable> back = toTest.suffleList(all);
		assertEquals(back.size(), 10);
		for (int i = 0; i < all.size(); i++) {
			Sampleable isInside = all.get(i);
			assertTrue(back.contains(isInside));
		}

		// min 5 solten adere pos. haben
		int notEqualCount = 0;
		for (int i = 0; i < all.size(); i++) {
			Sampleable a = all.get(i);
			Sampleable b = back.get(i);
			if (!a.equals(b)) {
				notEqualCount++;
			}
		}

		assertTrue(notEqualCount >= 5);

	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 18.08.2006
	 */
	public void testInMemorySample_bised_70N_30P() {
		List<Sampleable> all = this.generateNObjects(20, true);
		all.addAll(this.generateNObjects(100, false));
		all.addAll(this.generateNObjects(200, true));

		final Sampler<Sampleable> toTest = new Sampler<Sampleable>();
		List<Sampleable> back = toTest.contolledRandomSample(all, 100, 0.7);
		assertEquals(back.size(), 100);
		assertEquals(getAnzahlPosNeg(back, true), 30);
		assertEquals(getAnzahlPosNeg(back, false), 70);
	}

	private List<Sampleable> generateNObjects(int n, boolean isPositiveExample) {
		final List<Sampleable> back = new ArrayList<Sampleable>();
		for (int i = 0; i < n; i++) {
			back.add(new SampleableImpl(i, System.currentTimeMillis(),
					isPositiveExample, true));
		}

		return back;
	}

	private int getAnzahlPosNeg(List<Sampleable> toCheck, boolean isPos) {
		int back = 0;
		for (Sampleable current : toCheck) {
			if (current.isPositiveExample() == isPos) {
				back++;
			}
		}

		return back;
	}

}
