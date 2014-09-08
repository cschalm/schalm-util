/*
 * Created on 04.04.2005
 */
package org.schalm.util.helper.xml;

import org.schalm.util.helper.string.StringHelper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * ContentHandler for indentation of Xml-Files.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: IndentContentHandler.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class IndentContentHandler extends DefaultHandler {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private StringBuffer sb = null;
    private int indents;
    private String lastTag = null;

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        sb.append(indent());
        sb.append("<").append(qName);
        indents++;
        for (int i = atts.getLength() - 1; i >= 0; i--) {
            String attName = atts.getQName(i);
            String attValue = atts.getValue(i);
            sb.append(LINE_SEPARATOR);
            sb.append(indent());
            sb.append(attName).append("=\"").append(attValue).append("\"");
        }
        sb.append(">");
        lastTag = qName;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        indents--;
        boolean matchingTag = lastTag != null && qName.equalsIgnoreCase(lastTag);
        if (!matchingTag) {
            sb.append(indent());
        }
        sb.append("</").append(qName).append(">");
        lastTag = null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        indents = 0;
        sb = new StringBuffer();
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (!StringHelper.isQuasiEmpty(str)) {
            sb.append(str.trim());
        }
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (!StringHelper.isQuasiEmpty(str)) {
            sb.append(str.trim());
        }
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        sb.append(prefix);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    @Override
    public void skippedEntity(String name) throws SAXException {
        sb.append(name);
    }

    private String indent() {
        StringBuilder s = new StringBuilder();
        s.append(IndentContentHandler.LINE_SEPARATOR);
        for (int i = indents; i > 0; i--) {
            s.append("\t");
        }
        return s.toString();
    }

    @Override
    public String toString() {
        return StringHelper.deleteEmptyRows(sb.toString());
    }

}
