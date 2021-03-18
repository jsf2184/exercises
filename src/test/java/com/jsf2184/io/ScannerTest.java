package com.jsf2184.io;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Scanner;

public class ScannerTest {

    @Ignore
    @Test
    public void testScanning() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter 3 numbers");

        int a = scan.nextInt();
        int b = scan.nextInt();
        int c = scan.nextInt();
        scan.close();

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

    }
}
