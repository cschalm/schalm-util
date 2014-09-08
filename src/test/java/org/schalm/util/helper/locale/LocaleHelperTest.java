package org.schalm.util.helper.locale;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;
import org.junit.Test;
import org.schalm.util.helper.test.AbstractTest;

/**
 * Test of {@link LocaleHelper}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $id$
 */
public class LocaleHelperTest extends AbstractTest {

    /**
     * Test of {@link LocaleHelper#getResourceBundle()}.
     */
    @Test
    public void testGetResourceBundle() {
        assertNotNull(LocaleHelper.getResourceBundle());
    }

    /**
     * Test of {@link LocaleHelper#switchLanguage(java.util.Locale)}.
     */
    @Test
    public void testSwitchLanguageGerman() {
        LocaleHelper.switchLanguage(Locale.GERMAN);
        assertNotNull(LocaleHelper.getResourceBundle());
    }

}
