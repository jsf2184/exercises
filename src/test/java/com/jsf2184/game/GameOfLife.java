package com.jsf2184.game;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

@Slf4j
public class GameOfLife {

    public static class Board {
        int n;
        boolean[][] board;

        public Board(int n) {
            this.n = n;
            board = new boolean[n][n];
        }

        public void setState(boolean alive, int row, int column) {
            board[row][column] = alive ;
        }

        public boolean isLive(int row, int column) {
            return board[row][column];
        }

        public int countLiveNeighbors(int row, int column) {

            // Establish ranges for row: row-1 to row+1 (wary of boundaries)
            final int rowLow = Math.max(0, row - 1);
            final int rowHigh = Math.min(n-1, row + 1);

            // Establish range for columns: column -1 to column +1, wary of boundaries.
            final int colLow = Math.max(0, column - 1);
            final int colHigh = Math.min(n-1, column + 1);

            int count = 0;
            for (int r = rowLow; r <= rowHigh; r++) {
                for (int c = colLow; c <= colHigh; c++) {
                    if  (r == row && c == column) {
                        continue;
                    }
                    if (isLive(r, c)) {
                        count++;
                    }
                }
            }
            return count;
        }

        public void print() {
            for (int r = 0; r < n; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < n; c++) {
                    sb.append(isLive(r, c) ? '*' : '.');
                    sb.append(' ');
                }
                System.out.println(sb.toString());
            }
        }

        public void populateRandom(int density) {
            Random random = new Random();
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    final int v = random.nextInt();
                    if (v % density == 0) {
                        setState(true, r, c);
                    } else {
                        setState(false, r, c);
                    }
                }
            }
        }

        public void populate(int density) {
            this.board = new boolean[][]{
                    {true, false, false, true, false},
                    {false, true, true, false, false},
                    {true, false, true, false, false},
                    {false, true, false, true, true},
                    {true, false, false, true, false}
            };
        }


    }

    public static class Game {
        int n;
        int density;
        Board currentBoard;
        Board nextBoard;

        public Game(int n, int density) {
            this.n = n;
            this.density = density;
            currentBoard = new Board(n);
            currentBoard.populate(density);
            currentBoard.print();
            nextBoard = new Board(n);
        }

        public void playGame(int numPlays) {
            for (int play=1; play <= numPlays; play++ ) {
                tick();
                System.out.printf("Play = %d\n", play);
                currentBoard.print();
                System.out.println();
            }
        }

        // Calculate the next state of all the cells. Leave the result in the currentBoard.
        private void tick() {
            for (int r=0; r<n; r++) {
                for (int c = 0; c < n; c++) {
                    final boolean newState = applyRules(r, c);
                    nextBoard.setState(newState, r, c);
                }
            }
            // swap boards.
            Board temp = currentBoard;
            currentBoard = nextBoard;
            nextBoard = temp;
        }

        // apply rules to a given cell to come up with the next state for that cell.
        public boolean applyRules(int r, int c) {
            final int liveNeighbors = currentBoard.countLiveNeighbors(r, c);
            final boolean wasAlive = currentBoard.isLive(r, c);
            final boolean newState = calcState(wasAlive, liveNeighbors);
            return newState;
        }

        private boolean calcState(boolean wasAlive, int liveNeighbors) {
            if (wasAlive) {
                return  liveNeighbors == 2 || liveNeighbors == 3;
            } else {
                return liveNeighbors == 3;
            }
        }
    }



    @Test
    public void countLiveNeighbors() {
        Board board = new Board(4);
        for (int r=0; r<4; r++) {
            for (int c=0; c<4; c++) {
                board.setState(true, r, c);
            }
        }
        board.print();

        int[][] expected = new int[][]{
                {3, 5, 5 ,3},
                {5, 8, 8, 5},
                {5, 8, 8, 5},
                {3, 5, 5 ,3}};


        for (int r=0; r<4; r++) {
            for (int c=0; c<4; c++) {
                final int count = board.countLiveNeighbors(r, c);
                Assert.assertEquals(count, expected[r][c]);
            }
        }
    }

    @Test
    public void countNeighborsWhenAllDead() {
        Board board = new Board(4);
        board.print();

        for (int r=0; r<4; r++) {
            for (int c=0; c<4; c++) {
                final int count = board.countLiveNeighbors(r, c);
                log.info("[{}][{}] : count = {}", r, c, count);
            }
        }
    }

    @Test
    public void testPopAndPrint() {
        Board board = new Board(4);
        board.populate(3);
        board.print();
    }


    @Test
    public void playGame() {
        Game game = new Game(5, 3);
        game.playGame(10);
    }

}
