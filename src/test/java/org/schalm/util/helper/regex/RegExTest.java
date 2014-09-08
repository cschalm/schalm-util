package org.schalm.util.helper.regex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.schalm.util.helper.test.AbstractTest;

/**
 * Unit-Test for {@link RegEx}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $id$
 */
public class RegExTest extends AbstractTest {
    @Test
    public void testIsValidEmail() {
        assertTrue("cschalm@users.sourceforge.net is NOT valid?!?", RegEx.EMAIL.isValid("cschalm@users.sourceforge.net"));
        assertTrue("a+b@ge.net is NOT valid?!?", RegEx.EMAIL.isValid("a+b@ge.net"));
        assertTrue("a_b.2@ge2.net3 is NOT valid?!?", RegEx.EMAIL.isValid("a_b.2@ge2.net3"));
    }

    @Test
    public void testIsInValidEmail() {
        assertFalse("@ab@ge.net IS valid?!?", RegEx.EMAIL.isValid("@ab@ge.net"));
        assertFalse("a@b@ge.net IS valid?!?", RegEx.EMAIL.isValid("a@b@ge.net"));
        assertFalse("a@@ge.net IS valid?!?", RegEx.EMAIL.isValid("a@@ge.net"));
    }

    @Test
    public void testIsValidBIC() {
        assertTrue("UBSWCHZH80A is NOT valid?!?", RegEx.BIC.isValid("UBSWCHZH80A"));
        assertTrue("SSKMDEMM is NOT valid?!?", RegEx.BIC.isValid("SSKMDEMM"));
        assertTrue("SSKMDEMMXXX is NOT valid?!?", RegEx.BIC.isValid("SSKMDEMMXXX"));
        assertTrue("MARKDEFF is NOT valid?!?", RegEx.BIC.isValid("MARKDEFF"));
        assertTrue("MARKDEFFXXX is NOT valid?!?", RegEx.BIC.isValid("MARKDEFFXXX"));
        assertTrue("NOLADE21KIE is NOT valid?!?", RegEx.BIC.isValid("NOLADE21KIE"));
    }

    @Test
    public void testIsInValidBIC() {
        assertFalse("MARKDEF IS valid?!?", RegEx.BIC.isValid("MARKDEF"));
        assertFalse("4711XX123 IS valid?!?", RegEx.BIC.isValid("4711XX123"));
        assertFalse("ABCDEF123456 IS valid?!?", RegEx.BIC.isValid("ABCDEF123456"));
    }

    @Test
    public void testIsValidIBAN() {
        assertTrue("CH510022522595291301C is NOT valid?!?", RegEx.IBAN.isValid("CH510022522595291301C"));
    }

    @Test
    public void testIsInValidIBAN() {
        assertFalse("CHDE1 IS valid?!?", RegEx.IBAN.isValid("CHDE1"));
    }

    @Test
    public void testIsValidUrl() {
        assertTrue("https://www.forwardurl.de/deinspace/meinspace.html?ich=Admin&amp;password=geheim123_.+-#spannend is NOT valid?!?",
            RegEx.URL.isValid("https://www.forwardurl.de/deinspace/meinspace.html?ich=Admin&amp;password=geheim123_.+-#spannend"));
    }

    @Test
    public void testIsInValidUrl() {
        assertFalse("https://www.url.de/meinspace?ich=Admin?password=geheim IS valid?!?", RegEx.URL.isValid("https://www.url.de/meinspace?ich=Admin?password=geheim"));
    }

}
