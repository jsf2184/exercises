package com.jsf2184;

import org.junit.Test;

import java.util.Arrays;

public class TwoDimArrayPlay {


    public int[][] initArray() {
        int[][]  res = new int[5][]; // 5 rows;
        for (int row = 0; row < res.length; row++) {
            int columns = row * 2;
            res[row] = new int[columns];
            for (int col=0; col < columns; col++) {
                res[row][col] = 10 * row + col;
            }
        }
        return res;
    }

    public void printArray(int[][] array) {
        Arrays.stream(array)
                .map(Arrays::toString)
                .forEach(System.out::println);
    }

    public void printArray2(int[][] array)
    {
        for (int r = 0; r< array.length; r++) {
            for (int c = 0; c< array[r].length; c++) {
                System.out.printf("%d ", array[r][c]);
            }
            System.out.println();
        }
    }

    @Test
    public void popAndPrintArray() {
        int[][] array = initArray();
        printArray(array);
        printArray2(array);
    }

    @Test
    public void popAndPrintArrayWithConstants() {
        int[][] array = {
                {},
                {10, 11},
                {20, 21, 22, 23},
                {30, 31, 32, 33, 34, 35},
                {40, 41, 42, 43, 44, 45, 46, 47 }
        };
        printArray(array);
        printArray2(array);
    }


}
