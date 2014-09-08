package org.schalm.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.schalm.util.helper.xml.XmlHelper;

/**
 * Util class for unit tests.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: TestUtil.java 161 2014-03-06 13:27:59Z cschalm $
 */
public class TestUtil {
    private TestUtil() {
    }

    /**
     * Compares two Strings bitwise.
     *
     * @param expected
     * @param result
     */
    public static void compareStrings(String expected, String result) {
        assertNotNull("Expected content is null!", expected);
        assertNotNull("Content is null!", result);
        assertEquals("Length is different!", expected.length(), result.length());
        assertEquals("Strings are different!", expected, result);
        final byte[] resultBytes = result.getBytes();
        final byte[] expectedBytes = expected.getBytes();
        for (int i = 0; i < resultBytes.length; i++) {
            assertEquals("Bytes at position " + i + " are different!", expectedBytes[i], resultBytes[i]);
        }
    }

    /**
     * Compares two XML-Strings.
     *
     * @param expected
     * @param result
     * @throws java.lang.Exception
     */
    public static void compareXmlStrings(String expected, String result) throws Exception {
        assertNotNull("Expected content is null!", expected);
        assertNotNull("Content is null!", result);
        assertTrue("XML-Strings are different!", XmlHelper.equalsStructurally(expected, result));
    }

}
