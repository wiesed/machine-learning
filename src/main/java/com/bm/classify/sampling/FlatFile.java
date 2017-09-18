package com.bm.classify.sampling;

import com.bm.classify.sampling.filter.IFilter;
import com.bm.classify.sampling.filter.IIndexFilter;
import com.bm.classify.sampling.filter.IPkIdFilter;
import com.bm.common.util.BmUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Schreibt ein Objekt als CSF in eine Datei. Des objekt muss folgende
 * bedingungen erfuellen a) Es darf nur Eigenschaften vom folgenden typ haben
 * (Integer, Long, String) b) Keine referenzen zu anderen Objekten c)
 * Parameterlosen konstruktor
 *
 * @param <T> der typ des objektes was als cvs geschrieben werden soll.
 * @author Daniel Wiese
 * @version 1.0 / 12.05.2004
 */
public class FlatFile<T> {

	private static final String EMPTY_ATTRIB = "#";

	private static final Logger log = Logger.getLogger(FlatFile.class);

	private static final String SEPARATOR = ";";

	private final StringBuffer aktLine = new StringBuffer();

	private Class<T> classToCreate = null;

	private int dataCount = 0;

	private Field[] fieldsToReadWrite = null;

	private final File file;

	private boolean hasId = false;

	private boolean isWrite;

	private RandomAccessHelper rdAccessHelper = null;

	private BufferedWriter writer;

	/**
	 * Constructor.
	 *
	 * @param classToCrate -
	 *                     die klasse des typen der als csv geschreiben werden soll.
	 * @param isWrite      -
	 *                     true wenn schreibender zugriff erlaubt werden soll (sonst nur
	 *                     lesen), dann wird eine existente alte datei ersetzt.
	 */
	public FlatFile(
			final Class<T> classToCrate,
			final boolean isWrite) {
		this(classToCrate, isWrite, false, classToCrate.getName());
		for (final Class<?> interf : classToCrate.getInterfaces()) {
			if (interf.equals(SampleableWithId.class)) {
				this.hasId = true;
			}
		}
	}

	/**
	 * Constructor.
	 *
	 * @param classToCrate -
	 *                     die klasse des typen der als csv geschreiben werden soll.
	 * @param isWrite      -
	 *                     true wenn schreibender zugriff erlaubt werden soll (sonst nur
	 *                     lesen), dann wird eine
	 * @param isBackup     -
	 *                     true dann wird ein backup file geschrieben
	 */
	public FlatFile(
			final Class<T> classToCrate,
			final boolean isWrite,
			final boolean isBackup) {
		this(classToCrate, isWrite, isBackup, classToCrate.getName());

	}


	/**
	 * Constructor.
	 *
	 * @param classToCreate -
	 *                      die klasse des typen der als csv geschreiben werden soll.
	 * @param isWrite       -
	 *                      true wenn schreibender zugriff erlaubt werden soll (sonst nur
	 *                      lesen), dann wird eine
	 * @param isBackup      -
	 *                      true dann wird ein backup file geschrieben
	 * @param filename      der dateiname
	 */
	public FlatFile(
			final Class<T> classToCreate,
			final boolean isWrite,
			final boolean isBackup,
			final String filename) {
		this.classToCreate = classToCreate;
		this.fieldsToReadWrite = this.classToCreate.getDeclaredFields();
		this.createTempDirIfNotExists();

		this.file = (new File(BmUtil.getTempDirectory() + "/FlatFile/" + filename
				+ ((isBackup) ? ".bck.csv" : ".csv")));
		this.isWrite = isWrite;
		try {
			if (this.isWrite) {
				this.writer = new BufferedWriter(new FileWriter(this.file));
				try {
					this.writer.write(this.getHeaderLine());
					this.writer.newLine();
				} catch (final IOException e) {
					throw new RuntimeException("IO Exception while accessing the CSV");
				}
			}
		} catch (final FileNotFoundException e) {
			throw new RuntimeException("File does not exists");
		} catch (final IOException e) {
			throw new RuntimeException("IO Exception while accessing the CSV");
		}
	}

	/**
	 * Schliesst eine geoffnete csv datei.
	 *
	 * @author Daniel Wiese
	 * @since 15.08.2006
	 */
	public void closeFile() {
		try {
			if (this.writer != null) {
				this.writer.flush();
				this.writer.close();
			}
		} catch (final IOException e) {
			log.warn("File was already closed !");
		}
	}

	/**
	 * Filename.
	 *
	 * @return filename
	 */
	public String getFileName() {
		return this.file.toString();
	}

	/**
	 * Returns a id for a current line.
	 *
	 * @param currentLine -
	 *                    the current line
	 * @return - the id
	 * @author Daniel Wiese
	 * @since 27.08.2006
	 */
	public Long getId(final String currentLine) {
		Long back = null;
		T retobj = null;
		try {
			retobj = this.classToCreate.newInstance();
			final String[] values = this.parseAttributes(currentLine);
			if (retobj != null) {
				this.parseLineToObject(retobj, values);
				if (retobj instanceof SampleableWithId) {
					back = ((SampleableWithId) retobj).getID();
				}
			}
		} catch (final InstantiationException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return back;
	}

	/**
	 * Liefert die gesammtanzahl der zeilen wieder.
	 *
	 * @return die gesammtanzahl der zeilen
	 * @author Daniel Wiese
	 * @since 07.09.2006
	 */
	public int getLineCount() {
		if (this.rdAccessHelper == null) {
			log.info("Constructing Flat-File index ..");
			this.rdAccessHelper = new RandomAccessHelper(this.file, this);
			log.info(".. Index constructed!");
		}
		return this.rdAccessHelper.getLinesCount();
	}

	public int getWritedDataCount() {
		return this.dataCount;
	}

	/**
	 * Liefert true wann der sample file typ eine id spalte bessitzt.
	 *
	 * @return - true wann der sample file typ eine id spalte bessitzt.
	 * @author Daniel Wiese
	 * @since 27.08.2006
	 */
	public boolean hasId() {
		return this.hasId;
	}

	/**
	 * Liest eine menge von objekten aus dem flat file (in der reienfolge wie
	 * sie in der datei vorkommen und wendet den uebergebenen filter an. Damit
	 * werden nur definierte objekte gelesen.
	 *
	 * @param filter -
	 *               der aktuelle flat file filter
	 * @return - eine listw mit gelesenen objekten
	 * @author Daniel Wiese
	 * @since 15.08.2006
	 */
	public List<T> readFilteredLines(final IFilter<T> filter) {
		if (this.isWrite) {
			throw new RuntimeException("Can't read in a write mode");
		}

		// process reading objects
		final List<T> back = new ArrayList<T>();
		String aktLine = null;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(this.file));
			// check ty consistency
			aktLine = input.readLine();
			if ((aktLine != null) && !this.checkCVSHeaderLine(aktLine)) {
				throw new RuntimeException("The CSF ist not consitent with the Class");
			}

			// iterate through all other lines and create the corresponding
			// object
			int lineNumber = -1;
			while ((aktLine = input.readLine()) != null) {
				lineNumber++;
				final T retobj = this.classToCreate.newInstance();
				final String[] values = this.parseAttributes(aktLine);
				if (retobj != null) {
					this.parseLineToObject(retobj, values);
					if (filter.isValid(retobj, lineNumber)) {
						back.add(retobj);
					}
				}
			}
			input.close();
		} catch (final Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				log.error("Fatal error reading file", e);
				throw new RuntimeException("Fatal error reading file");
			}
		}
		return back;
	}

	/**
	 * Liest mit hilfe von random access nur bestimmte ausgewahlte zeilen. Die
	 * methode bringt erst beim zweiten aufruf geschindigkeitsvorteile.
	 *
	 * @param filter -
	 *               der filter
	 * @return die liste mit den gelesneen objekten.
	 */
	public List<T> readFilteredLinesWithRandomAccess(final IIndexFilter<T> filter) {
		if (this.isWrite) {
			throw new RuntimeException("Can�t read in a write mode");
		}

		if (this.rdAccessHelper == null) {
			log.info("Constructing Flat-File index ..");
			this.rdAccessHelper = new RandomAccessHelper(this.file, this);
			log.info(".. Index constructed!");
		}

		this.rdAccessHelper.prepareLinesToRead(filter.getIndexesToLoad());

		// process reading objects
		final List<T> back = new ArrayList<T>();
		this.performRandomAccessRead(filter, back);

		return back;
	}

	/**
	 * Liest mit hilfe von random access nur bestimmte ausgewahlte indexe. Die
	 * methode bringt erst beim zweiten aufruf geschindigkeitsvorteile.
	 *
	 * @param filter -
	 *               der filter
	 * @return die liste mit den gelesneen objekten.
	 */
	public List<T> readFilteredLinesWithRandomAccess(final IPkIdFilter<T> filter) {
		if (this.isWrite) {
			throw new RuntimeException("Can�t read in a write mode");
		}

		if (this.rdAccessHelper == null) {
			log.info("Constructing Flat-File index ..");
			this.rdAccessHelper = new RandomAccessHelper(this.file, this);
			log.info(".. Index constructed!");
		}

		this.rdAccessHelper.prepareIdsToRead(filter.getIdsToLoad());

		// process reading objects
		final List<T> back = new ArrayList<T>();
		this.performRandomAccessRead(filter, back);

		return back;
	}

	/**
	 * Loascht den internen counter (der protokolliert wieviele objekte diese
	 * instanz bereits als csv geschrieben hat).
	 *
	 * @author Daniel Wiese
	 * @since 15.08.2006
	 */
	public void resetCounter() {
		this.dataCount = -1;
	}

	/**
	 * Schaltet nach dem Schrieben in den Lesemodus.
	 *
	 * @author Daniel Wiese
	 * @since 15.08.2006
	 */
	public void switchToReadModeAfterWrite() {
		if (this.writer != null) {
			this.closeFile();
		}

		this.writer = null;
		this.isWrite = false;
	}

	/**
	 * Schreibt ein flat file objekt auf die platte (als CSV).
	 *
	 * @param toWrite -
	 *                das objekt, was auf die platte geschrieben werden soll.
	 * @author Daniel Wiese
	 * @since 14.08.2006
	 */
	public void writeObject(final T toWrite) {
		if (!this.isWrite) {
			throw new RuntimeException("Can�t write in a read mode");
		}

		// clear akt string buffer
		this.aktLine.delete(0, this.aktLine.length());
		for (int i = 0; i < this.fieldsToReadWrite.length; i++) {
			try {
				// setzt eventuell fehlende schreibrechte
				if (!this.fieldsToReadWrite[i].isAccessible()) {
					this.fieldsToReadWrite[i].setAccessible(true);
				}

				final Object value = this.fieldsToReadWrite[i].get(toWrite);
				// write string
				if ((value != null) && (value instanceof String)) {
					this.writeAttribute(this.aktLine, value);
				} else if ((value != null) && (value instanceof Integer)) {
					this.writeAttribute(this.aktLine, value);
				} else if ((value != null) && (value instanceof Double)) {
					this.writeAttribute(this.aktLine, value);
				} else if ((value != null) && (value instanceof Long)) {
					this.writeAttribute(this.aktLine, value);
				} else if ((value != null) && (value instanceof Boolean)) {
					this.writeAttribute(this.aktLine, value);
				} else {
					this.writeAttribute(this.aktLine, null);
				}
			} catch (final Exception e) {
				log.error("Konnte das Attribut (" + this.fieldsToReadWrite[i].getName()
						+ ") nicht lesen", e);
				this.writeAttribute(this.aktLine, null);
			}
			if (i + 1 < this.fieldsToReadWrite.length) {
				this.aktLine.append(SEPARATOR);
			}
		}
		try {
			this.writer.write(this.aktLine.toString());
			this.writer.newLine();
			this.dataCount++;
		} catch (final IOException e) {
			log.error("IO Exception while accessing the CSF", e);
			throw new RuntimeException("IO Exception while accessing the CSF");
		}
	}

	private boolean checkCVSHeaderLine(final String headerLine) {
		boolean back = true;
		final String[] attribsCVS = this.parseAttributes(headerLine);
		final String[] attribsClass = this.parseAttributes(this.getHeaderLine());
		if (attribsCVS.length != attribsClass.length) {
			return false;
		}
		for (int i = 0; i < attribsClass.length; i++) {
			back = back && attribsCVS[i].equalsIgnoreCase(attribsClass[i]);
		}
		return back;
	}

	private void createTempDirIfNotExists() {
		(new File(BmUtil.getTempDirectory() + "/FlatFile/")).mkdirs();
	}

	private String getHeaderLine() {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < this.fieldsToReadWrite.length; i++) {
			sb.append(this.fieldsToReadWrite[i].getName());
			if (i + 1 < this.fieldsToReadWrite.length) {
				sb.append(SEPARATOR);
			}
		}
		return sb.toString();
	}

	private String[] parseAttributes(final String toParse) {
		final String[] back = new String[this.fieldsToReadWrite.length];
		final StringTokenizer st = new StringTokenizer(toParse, SEPARATOR);
		int count = -1;
		while (st.hasMoreTokens()) {
			count++;
			final String aktParam = st.nextToken();
			if (aktParam.equalsIgnoreCase(EMPTY_ATTRIB)) {
				back[count] = null;
			} else {
				back[count] = aktParam;
			}
		}
		if ((count + 1) != back.length) {
			throw new RuntimeException("The attributes (" + toParse
					+ ") aof the class changed, The curren CSF-Version in not comatible");
		}
		return back;
	}

	/**
	 * Liest die attribute als string array in das uebergebene objekt ein.
	 *
	 * @param obj    -
	 *               ein leeres objekt
	 * @param values -
	 *               die values, die im objekt gesetzt werden soll.
	 * @throws IllegalAccessException -
	 *                                im fehlerfall
	 */
	private void parseLineToObject(final T obj, final String[] values)
			throws IllegalAccessException {
		for (int i = 0; i < this.fieldsToReadWrite.length; i++) {
			// setzt eventuell fehlende schreibrechte
			if (!this.fieldsToReadWrite[i].isAccessible()) {
				this.fieldsToReadWrite[i].setAccessible(true);
			}

			if (this.fieldsToReadWrite[i].getType()
										 .equals(String.class)) {
				// liest String
				this.fieldsToReadWrite[i].set(obj, values[i]);
			} else if (this.fieldsToReadWrite[i].getType()
												.equals(Integer.class)) {
				// liest Integer
				this.fieldsToReadWrite[i].set(obj, (values[i] != null) ? new Integer(values[i])
						: null);
			} else if (this.fieldsToReadWrite[i].getType()
												.equals(Long.class)) {
				// liest Long
				if (values[i] != null) {
					final String corrected = values[i].replace(",", ".");
					this.fieldsToReadWrite[i].set(obj, new Long(corrected));
				} else {
					this.fieldsToReadWrite[i].set(obj, null);
				}
			} else if (this.fieldsToReadWrite[i].getType()
												.equals(Double.class)) {
				final String temp = (values[i] != null) ? values[i].replace(',', '.') : "";
				this.fieldsToReadWrite[i].set(obj, (values[i] != null) ? new Double(temp) : null);
			} else if (this.fieldsToReadWrite[i].getType()
												.equals(Boolean.class)) {
				// liest Integer
				this.fieldsToReadWrite[i].set(obj, (values[i] != null) ? new Boolean(values[i])
						: null);
			}
		}
	}

	private void performRandomAccessRead(final IFilter<T> filter, final List<T> back) {
		if ((this.aktLine != null) && !this.checkCVSHeaderLine(this.rdAccessHelper.getHeaderLine())) {
			throw new RuntimeException("The CSF ist not consitent with the Class");
		}

		int linesReaded = -1;
		try {
			final List<String> rawlines = this.rdAccessHelper.readAllPreparedLines();
			for (final String rawLine : rawlines) {
				linesReaded++;
				final T retobj = this.classToCreate.newInstance();
				final String[] values = this.parseAttributes(rawLine);
				if (retobj != null) {
					this.parseLineToObject(retobj, values);
					if (filter.isValid(retobj, this.rdAccessHelper.getLineIndexNumber(linesReaded))) {
						back.add(retobj);
					}
				}
			}

		} catch (final Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				log.error("Fatal error reading file", e);
				throw new RuntimeException("Fatal error reading file");
			}
		}
	}

	private void writeAttribute(final StringBuffer toWrite, final Object attrib) {
		if (attrib != null) {
			if (attrib instanceof Double) {
				final String doublStr = attrib.toString();
				final String neuDoubl = doublStr.replace('.', ',');
				toWrite.append(neuDoubl);
			} else {
				toWrite.append(attrib);
			}
		} else {
			toWrite.append(EMPTY_ATTRIB);
		}
	}

	/**
	 * Speichert das generierte flat file als backup.
	 *
	 * @param pattern -
	 *                fuer welche klasse das flat file generiert wurde.
	 * @author Daniel Wiese
	 * @since 15.08.2006
	 */
	public static final void savePreviousFile(final Class<?> pattern) {
		final File toSave = new File(BmUtil.getTempDirectory() + pattern.getName() + ".csv");
		final File backupName = new File(BmUtil.getTempDirectory() + pattern.getName() + ".bck.csv");
		if (backupName.exists()) {
			backupName.delete();
		}
		if (toSave.exists()) {
			toSave.renameTo(backupName);
		}
	}
}
