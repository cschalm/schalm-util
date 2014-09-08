package org.schalm.util.helper.log;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.Test;
import org.schalm.util.helper.test.AbstractTest;

/**
 * UnitTest for {@link InMemoryLogger}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: InMemoryLoggerTest.java 161 2014-03-06 13:27:59Z cschalm $
 */
public class InMemoryLoggerTest extends AbstractTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);

    @Test
    public void testLogText() {
        String testText = "abcdefghijklmnopqrstuvwxyz";
        InMemoryLogger log = new InMemoryLogger(sdf);
        log.log(testText);
        final String result = log.toString();
        assertFalse("Nothing in logfile!", result.isEmpty());
        assertTrue("Content in logfile is missing!", result.indexOf(testText) > 0);
    }

    @Test
    public void testLogException() {
        InMemoryLogger log = new InMemoryLogger(sdf);
        try {
            System.out.println(10 / (5 - 3 - 2));
        } catch (Exception e) {
            e.printStackTrace(log.getWriter());
        }
        assertFalse(log.toString().isEmpty());
    }

    @Test
    public void testLogExceptionAndText() {
        InMemoryLogger log = new InMemoryLogger(sdf);
        try {
            System.out.println(10 / (5 - 3 - 2));
        } catch (Exception e) {
            log.log("Error: " + e.getMessage(), e);
        }
        // System.out.println(log.toString());
        assertFalse(log.toString().isEmpty());
    }

}
