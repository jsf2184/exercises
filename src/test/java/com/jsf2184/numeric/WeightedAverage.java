package com.jsf2184.numeric;

import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public class WeightedAverage {

    static class Pair {
        int _price;
        int _qty;

        public Pair(int price, int qty) {
            _price = price;
            _qty = qty;
        }
        public int getPrice() {
            return _price;
        }
        public int getQty() {
            return _qty;
        }
    }

    @Test
    public void simpleTest() {
        List<Pair> simpleInput = getSimpleInput();
        tryAlternatives(simpleInput);
    }

    @Test
    public void biggerTest() {
        List<Pair> simpleInput = getRandomInput(1000000);
        tryAlternatives(simpleInput);
    }

    @Test
    public void testLongBigEnough() {
        long maxVolume = Long.MAX_VALUE / 1000000;
        System.out.printf("max long is %d\n", Long.MAX_VALUE);
        System.out.printf("max volume for $1,000,000 instrument is %d\n", maxVolume);

    }


    public void tryAlternatives(List<Pair> input) {
        reportOnApproach(input,
                         "calcWeightedAvgByLongSummingTotals",
                         WeightedAverage::calcWeightedAvgByLongSummingTotals);
        reportOnApproach(input,
                         "calcWeightedAvgByRepeatedDoubleAverages",
                         WeightedAverage::calcWeightedAvgByRepeatedDoubleAverages);
        reportOnApproach(input,
                         "calcWeightedAvgByBigIntegerSummingTotals",
                         WeightedAverage::calcWeightedAvgByBigIntegerSummingTotals);

    }

    public void reportOnApproach(List<Pair> input,
                                 String label,
                                 Function<List<Pair>, Double> f)
    {
        long start = DateTime.now().getMillis();
        Double res = f.apply(input);
        long finish = DateTime.now().getMillis();
        long duration = finish - start;
        System.out.printf("using %s, res = %f, duration = %d\n", label, res, duration);
    }

    static double calcWeightedAvgByRepeatedDoubleAverages(List<Pair> pairs) {

        double runningAvg = 0;
        long runningQty = 0;

        for(Pair p : pairs) {
            int newQty = p.getQty();
            long newQtyTotal = runningQty + newQty;

            // Compute how much 'weight' we should assign to the trades that were reported previously.
            // Obviously that's a function of how the ration between our prior qty and the one that will
            // now result from this latest trade.
            //
            double priorWeight = (double) runningQty/ newQtyTotal;
            double priorComponent = priorWeight * runningAvg;
            double newWeight = (double) newQty / newQtyTotal;
            double newComponent = newWeight * p.getPrice();
            runningAvg = priorComponent + newComponent;
            runningQty = newQtyTotal;
        }
        return  runningAvg;
    }

    static double calcWeightedAvgByRepeatedBigDecimalAverages(List<Pair> pairs) {

        BigDecimal runningAvg = new BigDecimal(0);
        BigDecimal runningQty = new BigDecimal(0);

        for(Pair p : pairs) {
            BigDecimal newQty = BigDecimal.valueOf(p.getQty());
            BigDecimal newQtyTotal = runningQty.add(newQty);
            BigDecimal priorWeight = runningQty.divide(newQtyTotal, 32, BigDecimal.ROUND_UP);
            BigDecimal priorComponent = priorWeight.multiply(runningAvg);
            BigDecimal newWeight = newQty.divide(newQtyTotal, 32, BigDecimal.ROUND_UP);
            BigDecimal newComponent = newWeight.multiply(BigDecimal.valueOf(p.getPrice()));
            runningAvg = priorComponent.add(newComponent);
            runningQty = newQtyTotal;
        }
        return  runningAvg.doubleValue();
    }


    static double calcWeightedAvgByBigIntegerSummingTotals(List<Pair> pairs) {
        BigInteger runningPriceTotal = BigInteger.valueOf(0L);
        BigInteger runningQtyTotal = BigInteger.valueOf(0L);
        for(Pair p : pairs) {
            BigInteger delta = BigInteger.valueOf(p.getPrice()).multiply(BigInteger.valueOf(p.getQty()));
            runningPriceTotal = runningPriceTotal.add(delta);
            runningQtyTotal = runningQtyTotal.add(BigInteger.valueOf(p.getQty()));
        }
        double res = runningPriceTotal.doubleValue() / runningQtyTotal.doubleValue();
        return res;
    }

    static double calcWeightedAvgByLongSummingTotals(List<Pair> pairs) {
        long runningPriceTotal = 0;
        long runningQtyTotal = 0;
        for(Pair p : pairs) {
            long delta = p.getPrice() * p.getQty();
            runningPriceTotal = runningPriceTotal + delta;
            runningQtyTotal = runningQtyTotal + p.getQty();
        }
        double res = ((double) runningPriceTotal) / runningQtyTotal;

        return res;
    }

    List<Pair> getRandomInput(int n) {
        Random random = new Random(DateTime.now().getMillisOfDay());
        List<Pair> res = new ArrayList<>();
        IntStream.range(0,n).forEach(i-> {
            int price = random.nextInt(10000);
            int qty = random.nextInt(9999) + 1;
            res.add(new Pair(price, qty));
        });
        return res;
    }

    List<Pair> getSimpleInput() {
        List<Pair> res = new ArrayList<>();
        res.add(new Pair(50, 9));  //  450
        res.add(new Pair(30, 1));  //   30
        res.add(new Pair(60, 6));  //  360
        res.add(new Pair(40, 4));  //  160
                                   // ----
                                   // 1000 / 20 = 50
        return res;
    }
}
