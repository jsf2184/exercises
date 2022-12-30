package com.jsf2184.se8.functional;

import com.jsf2184.threads.WaitTests;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Consumer;

public class ConsumerTests {

    // Pass in a Consumer.
    //    A Consumer takes 1 arguments of a particular type and returns nothing.


    public <T> void callConsumer(Consumer<T> consumer, T val) {
        consumer.accept(val);
    }
//    public void callConsumer(Consumer<Integer> consumer, int val) {
//        // Our consumer consumes an Integer.
//        consumer.accept(val);
//    }

    @Test
    public void testConsumer() {
        // Since the consumer doesn't return anything, the lamda that implements the consumer needs
        // to record that it was called in the array callRecord.
        //
        boolean callRecord[] = new boolean[10];
        for (int i=1; i<10; i+=2) {
            Consumer<Integer> consumer = j -> {
                callRecord[j] = true;
            };
            callConsumer(consumer, i );
        }
        for (int i=0; i<10; i++) {
            boolean expected = i %2 == 1;
            Assert.assertEquals(expected, callRecord[i]);
        }
    }

    @Test
    public void testConsumerChaining() {
        int a[] = new int[1];
        a[0] = 2;

//        Consumer<Integer> plus = (x -> {a[0] += x;});
        Consumer<Integer> plus = (x -> a[0] += x);
        Consumer<Integer> times = (x -> {a[0] *= x;});

        callConsumer(plus.andThen(times), 3); // (2+3) * 3 = 15
        Assert.assertEquals(15, a[0]);

        Consumer<Integer> compoundConsumer = plus.andThen(times);
        callConsumer(compoundConsumer, 4); // (15 + 4) * 4 = 76
        Assert.assertEquals(76, a[0]);




    }
}
