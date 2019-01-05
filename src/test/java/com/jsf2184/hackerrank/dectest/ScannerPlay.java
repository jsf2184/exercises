package com.jsf2184.hackerrank.dectest;

import org.junit.Assert;
import org.junit.Test;

import java.util.Scanner;

public class ScannerPlay {
    @Test
    public void testScannerOnString() {
        Scanner scanner = new Scanner("a b cde 123 3.5");
        String str1 = scanner.next();
        String str2 = scanner.next();
        String str3 = scanner.next();
        int ival = scanner.nextInt();
        double dval = scanner.nextDouble();
        Assert.assertEquals("a", str1);
        Assert.assertEquals("b", str2);
        Assert.assertEquals("cde", str3);
        Assert.assertEquals(123, ival);
        Assert.assertEquals(3.5, dval, .000001);

    }
}
