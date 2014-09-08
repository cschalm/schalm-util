package org.schalm.util.helper.regex;

import java.util.regex.Pattern;

/**
 * Enumeration for convenient usage of Regular Expressions.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $id$
 */
public enum RegEx {
    EMAIL("[^@]+@[^\\.|^@]+\\..+"),
    BIC("[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?"),
    IBAN("[A-Z]{2}[0-9]{2}[0-9A-Z]{1,30}"),
    URL("(https?\\:\\/\\/)?([a-z0-9\\-.]*)(\\.([a-z]{2,3}))+(\\:[0-9]{2,5})?(\\/([a-z0-9+_-]\\.?)+)"
            + "*\\/?(\\?[a-zA-Z+&amp;$_.-][a-zA-Z0-9;:@&amp;%=+/$_.-]*)?(#[a-zA-Z_.-][a-zA-Z0-9+$_.-]*)?");
    private final String pattern;

    private RegEx(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get the pattern for this Regular Expression.
     *
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Test a given String if it's syntactically correct regarding the Regular
     * Expression behind it's type.
     *
     * @param input Strimng to check
     * @return true if valid
     */
    public boolean isValid(String input) {
        return Pattern.matches(getPattern(), input);
    }

}
