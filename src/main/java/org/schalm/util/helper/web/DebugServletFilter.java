package org.schalm.util.helper.web;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <code>ServletFilter</code> logging debug information to the logfile.<br />
 * This information includes:
 * <ul>
 * <li>the complete url of a request</li>
 * <li>the request headers</li>
 * <li>the request attributes</li>
 * <li>the request parameters</li>
 * <li>the session attributes</li>
 * </ul>
 * To use this filter in your web application, you have to configure it in your WEB-INF/web.xml like this:<br />
 * <p>
 * <code>
 * &lt;filter&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;filter-name&gt;DebugFilter&lt;/filter-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;filter-class&gt;org.schalm.util.helper.web.DebugServletFilter&lt;/filter-class&gt;<br />
 * &lt;/filter&gt;<br />
 * &lt;filter-mapping&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;filter-name&gt;DebugFilter&lt;/filter-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&lt;servlet-name&gt;*&lt;/servlet-name&gt;<br />
 * &lt;/filter-mapping&gt;<br />
 * </code>
 * </p>
 * <p>
 * <strong>Note:</strong>The filter is logging the output only, if the log-level for this class is set to debug!
 * </p>
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: DebugServletFilter.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class DebugServletFilter implements Filter {
    private static final Log log = LogFactory.getLog(DebugServletFilter.class);

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        if (log.isDebugEnabled() && request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            final Map<String, String> headersMap = WebHelper.getRequestHeaders(req);
            final Map<String, String> requestAttributesMap = WebHelper.getRequestAttributes(req);
            final Map<String, String> requestParametersMap = WebHelper.getRequestParameters(req);
            final Map<String, String> sessionAttributesMap = WebHelper.getSessionAttributes(req);
            StringBuilder sb = new StringBuilder("Debug Information:\n");
            sb.append("Requested url: ").append(req.getMethod()).append(" ").append(req.getRequestURI()).append("\n");
            getSection(sb, "Request Header", headersMap);
            getSection(sb, "Request Attributes", requestAttributesMap);
            getSection(sb, "Request Parameters", requestParametersMap);
            getSection(sb, "Session Attributes", sessionAttributesMap);
            log.debug(sb.toString());
        }
        fc.doFilter(request, response);
    }

    private static void getSection(StringBuilder sb, String title, Map<String, String> data) {
        sb.append(title).append(":\n");
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                sb.append("Key: ").append(entry.getKey()).append("\tValue: ").append(entry.getValue()).append("\n");
            }
        }
    }

}
