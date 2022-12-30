package com.jsf2184.cracking;

import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

public class ZeroMatrix_1_8 {

    public void zeroMatrix(int[][] matrix) {
        int numRows = matrix.length;
        if (numRows == 0) {
            return;
        }
        int numCols = matrix[0].length;
        boolean anyRow0Zeros = false;
        for (int v : matrix[0]) {
            if (v == 0) {
                anyRow0Zeros = true;
                break;
            }
        }
        boolean anyCol0Zeros = false;
        for (int y = 0; y<numRows; y++) {
            if (matrix[y][0] == 0) {
                anyCol0Zeros = true;
                break;
            }
        }

        for (int r=1; r<numRows; r++) {
            for (int c=1; c < numCols; c++) {
                if (matrix[r][c] == 0) {
                    matrix[0][c] = 0;
                    matrix[r][0] = 0;
                }
            }
        }
        for (int c=0; c<numCols; c++) {
            if (matrix[0][c] == 0) {
                zeroColumn(c, matrix);
            }
        }
        for (int r=0; r<numRows; r++) {
            if (matrix[r][0] == 0) {
                zeroRow(r, matrix);
            }
        }

        if (anyCol0Zeros) {
            zeroColumn(0, matrix);
        }
        if (anyRow0Zeros) {
            zeroRow(0, matrix);
        }
    }

    public void zeroColumn(int c, int[][] matrix) {
        for( int r=0; r<matrix.length; r++) {
            matrix[r][c] = 0;
        }
    }

    public void zeroRow(int r, int[][] matrix) {
        for( int c=0; c<matrix[0].length; c++) {
            matrix[r][c] = 0;
        }
    }

    @Test
    public void testIt() {
        final int[][] matrix = createMatrix(4, 6, 3);
        printMatrix(matrix);
        zeroMatrix(matrix);
        System.out.println();
        printMatrix(matrix);
    }
    public static int[][] createMatrix(int rows, int columns, int numZeros) {
        int[][] res = new int[rows][];
        for (int r = 0; r<rows; r++) {
            res[r] = new int[columns];
            for (int c =0; c<columns; c++) {
                res[r][c] = 1;
            }
        }
        Random random = new Random();
        for (int i=0; i<numZeros; i++) {
            int rval = random.nextInt(rows * columns);
            int r= rval / columns;
            int c = rval % columns;
            res[r][c] = 0;
        }
        return res;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int v : row) {
                System.out.printf(" %d", v);
            }
            System.out.println();
        }
    }

}
