package org.schalm.util.helper.string;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.schalm.util.helper.test.AbstractTest;
import org.schalm.util.test.TestUtil;

/**
 * UnitTest for {@link StringHelper}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: StringHelperTest.java 161 2014-03-06 13:27:59Z cschalm $
 */
public class StringHelperTest extends AbstractTest {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Test of {@link StringHelper#isEmpty(java.lang.String)}.
	 */
	@Test
	public void testIsEmpty() {
		String content = null;
		assertTrue(StringHelper.isEmpty(content));
		content = new String();
		assertTrue(StringHelper.isEmpty(content));
		content = "\t\n\t";
		assertFalse(StringHelper.isEmpty(content));
	}

	/**
	 * Test of {@link StringHelper#isQuasiEmpty(java.lang.String) }.
	 */
	@Test
	public void testIsQuasiEmpty() {
		String content = null;
		assertTrue(StringHelper.isQuasiEmpty(content));
		content = new String();
		assertTrue(StringHelper.isQuasiEmpty(content));
		content = "\t\n\t";
		assertTrue(StringHelper.isQuasiEmpty(content));
	}

	/**
	 * Test of {@link StringHelper#deleteEmptyRows(java.lang.String)}.
	 */
	@Test
	public void testDeleteEmptyRows() {
		String content = null;
		assertNotNull(StringHelper.deleteEmptyRows(content));
		content = "";
		assertNotNull(StringHelper.deleteEmptyRows(content));
		assertTrue(StringHelper.deleteEmptyRows(content).length() == 0);
		content = "1\n\t\n2\n\t\n3\n\t\n4\n\t\n5\n\t\n";
		assertNotNull(StringHelper.deleteEmptyRows(content));
		assertTrue(StringHelper.deleteEmptyRows(content).length() > 0);
		String expected = "1" + LINE_SEPARATOR + "2" + LINE_SEPARATOR + "3" + LINE_SEPARATOR + "4" + LINE_SEPARATOR + "5" + LINE_SEPARATOR + "";
		final String result = StringHelper.deleteEmptyRows(content);
        // System.out.println("Content:\n\"" + content + "\"");
		// System.out.println("Expected:\n\"" + expected + "\"");
		// System.out.println("Result:\n\"" + result + "\"");
		TestUtil.compareStrings(expected, result);
	}

	/**
	 * Test of {@link StringHelper#getSizeString(long) }.
	 */
	@Test
	public void testGetSizeString() {
		Assert.assertEquals("512 B", StringHelper.getSizeString(512));
		Assert.assertEquals("-512 B", StringHelper.getSizeString(-512));
		Assert.assertEquals("2 KB", StringHelper.getSizeString(2048));
		Assert.assertEquals("-2 KB", StringHelper.getSizeString(-2048));
		Assert.assertEquals("1,5 MB", StringHelper.getSizeString(1572864));
		Assert.assertEquals("-1,5 MB", StringHelper.getSizeString(-1572864));
	}

	/**
	 * Test of {@link StringHelper#getIdentity(java.lang.Object) }.
	 */
	@Test
	public void testGetIdentity() {
		Assert.assertTrue(StringHelper.getIdentity(new Integer(0)).startsWith("java.lang.Integer@"));
		Assert.assertTrue(StringHelper.getIdentity(new Exception()).startsWith("java.lang.Exception@"));
	}

}
