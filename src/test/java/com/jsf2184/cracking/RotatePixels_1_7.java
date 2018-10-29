package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class RotatePixels_1_7 {


    public static class Coord {
        int _row;
        int _column;

        public Coord(int row, int column) {
            _row = row;
            _column = column;
        }

        public int getRow() {
            return _row;
        }

        public int getColumn() {
            return _column;
        }

        public Coord incColumn() {
            Coord res = new Coord(_row, _column+1);
            return res;
        }

        public Coord decColumn() {
            Coord res = new Coord(_row, _column-1);
            return res;
        }

        public Coord incRow() {
            Coord res = new Coord(_row+1, _column);
            return res;
        }

        public Coord decRow() {
            Coord res = new Coord(_row-1, _column);
            return res;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return _row == coord._row &&
                    _column == coord._column;
        }
    }

    enum Direction {
        Right,
        Down,
        Left,
        Up;

        Direction getNext() {
            switch (this) {
                case Right:
                    return Down;
                case Down:
                    return Left;
                case Left:
                    return Up;
                case Up:
                    return Right;
            }
            return null;
        }

        Function<Coord, Coord> getCoordOperation() {
            switch (this) {
                case Right:
                    return Coord::incColumn;
                case Down:
                    return Coord::incRow;
                case Left:
                    return Coord::decColumn;
                case Up:
                    return Coord::decRow;
            }
            return null;

        }
    }


    public static class Frame {
        int _offset;
        int _length;
        @SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
        List<Coord> getFrameCoordinates() {
            List<Coord> res = new ArrayList<>();
            Coord origin = new Coord(_offset, _offset);
            Direction direction = Direction.Right;
            Coord current = origin;

            while (true) {

            }
        }

    }

    @Test
    public void testDirection() {
        Assert.assertEquals(Direction.Down, Direction.Right.getNext());
        Assert.assertEquals(Direction.Left, Direction.Down.getNext());
        Assert.assertEquals(Direction.Up, Direction.Left.getNext());
        Assert.assertEquals(Direction.Right, Direction.Up.getNext());

    }
}
