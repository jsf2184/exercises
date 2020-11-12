package com.jsf2184.numeric.primes;

import java.util.ArrayList;
import java.util.List;

public class PrimeGeneratorViaMod implements IPrimeGenerator {
    @Override
    public List<Integer> getPrimes(int max) {
        List<Integer> primes = new ArrayList<>();
        for (int i=2; i<max; i++) {
            boolean isPrime = true;
            for (int p : primes) {
                if (i % p == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            }
        }
        return primes;
    }
}
