package org.schalm.util.helper.string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper for string operations.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: StringHelper.java 158 2014-03-01 22:22:07Z cschalm $
 */
public final class StringHelper {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final Log log = LogFactory.getLog(StringHelper.class);

	private StringHelper() {
		// utility class
	}

	/**
	 * Check if string is null or empty.
	 *
	 * @param input the string to be examined
	 * @return true, if sting is null or length is 0
	 */
	public static boolean isEmpty(final String input) {
		if (input == null) {
			return true;
		}
		return input.isEmpty();
	}

	/**
	 * Check if given string only contains blanks and tabs.
	 *
	 * @param input the string to be examined
	 * @return true if string only contains blanks and tabs, false if not
	 */
	public static boolean isQuasiEmpty(final String input) {
		if (StringHelper.isEmpty(input)) {
			return true;
		}
		String cleaned = input.replaceAll("\\W", ""); // [a-zA-Z_0-9]
		return isEmpty(cleaned);
	}

	/**
	 * Removes all blank lines and lines without visible characters.
	 *
	 * @param in the string to clean
	 * @return the cleaned string
	 */
	public static String deleteEmptyRows(final String in) {
		if (in == null || in.isEmpty()) {
			return "";
		}
		StringBuilder out = new StringBuilder();
		String row;
		try (BufferedReader input = new BufferedReader(new StringReader(in))) {
			while ((row = input.readLine()) != null) {
				if (!StringHelper.isQuasiEmpty(row)) {
					out.append(row).append(StringHelper.LINE_SEPARATOR);
				}
			}
		} catch (IOException e) {
			log.error("Error reading input: " + e.getMessage(), e);
			return in;
		}
		return out.toString();
	}

	/**
	 * Create a formatted string with the given number of bytes in the highest reasonable unit, like 512 B, 1,3 KB or 2 MB.
	 *
	 * @param bytes
	 * @return the formatted string
	 */
	public static String getSizeString(long bytes) {
		NumberFormat nf = NumberFormat.getInstance();
		if (Math.abs(bytes) < 1024) {
			return bytes + " B";
		}
		if (Math.abs(bytes) >= 1024 * 1024) {
			float megaBytes = (float) bytes / (1024 * 1024);
			return nf.format(megaBytes) + " MB";
		}
		float kiloBytes = (float) bytes / 1024;

		return nf.format(kiloBytes) + " KB";
	}

	/**
	 * Get the identity of the given object as used in a debugger.<br />
	 * Example: java.lang.String@a1b2c3
	 *
	 * @param o
	 * @return
	 */
	public static String getIdentity(Object o) {
		return o.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(o));
	}

}
