package org.schalm.util.helper.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.schalm.util.helper.test.AbstractTest;

/**
 * Unit-Test for {@link TimeHelper}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $id$
 */
public class TimeHelperTest extends AbstractTest {
    private static final Logger log = Logger.getLogger(TimeHelperTest.class);

    @Test
    public void testMeasurement() {
        TimeHelper timeHelper = new TimeHelper();
        timeHelper.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            log.fatal(ex);
        }
        String result = timeHelper.stop();
        assertNotNull(result);
        assertFalse("Measured time is EMPTY!", result.isEmpty());
    }

    @Test
    public void testGetTimeString() {
        assertEquals("Expected human-readable time is wrong!", "0,9 ms", TimeHelper.getTimeString(900_000));
        assertEquals("Expected human-readable time is wrong!", "1,9 ms", TimeHelper.getTimeString(1_900_000));
        assertEquals("Expected human-readable time is wrong!", "1.900 ms (1,9 sec)", TimeHelper.getTimeString(1_900_000_000));
    }

}
