package org.schalm.util.helper.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.schalm.util.helper.test.AbstractTest;

/**
 * Unit-Test for {@link WebHelper}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $id$
 */
public class WebHelperTest extends AbstractTest {

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test of {@link WebHelper#getRequestHeaders(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetRequestHeaders() {
		when(request.getHeaderNames()).thenReturn(new StringTokenizer("one,two,three", ","));
		when(request.getHeaders("one")).thenReturn(new StringTokenizer("1"));
		when(request.getHeaders("two")).thenReturn(new StringTokenizer("2,2", ","));
		when(request.getHeaders("three")).thenReturn(new StringTokenizer("3,3,3", ","));

		Map<String, String> requestHeaders = WebHelper.getRequestHeaders(request);
		assertNotNull(requestHeaders);
		assertFalse(requestHeaders.isEmpty());
		assertEquals("1", requestHeaders.get("one"));
		assertEquals("2,<br />2", requestHeaders.get("two"));
		assertEquals("3,<br />3,<br />3", requestHeaders.get("three"));
	}

	/**
	 * Test of {@link WebHelper#getRequestAttributes(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetRequestAttributes() {
		when(request.getAttributeNames()).thenReturn(new StringTokenizer("one,two", ","));
		when(request.getAttribute("one")).thenReturn("1");
		when(request.getAttribute("two")).thenReturn("2");

		Map<String, String> requestAttributes = WebHelper.getRequestAttributes(request);
		assertNotNull(requestAttributes);
		assertFalse(requestAttributes.isEmpty());
		assertEquals("1", requestAttributes.get("one"));
		assertEquals("2", requestAttributes.get("two"));
	}

	/**
	 * Test of {@link WebHelper#getRequestParameters(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetRequestParameters() {
		when(request.getParameterNames()).thenReturn(new StringTokenizer("one,two,three", ","));
		when(request.getParameterValues("one")).thenReturn(new String[]{"1"});
		when(request.getParameterValues("two")).thenReturn(new String[]{"2", "2"});
		when(request.getParameterValues("three")).thenReturn(new String[]{"3", "3", "3"});

		Map<String, String> requestParameters = WebHelper.getRequestParameters(request);
		assertNotNull(requestParameters);
		assertFalse(requestParameters.isEmpty());
		assertEquals("1", requestParameters.get("one"));
		assertEquals("2, 2", requestParameters.get("two"));
		assertEquals("3, 3, 3", requestParameters.get("three"));
	}

	/**
	 * Test of {@link WebHelper#getSessionAttributes(javax.servlet.http.HttpServletRequest)} when HttpSession is NULL.
	 */
	@Test
	public void testGetSessionAttributesNullSession() {
		when(request.getSession(false)).thenReturn(null);

		Map<String, String> sessionAttributes = WebHelper.getSessionAttributes(request);
		assertNull(sessionAttributes);
	}

	/**
	 * Test of {@link WebHelper#getSessionAttributes(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetSessionAttributes() {
		when(session.getAttributeNames()).thenReturn(new StringTokenizer("one,two", ","));
		when(session.getAttribute("one")).thenReturn("1");
		when(session.getAttribute("two")).thenReturn("2");
		when(request.getSession(false)).thenReturn(session);

		Map<String, String> sessionAttributes = WebHelper.getSessionAttributes(request);
		assertNotNull(sessionAttributes);
		assertFalse(sessionAttributes.isEmpty());
		assertEquals("1", sessionAttributes.get("one"));
		assertEquals("2", sessionAttributes.get("two"));
	}

	/**
	 * Test of {@link WebHelper#getClassPath()}.
	 */
	@Test
	public void testGetClassPath() {
		List<String> classPath = WebHelper.getClassPath();
		assertNotNull(classPath);
		assertFalse(classPath.isEmpty());
		System.out.println("Size: " + classPath.size());
		System.out.println(Arrays.toString(classPath.toArray(new String[0])));
	}

	/**
	 * Test of {@link WebHelper#getSystemProperties()}.
	 */
	@Test
	public void testGetSystemProperties() {
		Map<String, String> systemProperties = WebHelper.getSystemProperties();
		assertNotNull(systemProperties);
		assertFalse(systemProperties.isEmpty());
	}

}
