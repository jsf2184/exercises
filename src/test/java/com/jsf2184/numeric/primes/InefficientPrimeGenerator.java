package com.jsf2184.numeric.primes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InefficientPrimeGenerator implements IPrimeGenerator{

    public boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        final double sqrt = Math.sqrt(n);
        if (n %2 == 0) {
            return false;
        }
        for (int i=3; i<=sqrt; i+= 2) {
            if (n %i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Integer> getPrimes(int max) {
        return IntStream.range(2, max)
                 .boxed()
                 .filter(this::isPrime)
                 .collect(Collectors.toList());
    }
}
