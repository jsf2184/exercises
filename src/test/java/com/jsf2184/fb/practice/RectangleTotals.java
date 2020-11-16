package com.jsf2184.fb.practice;

import org.junit.Test;

public class RectangleTotals {
    int calcMaxTotal(int [][] array) {
        int rowCount = array.length;
        int columnCount = array[0].length;
        int [][] preCalcs = new int[rowCount][columnCount];
        Integer max = null;
        for (int r=0; r<rowCount; r++) {
            int rowBest = calcRectangle(r, columnCount, array, preCalcs);
            max = max == null ? rowBest : Math.max(rowBest, max);
        }
        return max;
    }

    int calcRectangle(int rowNum, int columnCount, int[][] array, int[][]preCalcs) {
        int rowTotal = 0;
        Integer bestValue = null;
        for (int c=0; c<columnCount; c++) {
            rowTotal += array[rowNum][c];
            int priorTotal = 0;
            if (rowNum >= 1) {
                priorTotal = preCalcs[rowNum-1][c];
            }
            int rectangleTotal = priorTotal + rowTotal;
            preCalcs[rowNum][c] = rectangleTotal;
            bestValue = bestValue == null ? rectangleTotal : Math.max(bestValue, rectangleTotal);
        }
        return bestValue == null ? 0 : bestValue;
    }

    @Test
    public void testIt() {
        int[][] array = {
            {10, 3, 4, 6},
            {-5, 2, 12, 11},
            { 3, 2, 8, 4}
        };
        int result = calcMaxTotal(array);


    }
}
