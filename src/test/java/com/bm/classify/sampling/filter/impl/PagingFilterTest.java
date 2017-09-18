package com.bm.classify.sampling.filter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import com.bm.classify.sampling.FlatFile;
import com.bm.classify.sampling.FlatFileExample;

/**
 * Junit test for paging.
 * @author Daniel Wiese
 *
 */
public class PagingFilterTest extends TestCase {

	private final Random rd = new Random(System.currentTimeMillis());

	/**
	 * Junit test.
	 * 
	 * @author Daniel Wiese
	 * @since 15.08.2006
	 */
	public void testPagingFilter_pageSize1000_objects3735() {
		final FlatFile<FlatFileExample> toTest = new FlatFile<FlatFileExample>(
				FlatFileExample.class, true);
		List<FlatFileExample> toWrite = this.generateNObjects(3735);
		for (FlatFileExample current : toWrite) {
			toTest.writeObject(current);
		}

		toTest.switchToReadModeAfterWrite();
		assertEquals(toTest.getWritedDataCount(), 3735);

		final PagingFilter<FlatFileExample> filterToTest = new PagingFilter<FlatFileExample>(
				toTest.getLineCount(), 1000);
		// first page
		assertTrue(filterToTest.hasNextPage());
		List<FlatFileExample> readed = toTest
				.readFilteredLinesWithRandomAccess(filterToTest);

		testIfAllExpectedObjectsWereReaded(0, 1000, toWrite, readed);
		// second page
		filterToTest.nextPage();
		assertTrue(filterToTest.hasNextPage());
		readed = toTest.readFilteredLinesWithRandomAccess(filterToTest);
		testIfAllExpectedObjectsWereReaded(1000, 2000, toWrite, readed);

		// third page
		filterToTest.nextPage();
		assertTrue(filterToTest.hasNextPage());
		readed = toTest.readFilteredLinesWithRandomAccess(filterToTest);
		testIfAllExpectedObjectsWereReaded(2000, 3000, toWrite, readed);

		// forth page
		filterToTest.nextPage();
		assertFalse(filterToTest.hasNextPage());
		readed = toTest.readFilteredLinesWithRandomAccess(filterToTest);
		testIfAllExpectedObjectsWereReaded(3000, 3735, toWrite, readed);

	}

	private void testIfAllExpectedObjectsWereReaded(int start, int end,
			List<FlatFileExample> all, List<FlatFileExample> readed) {
		int counter = 0;
		for (int i = start; i < end; i++) {
			assertEquals(all.get(i), readed.get(counter));
			counter++;
		}
	}

	private List<FlatFileExample> generateNObjects(int n) {
		List<FlatFileExample> back = new ArrayList<FlatFileExample>();
		for (int i = 0; i < n; i++) {
			final FlatFileExample toAdd = new FlatFileExample();
			toAdd.setName("Objekt nr. - " + i);
			if (rd.nextBoolean()) {
				toAdd.setParam1(rd.nextLong());
			}
			if (rd.nextBoolean()) {
				toAdd.setParam2(rd.nextInt());
			}
			if (rd.nextBoolean()) {
				toAdd.setParam3(rd.nextDouble());
			}
			if (rd.nextBoolean()) {
				toAdd.setParam4(rd.nextBoolean());
			}
			back.add(toAdd);
		}

		return back;
	}

}
