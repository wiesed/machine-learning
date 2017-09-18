package com.bm.common.util;

import static com.bm.common.util.BmUtil.getOs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Klasse zipped und unzipped Dateien.
 * 
 * @author Fabian Bauschulte
 * @since 08.01.2006
 */
public final class ZipUtil {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ZipUtil.class);

	/**
	 * Hide Construktor.
	 * 
	 */
	private ZipUtil() {

	}

	/**
	 * Findet eine datie die sich in einer jar datei befindet und entpackt die
	 * gesammt jar datei in des zielverzeichniss.
	 * 
	 * @param fileInJar -
	 *            the name of the libary
	 * @param destination
	 *            das ziel verzeichniss
	 */
	public static void unzipJarWithFile(String fileInJar, File destination) {
		try {
			final URL url = Thread.currentThread().getContextClassLoader()
					.getResource(fileInJar);
			if (url == null) {
				throw new RuntimeException("The " + fileInJar
						+ " is missing in the App-Server classpath");
			}
			final String jarName = isolateJarName(url);
			if (jarName == null) {
				throw new RuntimeException("Nor jar-file contains the ("
						+ fileInJar + ") File");
			}
			InputStream input = new FileInputStream(new File(jarName));

			if (destination == null || !destination.isDirectory()) {
				throw new RuntimeException(
						"The destination-dir is not specified");
			}
			unzip(input, destination);
			input.close();
			// load the libary
			log.info("Operating System: " + getOs());
		} catch (FileNotFoundException e) {
			log.error("The libary " + fileInJar + " was not found", e);
			throw new RuntimeException("The libary " + fileInJar
					+ " was not found");
		} catch (IOException e) {
			log.error("The libary " + fileInJar + " can not be accessed", e);
			throw new RuntimeException("The libary " + fileInJar
					+ " can not be accessed");
		}
	}

	/**
	 * Zipped eine Menge von Dateien.
	 * 
	 * @author Fabian Bauschulte
	 * @since 08.01.2006
	 * @param toZip
	 *            quellen
	 * @param zipped
	 *            Ziel
	 */
	public static void zipFile(File[] toZip, File zipped) throws IOException {
		// These are the files to include in the ZIP file

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		// Create the ZIP file

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipped));

		// Compress the files
		for (int i = 0; i < toZip.length; i++) {
			FileInputStream in = new FileInputStream(toZip[i]);

			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(toZip[i].getName()));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Complete the entry
			out.closeEntry();
			in.close();
		}

		// Complete the ZIP file
		out.close();

	}

	/**
	 * Unzipped eine Datei. Die erste die gefunden wird!
	 * 
	 * @author Fabian Bauschulte
	 * @since 08.01.2006
	 * @param zipped
	 *            Quelle
	 * @param unzipped
	 *            Zeil
	 */
	public static void unzipOneFile(File zipped, File unzipped)
			throws IOException {

		// Open the ZIP file
		ZipInputStream in = new ZipInputStream(new FileInputStream(zipped));

		// Get the first entry
		in.getNextEntry();

		// Open the output file

		OutputStream out = new FileOutputStream(unzipped);

		// Transfer bytes from the ZIP file to the output file
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		// Close the streams
		out.close();
		in.close();

	}

	/**
	 * Dump the contents of a JarArchive to the specified destination.
	 * 
	 * @param zip
	 *            the input jar/zip file
	 * @param dest
	 *            das zielverzeichniss.
	 */
	public static void unzip(File zip, File dest) throws IOException {
		unzip(new FileInputStream(zip), dest);
	}

	/**
	 * Dump the contents of a JarArchive to the specified destination.
	 * 
	 * @param in
	 *            the input stream dhe jar file
	 * @param dest
	 *            das zielverzeichniss.
	 */
	public static void unzip(InputStream in, File dest) throws IOException {
		if (!dest.exists()) {
			dest.mkdirs();
		}
		if (!dest.isDirectory()) {
			throw new IOException("Destination must be a directory.");
		}
		JarInputStream jin = new JarInputStream(in);
		byte[] buffer = new byte[1024];

		ZipEntry entry = jin.getNextEntry();
		while (entry != null) {
			String fileName = entry.getName();
			if (fileName.charAt(fileName.length() - 1) == '/') {
				fileName = fileName.substring(0, fileName.length() - 1);
			}
			if (fileName.charAt(0) == '/') {
				fileName = fileName.substring(1);
			}
			if (File.separatorChar != '/') {
				fileName = fileName.replace('/', File.separatorChar);
			}
			File file = new File(dest, fileName);
			if (entry.isDirectory()) {
				// make sure the directory exists
				file.mkdirs();
				jin.closeEntry();
			} else {
				// make sure the directory exists
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}

				// dump the file
				OutputStream out = new FileOutputStream(file);
				int len = 0;
				while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				out.close();
				jin.closeEntry();
				file.setLastModified(entry.getTime());
			}
			entry = jin.getNextEntry();
		}
		/*
		 * Explicity write out the META-INF/MANIFEST.MF so that any headers such
		 * as the Class-Path are see for the unpackaged jar
		 */
		Manifest mf = jin.getManifest();
		if (mf != null) {
			File file = new File(dest, "META-INF/MANIFEST.MF");
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			OutputStream out = new FileOutputStream(file);
			mf.write(out);
			out.flush();
			out.close();
		}
		jin.close();
	}

	/**
	 * In-Memmory unzip.
	 * 
	 * @param zipped
	 *            file zipped
	 * @return unzipped file
	 * @throws IOException
	 */
	public static byte[] unzip(byte[] zipped) throws IOException {

		// Open the ZIP file
		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zipped));

		// Get the first entry
		in.getNextEntry();

		// Open the output file

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// Transfer bytes from the ZIP file to the output file
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		return out.toByteArray();
	}

	/**
	 * Isolates a jar file when a file was found inside a jar
	 * 
	 * @param fileInJar -
	 *            the path to the file inside the jar file
	 * @return - the name of the jar file
	 */
	private static String isolateJarName(URL fileInJar) {
		String urlSt = fileInJar.getFile();
		urlSt = urlSt.substring("file:/".length(), urlSt.indexOf("!"));
		// under linux, solaris we need an absolute path
		if (getOs() == OsTypes.OSTYPE_LINUX
				|| getOs() == OsTypes.OSTYPE_SOLARIS || getOs() == OsTypes.OSTYPE_MAC) {
			urlSt = "/" + urlSt;
		}
		return urlSt;
	}

}
