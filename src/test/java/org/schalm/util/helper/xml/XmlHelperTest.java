package org.schalm.util.helper.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.Ignore;
import org.junit.Test;
import org.schalm.util.helper.file.FileHelper;
import org.schalm.util.helper.test.AbstractTest;
import org.schalm.util.test.TestUtil;
import org.w3c.dom.Document;

/**
 * UnitTest for {@link XmlHelper}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: XmlHelperTest.java 163 2014-03-06 14:02:38Z cschalm $
 */
public class XmlHelperTest extends AbstractTest {

    @Test
    public void testIndentXmlSax() throws Exception {
        File input = new File("src/test/resources/input.xml");
        final String result = XmlHelper.indentXmlSax(input);
        final String expected = FileHelper.readTextFile(new File("src/test/resources/expectedResultAll.xml"));
        // System.out.println("Expected:\n" + expected);
        // System.out.println("Result:\n" + result);
        TestUtil.compareStrings(expected, result);
    }

    @Test
    public void testIO() throws Exception {
        File input = new File("src/test/resources/expectedResultAll.xml");
        final Document doc = XmlHelper.readXmlFile(input);
        assertNotNull("DOMDocument is null!", doc);
        XmlHelper.setTagValue(doc, "init", "test");
        File output = File.createTempFile("test", ".xml");
        output.deleteOnExit();
        // System.out.println(output.getCanonicalPath());
        XmlHelper.saveXmlFile(doc, output);
        final Document readDoc = XmlHelper.readXmlFile(output);
        final String tagValue = XmlHelper.getTagValue(readDoc, "init");
        // System.out.println("\"" + tagValue + "\"");
        assertEquals("Values are not equal!", "test", tagValue);
    }

    @Test
    public void testDomTree2String() throws Exception {
        File input = new File("src/test/resources/input.xml");
        String expected = FileHelper.readTextFile(new File("src/test/resources/expectedResultString.xml")).trim();
        Document doc = XmlHelper.readXmlFile(input);
        assertNotNull("DOMDocument is null!", doc);
        String result = XmlHelper.domTree2String(doc).trim();
        // System.out.println("Result:\n" + result + "\nLength: " + result.length());
        // System.out.println("Expected:\n" + expected + "\nLength: " + expected.length());
        TestUtil.compareStrings(expected, result);
    }

    @Test
    public void testDomTree2StringOnlyElements() throws Exception {
        File input = new File("src/test/resources/inputOnlyElements.xml");
        String expected = FileHelper.readTextFile(new File("src/test/resources/expectedResultStringOnlyElements.xml")).trim();
        Document doc = XmlHelper.readXmlFile(input);
        assertNotNull("DOMDocument is null!", doc);
        String result = XmlHelper.domTree2String(doc).trim();
        // System.out.println("Result:\n" + result + "\nLength: " + result.length());
        // System.out.println("Expected:\n" + expected + "\nLength: " + expected.length());
        TestUtil.compareStrings(expected, result);
    }

    @Test
    @Ignore
    public void testIndentXmlDom() throws Exception {
        File input = new File("src/test/resources/input.xml");
        final String result = XmlHelper.indentXmlDom(FileHelper.readTextFile(input));
        final String expected = FileHelper.readTextFile(new File("src/test/resources/expectedResultAll.xml"));
        System.out.println("Expected:\n" + expected);
        System.out.println("Result:\n" + result);
        assertTrue("Indented XML has different structure!", XmlHelper.equalsStructurally(result, expected));
    }

    @Test
    public void testIndentXmlSax2() throws Exception {
        File input = new File("src/test/resources/input.xml");
        final String result = XmlHelper.indentXmlSax2(input);
        final String expected = FileHelper.readTextFile(new File("src/test/resources/expectedResultAllSax.xml"));
//        System.out.println("Expected:\n" + expected);
//        System.out.println("Result:\n" + result);
        TestUtil.compareStrings(expected, result);
    }

}
