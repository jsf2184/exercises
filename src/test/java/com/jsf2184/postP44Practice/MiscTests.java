package com.jsf2184.postP44Practice;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MiscTests {

    @Test
    public void testNumDiffPairs() {
        testOnce(new int[]{1, 3, 4, 5, 7}, 3);
        testOnce(new int[]{1, 7, 5, 9, 2, 12, 3}, 4);
        int v = stringSize(null);
        System.out.println(v);

        callIt(null);
        callIt("                ");
        callIt("abc");
        callIt("   5 ");


    }

    public void callIt(String s ) {
        System.out.printf("\nCallIt with input: %s\n", s);
        try {
            System.out.printf("got %s\n", toInteger(s));
        } catch (Exception e) {
            System.out.printf("caught exception: %s\n", e.getMessage() );
        }
    }
    public static int toInteger(String s) throws Exception {
        if (s == null ) {
            throw new Exception("input line must not be null");
        }
        s = s.trim();
        if (s.length() == 0) {
            throw new Exception("missing integer input");
        }
        int result = Integer.parseInt(s);
        return result;
    }

    static HashMap<Character, Character> closerToOpener;
    static {
        closerToOpener = new HashMap<>();
        closerToOpener.put(')', '(');
        closerToOpener.put(']', '[');
        closerToOpener.put('}', '{');
    }

    @Test
    public void callIsBalanced() {
        Assert.assertTrue(isBalanced("()"));
    }
    public static boolean isBalanced(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i=0; i<s.length(); i++) {
            boolean pushOp = false;
            char c = s.charAt(i);
            Character expectedOpener = closerToOpener.get(c);
            if (expectedOpener == null) {
                // not a closer be an opener so stack it.
                stack.push(c);
            } else {
                // its a closer. See if the stack has the expected value.
                if (stack.empty()) {
                    // the stack has nothing so we have an unexpected closer
                    return false;
                }
                Character priorOpener = stack.pop();
                if (!priorOpener.equals(expectedOpener)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();

    }

    @Test
    public void cmeFindLowestUniqueNumber() {
        processNumLine("1 4 1 4 8 9 ");
    }

    public static void processNumLine(String line) {
        int countMap[] = new int[32768];
        Scanner scanner = new Scanner(line);
        while (scanner.hasNextInt()) {
            int val = scanner.nextInt();
            countMap[val]++;
        }

        for (int i = 1; i<countMap.length; i++) {
            if (countMap[i] == 1) {
                System.out.printf("%d\n", i);
                break;
            }
        }
    }

    @Test
    public void testCmeRansom() throws IOException {
        System.out.println( canConstructNote("AAA", Arrays.asList("AAA")));
    }

    public boolean canConstructNote(String note, List<String> magazineStrings) throws IOException {
        int[] magazineChars = new int[256];
        for (String mag : magazineStrings) {
            for (int i=0; i<mag.length(); i++) {
                char c = mag.charAt(i);
                magazineChars[c]++;
            }
        }

        for (int i=0; i< note.length(); i++) {
            char n = note.charAt(i);
            if (n != ' ') {
                magazineChars[n]--;
                if (magazineChars[n] < 0) {
                    return false;
                }
            }
        }
        return true;
    }








    public static int stringSize(Object s) {
        try {
            return s.toString().length();
        } catch (Exception ex) {
            return -2;
        }finally {
            return  -1;
        }
    }
    public void testOnce(int[] input, int expected) {
        Assert.assertEquals(expected, numDiffPairs(input));
    }
    public long numDiffPairs(int[] array) {
        Set<Integer> set = IntStream.of(array).boxed().collect(Collectors.toSet());
        long result = IntStream.of(array).map(v -> v + 2).filter(set::contains).count();
        return result;
    }
}
