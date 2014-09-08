package org.schalm.util.helper.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.schalm.util.helper.file.FileHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Helper for operations on xml objects.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: XmlHelper.java 163 2014-03-06 14:02:38Z cschalm $
 */
public final class XmlHelper {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private XmlHelper() {
        // utility class
    }

    /**
     * Converts the specified node to a string, recursively.
     *
     * @param node
     * @return a textual representation of the node
     */
    public static String domTree2String(final Node node) {
        XmlHelper xmlHelper = new XmlHelper();
        Helper helper = new Helper();
        return xmlHelper.domTree2String(node, helper);
    }

    private static class Helper {

        private boolean breakLine;

        public Helper() {
        }

        public boolean isBreakLine() {
            return breakLine;
        }

        public void setBreakLine(boolean breakLine) {
            this.breakLine = breakLine;
        }

    }

    private String domTree2String(final Node node, Helper helper) {
        StringBuilder sb = new StringBuilder();
        int type = node.getNodeType();
        switch (type) {
            case Node.DOCUMENT_NODE:
                sb.append("<?xml version=\"1.0\"?>");
                sb.append(domTree2String(((Document) node).getDocumentElement(), helper));
                break;
            case Node.ELEMENT_NODE:
                sb.append(XmlHelper.LINE_SEPARATOR).append("<").append(node.getNodeName());
                NamedNodeMap attrs = node.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node attr = attrs.item(i);
                    sb.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
                }
                sb.append(">");
                helper.setBreakLine(false);
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++) {
                        sb.append(domTree2String(children.item(i), helper));
                    }
                }
                break;
            case Node.ENTITY_REFERENCE_NODE:
                sb.append("&").append(node.getNodeName()).append(";");
                helper.setBreakLine(false);
                break;
            case Node.CDATA_SECTION_NODE:
                sb.append("<![CDATA[").append(node.getNodeValue()).append("]]>");
                helper.setBreakLine(false);
                break;
            case Node.TEXT_NODE:
                sb.append(node.getNodeValue());
                helper.setBreakLine(false);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                sb.append("<?").append(node.getNodeName());
                String data = node.getNodeValue();
                sb.append(" ").append(data);
                sb.append("?>").append(XmlHelper.LINE_SEPARATOR);
                break;
            case Node.COMMENT_NODE:
                sb.append("<!--").append(node.getNodeValue()).append("-->");
                helper.setBreakLine(false);
                break;
            default:
                sb.append(node.getNodeValue());
                helper.setBreakLine(false);
                break;
        }
        if (type == Node.ELEMENT_NODE) {
            if (helper.isBreakLine()) {
                sb.append(XmlHelper.LINE_SEPARATOR);
            }
            sb.append("</").append(node.getNodeName()).append(">");
            helper.setBreakLine(true);
        }
        return sb.toString();
    }

    /**
     * Reads the file and returns the document.
     *
     * @param file
     * @return the document from the file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document readXmlFile(File file) throws ParserConfigurationException, SAXException, IOException {
        return readXmlStream(new FileInputStream(file));
    }

    /**
     * Reads xml from a stream and returns the document.
     *
     * @param stream the input to read from
     * @return DOM document of the input
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document readXmlStream(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document domDoc = builder.parse(stream);

        return domDoc;
    }

    /**
     * Writes the document to the file<br/>.
     *
     * @param doc
     * @param file
     * @throws IOException
     */
    public static void saveXmlFile(Document doc, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            String content = "<?xml version=\"1.0\"?>";
            content += XmlHelper.domTree2String(doc.getDocumentElement());
            bw.write(content, 0, content.length());
            bw.flush();
        }
    }

    /**
     * Get the value of the first tag found with the given name in the given document<br/>.
     *
     * @param doc
     * @param tagName
     * @return the value of the node or the empty string, if no node with this name exits in this document
     */
    public static String getTagValue(Document doc, String tagName) {
        NodeList tagList = doc.getElementsByTagName(tagName);
        if (tagList.getLength() > 0) {
            return tagList.item(0).getFirstChild().getNodeValue().trim();
        }
        return "";
    }

    /**
     * Set the value of the first tag found with the given name in the given document<br/>. If the tag with the
     * requested name doesn't have a child as textnode with the tagValue is created.
     *
     * @param doc
     * @param tagName
     * @param tagValue
     */
    public static void setTagValue(Document doc, String tagName, String tagValue) {
        NodeList tagList = doc.getElementsByTagName(tagName);
        if (tagList.getLength() > 0) {
            if (tagList.item(0).getFirstChild() != null) {
                tagList.item(0).getFirstChild().setNodeValue(tagValue);
            } else {
                tagList.item(0).appendChild(doc.createTextNode(tagValue));
            }
        }
    }

    /**
     * Get the value of the attribute with the given name of the first tag found with the given tag name in the given
     * document<br/>.
     *
     * @param doc
     * @param tagName
     * @param attributeName
     * @return the value of the attribute with the given name of the node or the empty string, if no node with this name
     * exits in this document or the attribute does not exist
     */
    public static String getTagAttributeValue(Document doc, String tagName, String attributeName) {
        NodeList tagList = doc.getElementsByTagName(tagName);
        if (tagList.getLength() > 0) {
            NamedNodeMap attributes = tagList.item(0).getAttributes();
            if (attributes != null) {
                Node attribute = attributes.getNamedItem(attributeName);
                if (attribute != null) {
                    return attribute.getNodeValue().trim();
                }
            }
        }
        return "";
    }

    /**
     * Performs a xpath query on a document and returns the matching nodelist.
     *
     * @param doc
     * @param xpathQuery
     * @return nodelist of matches
     * @throws Exception
     */
    public static NodeList query(Document doc, String xpathQuery) throws Exception {
        NodeList result = null;
        // prepare XPath
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            // query xml
            result = (NodeList) xpath.evaluate(xpathQuery, doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpx) {
            throw new Exception("Error evaluating XPath: " + xpx.getMessage(), xpx);
        }
        return result;
    }

    /**
     * Performs a xpath query on a document and returns the matching nodelist.
     *
     * @param context the Context where to start with the query
     * @param query the XPath-Query
     * @return nodelist of matches
     * @throws Exception on error
     */
    public static NodeList query(Node context, String query) throws Exception {
        NodeList result = null;
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            result = (NodeList) xpath.evaluate(query, context, XPathConstants.NODESET);
        } catch (XPathExpressionException xpx) {
            throw new Exception("Error evaluating XPath: " + xpx.getMessage(), xpx);
        }
        return result;
    }

    /**
     * Parse a file and send the sax events to the content handler.
     *
     * @param file
     * @param handler
     * @throws IOException
     * @throws TransformerException
     */
    public static void parse(File file, ContentHandler handler) throws IOException, TransformerException {
        final SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        final Transformer transformer = factory.newTransformer();
        transformer.transform(new StreamSource(new FileReader(file)), new SAXResult(handler));
    }

    /**
     * Read a file and return it as beautified indented string.
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String indentXmlSax(File file) throws Exception {
        final String fileContents = FileHelper.readTextFile(file);
        int endOfPrologue = fileContents.indexOf("?>");
        String prologue = fileContents.substring(0, endOfPrologue + 2);
        StringBuffer sb = new StringBuffer(prologue).append(LINE_SEPARATOR);
        IndentContentHandler handler = new IndentContentHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(file, handler);
        sb.append(handler.toString());

        return sb.toString();
    }

    /**
     * Returns the String-Representation of the given DOM-Node as well-formed DOM-Document.
     *
     * @param node DOM-Node to print
     * @param indent if true resulting XML is endented
     * @return <code>String</code> - Node as XML-String
     * @throws Exception on error
     */
    public static String domNode2String(Node node, boolean indent) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(node);
        transformer.transform(source, result);

        String xmlString = result.getWriter().toString();

        return xmlString;
    }

    /**
     * Returns true if the two DOM-Documents are structurally the same.
     *
     * @param first xml-text of first Document to compare
     * @param second xml-text of second Document to compare
     * @return <code>boolean</code> - true if both Documents are structurally the same
     * @throws Exception on error
     */
    public static boolean equalsStructurally(String first, String second) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);

        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document docOne = documentBuilder.parse(new InputSource(new StringReader(first)));
        Document docTwo = documentBuilder.parse(new InputSource(new StringReader(second)));

        return equalsStructurally(docOne, docTwo);
    }

    /**
     * Returns true if the two Documents are structurally the same.
     *
     * @param first first Document to compare
     * @param second second Document to compare
     * @return <code>boolean</code> - true if both Documents are structurally the same
     * @throws Exception on error
     */
    public static boolean equalsStructurally(Document first, Document second) throws Exception {
        first.normalizeDocument();
        second.normalizeDocument();

        return first.isEqualNode(second);
    }

    public static String indentXmlDom(String inXml) throws Exception {
        final InputSource src = new InputSource(new StringReader(inXml));
        final Document domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);
        String encoding = domDoc.getXmlEncoding();
        if (encoding == null) {
            // defaults to UTF-8
            encoding = "UTF-8";
        }
        final Node document = domDoc.getDocumentElement();
        final boolean keepDeclaration = inXml.startsWith("<?xml");

        final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        final LSSerializer writer = impl.createLSSerializer();

        // Set this to true if the output needs to be beautified.
        writer.getDomConfig().setParameter("format-pretty-print", true);
        // Set this to true if the declaration is needed to be outputted.
        writer.getDomConfig().setParameter("xml-declaration", keepDeclaration);

        LSOutput lsOutput = impl.createLSOutput();
        lsOutput.setEncoding(encoding);
        Writer stringWriter = new StringWriter();
        lsOutput.setCharacterStream(stringWriter);
        writer.write(document, lsOutput);

        return stringWriter.toString();
    }

    /**
     * Read a file and return it as beautified indented string.
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String indentXmlSax2(File file) throws Exception {
        XMLWriter handler = new XMLWriter();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(file, handler);

        return handler.toString();
    }

}
