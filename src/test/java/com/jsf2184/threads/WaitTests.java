package com.jsf2184.threads;

import com.jsf2184.utility.Utility;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.jsf2184.threads.ConcurrencyPlay.sleep;

public class WaitTests {

    // With this PubValue class the publisher does not wait for a client to consume before possibly
    // publishing again. Therefore, a reader is not guaranteed to get all published values, but they
    // are guaranteed to get the last one.
    //
    public static class PubValue {
        int _value = 0;
        public synchronized void publish() {
            _value++;
            notify();
        }

        public synchronized int getNext(int prior) {
            // We want to wait until there is a new value past what we saw earlier
            while (_value <= prior) {
                try {
                    wait();
                } catch (InterruptedException ignore) {
                }
            }
            return _value;
        }
    }

    public static class ReliablePubValue {
        int _value = 0;
        boolean _empty = true;

        public synchronized void publish() {
            sleep(5);
            // publisher uses waitSignal() to wait until the _empty flag is true, meaning that a consumer has
            // retrieved the last value that was published.
            //
            waitSignal(() -> _empty, "pubber");
            _value++;
            _empty = false;
            // notify consumer that we have a new value.
            notifyAll();
        }

        public synchronized int getNext(String id) {
            // Use waitSignal to wait until a published value is available.
            waitSignal(() -> !_empty, id);
            _empty = true;
            notifyAll();
            return _value;
        }

        public void waitSignal(Supplier<Boolean> condition, String id) {
            while (!condition.get()) {
                try {
                    System.out.printf("ReliablePubValue.waitSignal(): begin wait() w/ id=%s\n", id);
                    wait();
                    System.out.printf("ReliablePubValue.waitSignal(): waking from wait(), w/ id=%s\n", id);

                } catch (InterruptedException ignore) {
                }
            }
            // When we get here, the condition is true and we have the lock.
            System.out.printf("ReliablePubValue.waitSignal(): returning for id=%s\n", id);
        }
    }


    @Test
    public void testWait() {
        PubValue pubValue = new PubValue();
        // On a single publishing thread, publish 10 times without an occasional slight 1 ms delay.
        new Thread(() -> IntStream.range(0, 10)
                .forEach(x ->  {
                    pubValue.publish();
                    if (x %2 == 0) Utility.sleep(1);
                }   )).start();

        // And, now in our main receiving thread, wait for new values. Note that we may not recieve all the published values
        // but we are sure to get the last one.
        //
        int v = 0;
        while ((v = pubValue.getNext(v)) != 10) {
            System.out.printf("getNext(): returned %d\n", v);
        }
        Assert.assertEquals(10, v);
        System.out.printf("getNext(): returned %d\n", v);
    }

    // This class helps us understand what happens when we have several Consumers all listening on a single ReliablePubValue.
    // When our Consumer class gets a value, it records that value in the map supplied to its constructor.
    //
    public static class Consumer implements Runnable {
        int _id;
        CountDownLatch _countDownLatch;
        ReliablePubValue _reliablePubValue;
        ConcurrentHashMap <Integer, AtomicInteger> _map;

        // Note that the CountdownLatch is used to indicate that some thread got the last value expected: i.e. 100.
        public Consumer(int id,
                        CountDownLatch countDownLatch,
                        ReliablePubValue reliablePubValue,
                        ConcurrentHashMap<Integer, AtomicInteger> map)
        {
            _id = id;
            _countDownLatch = countDownLatch;
            _reliablePubValue = reliablePubValue;
            _map = map;
        }

        @Override
        public void run() {
            int v;
            while (true)  {
                v = _reliablePubValue.getNext(Integer.toString(_id));
                System.out.printf("Consumer[%02d]: received value: %d\n",  _id,  v);
                AtomicInteger atomicInteger = _map.computeIfAbsent(v, (k) -> new AtomicInteger(0));
                atomicInteger.incrementAndGet();
                _countDownLatch.countDown();
                if (v >= 100) {
                    break;
                }
            }

        }
    }
    @Test
    public void testLockstep() throws InterruptedException {
        ReliablePubValue reliablePubValue = new ReliablePubValue();
        Thread producerThread = new Thread(
                () -> IntStream.range(0, 100).forEach(x -> { reliablePubValue.publish(); })
        );
        producerThread.start();

        List<Thread> subscriberThreads = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ConcurrentHashMap <Integer, AtomicInteger> map = new ConcurrentHashMap<>();

        for (int i=0; i<10; i++) {
            Consumer consumer = new Consumer(i, countDownLatch, reliablePubValue, map);
            Thread subscriber = new Thread(consumer);
            subscriberThreads.add(subscriber);
        }

        subscriberThreads.forEach(Thread::start);
        countDownLatch.await();
        for (int i=1; i<= 100; i++) {
            AtomicInteger atomicInteger = map.get(i);
            System.out.printf("Verify map for %d\n", i);
            Assert.assertNotNull(atomicInteger);
            Assert.assertEquals(1, atomicInteger.get());
        }
    }

}
