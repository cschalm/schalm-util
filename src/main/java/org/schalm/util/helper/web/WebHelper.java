package org.schalm.util.helper.web;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Helper class for web applications.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: WebHelper.java 165 2014-03-11 14:37:46Z cschalm $
 */
public final class WebHelper {

	private WebHelper() {
		// utility classes do not need a public constructor
	}

	/**
	 * Get all header fields for a request.
	 *
	 * @param req the request to examine
	 * @return a map with key-value pairs containing all header fields and their values
	 */
	public static Map<String, String> getRequestHeaders(HttpServletRequest req) {
		Map<String, String> headersMap = new HashMap<>();
		Enumeration headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			Enumeration headerValues = req.getHeaders(headerName);
			StringBuilder sb = new StringBuilder();
			while (headerValues.hasMoreElements()) {
				String headerValue = (String) headerValues.nextElement();
				if (sb.length() > 0) {
					sb.append(",<br />");
				}
				sb.append(headerValue);
			}
			headersMap.put(headerName, sb.toString());
		}
		return headersMap;
	}

	/**
	 * Get all attributes for a request.
	 *
	 * @param req the request to examine
	 * @return a map with key-value pairs containing all attributes and their values
	 */
	public static Map<String, String> getRequestAttributes(HttpServletRequest req) {
		Map<String, String> attributesMap = new HashMap<>();
		Enumeration attributeNames = req.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = (String) attributeNames.nextElement();
			Object attributeValue = req.getAttribute(attributeName);
			attributesMap.put(attributeName, attributeValue.toString());
		}
		return attributesMap;
	}

	/**
	 * Get all parameters for a request.
	 *
	 * @param req the request to examine
	 * @return a map with key-value pairs containing all parameters and their values
	 */
	public static Map<String, String> getRequestParameters(HttpServletRequest req) {
		Map<String, String> parametersMap = new HashMap<>();
		Enumeration parameterNames = req.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			String[] parameterValues = req.getParameterValues(parameterName);
			StringBuilder sb = new StringBuilder();
			if (parameterValues != null) {
				for (String parameterValue : parameterValues) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(parameterValue);
				}
				parametersMap.put(parameterName, sb.toString());
			}
		}
		return parametersMap;
	}

	/**
	 * Get all attributes of the session for a request.
	 *
	 * @param req the request to examine
	 * @return a map with key-value pairs containing all session attributes and their values
	 */
	public static Map<String, String> getSessionAttributes(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		Map<String, String> attributesMap = new HashMap<>();
		Enumeration attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = (String) attributeNames.nextElement();
			Object attributeValue = session.getAttribute(attributeName);
			attributesMap.put(attributeName, attributeValue.toString());
		}
		return attributesMap;
	}

	/**
	 * Get the classpath for the current thread.
	 *
	 * @return a list of path entries
	 */
	public static List<String> getClassPath() {
		List<String> classPathList = new ArrayList<>();
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader parent = null;
		while (currentClassLoader != null && !currentClassLoader.equals(parent)) {
			if (currentClassLoader instanceof URLClassLoader) {
				URL[] urls = ((URLClassLoader) currentClassLoader).getURLs();
				for (URL url : urls) {
					classPathList.add(url.toString());
				}
			}
			parent = currentClassLoader;
			currentClassLoader = currentClassLoader.getParent();
		}
		return classPathList;
	}

	/**
	 * Get all system properties.
	 *
	 * @return a map with key-value pairs containing all system properties and their values
	 */
	public static Map<String, String> getSystemProperties() {
		Map<String, String> propertiesMap = new HashMap<>();
		for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
			propertiesMap.put(entry.getKey().toString(), String.valueOf(entry.getValue()));
		}
		return propertiesMap;
	}

}
