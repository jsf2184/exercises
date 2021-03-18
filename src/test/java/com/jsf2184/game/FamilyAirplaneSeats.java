package com.jsf2184.game;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class FamilyAirplaneSeats {

    public static final int FAMILY_SIZE = 4;

    public enum SeatLetter {
        A(0),B(1),C(2),D(3),E(4),F(5),G(6),H(7),J(8),K(9);
        int idx;
        SeatLetter(int idx) {
            this.idx = idx;
        }
    }

    @Data
    public static class Seat {
        SeatLetter seatLetter;
        int rowNumber;

        public Seat(SeatLetter seatLetter, int rowNumber) {
            this.seatLetter = seatLetter;
            this.rowNumber = rowNumber;
        }

        public static Seat parse(String str) {
            if (str == null || str.length() < 2) {
                return null;
            }
            final int len = str.length();
            String letter = str.substring(len-1);
            SeatLetter seatLetter;
            try {
                seatLetter = SeatLetter.valueOf(letter);
                String numStr = str.substring(0, len-1);
                int rowNum = Integer.parseInt(numStr);
                return rowNum <=0 ? null : new Seat(seatLetter, rowNum);
            } catch (Exception ignore) {
                return null;
            }
        }
    }

    public static class Plane {
        Row[] rows;

        public Plane(int n) {
            rows = new Row[n+1];
            for (int r=0; r<=n; r++) {
                rows[r] = new Row();
            }
        }
        public void assignSeat(Seat s) {
            if (s.rowNumber > 0 && s.rowNumber < rows.length) {
                Row row = rows[s.rowNumber];
                row.take(s.seatLetter);
            }
        }
        public int assignFamilySeats() {
            final Integer result = Arrays.stream(rows,1, rows.length)
                                         .map(Row::assignForFamilies)
                                         .reduce(0, Integer::sum);
            return result;
        }
    }


    public static class Row {
        boolean[] seats;

        public Row() {
            seats = new boolean[SeatLetter.K.idx+1];
        }

        void take(SeatLetter s) {
            seats[s.idx] = true;
        }

        boolean isFamilySeatGroupAvailable(SeatLetter start) {
            for(int s= start.idx; s<start.idx+FAMILY_SIZE; s++ ) {
                if (seats[s]) {
                    return false;
                }
            }
            return true;
        }

        void assignFamilySeats(SeatLetter start) {
            for (int s = start.idx; s < start.idx + FAMILY_SIZE; s++) {
                seats[s] = true;
            }
        }

        int checkAndAssign(SeatLetter start) {
            if (isFamilySeatGroupAvailable(start)) {
                assignFamilySeats(start);
                return 1;
            }
            return 0;
        }

        int assignForFamilies() {
            int count = 0;
            count += checkAndAssign(SeatLetter.B);
            count += checkAndAssign(SeatLetter.F);
            // Only chance of using middle seats is if we failed to get the end ones.
            if (count == 0) {
                count += checkAndAssign(SeatLetter.D);
            }
            return count;
        }
    }

    public static int solution(int n, String S) {
        Plane plane = new Plane(n);
        final String[] seatStrs = S.split(" ");
        Arrays.stream(seatStrs)
              .map(Seat::parse)
              .filter(Objects::nonNull)
              .forEach(plane::assignSeat);
        final int result = plane.assignFamilySeats();
        return result;
    }

    @Test
    public void testSeatParse() {
        Assert.assertNull(Seat.parse(null));
        Assert.assertNull(Seat.parse(""));
        Assert.assertNull(Seat.parse("1"));
        Assert.assertNull(Seat.parse("A"));
        Assert.assertNull(Seat.parse("1Z"));
        Assert.assertNull(Seat.parse("0F"));
        Assert.assertNull(Seat.parse("12XA"));

        Assert.assertEquals(new Seat(SeatLetter.F, 23), Seat.parse("23F"));
        Assert.assertEquals(new Seat(SeatLetter.G, 1), Seat.parse("1G"));
    }

    @Test
    public void testSolution() {
        // Row 1 - Precludes Left Side, Use Right      : 1
        // Row 2 - Precludes Center and Right, Use Left: 1
        // Row 3 - Precludes Left and Right, Use Center: 1
        // Row 4 - Precludes All                       : 0

        testAssignFamiliySeating(4, "1A 2F 1C 3C 3H 4D 4G", 3);
    }
    public void testAssignFamiliySeating(int n, String s, int expected) {
        final int actual = solution(n, s);
        Assert.assertEquals(expected, actual);
    }
}
