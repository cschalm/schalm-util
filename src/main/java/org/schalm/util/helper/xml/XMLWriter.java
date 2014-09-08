/*--------------------------------------------------------------------------
 * Copyright (c) 2007 OpenMethods, LLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Trip Gilman (OpenMethods), Lonnie G. Pryor (OpenMethods),
 *    T.D. Barnes (OpenMethods) - initial API and implementation
 -------------------------------------------------------------------------*/
package org.schalm.util.helper.xml;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import org.schalm.util.helper.string.StringHelper;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A pretty-printing XML encoder.
 *
 * @author Lonnie Pryor
 * @version $Id: XMLWriter.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class XMLWriter extends DefaultHandler {

    /**
     * The output stream to write to.
     */
    private ByteArrayOutputStream output = null;
    /**
     * The character to use for indentation.
     */
    private char indentCharacter = '\t';
    private final char deepIndentCharacter = '\t';
    /**
     * The number of indent characters to use for normal indentations.
     */
    private int indentSize = 1;
    /**
     * The number of indent characters to use for deep indentations.
     */
    private int deepIndentSize = 1;
    /**
     * The number of parent elements from the current position.
     */
    private int elementDepth = 0;
    /**
     * The mapping of prefixes to stacks of name space URIs.
     */
    private final Map<String, Object> namespaceURIsByPrefix = new HashMap<>();
    /**
     * The mapping of name space URIs to stacks of prefixes.
     */
    private final Map<String, Object> prefixesByNamespaceURI = new HashMap<>();
    /**
     * The prefixes mapped since the last element was started.
     */
    private final Set<String> newPrefixes = new LinkedHashSet<>();
    /**
     * True if the parent element's start tag is still open.
     */
    private boolean parentElementIncomplete = false;
    /**
     * True if the last thing written was CDATA.
     */
    private boolean afterCDATA = false;

    /**
     * Creates a new XMLWriter.
     */
    public XMLWriter() {
        this.output = new ByteArrayOutputStream();
    }

    /**
     * Sets the character to use for indentation.
     *
     * @param indentCharacter The character to use for indentation.
     */
    public void setIndentCharacter(char indentCharacter) {
        this.indentCharacter = indentCharacter;
    }

    /**
     * Sets the number of indent characters to use for normal indentations.
     *
     * @param indentSize The number of indent characters to use for normal
     * indentations.
     */
    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize < 0 ? 0 : indentSize;
    }

    /**
     * Sets the number of indent characters to use for deep indentations.
     *
     * @param deepIndentSize The number of indent characters to use for deep
     * indentations.
     */
    public void setDeepIndentSize(int deepIndentSize) {
        this.deepIndentSize = deepIndentSize < 0 ? 0 : deepIndentSize;
    }

    /**
     * Creates a TrAX result object that writes to this encoder.
     *
     * @return A new TrAX result object that writes to this encoder.
     */
    public Result toXMLResult() {
        return new SAXResult(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    @Override
    public void setDocumentLocator(Locator locator) {
        // Ignore the document locator.
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        elementDepth = 0;
        namespaceURIsByPrefix.clear();
        prefixesByNamespaceURI.clear();
        newPrefixes.clear();
        parentElementIncomplete = false;
        afterCDATA = false;
        try {
            output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n".getBytes("UTF-8")); //$NON-NLS-1$
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        try {
            output.write("\r".getBytes("UTF-8"));
            output.write("\n".getBytes("UTF-8"));
            afterCDATA = false;
            parentElementIncomplete = false;
            newPrefixes.clear();
            prefixesByNamespaceURI.clear();
            namespaceURIsByPrefix.clear();
            elementDepth = 0;
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        stackedIndexPush(namespaceURIsByPrefix, prefix, uri);
        stackedIndexPush(prefixesByNamespaceURI, uri, prefix);
        newPrefixes.remove(prefix);
        newPrefixes.add(prefix);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        String uri = stackedIndexPop(namespaceURIsByPrefix, prefix);
        stackedIndexRemove(prefixesByNamespaceURI, uri, prefix);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        try {
            ensureParentElementIsComplete();
            // Open the start tag.
            output.write("\r".getBytes("UTF-8"));
            output.write("\n".getBytes("UTF-8"));
            indent();
            output.write("<".getBytes("UTF-8"));
            writeName(uri, localName, name);
            // Write the prefix mappings.
            for (Iterator<String> i = newPrefixes.iterator(); i.hasNext();) {
                output.write("\r".getBytes("UTF-8"));
                output.write("\n".getBytes("UTF-8"));
                deepIndent();
                String prefix = i.next();
                String xmlns = stackedIndexPeek(namespaceURIsByPrefix, prefix);
                if (prefix.length() == 0) {
                    writeNameValuePair(null, null, "xmlns", xmlns); //$NON-NLS-1$
                } else {
                    writeNameValuePair(null, null, "xmlns:" + prefix, xmlns); //$NON-NLS-1$
                }
            }
            newPrefixes.clear();
            // Write the attributes.
            for (int i = 0; i < atts.getLength(); ++i) {
                if ("http://www.w3.org/2000/xmlns/".equals(atts.getURI(i))) { //$NON-NLS-1$
                    continue;
                }
                output.write("\r".getBytes("UTF-8"));
                output.write("\n".getBytes("UTF-8"));
                deepIndent();
                writeNameValuePair(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), atts.getValue(i));
            }
            parentElementIncomplete = true;
            afterCDATA = false;
            ++elementDepth;
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        try {
            --elementDepth;
            if (parentElementIncomplete) {
                output.write(" />".getBytes("UTF-8")); //$NON-NLS-1$
                parentElementIncomplete = false;
            } else {
                output.write("\r".getBytes("UTF-8"));
                output.write("\n".getBytes("UTF-8"));
                indent();
                output.write("</".getBytes("UTF-8")); //$NON-NLS-1$
                writeName(uri, localName, name);
                output.write(">".getBytes("UTF-8"));
            }
            afterCDATA = false;
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            ensureParentElementIsComplete();
            output.write("\r".getBytes("UTF-8"));
            output.write("\n".getBytes("UTF-8"));
            indent();
            output.write("<?".getBytes("UTF-8")); //$NON-NLS-1$
            output.write(target.getBytes("UTF-8"));
            output.write(" ".getBytes("UTF-8"));
            output.write(data.getBytes("UTF-8"));
            output.write(" ?>".getBytes("UTF-8")); //$NON-NLS-1$
            afterCDATA = false;
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            if (!afterCDATA) {
                ensureParentElementIsComplete();
                output.write("\r".getBytes("UTF-8"));
                output.write("\n".getBytes("UTF-8"));
                indent();
            }
            boolean hadCR = false;
            for (int i = 0; i < length; ++i) {
                char c = ch[start + i];
                if (c == '\n' || hadCR) {
                    hadCR = false;
                    output.write("\r".getBytes("UTF-8"));
                    output.write("\n".getBytes("UTF-8"));
                    indent();
                    if (c != '\n') {
                        output.write(Character.toString(c).getBytes("UTF-8"));
                    }
                } else {
                    switch (c) {
                        case '\r':
                            hadCR = true;
                            break;
                        case '\'':
                            output.write("&apos;".getBytes("UTF-8"));
                            break;
                        case '"':
                            output.write("&quot;".getBytes("UTF-8"));
                            break;
                        case '&':
                            output.write("&amp;".getBytes("UTF-8"));
                            break;
                        case '<':
                            output.write("&lt;".getBytes("UTF-8"));
                            break;
                        case '>':
                            output.write("&gt;".getBytes("UTF-8"));
                            break;
                        default:
                            output.write(Character.toString(c).getBytes("UTF-8"));
                    }
                }
            }
            if (hadCR) {
                output.write("\r".getBytes("UTF-8"));
                output.write("\n".getBytes("UTF-8"));
            }
            afterCDATA = true;
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        // Ignore extra whitespace.
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    @Override
    public void skippedEntity(String name) throws SAXException {
        // Ignore skipped entities.
    }

    /**
     * Ensures that the ">" character is written to close the parent element
     * start tag if it is still open.
     */
    private void ensureParentElementIsComplete() {
        if (parentElementIncomplete) {
            try {
                output.write(">".getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            parentElementIncomplete = false;
        }
    }

    /**
     * Intents to the current level.
     */
    private void indent() throws SAXException {
        try {
            for (int i = 0; i < elementDepth; ++i) {
                for (int j = 0; j < indentSize; ++j) {
                    output.write(Character.toString(indentCharacter).getBytes("UTF-8"));
                }
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /**
     * Intents to the current level plus the deep indent.
     */
    private void deepIndent() throws SAXException {
        try {
            indent();
            for (int i = 0; i < deepIndentSize; ++i) {
                output.write(Character.toString(deepIndentCharacter).getBytes("UTF-8"));
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /**
     * Writes an XML node name to the output stream.
     *
     * @param uri The URI of the element or an empty string.
     * @param localName The local name of the element or an empty string.
     * @param qualifiedName The qualified name of the element or an empty
     * string.
     */
    private void writeName(String uri, String localName, String qualifiedName) throws SAXException {
        try {
            if (uri == null || uri.length() == 0) {
                if (qualifiedName == null || qualifiedName.length() == 0) {
                    output.write(localName.getBytes("UTF-8"));
                } else {
                    output.write(qualifiedName.getBytes("UTF-8"));
                }
            } else if (localName == null || localName.length() == 0) {
                output.write(qualifiedName.getBytes("UTF-8"));
            } else {
                String prefix = stackedIndexPeek(prefixesByNamespaceURI, uri);
                if (prefix == null || prefix.length() == 0) {
                    output.write(localName.getBytes("UTF-8"));
                } else {
                    output.write(prefix.getBytes("UTF-8"));
                    output.write(":".getBytes("UTF-8"));
                    output.write(localName.getBytes("UTF-8"));
                }
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /**
     * Writes an XML name/value pair to the output stream.
     *
     * @param uri The URI of the element or an empty string.
     * @param localName The local name of the element or an empty string.
     * @param qualifiedName The qualified name of the element or an empty
     * string.
     */
    private void writeNameValuePair(String uri, String localName, String qualifiedName, String value) throws SAXException {
        try {
            writeName(uri, localName, qualifiedName);
            output.write("=".getBytes("UTF-8"));
            output.write("\"".getBytes("UTF-8"));
            if (value != null) {
                for (int i = 0; i < value.length(); i++) {
                    switch (value.charAt(i)) {
                        case '"':
                            output.write("&quot;".getBytes("UTF-8"));
                            break;
                        case '&':
                            output.write("&amp;".getBytes("UTF-8"));
                            break;
                        case '<':
                            output.write("&lt;".getBytes("UTF-8"));
                            break;
                        case '>':
                            output.write("&gt;".getBytes("UTF-8"));
                            break;
                        default:
                            output.write(Character.toString(value.charAt(i)).getBytes("UTF-8"));
                    }
                }
            }
            output.write("\"".getBytes("UTF-8"));
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    /**
     * Pushes
     * <code>value</code> on to a stack stored in
     * <code>index</code> under
     * <code>key</code>.
     *
     * @param index The index of stacks by key.
     * @param key The key that identifies the stack.
     * @param value The value to push onto the stack.
     */
    @SuppressWarnings("unchecked")
    private void stackedIndexPush(Map<String, Object> index, String key, String value) {
        Object stack = index.get(key);
        if (stack == null) {
            index.put(key, value);
        } else {
            LinkedList<String> values = null;
            if (stack instanceof String) {
                values = new LinkedList<>();
                values.addFirst((String) stack);
                index.put(key, values);
            } else {
                values = (LinkedList<String>) stack;
            }
            values.addFirst(value);
        }
    }

    /**
     * Returns the top value of the stack stored in
     * <code>index</code> under
     * <code>key</code>.
     *
     * @param index The index of stacks by key.
     * @param key The key that identifies the stack.
     * @return The top value from the specified stack.
     */
    @SuppressWarnings("unchecked")
    private String stackedIndexPeek(Map<String, Object> index, String key) {
        Object stack = index.get(key);
        if (stack == null) {
            return null;
        } else if (stack instanceof String) {
            return (String) stack;
        } else {
            return ((LinkedList<String>) stack).getFirst();
        }
    }

    /**
     * Pops the top value off of the stack stored in
     * <code>index</code> under
     * <code>key</code>.
     *
     * @param index The index of stacks by key.
     * @param key The key that identifies the stack.
     * @return The value that was popped off the specified stack.
     */
    @SuppressWarnings("unchecked")
    private String stackedIndexPop(Map<String, Object> index, String key) {
        Object stack = index.get(key);
        if (stack == null) {
            return null;
        } else if (stack instanceof String) {
            index.remove(key);
            return (String) stack;
        } else {
            LinkedList<String> values = (LinkedList<String>) stack;
            String value = values.removeFirst();
            if (values.isEmpty()) {
                index.remove(key);
            }
            return value;
        }
    }

    /**
     * Removes the highest occurence of
     * <code>value</code> from the stack stored in
     * <code>index</code> under
     * <code>key</code>.
     *
     * @param index The index of stacks by key.
     * @param key The key that identifies the stack.
     * @return The value to remove from the specified stack.
     */
    @SuppressWarnings("unchecked")
    private void stackedIndexRemove(Map<String, Object> index, String key, String value) {
        Object stack = index.get(key);
        if (stack == null) {
            return;
        } else if (stack instanceof String) {
            if (stack.equals(value)) {
                index.remove(key);
            }
        } else {
            LinkedList<String> values = (LinkedList<String>) stack;
            values.remove(value);
            if (values.isEmpty()) {
                index.remove(key);
            }
        }
    }

    @Override
    public String toString() {
        return StringHelper.deleteEmptyRows(new String(output.toByteArray()));
    }

}
