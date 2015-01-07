package org.schalm.util.helper.app;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Helper to measure times in a application.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: TimeHelper.java 167 2014-04-16 09:10:12Z cschalm $
 */
public class TimeHelper {
    private long startTime;

    /**
     * Beginn the measuremnet of time.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * End the measurement of time.
     *
     * @return the time difference between start and stop human readable
     */
    public String stop() {
        return getTimeString(System.nanoTime() - startTime);
    }

    /**
     * Get a human readable String of the given time-period.
     *
     * @param nanoTime time-period
     * @return human readable String
     */
    public static String getTimeString(long nanoTime) {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        float time = nanoTime;
        time /= 1000000;
        String result = nf.format(time) + " ms";
        if (time >= 1000) {
            time /= 1000;
            result += " (" + nf.format(time) + " sec)";
        }

        return result;
    }

}
