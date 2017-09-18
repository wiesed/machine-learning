package com.bm.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.bm.common.DefConstants;

/**
 * Utility Methoden fuer das Trader-Paket.
 * 
 * @author fbauschulte
 */
public final class BmUtil {

	private static OsTypes type = OsTypes.OSTYPE_UNKNOWN;
	private static DecimalFormat decFormat = new DecimalFormat("000.00");
	private static DecimalFormat intFormat = new DecimalFormat("000000");

	private BmUtil() {

	}

	/**
	 * Ermittelt anhand des Trennzeichens das Betriebssystem.
	 * 
	 * @return true, wenn das System nicht "/" als File-Trenner nutzt (=
	 *         Windows).
	 */
	public static boolean isWindows() {
		try {
			if (System.getProperty("file.separator").equals("/")) {
				return false;
			}

		} catch (Exception ex) {
		}

		return true;
	}

	/**
	 * Returns the temp directory, depending on the current operating system.
	 * 
	 * @author Daniel Wiese
	 * @since 03.06.2006
	 * @return the temp directory, depending on the current operating system
	 */
	public static String getTempDirectory() {
		final OsTypes opSystem = getOs();
		if (opSystem == OsTypes.OSTYPE_WINDOWS
				|| opSystem == OsTypes.OSTYPE_WINNT) {
			return DefConstants.TEMP_WINDOWS;
		} else if (opSystem == OsTypes.OSTYPE_LINUX) {
			return DefConstants.TEMP_LINUX;
		}

		// default
		return DefConstants.TEMP_LINUX;
	}

	/**
	 * Determines the OS.
	 * 
	 * @return an integer identifying the OS (one of the OSTYPE constants)
	 */
	public static OsTypes getOs() {
		if (type == OsTypes.OSTYPE_UNKNOWN) {

			final String osname = System.getProperty("os.name").toLowerCase();

			if (osname.indexOf("windows") != -1) {
				if (osname.indexOf("nt") != -1 || osname.indexOf("2000") != -1
						|| osname.indexOf("xp") != -1) {
					type = OsTypes.OSTYPE_WINNT;
				} else if (osname.indexOf("ce") != -1) {
					type = OsTypes.OSTYPE_WINCE;
				} else {
					type = OsTypes.OSTYPE_WINDOWS;
				}
			} else if (osname.indexOf("linux") != -1
					|| osname.indexOf("bsd") != -1) {
				type = OsTypes.OSTYPE_LINUX;
			} else if (osname.indexOf("mac os") != -1
					|| osname.indexOf("macos") != -1) {
				type = OsTypes.OSTYPE_MAC;
			} else if (osname.indexOf("solaris") != -1) {
				type = OsTypes.OSTYPE_SOLARIS; // could also be old freebsd
				// version
			} else if (osname.indexOf("netware") != -1) {
				type = OsTypes.OSTYPE_NETWARE;
			} else if (osname.indexOf("os/2") != -1) {
				type = OsTypes.OSTYPE_OS2;
			} else {
				type = OsTypes.OSTYPE_UNKNOWN;
			}
		}

		return type;
	}

	/**
	 * Desrialisiert eine liste.
	 * 
	 * @param <T>
	 *            der typ der listenelemente
	 * @param file
	 *            die dateiname
	 * @return die deserialisierte liste.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> deserializeList(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(in);
			T[] klassifkationen = (T[]) s.readObject();
			return Arrays.asList(klassifkationen);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(
					"Kann Objekt nicht deserialisieren> Datei ("
							+ file.getName() + ")", e);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Kann Objekt nicht deserialisieren> Datei ("
							+ file.getName() + ")", e);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(
					"Kann Objekt nicht deserialisieren> Datei ("
							+ file.getName() + ")", e);
		}
	}

	/**
	 * Serialisiert eine liste.
	 * 
	 * @param type
	 *            - der type (klasse) der listenelemente *
	 * @param toPersistList
	 *            die zu speichernde liste
	 * @param file
	 *            die datei
	 * @param <T>
	 *            der typ der listenelemente
	 */
	@SuppressWarnings("unchecked")
	public static <T> void serializeList(Class<T> type, List<T> toPersistList,
			File file) {
		try {
			T[] arrayToFill = (T[]) Array.newInstance(type, toPersistList
					.size());
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream s = new ObjectOutputStream(out);
			T[] toPersist = toPersistList.toArray(arrayToFill);
			s.writeObject(toPersist);
			s.flush();
		} catch (IOException e) {
			throw new RuntimeException("Error writing list to the file system",
					e);
		}

	}

	/**
	 * Schreibt ein objkt in eine datei.
	 * 
	 * @param <T>
	 *            der zu speichernde objekt typ
	 * @param toSave
	 *            zu speichernde objekt
	 * @param fileName
	 *            der datei name
	 */
	public static <T> void saveToFile(final T toSave, final File fileName) {
		try {
			final ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fileName, false));
			oos.writeObject(toSave);
			oos.close();

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Liest ein objekt aus der Datei.
	 * 
	 * @param <T>
	 *            der zu lesende objekt typ
	 * @param filetemp
	 *            die datei.
	 * @return das deserilisierte objekt
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadFromFile(final File filetemp) {
		try {
			final ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(filetemp));
			final T pool = (T) ois.readObject();
			ois.close();
			return pool;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Retrun a short class name. E.g. java.util.StringTokenizer will be
	 * StringTokenizer
	 * 
	 * @param longClassName
	 *            - the long fully qualified calss name
	 * @return - short class name
	 */
	public static String getShortClassName(String longClassName) {
		final StringTokenizer tk = new StringTokenizer(longClassName, ".");
		String last = longClassName;
		while (tk.hasMoreTokens()) {
			last = tk.nextToken();
		}

		return last;
	}

	/**
	 * Return a short class name. E.g. java.util.StringTokenizer will be
	 * StringTokenizer
	 * 
	 * @param clazz
	 *            - for class
	 * @return - short class name
	 */
	public static String getShortClassName(Class<?> clazz) {
		return getShortClassName(clazz.getName());
	}

	/**
	 * Formattiert eine liste so , dass sie in einer tabelle eingestet werden
	 * kann. Eine tabelle wird dann durch einen mehrfachen aufruf erzeugt:
	 * 
	 * <pre>
	 *   Row1 |  12.33 |   1.32 | 223.32 |   1.32 |    .21 | 
	 *   Row2 |      1 |     34 |    322 |   4555 |      2 |
	 * </pre>
	 * 
	 * @param rowName
	 *            der name der spalte
	 * @param toFormat
	 *             die liste die formattiert werden soll
	 * @return der formattierte string
	 */
	public static String formatListAsTable(String rowName,
			List<? extends Number> toFormat) {
		final StringBuilder sb = new StringBuilder();
		sb.append(rowName);
		sb.append(" | ");
		for (Number number : toFormat) {
			if (number instanceof Integer || number instanceof Long) {
				sb.append(align(intFormat.format(number))).append(" | ");
			} else {
				sb.append(align(decFormat.format(number))).append(" | ");
			}
		}
		return sb.toString();
	}

	/**
	 * Entfernt fuerhrende nullen von einer formattierten Zahl.
	 * 
	 * @param s
	 *            der eingabe string
	 * @return die ausgabe
	 */
	public static String align(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		int i = 0;

		while (i < s.length() && s.charAt(i) == '0') {

			i++;
		}
		// Sonderfall
		if (i == s.length()) {
			i--;
		}

		return s.substring(0, i + 1).replace('0', ' ')
				+ ((i + 1 == s.length()) ? "" : s.substring(i + 1));

	}

}
