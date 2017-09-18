package com.bm.classify.sampling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.bm.classify.sampling.filter.impl.NoFilter;
import com.bm.classify.sampling.filter.impl.SelectedIdsFilter;
import com.bm.classify.sampling.filter.impl.SelectedIndexFilter;

import junit.framework.TestCase;

/**
 * Testet das flat fle schrieben.
 * 
 * @author Daniel Wiese
 * @since 15.08.2006
 */
public class FlatFileTest extends TestCase {

    private final Random rd = new Random(System.currentTimeMillis());

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 15.08.2006
     */
    public void testWriteReadFlatFile_happyPath() {
        final FlatFile<FlatFileExample> toTest = new FlatFile<FlatFileExample>(
                FlatFileExample.class, true);
        List<FlatFileExample> toWrite = this.generateNObjects(20);
        for (FlatFileExample current : toWrite) {
            toTest.writeObject(current);
        }

        toTest.switchToReadModeAfterWrite();
        assertEquals(toTest.getWritedDataCount(), 20);

        final List<FlatFileExample> readed = toTest
                .readFilteredLines(new NoFilter<FlatFileExample>());
        assertEquals(readed.size(), 20);
        assertEquals(readed, toWrite);

    }

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 15.08.2006
     */
    public void testWriteReadFlatFile_filterIndexes() {
        final FlatFile<FlatFileExample> toTest = new FlatFile<FlatFileExample>(
                FlatFileExample.class, true);
        List<FlatFileExample> toWrite = this.generateNObjects(20);
        for (FlatFileExample current : toWrite) {
            toTest.writeObject(current);
        }

        toTest.switchToReadModeAfterWrite();
        assertEquals(toTest.getLineCount(), 20);
        assertEquals(toTest.getWritedDataCount(), 20);

        final Set<Integer> selected = new HashSet<Integer>();
        selected.add(1);
        selected.add(3);
        selected.add(5);
        selected.add(8);
        selected.add(10);
        selected.add(15);
        List<FlatFileExample> readed = toTest
                .readFilteredLines(new SelectedIndexFilter<FlatFileExample>(selected));
        assertEquals(readed.size(), 6);
        assertEquals(readed.get(0), toWrite.get(1));
        assertEquals(readed.get(1), toWrite.get(3));
        assertEquals(readed.get(2), toWrite.get(5));
        assertEquals(readed.get(3), toWrite.get(8));
        assertEquals(readed.get(4), toWrite.get(10));
        assertEquals(readed.get(5), toWrite.get(15));

        // and read it again
        readed = toTest.readFilteredLines(new SelectedIndexFilter<FlatFileExample>(selected));
        assertEquals(readed.size(), 6);
        assertEquals(readed.get(0), toWrite.get(1));
        assertEquals(readed.get(1), toWrite.get(3));
        assertEquals(readed.get(2), toWrite.get(5));
        assertEquals(readed.get(3), toWrite.get(8));
        assertEquals(readed.get(4), toWrite.get(10));
        assertEquals(readed.get(5), toWrite.get(15));

    }

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 15.08.2006
     */
    public void testWriteReadFlatFile_rdAccess_filterIndexes() {
        final FlatFile<FlatFileExample> toTest = new FlatFile<FlatFileExample>(
                FlatFileExample.class, true);
        List<FlatFileExample> toWrite = this.generateNObjects(20000);
        for (FlatFileExample current : toWrite) {
            toTest.writeObject(current);
        }

        toTest.switchToReadModeAfterWrite();
        assertEquals(toTest.getWritedDataCount(), 20000);

        final Set<Integer> selected = new HashSet<Integer>();
        selected.add(1);
        selected.add(3);
        selected.add(10);
        selected.add(20);
        selected.add(5655);
        selected.add(8987);
        selected.add(10000);
        selected.add(15000);
        final List<FlatFileExample> readed = toTest
                .readFilteredLinesWithRandomAccess(new SelectedIndexFilter<FlatFileExample>(
                        selected));
        assertEquals(readed.size(), 8);
        assertEquals(readed.get(0), toWrite.get(1));
        assertEquals(readed.get(1), toWrite.get(3));
        assertEquals(readed.get(2), toWrite.get(10));
        assertEquals(readed.get(3), toWrite.get(20));
        assertEquals(readed.get(4), toWrite.get(5655));
        assertEquals(readed.get(5), toWrite.get(8987));
        assertEquals(readed.get(6), toWrite.get(10000));
        assertEquals(readed.get(7), toWrite.get(15000));

    }

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 15.08.2006
     */
    public void testWriteReadFlatFile_rdAccess_withID() {
        final FlatFile<FlatFileExampleWithID> toTest = new FlatFile<FlatFileExampleWithID>(
                FlatFileExampleWithID.class, true);
        List<FlatFileExampleWithID> toWrite = this.generateNObjectsWithID(5000);
        for (FlatFileExampleWithID current : toWrite) {
            toTest.writeObject(current);
        }

        toTest.switchToReadModeAfterWrite();
        assertEquals(toTest.getWritedDataCount(), 5000);

        final Set<Long> selected = new HashSet<Long>();
        selected.add(100L);
        selected.add(103L);
        selected.add(160L);
        selected.add(720L);
        selected.add(3155L);
        final List<FlatFileExampleWithID> readed = toTest
                .readFilteredLinesWithRandomAccess(new SelectedIdsFilter<FlatFileExampleWithID>(
                        selected));
        assertEquals(readed.size(), 5);
        assertEquals(readed.get(0).getID(), Long.valueOf(100));
        assertEquals(readed.get(1).getID(), Long.valueOf(103));
        assertEquals(readed.get(2).getID(), Long.valueOf(160));
        assertEquals(readed.get(3).getID(), Long.valueOf(720));
        assertEquals(readed.get(4).getID(), Long.valueOf(3155));

    }

    /**
     * Junit test.
     * 
     * @author Daniel Wiese
     * @since 15.08.2006
     */
    public void testWriteReadFlatFile_closeTwice() {
        final FlatFile<FlatFileExample> toTest = new FlatFile<FlatFileExample>(
                FlatFileExample.class, true);
        List<FlatFileExample> toWrite = this.generateNObjects(20);
        for (FlatFileExample current : toWrite) {
            toTest.writeObject(current);
        }

        toTest.closeFile();
        toTest.switchToReadModeAfterWrite();
        assertEquals(toTest.getWritedDataCount(), 20);

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

    private List<FlatFileExampleWithID> generateNObjectsWithID(int n) {
        List<FlatFileExampleWithID> back = new ArrayList<FlatFileExampleWithID>();
        for (int i = 0; i < n; i++) {
            final FlatFileExampleWithID toAdd = new FlatFileExampleWithID();
            toAdd.setId(Long.valueOf(100 + i));
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
