package com.jsf2184.ucourse.docsign;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class GroupComputersInNetworks {


    static public int groupComputers(int minGroupSize, int minThreshold, int[] speeds) {
        int runningSpeedTotal = 0;
        int currentGroupSize = 0;
        int networksFound = 0;
        for (int i=0; i<speeds.length; i++) {
            int currentSpeed = speeds[i];
            runningSpeedTotal += currentSpeed;
            currentGroupSize += 1;
            if (currentGroupSize >= minGroupSize && runningSpeedTotal >= minThreshold) {
                networksFound++;
                int start = (i - currentGroupSize) + 1;
                reportOnFind(networksFound, start, i, currentGroupSize, runningSpeedTotal, speeds);
                currentGroupSize = 0;
                runningSpeedTotal = 0;
            }
        }
        return networksFound;
    }

    static public int groupComputersBackwards(int minGroupSize, int minThreshold, int[] speeds) {
        int runningSpeedTotal = 0;
        int currentGroupSize = 0;
        int networksFound = 0;
        for (int i=speeds.length-1; i>=0; i--) {
            int currentSpeed = speeds[i];
            runningSpeedTotal += currentSpeed;
            currentGroupSize += 1;
            if (currentGroupSize >= minGroupSize && runningSpeedTotal >= minThreshold) {
                networksFound++;
                int finish = (i+currentGroupSize) - 1;
                reportOnFind(networksFound, i, finish, currentGroupSize, runningSpeedTotal, speeds);
                currentGroupSize = 0;
                runningSpeedTotal = 0;
            }
        }
        return networksFound;
    }

    public static void reportOnFind(int networksFound, int start, int finish, int currentGroupSize, int runningSpeedTotal, int[] speeds) {
        StringBuilder sb = new StringBuilder();
        for (int c=start; c<=finish; c++) {
            sb.append(String.format(" [%d]:%d", c, speeds[c]));
        }
        log.info("found network {} of size={} and speedTotal={} with computers {}",
                 networksFound, currentGroupSize, runningSpeedTotal, sb);

    }



    @Test
    public void test() {
        runBothDirections(2, 15, new int[] {5, 7, 9, 12, 10, 13});

    }

    public void runBothDirections(int minGroupSize, int minThreshold, int[] speeds) {
        final int forwards = groupComputers(minGroupSize, minThreshold, speeds);
        final int backwards = groupComputersBackwards(minGroupSize, minThreshold, speeds);
        Assert.assertEquals(forwards, backwards);
    }
}
