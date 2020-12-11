package com.jsf2184.esight;

import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Beader {
    public int solution(int[] A) {
        Set<Integer> numbersLeft = new HashSet<>();
        for (int i=0; i<A.length; i++) {
            numbersLeft.add(i);
        }
        Integer length;
        int best = 0;
        while ((length = tryOne(numbersLeft, A)) != null ) {
            best = Math.max(best, length);
        }
        return best;
    }

    Integer tryOne(Set<Integer> numbersLeft, int[] A) {
        final Optional<Integer> first = numbersLeft.stream().findFirst();
        if (!first.isPresent()) {
            return null;
        }
        final Integer starting = first.get();
        int current = starting;
        int count = 1;
        while (true) {
            numbersLeft.remove(current);
            int next = A[current];
            if (next == starting) {
                return count;
            }
            current = next;
            count++;
        }
    }

    @Test
    public void tryIt() {
        final int result = solution(new int[]{5, 4, 0, 3, 1, 6, 2});
    }

}
