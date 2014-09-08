package org.schalm.util.helper.file;

import java.io.File;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.schalm.util.helper.test.AbstractTest;
import org.schalm.util.test.TestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * UnitTest for {@link FileHelper}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: FileHelperTest.java 161 2014-03-06 13:27:59Z cschalm $
 */
public class FileHelperTest extends AbstractTest {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    public void testIO() throws Exception {
        String testContent = "abcdefghijklmnopqrstuvwxyzöäüß" + LINE_SEPARATOR;
        File file = File.createTempFile("test", ".txt");
        file.deleteOnExit();
        FileHelper.writeTextFile(file, testContent);
        final String result = FileHelper.readTextFile(file);
        TestUtil.compareStrings(testContent, result);
    }

    @Test
    public void testZip() throws Exception {
        File currDir = new File("./target/test-classes");
        File destFile = File.createTempFile("test", ".zip");
        destFile.deleteOnExit();
        FileHelper.zip(destFile, currDir.listFiles());
        assertTrue("created zip file does not exist!", destFile.exists());
    }

    @Test
    public void testCRC32() throws Exception {
        File file = new File("./target/test-classes/test.zip");
        assertEquals("CRC32 not equal!", "afaa0e73".toUpperCase(), FileHelper.getCRC32(file));
    }

    @Test
    public void testMD5() throws Exception {
        File file = new File("./target/test-classes/test.zip");
        assertEquals("MD5 not equal!", "7576d1958deffb22d142f5b68147028d".toUpperCase(), FileHelper.getMD5(file));
    }

    @Test
    public void testSHA1() throws Exception {
        File file = new File("./target/test-classes/test.zip");
        assertEquals("SHA-1 not equal!", "50e09aac86f3e432bf548098979d4aaefc171556".toUpperCase(), FileHelper.getSHA1(file));
    }

    @Test
    public void testUnzip() throws Exception {
        String tempDir = System.getProperty("java.io.tmpdir");
        File testDir = new File(tempDir + FileHelper.FILE_SEPARATOR + new Date().getTime());
        testDir.mkdirs();
        testDir.deleteOnExit();
        File zipFile = new File("./target/test-classes/test.zip");
        FileHelper.unzip(zipFile, testDir);
        String[] files = testDir.list();
        Assert.assertEquals("Number of unzipped files is wrong!", 5, files.length);
    }

}
