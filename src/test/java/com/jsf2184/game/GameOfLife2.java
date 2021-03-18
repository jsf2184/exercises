package com.jsf2184.game;

import org.junit.Assert;
import org.junit.Test;

public class GameOfLife2 {
    public static class Cell {
        boolean[] versions;
        private Board board;

        public Cell(Board board, boolean initialValue) {
            this.board = board;
            this.versions = new boolean[2];
            versions[board.getCurrent()] = initialValue;        }

        boolean getState() {
            return versions[board.getCurrent()];
        }

        public void setCurrentState(boolean state) {
            versions[board.getCurrent()] = state;
        }

        private void setNextState(boolean state) {
            versions[board.getNext()] = state;
        }

        public void applyRules(int myRow, int myColumn) {
            final int liveNeighbors = countLiveNeighbors(myRow, myColumn);
            final boolean newState = calcState(getState(), liveNeighbors);
            setNextState(newState);
        }

        private boolean calcState(boolean wasAlive, int liveNeighbors) {
            if (wasAlive) {
                return  liveNeighbors == 2 || liveNeighbors == 3;
            } else {
                return liveNeighbors == 3;
            }
        }

        public int countLiveNeighbors(int myRow, int myColumn) {
            int n = board.getN();
            final int rowLow = Math.max(0, myRow - 1);
            final int rowHigh = Math.min(n-1, myRow + 1);

            // Establish range for columns: column -1 to column +1, wary of boundaries.
            final int colLow = Math.max(0, myColumn - 1);
            final int colHigh = Math.min(n-1, myColumn + 1);

            int liveCount = 0;
            for (int r = rowLow; r <= rowHigh; r++) {
                for (int c = colLow; c <= colHigh; c++) {
                    if  (r == myRow && c == myColumn) {
                        continue;
                    }
                    if (board.getState(r, c)) {
                        liveCount++;
                    }
                }
            }
            return liveCount;
        }

    }

    public static class Board {

        Cell[][] board;
        int current;
        int n;

        public Board(int n, boolean[][] initialValues) {
            this.n = n;
            current = 0;
            board = new Cell[n][n];
            for (int r=0; r<n; r++) {
                for(int c=0; c<n; c++) {
                    board[r][c] = new Cell(this, initialValues[r][c]);
                }
            }
        }

        public int getCurrent() {
            return current;
        }

        public int getNext() {
            return current == 0? 1 : 0;
        }

        public int getN() {
            return n;
        }

        public boolean getState(int row, int column) {
            return board[row][column].getState();
        }

        private void tick() {
            for (int r=0; r<n; r++) {
                for (int c = 0; c < n; c++) {
                    board[r][c].applyRules(r, c);
                }
            }
            // now that we are done, switch what was our next to our current;
            current = (current+1) %2;
        }

        public void print() {
            for (int r = 0; r < n; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < n; c++) {
                    sb.append(getState(r, c) ? '*' : '.');
                    sb.append(' ');
                }
                System.out.println(sb.toString());
            }
        }

        public void playGame(int numTicks) {
            for (int tick=1; tick <= numTicks; tick++ ) {
                tick();
                System.out.printf("Play = %d\n", tick);
                print();
                System.out.println();
            }
        }


    }

    @Test
    public void testBoardConstructor() {
        final boolean[][] initialValues = {
                {true, false, false, true, false},
                {false, true, true, false, false},
                {true, false, true, false, false},
                {false, true, false, true, true},
                {true, false, false, true, false}
        };
        Board board = new Board(5, initialValues);
        board.playGame(10);
    }
}
