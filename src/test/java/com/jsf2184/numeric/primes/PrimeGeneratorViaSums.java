package com.jsf2184.numeric.primes;

import java.util.ArrayList;
import java.util.List;

public class PrimeGeneratorViaSums implements IPrimeGenerator {
    @Override
    public List<Integer> getPrimes(int max) {
        List<Integer> result = new ArrayList<>();
        boolean[] flags = new boolean[max];
        for (int i=2; i<max; i++) {
            if (!flags[i]) {
                result.add(i);
                for (int j= 2 * i; j<max; j+=i) {
                    flags[j] = true;
                }
            }
        }
        return result;
    }
}
