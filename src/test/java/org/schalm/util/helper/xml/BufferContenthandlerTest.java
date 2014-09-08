package org.schalm.util.helper.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Test;
import org.schalm.util.helper.file.FileHelper;
import org.schalm.util.helper.test.AbstractTest;
import org.schalm.util.test.TestUtil;

/**
 * UnitTest for {@link BufferContentHandler}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: BufferContenthandlerTest.java 110 2011-10-21 09:48:20Z cschalm
 * $
 */
public class BufferContenthandlerTest extends AbstractTest {
    @Test
    public void testBuffer() throws Exception {
        BufferContentHandler handler = new BufferContentHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(this.getClass().getClassLoader().getResourceAsStream("input.xml"), handler);
        String result = handler.toString();
        String expected = FileHelper.readTextStream("expectedResultNoPrologue.xml");
        TestUtil.compareStrings(expected.trim(), result);
    }

}
