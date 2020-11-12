package com.jsf2184.numeric.primes;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PrimeGeneratorTests {
    @Test
    public  void testGetPrimesBySums() {
        IPrimeGenerator generatorBySums = new PrimeGeneratorViaSums();
        final List<Integer> sumPrimes = generatorBySums.getPrimes(100);
        IPrimeGenerator generatorByMod = new PrimeGeneratorViaMod();
        final List<Integer> modPrimes = generatorByMod.getPrimes(100);
        Assert.assertEquals(sumPrimes, modPrimes);
        sumPrimes.forEach(System.out::println);
    }
}
