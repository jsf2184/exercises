package com.jsf2184;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.stream.Stream;

public class Solution {
    public static void main( String[] args )
    {
//        readScannerLines();
        readWithBufferedReader();
    }

    public static void readScannerLines() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a line or q to quit");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            if (line.equals("q")){
                break;
            }
            System.out.println("Enter a line or q to quit");
        }
    }

    public static void readWithBufferedReader() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter a line or q to quit");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                System.out.println("Enter a line or q to quit");
                if (line.equals("q")){
                    break;
                }

            }
        } catch (IOException ignore) {

        }
    }

}
