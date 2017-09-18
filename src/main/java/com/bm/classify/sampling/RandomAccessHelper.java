package com.bm.classify.sampling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * Repraesentiert eine kalsse die einen direkten zugriff auf bestimmte zeilen
 * des files ermoeglicht.
 *
 * @author Daniel Wiese
 */
public class RandomAccessHelper {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(RandomAccessHelper.class);

	private Map<Integer, Long> indexByteArrayOffsetMap = new HashMap<Integer, Long>();

	private Map<Integer, Long> insideByteArrayOffsetMap = new HashMap<Integer, Long>();

	private Map<Long, Integer> idIndexMap = new HashMap<Long, Integer>();

	private String headerLine;

	private final File referenceFile;

	private List<Integer> linesToRead;

	private final FlatFile<?> flatFile;

	private int counter;

	private int totalLinesCount;

	/**
	 * Consructor.
	 *
	 * @param referenceFile -
	 *                      das file auf das spaeter durch random access zugegriffen
	 *                      werden soll.
	 * @param flatFile      -
	 *                      das flat file
	 */
	public RandomAccessHelper(
			File referenceFile,
			FlatFile<?> flatFile) {
		this.referenceFile = referenceFile;
		this.flatFile = flatFile;
		this.buildIndex(referenceFile);
	}

	private void buildIndex(File file) {
		try {
			final FileInputStream fis = new FileInputStream(file);
			final FileChannel fc = fis.getChannel();
			final ByteBuffer buf = ByteBuffer.allocateDirect(4096);
			long outerLoop = 0;
			long innerLoop = 0;
			int currentLine = 0;
			final StringBuilder firstLine = new StringBuilder();
			final StringBuilder currentLineString = new StringBuilder();
			boolean isNextLineStarting = false;
			while (fc.read(buf) != -1) {
				outerLoop++;
				buf.flip();
				innerLoop = 0;
				while (buf.position() < buf.limit()) {
					innerLoop++;
					final char c = (char) buf.get();
					if (c == '\n') {
						// wenn der flat file eine id hat
						if (currentLine != 0 && this.flatFile.hasId()) {
							final Long id = this.flatFile.getId(currentLineString.toString());
							this.idIndexMap.put(id, Integer.valueOf(currentLine - 1));
							currentLineString.delete(0, currentLineString.length());
						}
						currentLine++;
						isNextLineStarting = true;
					} else {
						if (currentLine == 0) {
							if (c != '\r') {
								firstLine.append(c);
							}
						} else if (isNextLineStarting) {
							isNextLineStarting = false;
							if (this.flatFile.hasId()) {
								currentLineString.append(c);
							}
							this.indexByteArrayOffsetMap.put(Integer.valueOf(currentLine), Long
									.valueOf(outerLoop));
							this.insideByteArrayOffsetMap.put(Integer.valueOf(currentLine), Long
									.valueOf(innerLoop));
						} else if (this.flatFile.hasId()) {
							currentLineString.append(c);
						}
					}
				}
				buf.clear();
			}

			fc.close(); // Done with the channel, so close it
			this.totalLinesCount = currentLine - 1;
			this.headerLine = firstLine.toString();
		} catch (FileNotFoundException e) {
			log.info("Cant build index -file offset", e);
			throw new RuntimeException("Cant build index -file offset", e);
		} catch (IOException e) {
			log.info("Cant build index -file offset", e);
			throw new RuntimeException("Cant build index -file offset", e);

		}
	}

	/**
	 * Liefert die gesammtanzahl der Zeilen zurueck.
	 *
	 * @return gesammtanzahl der Zeilen
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 */
	public int getLinesCount() {
		return this.totalLinesCount;
	}

	/**
	 * Sagt welche zeilen als naechstes gelesen werden sollen.
	 *
	 * @param lines -
	 *              die zeilen die gelesen werden sollen
	 */
	public void prepareLinesToRead(Set<Integer> lines) {
		this.linesToRead = new ArrayList<Integer>(lines);
		Collections.sort(this.linesToRead);
		this.counter = 0;

	}

	/**
	 * Sagt welche id's als naechstes gelesen werden sollen. Diese werden in
	 * zeilen umgewandelt.
	 *
	 * @param ids -
	 *            die zeilen die gelesen werden sollen
	 */
	public void prepareIdsToRead(Set<Long> ids) {
		this.linesToRead = new ArrayList<Integer>();
		for (Long id : ids) {
			if (this.idIndexMap.containsKey(id)) {
				this.linesToRead.add(this.idIndexMap.get(id));
			}
		}
		Collections.sort(this.linesToRead);
		this.counter = 0;

	}

	/**
	 * Reads the next line (of the specified indexes).
	 *
	 * @return - the next line
	 */
	public List<String> readAllPreparedLines() {
		final List<String> back = new ArrayList<String>();
		try {
			final FileInputStream fis = new FileInputStream(this.referenceFile);
			final FileChannel fc = fis.getChannel();
			final ByteBuffer buf = ByteBuffer.allocateDirect(4096);
			long outerLoop = 0;
			long innerLoop = 0;
			int linesReaded = 0;
			StringBuilder currentLineReaded = new StringBuilder();
			boolean isReadingMode = false;
			while (fc.read(buf) != -1) {
				if (this.terminate(linesReaded)) {
					break;
				}
				outerLoop++;
				buf.flip();
				if (isReadingMode || outerLoop == getNextValidOuterIndex(linesReaded)) {
					innerLoop = 0;
					while (buf.position() < buf.limit()) {
						if (this.terminate(linesReaded)) {
							break;
						}
						innerLoop++;
						final char c = (char) buf.get();
						if (isReadingMode
								|| (innerLoop == getNextValidInnerIndex(linesReaded) && outerLoop == getNextValidOuterIndex(linesReaded))) {

							if (c == '\n') {
								back.add(currentLineReaded.toString());
								currentLineReaded.delete(0, currentLineReaded.length());
								linesReaded++;
								isReadingMode = false;
							} else if (c != '\r') {
								isReadingMode = true;
								currentLineReaded.append(c);
							}
						}
					}

				}
				buf.clear();
			}

			fc.close(); // Done with the channel, so close it
			return back;
		} catch (FileNotFoundException e) {
			log.info("Cant read line (" + counter + ")", e);
			throw new RuntimeException("Cant build index -file offset", e);
		} catch (IOException e) {
			log.info("Cant read line (" + counter + ")", e);
			throw new RuntimeException("Cant build index -file offset", e);
		}

	}

	private long getNextValidOuterIndex(int linesReaded) {
		// die indexe sind null basierend
		return this.indexByteArrayOffsetMap.get(linesToRead.get(linesReaded) + 1);
	}

	private boolean terminate(int linesReaded) {
		return linesReaded >= linesToRead.size();
	}

	private long getNextValidInnerIndex(int linesReaded) {
		// die indexe sind null basierend
		return this.insideByteArrayOffsetMap.get(linesToRead.get(linesReaded) + 1);
	}

	/**
	 * Returns the headerLine.
	 *
	 * @return Returns the headerLine.
	 */
	public String getHeaderLine() {
		return headerLine;
	}

	/**
	 * Liefert die absolute index position im Flat file.
	 *
	 * @param relPos -
	 *               die relative position (zu lines to read)
	 * @return - die absolute position
	 * @author Daniel Wiese
	 * @since 26.08.2006
	 */
	public int getLineIndexNumber(int relPos) {
		return this.linesToRead.get(relPos);
	}

}
