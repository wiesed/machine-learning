package com.bm.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Erzeugt HashCodes aus einen String.
 * 
 * @author Fabian Bauschulte
 * 
 */
public final class MD5HashGenerator {

	private MD5HashGenerator() {

	}

	/**
	 * <u>MD5-Hash erzeugen.</u>
	 * 
	 * @param toHash
	 *            String fuer den der Hash erzeugt werden soll.
	 * @return den MD5 Hashcode des Strings.
	 */
	public static String getMD5HashCode(final String toHash) {

		MessageDigest md = null;
		byte[] encryptMsg = null;

		try {
			md = MessageDigest.getInstance("MD5"); // getting a 'MD5-Instance'
			encryptMsg = md.digest(toHash.getBytes()); // solving the MD5-Hash
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 Algorithmus nicht gefunden.", e);
		}

		String swap = ""; // swap-string for the result
		String byteStr = ""; // swap-string for current hex-value of byte
		final StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i <= encryptMsg.length - 1; i++) {

			byteStr = Integer.toHexString(encryptMsg[i]); // swap-string for
			// current hex-value
			// of byte

			switch (byteStr.length()) {
			case 1: // if hex-number length is 1, add a '0' before
				swap = "0" + Integer.toHexString(encryptMsg[i]);
				break;

			case 2: // correct hex-letter
				swap = Integer.toHexString(encryptMsg[i]);
				break;

			case 8: // get the correct substring
				swap = (Integer.toHexString(encryptMsg[i])).substring(6, 8);
				break;
			default: // doNothing
				break;
			}
			strBuf.append(swap); // appending swap to get complete hash-key
		}
		return strBuf.toString(); // String with the MD5-Hash

	}
}
