package org.schalm.util.helper.locale;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper class for I18N.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: LocaleHelper.java 161 2014-03-06 13:27:59Z cschalm $
 */
public final class LocaleHelper {
    private static ResourceBundle rb = ResourceBundle.getBundle("org.schalm.util.helper.locale.schalm", Locale.GERMAN);

    private LocaleHelper() {
        // utility class
    }

    public static void switchLanguage(Locale locale) {
        LocaleHelper.rb = ResourceBundle.getBundle("org.schalm.util.helper.locale.schalm", locale);
    }

    public static ResourceBundle getResourceBundle() {
        return rb;
    }

}
