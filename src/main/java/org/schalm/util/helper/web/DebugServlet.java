package org.schalm.util.helper.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for displaying information about the system, the application and the request.<br />
 * This information includes:
 * <ul>
 * <li>the complete url of a request</li>
 * <li>the classpath</li>
 * <li>the system properties</li>
 * <li>the request headers</li>
 * <li>the request attributes</li>
 * <li>the request parameters</li>
 * <li>the session attributes</li>
 * <li>the cookies with all details</li>
 * </ul>
 * To use this servlet in your web application, you have to configure it in your WEB-INF/web.xml like this:<br />
 * <p>
 * <code>
 * &lt;servlet&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;servlet-name&gt;Debug Servlet&lt;/servlet-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;servlet-class&gt;org.schalm.util.helper.web.DebugServlet&lt;/servlet-class&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;<br />
 * &lt;/servlet&gt;<br />
 * &lt;servlet-mapping&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;servlet-name&gt;Debug Servlet&lt;/servlet-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;url-pattern&gt;/debug/*&lt;/url-pattern&gt;<br />
 * &lt;/servlet-mapping&gt;<br />
 * </code>
 * </p>
 * <p>
 * Like this you can insert &quot;/debug/&quot; into any url of your application to get all this information,<br />
 * e.g. &quot;http://localhost:8080/TestWeb<strong>/debug</strong>/helloWorld.jsf&quot;
 * </p>
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: DebugServlet.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class DebugServlet extends HttpServlet {
    private static final String STYLES
            = "body { font-family: Verdana, Arial, sans-serif; font-size: 12px; margin: 10px; } "
            + "table { border: 1px solid black; table-layout: fixed; width: 100%; border-collapse: collapse; } "
            + "td, th { margin: 0; padding: 2px; border: 1px solid black; } "
            + "th { background-color: gray; padding: 3px; border-bottom: 2px solid black; } "
            + ".pageHeadline { font-size: 16px; font-weight: bold; margin: 20px; } "
            + ".section { padding: 20px; } "
            + ".sectionHeadline { font-weight: bold; margin: 10px auto; } "
            + ".oddRow { background-color: gray; color: white; } "
            + ".evenRow { background-color: white; color: black; }";

    @Override
    public String getServletInfo() {
        return "Servlet showing debug information";
    }

    @Override
    public String getServletName() {
        return getClass().getName();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDebug(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDebug(req, resp);
    }

    private void doDebug(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<String> classPathList = WebHelper.getClassPath();
        final Map<String, String> systemPropertiesMap = WebHelper.getSystemProperties();
        final Map<String, String> headersMap = WebHelper.getRequestHeaders(req);
        final Map<String, String> requestAttributesMap = WebHelper.getRequestAttributes(req);
        final Map<String, String> requestParametersMap = WebHelper.getRequestParameters(req);
        final Map<String, String> sessionAttributesMap = WebHelper.getSessionAttributes(req);

        PrintWriter writer = resp.getWriter();
        writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
                + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n");
        writer.write("<head>\n");
        writer.write("<title>Debug Information</title>\n");
        writer.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n");
        writer.write("<style type=\"text/css\">" + STYLES + "</style>\n");
        writer.write("</head>\n");
        writer.write("<body>\n");
        writer.write("<div id=\"top\">\n");
        writer.write("<div class=\"pageHeadline\">Debug Information</div>\n");
        writer.write("<div class=\"section\">\n");
        writer.write("<p>This page provides the following information:</p>\n");
        writer.write("<ul>\n");
        writer.write("<li>The <a href=\"#location\">Location</a> of this page</li>\n");
        writer.write("<li>The <a href=\"#classpath\">Classpath</a> of this web application</li>\n");
        writer.write("<li>The <a href=\"#systemProperties\">System Properties</a> of this vm</li>\n");
        writer.write("<li>The <a href=\"#requestHeaders\">Request Headers</a> of this request</li>\n");
        writer.write("<li>The <a href=\"#requestAttributes\">Request Attributes</a> of this request</li>\n");
        writer.write("<li>The <a href=\"#requestParameters\">Request Parameters</a> of this request</li>\n");
        writer.write("<li>The <a href=\"#sessionAttributes\">Session Attributes</a> of this web application</li>\n");
        writer.write("<li>The <a href=\"#cookies\">Cookies</a> of this web application</li>\n");
        writer.write("</ul>\n");
        writer.write("</div>\n");
        writer.write("<div id=\"location\" class=\"section\">\n");
        writer.write("<div class=\"sectionHeadline\">Location</div>\n");
        writer.write("<code>" + req.getMethod() + " " + req.getRequestURI() + "</code><br />\n");
        writer.write("<a href=\"#top\">top</a>\n");
        writer.write("</div>\n");
        writer.write("<div id=\"classpath\" class=\"section\">\n");
        writer.write("<div class=\"sectionHeadline\">Classpath</div>\n");
        writer.write("<ul>\n");
        for (String classPath : classPathList) {
            writer.write("<li>" + classPath + "</li>\n");
        }
        writer.write("</ul>\n");
        writer.write("<a href=\"#top\">top</a>\n");
        writer.write("</div>\n");
        printSection(writer, "System Properties", "systemProperties", systemPropertiesMap);
        printSection(writer, "Request Headers", "requestHeaders", headersMap);
        printSection(writer, "Request Attributes", "requestAttributes", requestAttributesMap);
        printSection(writer, "Request Parameters", "requestParameters", requestParametersMap);
        printSection(writer, "Session Attributes", "sessionAttributes", sessionAttributesMap);
        printCookieSection(writer, req);
        writer.write("</div>\n");
        writer.write("</body>\n");
        writer.write("</html>\n");
        writer.flush();
    }

    private void printSection(Writer writer, String title, String id, Map<String, String> data) throws IOException {
        writer.write("<div id=\"" + id + "\" class=\"section\">\n");
        writer.write("<div class=\"sectionHeadline\">" + title + "</div>\n");
        writer.write("<table class=\"dataTable\">\n");
        writer.write("<tr class=\"headerRow\"><th>Key</th><th>Value</th></tr>\n");
        if (data != null) {
            int rowIndex = 0;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write("<tr class=\"" + (rowIndex % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>" + entry.getKey() + "</td><td>"
                        + entry.getValue() + "</td></tr>\n");
                rowIndex++;
            }
        }
        writer.write("</table>\n");
        writer.write("<a href=\"#top\">top</a>\n");
        writer.write("</div>\n");
    }

    private void printCookieSection(PrintWriter writer, HttpServletRequest req) {
        writer.write("<div id=\"cookies\" class=\"section\">\n");
        writer.write("<div class=\"sectionHeadline\">Cookies</div>\n");
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                writer.write("<table class=\"dataTable\">\n");
                writer.write("<tr class=\"headerRow\"><th>Key</th><th>Value</th></tr>\n");
                int rowIndex = 0;
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Name</td><td>"
                        + cookies[i].getName() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Value</td><td>"
                        + cookies[i].getValue() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Comment</td><td>"
                        + cookies[i].getComment() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Domain</td><td>"
                        + cookies[i].getDomain() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Max Age</td><td>"
                        + cookies[i].getMaxAge() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Path</td><td>"
                        + cookies[i].getPath() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Version</td><td>"
                        + cookies[i].getVersion() + "</td></tr>\n");
                writer.write("<tr class=\"" + (rowIndex++ % 2 == 0 ? "evenRow" : "oddRow") + "\"><td>Secure</td><td>"
                        + cookies[i].getSecure() + "</td></tr>\n");

                writer.write("</table>\n<br />\n");
            }
        }
        writer.write("<a href=\"#top\">top</a>\n");
        writer.write("</div>\n");
    }

}
