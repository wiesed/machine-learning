package com.bm.classify.core.helper;

import com.bm.classify.ClassifyConstants;
import com.bm.classify.ClassifyEnvironment;
import com.bm.common.util.OsTypes;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import static com.bm.common.util.BmUtil.getOs;
import static com.bm.common.util.BmUtil.getTempDirectory;

/**
 * Beritet die SVM c++ libs fuer das jeweilige betriebssystem vor.
 *
 * @author Daniel Wiese
 * @since 02.06.2006
 */
public class NativeLibraryLoader implements ClassifyConstants {

	private static final String SVM_PATH = "svm" + File.separatorChar;

	/**
	 * logger instance *
	 */
	private static final Logger log = Logger
			.getLogger(NativeLibraryLoader.class);

	private final File tempDirectoryPath;

	/**
	 * Defaukt constructor.
	 */
	public NativeLibraryLoader() {
		this.tempDirectoryPath = new File(new File(getTempDirectory()),
				SVM_PATH);
		if (!this.tempDirectoryPath.exists()) {
			this.tempDirectoryPath.mkdirs();
		}
	}

	/**
	 * Extracts/Load the SVM execs depending to the operating system.
	 *
	 * @return - the paths to svm learn and svm classify
	 */
	public SVMEnviroment prepareSVMEnviroment() {
		SVMEnviroment back = null;
		if (getOs() == OsTypes.OSTYPE_WINDOWS
				|| getOs() == OsTypes.OSTYPE_WINNT) {
			back = new SVMEnviroment(this.tempDirectoryPath,
					ClassifyEnvironment.WINDOWS);
			if (!back.isValid()) {
				loadLibraryInJar(ClassifyEnvironment.WINDOWS);
			}
		} else if (getOs() == OsTypes.OSTYPE_LINUX) {
			back = new SVMEnviroment(this.tempDirectoryPath,
					ClassifyEnvironment.LINUX);
			if (!back.isValid()) {
				loadLibraryInJar(ClassifyEnvironment.LINUX);
			}
		} else if (getOs() == OsTypes.OSTYPE_MAC) {
			back = new SVMEnviroment(this.tempDirectoryPath,
					ClassifyEnvironment.MAC);
			if (!back.isValid()) {
				loadLibraryInJar(ClassifyEnvironment.MAC);
			}
		} else {
			throw new RuntimeException(
					"Usupported Operating-System (Only Windows/Linux is currently supported)");
		}

		return back;
	}

	/**
	 * Load a naitive lib packaged in a JAR file. I aupomatically extract the
	 * lib to the serve specific native lib directory
	 *
	 * @param environment - the name of the libary
	 *                    - in an error case
	 */
	private void loadLibraryInJar(ClassifyEnvironment environment) {
		try {
			final URL url = Thread.currentThread()
								  .getContextClassLoader()
								  .getResource(
										  "svm/" + environment.getIdentyfier());
			if (url == null) {
				throw new RuntimeException(
						"The svm-xxx.jar is missing in the App-Server classpath");
			}

			// vorher chmod a+x svm_classify ausfuehren
			final String jarName = url.getFile();
			if (jarName == null) {
				throw new RuntimeException(
						"The svm-xxx.jar does not contain the (" + environment
								+ ") File");
			}
			InputStream input = new FileInputStream(new File(jarName));

			if (this.tempDirectoryPath == null
					|| !this.tempDirectoryPath.isDirectory()) {
				throw new RuntimeException(
						"The clc-native-dir is not specified");
			}
			// load the libary
			unjar(input, this.tempDirectoryPath);
			input.close();
			mayExecuteChmodCommand(environment);
			log.info("Operating System: " + getOs());
		} catch (FileNotFoundException e) {
			log.error("The libary " + environment + " was not found", e);
			throw new RuntimeException("The libary " + environment
					+ " was not found");
		} catch (IOException e) {
			log.error("The libary " + environment + " can not be accessed", e);
			throw new RuntimeException("The libary " + environment
					+ " can not be accessed");
		}
	}

	private void mayExecuteChmodCommand(ClassifyEnvironment environment)
			throws IOException {
		if (environment.isExecuteChmodCommand()) {
			try {
				Process process = Runtime.getRuntime()
										 .exec(
												 "chmod " + environment.getChmodCommand() + " "
														 + this.tempDirectoryPath + File.separatorChar
														 + environment.getClassify_name());
				process.waitFor();
				process = Runtime.getRuntime()
								 .exec(
										 "chmod " + environment.getChmodCommand() + " "
												 + this.tempDirectoryPath + File.separatorChar
												 + environment.getLearn_name());
				process.waitFor();
			} catch (InterruptedException e) {
				log.error("Process interupted", e);
			}
		}
	}

	/**
	 * Isolates a jar file when a file was found inside a jar
	 *
	 * @param fileInJar - the path to the file inside the jar file
	 * @return - the name of the jar file
	 */
	private static String isolateJarName(URL fileInJar) {
		String urlSt = fileInJar.getFile();
		urlSt = urlSt.substring("file:/".length(), urlSt.indexOf("!"));
		// under linux, solaris we need an absolute path
		if (getOs() == OsTypes.OSTYPE_LINUX
				|| getOs() == OsTypes.OSTYPE_SOLARIS) {
			urlSt = "/" + urlSt;
		}
		return urlSt;
	}

	/**
	 * Dump the contents of a JarArchive to the dpecified destination.
	 *
	 * @param in   - the jar archive as input stream
	 * @param dest - the destination (to extract the content)
	 * @throws IOException - in an error case
	 */
	private static void unjar(InputStream in, File dest) throws IOException {

		if (!dest.exists()) {
			dest.mkdirs();
		}

		if (!dest.isDirectory()) {
			throw new IOException("Destination must be a directory.");
		}

		JarInputStream jin = new JarInputStream(in);
		final byte[] buffer = new byte[1024];
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

			final File file = new File(dest, fileName);
			if (entry.isDirectory()) {
				// make sure the directory exists
				file.mkdirs();
				jin.closeEntry();
			} else {
				// make sure the directory exists
				final File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}

				// dump the file

				final OutputStream out = new FileOutputStream(file);
				int len = 0;
				while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, len);
				}

				out.flush();
				out.close();
				jin.closeEntry();

			}

			entry = jin.getNextEntry();
		}
		jin.close();
	}
}
