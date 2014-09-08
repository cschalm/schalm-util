package org.schalm.util.helper.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger storing content in memory.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: InMemoryLogger.java 148 2014-02-12 20:50:20Z cschalm $
 */
public class InMemoryLogger {
    private final SimpleDateFormat sdf;
    private final ByteArrayOutputStream baos;
    private final PrintWriter writer;

    /**
     * Creates a new logger.
     *
     * @param sdf the desired format for the timestamps to every log entry
     */
    public InMemoryLogger(SimpleDateFormat sdf) {
        baos = new ByteArrayOutputStream();
        writer = new PrintWriter(baos);
        this.sdf = sdf;
    }

    /**
     * Get a <code>PrintWriter</code> to be used in Throwable.printStackTrace(PrintWriter s).
     *
     * @return a PrintWriter to store the Throwable's stacktrace
     */
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * Logs the given content by appending to internal buffer.
     *
     * @param content
     */
    public void log(String content) {
        writer.append(sdf.format(new Date())).append(": ");
        writer.append(content).append("\n");
    }

    /**
     * Logs the given content and the Throwable's stacktrace by appending to internal buffer.
     *
     * @param content
     * @param t
     */
    public void log(String content, Throwable t) {
        log(content);
        t.printStackTrace(writer);
        writer.append("\n");
    }

    /**
     * return the Logger's contents.
     *
     * @return
     */
    @Override
    public String toString() {
        writer.flush();
        return baos.toString();
    }

}
