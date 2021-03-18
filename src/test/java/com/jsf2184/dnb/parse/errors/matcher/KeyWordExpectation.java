package com.jsf2184.dnb.parse.errors.matcher;

import java.util.Arrays;
import java.util.List;

public class KeyWordExpectation {
    String input;
    KeyWordEnum expected;

    public KeyWordExpectation(String input, KeyWordEnum expected) {
        this.input = input;
        this.expected = expected;
    }

    public static List<KeyWordExpectation> getExpectationList() {
        return Arrays.asList(
                new KeyWordExpectation("no", KeyWordEnum.NO),
                new KeyWordExpectation("failed", KeyWordEnum.FAILED),
                new KeyWordExpectation("incorrect", KeyWordEnum.INCORRECT),
                new KeyWordExpectation("error", KeyWordEnum.ERROR),

                // some simple tests where case adjustment does the trick.
                new KeyWordExpectation("NO", KeyWordEnum.NO),
                new KeyWordExpectation("fAILEd", KeyWordEnum.FAILED),
                new KeyWordExpectation("INCORRECT", KeyWordEnum.INCORRECT),
                new KeyWordExpectation("eRRor", KeyWordEnum.ERROR),

                // special treatment for number zero
                new KeyWordExpectation("0", KeyWordEnum.NUMZERO),
                new KeyWordExpectation("00000", KeyWordEnum.NUMZERO),

                // tests that succeed because of special plural allowances
                new KeyWordExpectation("eRRors", KeyWordEnum.ERROR),
                new KeyWordExpectation("failures", KeyWordEnum.FAILURE),
                new KeyWordExpectation("excepTIONS", KeyWordEnum.EXCEPTION),

                // tests that fail because there is no special plural allowance
                new KeyWordExpectation("INCORRECTS", null),
                new KeyWordExpectation("invalids", null),

                // And a bunch that just don't match anything
                new KeyWordExpectation(null, null),
                new KeyWordExpectation("", null),
                new KeyWordExpectation("abc", null),
                new KeyWordExpectation("00009", null),
                new KeyWordExpectation("9", null)
        );
    }

    public String getInput() {
        return input;
    }

    public KeyWordEnum getExpected() {
        return expected;
    }
}
