package com.jsf2184.utility;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Utility {

    public static int _permuteCount = 0;
    private static final Logger _log = Logger.getLogger(Utility.class);

    public static void swap(int[] array, int x, int y) {
        int tmp = array[x];
        array[x] = array[y];
        array[y] = tmp;
    }

    public static int[] createArray(int length) {
        int[] res = new int[length];
        IntStream.range(0, length).forEach(i -> res[i] = i);
        return res;
    }

    public static void reverse(int[] array) {
        for (int b=0,e=array.length-1; b<e; b++, e--) {
            Utility.swap(array, b, e);
        }
    }

    public static int[]  shuffle(int size) {
        int[] array = createArray(size);
        return shuffle(array);
    }

    public static int[]  shuffle(int[] array) {
        Random random = new Random();
        for (int i=array.length-1; i>0; i--) {
            int idx = random.nextInt(i + 1);
            Utility.swap(array, i, idx);
        }
        return array;
    }

    public static void resetPermuteCount() {
        _permuteCount = 0;
    }

    public static int getPermuteCount() {
        return _permuteCount;
    }
    public static String splice(String s, int x) {
        if (s == null ||  x >= s.length() || x < 0) {
            throw new IllegalArgumentException();
        }
        String first = s.substring(0, x);
        String last = s.substring(x+1);
        return first + last;
    }



    public static void permute(String prefix, String s, List<String> res) {


        int length = s.length();
        if (length == 0) {
            res.add(prefix);
        }
        for (int i=0; i<length; i++) {
            _permuteCount++;
            char c = s.charAt(i);
            permute(prefix + c, splice(s, i), res);
        }
    }

    public static int fib(int n) {
        if (n== 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int res = 1;
        int prior = 0;
        for (int i=2; i<= n; i++) {
            int oldRes = res;
            res += prior;
            prior = oldRes;
        }
        return res;
    }

    public static int recurseFib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return recurseFib(n-1) + recurseFib(n-2);

    }

    public static void logArray(int arr[][]) {
        for (int[] anArr : arr) {
            _log.info(Arrays.toString(anArr));
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

}
