package org.schalm.util.helper.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Test;
import org.schalm.util.helper.file.FileHelper;
import org.schalm.util.helper.test.AbstractTest;
import org.schalm.util.test.TestUtil;

/**
 * UnitTest for {@link IndentContentHandler}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: IndentContenthandlerTest.java 110 2011-10-21 09:48:20Z cschalm
 * $
 */
public class IndentContenthandlerTest extends AbstractTest {
    @Test
    public void testIndention() throws Exception {
        IndentContentHandler handler = new IndentContentHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(this.getClass().getClassLoader().getResourceAsStream("input.xml"), handler);
        String result = handler.toString();
        String expected = FileHelper.readTextStream("expectedResult.xml");
        // System.out.println("Expected:\n" + expected + "\nResult:\n" + result);
        TestUtil.compareStrings(expected, result);
    }

}
