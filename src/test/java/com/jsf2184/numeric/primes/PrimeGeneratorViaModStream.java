package com.jsf2184.numeric.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PrimeGeneratorViaModStream implements IPrimeGenerator {
    @Override
    public List<Integer> getPrimes(final int max) {
        List<Integer> primes = new ArrayList<>();
        IntStream.range(2, max)
                 .forEach( i -> {
                     final Optional<Integer> primeFactor = primes.stream().filter(p -> i % p == 0).findFirst();
                     if (!primeFactor.isPresent()) {
                         primes.add(i);
                     }
                 });
        return primes;
    }
}
