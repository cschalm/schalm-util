package org.schalm.util.helper.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper methods for file operations.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: FileHelper.java 158 2014-03-01 22:22:07Z cschalm $
 */
public final class FileHelper {
    private static final Log log = LogFactory.getLog(FileHelper.class);
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    static final int BUFFER_SIZE = 0x10000; // input buffer size in bytes (64 KB)

    private FileHelper() {
        // utility class
    }

    /**
     * Read a text file and return it in one string.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readTextFile(File file) throws IOException {
        StringBuffer sb = new StringBuffer();
		try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
			FileHelper.read(sb, fileReader);
		}

        return sb.toString();
    }

    /**
     * Read a text file and return it in one string.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String readTextStream(String filename) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(FileHelper.class.getClassLoader().getResourceAsStream(filename)));
        FileHelper.read(sb, fileReader);

        return sb.toString();
    }

    private static void read(StringBuffer sb, BufferedReader fileReader) throws IOException {
        String line;
        do {
            line = fileReader.readLine();
            if (line != null) {
                sb.append(line).append(FileHelper.LINE_SEPARATOR);
            }
        } while (line != null);
    }

    /**
     * Write a string to a file.
     *
     * @param file
     * @param content
     * @throws IOException
     */
    public static void writeTextFile(File file, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(content, 0, content.length());
            bw.flush();
        }
    }

    /**
     * Create a new zip archive including the files given.<br />
     * Directories are skipped.
     *
     * @param destFile
     * @param files
     * @throws IOException
     */
    public static void zip(File destFile, File[] files) throws IOException {
        BufferedInputStream origin;
        FileOutputStream dest = new FileOutputStream(destFile);
        try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest))) {
            out.setMethod(ZipOutputStream.DEFLATED);
            byte[] data = new byte[BUFFER_SIZE];
            for (File file : files) {
                if (log.isDebugEnabled()) {
                    log.debug("Adding: " + file.getName());
                }
                if (file.isDirectory()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Skipping directory: " + file);
                    }
                    continue;
                }
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.flush();
        }
    }

    /**
     * Extract a zip archive to destDir.
     *
     * @param zipFile
     * @param destDir
     * @throws IOException
     */
    public static void unzip(File zipFile, File destDir) throws IOException {
        BufferedOutputStream dest;
        FileInputStream fis = new FileInputStream(zipFile);
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Extracting: " + entry);
                }
                int count;
                byte[] data = new byte[BUFFER_SIZE];
                FileOutputStream fos = new FileOutputStream(destDir.getAbsolutePath() + "/" + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
        }
    }

    /**
     * Compute a file's CRC32 checksum.
     *
     * @param file
     * @return hexadecimal string
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getCRC32(File file) throws IOException, NoSuchAlgorithmException {
        CRC32 crc32digest;
        try (FileInputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            crc32digest = new CRC32();
            int i;
            while ((i = input.read(buffer, 0, BUFFER_SIZE)) > 0) {
                crc32digest.update(buffer, 0, i);
            }
        }
        String rawtext = "00000000" + Long.toHexString(crc32digest.getValue());
        String crc32 = rawtext.substring(rawtext.length() - 8);

        return crc32.toUpperCase();
    }

    /**
     * Compute a file's MD5 checksum.
     *
     * @param file
     * @return hexadecimal string
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(File file) throws IOException, NoSuchAlgorithmException {
        return getChecksum(file, MessageDigest.getInstance("MD5"));
    }

    /**
     * Compute a file's SHA-1 checksum.
     *
     * @param file
     * @return hexadecimal string
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getSHA1(File file) throws IOException, NoSuchAlgorithmException {
        return getChecksum(file, MessageDigest.getInstance("SHA1"));
    }

    private static String getChecksum(File file, MessageDigest messageDigest) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int i;
            while ((i = input.read(buffer, 0, BUFFER_SIZE)) > 0) {
                messageDigest.update(buffer, 0, i);
            }
        }

        return formatHexBytes(messageDigest.digest());
    }

    /**
     * Format a raw array of binary bytes as a hexadecimal string.
     *
     * @param raw
     * @return
     */
    public static String formatHexBytes(byte[] raw) {
        StringBuffer buffer;
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int i;
        int value;

        buffer = new StringBuffer(raw.length * 2);
        for (i = 0; i < raw.length; i++) {
            value = raw[i];
            buffer.append(hexDigits[(value >> 4) & 0x0F]);
            buffer.append(hexDigits[value & 0x0F]);
        }
        return buffer.toString();
    }

}
