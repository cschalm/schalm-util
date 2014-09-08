package org.schalm.util.helper.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.junit.BeforeClass;

/**
 * Abstract SuperClass for tests to initialize logging.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: AbstractTest.java 161 2014-03-06 13:27:59Z cschalm $
 * @since 13.02.2012
 */
public abstract class AbstractTest {
    private static final Logger log = Logger.getLogger(AbstractTest.class);
    private static final int BUFFER_SIZE = 0x10000; // input buffer size in bytes (64 KB)

    /**
     * Initializes log4j for logging and sets-up the security-context.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        URL configURL = Loader.getResource("log4j_test.properties");
        if (configURL != null) {
            System.out.println("loading \"log4j_test.properties\" from " + configURL.toExternalForm());
            PropertyConfigurator.configure(configURL);
        } else {
            System.err.println("unable to load \"log4j_test.properties\", using defaults!");
            BasicConfigurator.configure();
            Logger.getRootLogger().setLevel(Level.DEBUG);
            Logger.getLogger("org.hibernate").setLevel(Level.WARN);
            Logger.getLogger("org.hibernatespatial").setLevel(Level.WARN);
        }
    }

    /**
     * Read a file and return its content as text.
     *
     * @param fileName file to read
     * @return <code>String</code> - file's content as text
     */
    public String readTextFile(String fileName) {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count;
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
            log.info("Read " + out.size() + " bytes from " + fileName);

            return out.toString();
        } catch (IOException e) {
            log.error("Unable to read file " + fileName + "! " + e.getMessage(), e);
            return "";
        }
    }

    /**
     * Assertion helper: Assert that a collection is not null and not empty.
     *
     * @param message the error message to display in case
     * @param collection the collection to check
     */
    public void assertNotEmpty(final String message, final Collection<?> collection) {
        assertNotNull(message, collection);
        assertFalse(message, collection.isEmpty());
    }

}
