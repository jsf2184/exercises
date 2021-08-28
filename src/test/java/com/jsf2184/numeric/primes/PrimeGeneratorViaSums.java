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

    // Developed this optimization in Avenue8 coding interview
    static List<Integer> findPrimes2(int n) {
        List<Integer> primes = new ArrayList<>();

        boolean[] dflags = new boolean[n+1];  // false means prime (or not divisible)
        for (int i=2; i<=n; i++) {

            if (!dflags[i]) {
                primes.add(i);

                long square = (long) i * (long ) i;
                if (square < n) {
                    for (int j = (int) square; j <= n; j += i) {
                        dflags[j] = true;
                    }
                }
            }

        }

        return primes;

    }


}
