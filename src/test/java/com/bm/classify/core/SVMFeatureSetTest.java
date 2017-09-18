package com.bm.classify.core;

import com.bm.common.enums.Label;

import junit.framework.TestCase;

/**
 * Junit test fuer den SVM vektor bauer.
 * 
 * @author Daniel Wiese
 * @since 03.06.2006
 */
public class SVMFeatureSetTest extends TestCase {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SVMFeatureSetTest.class);

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_negative_appendLast() {
		SVMFeatureSet toTest = new SVMFeatureSet(Label.NEGATIVE);
		toTest.appendFeature(0.5);
		assertEquals(toTest.toSVMString(), "-1 1:0.5 ");

		toTest.appendFeature(0.7);
		assertEquals(toTest.toSVMString(), "-1 1:0.5 2:0.7 ");

		toTest.appendFeature(0.8);
		assertEquals(toTest.toSVMString(), "-1 1:0.5 2:0.7 3:0.8 ");
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_positive_appendLast() {
		SVMFeatureSet toTest = new SVMFeatureSet(Label.POSITIVE);
		toTest.appendFeature(0.5);
		assertEquals(toTest.toSVMString(), "+1 1:0.5 ");

		toTest.appendFeature(0.7);
		assertEquals(toTest.toSVMString(), "+1 1:0.5 2:0.7 ");

		toTest.appendFeature(0.8);
		assertEquals(toTest.toSVMString(), "+1 1:0.5 2:0.7 3:0.8 ");
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_neutral_appendLast() {
		SVMFeatureSet toTest = new SVMFeatureSet(Label.UNLABLED);
		toTest.appendFeature(0.5);
		assertEquals(toTest.toSVMString(), "0 1:0.5 ");
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_positive_addFeatureWithPosition() {
		SVMFeatureSet toTest = new SVMFeatureSet(Label.POSITIVE);

		toTest.appendFeature(0.9, 10);
		toTest.appendFeature(0.95, 2);
		toTest.appendFeature(0.97, 5);
		assertEquals(toTest.toSVMString(), "+1 2:0.95 5:0.97 10:0.9 ");

		toTest.appendFeature(0.5);
		assertEquals(toTest.toSVMString(), "+1 2:0.95 5:0.97 10:0.9 11:0.5 ");

		toTest.appendFeature(0.7);
		assertEquals(toTest.toSVMString(),
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 ");
		// ein vorhandenes ersetzen
		toTest.appendFeature(0.88, 12);
		assertEquals(toTest.toSVMString(),
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.88 ");

		toTest.appendFeature(0.7);
		assertEquals(toTest.toSVMString(),
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.88 13:0.7 ");
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_positive_addVector() {
		SVMFeatureSet toTest = new SVMFeatureSet(Label.POSITIVE);

		toTest.appendFeature(0.9, 10);
		toTest.appendFeature(0.95, 2);
		toTest.appendFeature(0.97, 5);
		assertEquals(toTest.toSVMString(), "+1 2:0.95 5:0.97 10:0.9 ");

		toTest.appendFeature(0.5);
		assertEquals(toTest.toSVMString(), "+1 2:0.95 5:0.97 10:0.9 11:0.5 ");

		SVMFeatureSet toAppend = new SVMFeatureSet(Label.POSITIVE);
		toAppend.appendFeature(0.7);
		toAppend.appendFeature(0.8);

		toTest.appendFeature(toAppend);
		assertEquals(toTest.toSVMString(),
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ");
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_positive_addZeroPosition() {
		SVMFeatureSet toTest = new SVMFeatureSet(Label.POSITIVE);
		try {
			toTest.appendFeature(0.9, 0);
			fail("Excepion expected, posion was 0");
		} catch (RuntimeException expected) {
			log.debug("Expected exception");
		}

		try {
			toTest.appendFeature(0.9, -1);
			fail("Excepion expected, posion was -1");
		} catch (RuntimeException expected) {
			log.debug("Expected exception");
		}

		toTest.appendFeature(0.95, 2);
		toTest.appendFeature(0.97, 5);
		assertEquals(toTest.toSVMString(), "+1 2:0.95 5:0.97 ");

		toTest.appendFeature(0.5);
		assertEquals(toTest.toSVMString(), "+1 2:0.95 5:0.97 6:0.5 ");
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_parseFromString_LabelPositive() {
		SVMFeatureSet toTest = new SVMFeatureSet(
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ", true);
		assertEquals(toTest.toSVMString(),
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ");
		assertEquals(Label.POSITIVE, toTest.getLabel());
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_parseFromString_LabelNegative() {
		SVMFeatureSet toTest = new SVMFeatureSet(
				"-1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ", true);
		assertEquals(toTest.toSVMString(),
				"-1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ");
		assertEquals(Label.NEGATIVE, toTest.getLabel());
	}

	/**
	 * Junit test method.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 */
	public void testBuildVector_parseFromString_NoLabel() {
		SVMFeatureSet toTest = new SVMFeatureSet(
				"2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ", false);
		assertEquals(toTest.toSVMString(),
				"0 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ");
		assertEquals(Label.UNLABLED, toTest.getLabel());

		toTest.setLabel(Label.NEGATIVE);
		assertEquals(toTest.toSVMString(),
				"-1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ");

		toTest.setLabel(Label.POSITIVE);
		assertEquals(toTest.toSVMString(),
				"+1 2:0.95 5:0.97 10:0.9 11:0.5 12:0.7 13:0.8 ");
	}

}
