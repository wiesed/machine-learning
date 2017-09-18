package com.bm.classify.textclassification;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.bm.common.enums.Label;
import com.bm.data.bo.ai.ITermFrequency;

/**
 * Repraesentiert einen Junit test um aus einem Text vectoren zu bauen.
 * 
 * @author Daniel Wiese
 * 
 */
public class Text2SVMVectorGeneratorTest extends TestCase {

	private Text2SVMVectorGenerator toTest;

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		toTest = new Text2SVMVectorGenerator(new Dictionary(
				getTermsAndFrequencies(), 30));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		toTest = null;
	}

	/**
	 * Junit test method.
	 */
	public void testVectorGeneration_vectorAlwasStartsWith_min_1() {
		toTest.addText("der aaa", Label.POSITIVE);
		assertEquals(1, toTest.getAllVectors().size());
		assertEquals("+1 1:1.0 ", toTest.getAllVectors().get(0).toSVMString());

		toTest.clear();
		toTest.addText("der bbb", Label.POSITIVE);
		assertEquals(1, toTest.getAllVectors().size());
		assertEquals("+1 2:1.0 ", toTest.getAllVectors().get(0).toSVMString());

		toTest.clear();
		toTest.addText("cc bbb zzzz", Label.NEGATIVE);
		assertEquals(1, toTest.getAllVectors().size());
		assertEquals(
				"-1 2:0.6487560173001553 3:0.6487560173001553 6:0.39778293079728944 ",
				toTest.getAllVectors().get(0).toSVMString());

		toTest.clear();
		toTest.addText("aaa cc bbb zzzz", Label.POSITIVE);
		assertEquals(1, toTest.getAllVectors().size());
		assertEquals(
				"+1 1:0.6403571844346162 2:0.49829474375994603 3:0.49829474375994603 6:0.30552802330619433 ",
				toTest.getAllVectors().get(0).toSVMString());
	}

	private List<ITermFrequency> getTermsAndFrequencies() {
		final List<ITermFrequency> back = new ArrayList<ITermFrequency>();
		back.add(new TermFrequencyImpl("aaa", 3));
		back.add(new TermFrequencyImpl("bbb", 5));
		back.add(new TermFrequencyImpl("cc", 5));
		back.add(new TermFrequencyImpl("dd", 5));
		back.add(new TermFrequencyImpl("ee", 2));
		back.add(new TermFrequencyImpl("zzzz", 10));
		return back;
	}

}
