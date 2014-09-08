package org.schalm.util.helper.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * ContentHandler for buffering of Xml-Files.<br />
 * Can be used for chaining or stand-alone.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: BufferContentHandler.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class BufferContentHandler extends DefaultHandler {
    private StringBuilder buffer;
    private ContentHandler parent;

    public BufferContentHandler() {
        this(new DefaultHandler());
    }

    public BufferContentHandler(ContentHandler parent) {
        this.parent = parent;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        buffer.append(str);
        parent.characters(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        parent.endDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        buffer.append("</").append(qName).append(">");
        parent.endElement(uri, localName, qName);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        parent.endPrefixMapping(prefix);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        buffer.append(str);
        parent.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        parent.processingInstruction(target, data);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        parent.setDocumentLocator(locator);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        parent.skippedEntity(name);
    }

    @Override
    public void startDocument() throws SAXException {
        buffer = new StringBuilder();
        parent.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        buffer.append("<").append(qName);
        for (int i = attributes.getLength() - 1; i >= 0; i--) {
            String attName = attributes.getQName(i);
            String attValue = attributes.getValue(i);
            buffer.append(" ").append(attName).append("=\"").append(attValue).append("\"");
        }
        buffer.append(">");
        parent.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        parent.startPrefixMapping(prefix, uri);
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

}
