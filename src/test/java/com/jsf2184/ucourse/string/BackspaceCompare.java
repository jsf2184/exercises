package com.jsf2184.ucourse.string;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class BackspaceCompare {

    @Test
    public void testIt() {
        Assert.assertTrue(backspaceCompare("#a#bcd#", "bc"));
        Assert.assertTrue(backspaceCompare("##", ""));
        Assert.assertTrue(backspaceCompare("ab##", ""));
        Assert.assertTrue(backspaceCompare("bxj##tw", "bxo#j##tw"));
        Assert.assertTrue(backspaceCompare("abcd##", "ab"));
        Assert.assertFalse(backspaceCompare("#a#bcd#", "c"));
        Assert.assertFalse(backspaceCompare("#a#b#cd#", "bc"));
    }

    @Test
    public void testBuildStringWithFetcher() {
        Assert.assertEquals ("btw", StringUtils.reverse(buildStringWithFetcher("bxj##tw")));
        Assert.assertEquals ("btw", StringUtils.reverse(buildStringWithFetcher("bxo#j##tw")));

    }

    public boolean backspaceCompare(String S, String T) {
        Fetcher sfetch = new Fetcher(S);
        Fetcher tfetch = new Fetcher(T);
        Character schar;
        Character tchar;
        while (true) {
            schar = sfetch.getChar();
            tchar = tfetch.getChar();
            if (schar == null || tchar == null) {
                break;
            }
            if (!schar.equals(tchar)) {
                return false;
            }
        }
        return schar == null && tchar == null;
    }

    public static String buildStringWithFetcher(String s) {
        Fetcher fetcher = new Fetcher(s);
        Character c;
        StringBuilder sb = new StringBuilder();
        while ((c = fetcher.getChar()) != null) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static class Fetcher {
        String s;
        int idx;
        int eCount;
        public Fetcher(String s) {
            this.s = s;
            idx = s.length() - 1;
            eCount = 0;
        }


        Character getChar() {
            while (idx >= 0) {
                if (s.charAt(idx) == '#') {
                    eCount++;
                    idx--;
                    continue;
                }
                if (eCount > 0) {
                    eCount--;
                    idx--;
                    continue;
                }
                Character res = s.charAt(idx);
                idx--;
                return res;
            }
            return null;
        }
    }
}
