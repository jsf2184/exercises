package com.jsf2184.threads;

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

    public static class WaitValue {
        int _value = 0;
        public synchronized void publish() {
            _value++;
            notify();
        }

        public synchronized int getNext(int prior) {
            if (_value > prior) {
                return _value;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return _value;
        }
    }

    public static class WaitValueLockstep {
        int _value = 0;
        boolean _empty = true;

        public synchronized void publish() {
            waitSignal(() -> _empty);
            _value++;
            _empty = false;
            notifyAll();
        }

        public synchronized int getNext() {
            waitSignal(() -> !_empty);
            _empty = true;
            notifyAll();
            return _value;
        }

        public void waitSignal(Supplier<Boolean> condition) {
            while (!condition.get()) {
                try {
                    wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
    }


    @Test
    public void testWait() {
        WaitValue waitValue = new WaitValue();
        int v = 0;
        new Thread(() -> IntStream.range(0, 10).forEach(x -> {waitValue.publish(); /* sleep(1000); */})).start();
        while ((v = waitValue.getNext(v)) != 10) {
            System.out.printf("getNext(): returned %d\n", v);
        }
        System.out.printf("getNext(): returned %d\n", v);
    }

    public static class Consumer implements Runnable {
        int _id;
        CountDownLatch _countDownLatch;
        WaitValueLockstep _waitValueLockstep;
        ConcurrentHashMap <Integer, AtomicInteger> _map;


        public Consumer(int id,
                        CountDownLatch countDownLatch,
                        WaitValueLockstep waitValueLockstep,
                        ConcurrentHashMap<Integer, AtomicInteger> map)
        {
            _id = id;
            _countDownLatch = countDownLatch;
            _waitValueLockstep = waitValueLockstep;
            _map = map;
        }

        @Override
        public void run() {
            int v;
            while (true)  {
                v = _waitValueLockstep.getNext();
                System.out.printf("Consumer[%02d]: received value: %d\n",  _id,  v);
                AtomicInteger atomicInteger = _map.computeIfAbsent(v, (k) -> new AtomicInteger(0));
                atomicInteger.incrementAndGet();
                if (v >= 100) {
                    _countDownLatch.countDown();
                    break;
                }
            }

        }
    }
    @Test
    public void testLockstep() throws InterruptedException {
        WaitValueLockstep waitValueLockstep = new WaitValueLockstep();
        Thread producerThread = new Thread(
                () -> IntStream.range(0, 100).forEach(x -> { waitValueLockstep.publish(); })
        );
        producerThread.start();

        List<Thread> subscriberThreads = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ConcurrentHashMap <Integer, AtomicInteger> map = new ConcurrentHashMap<>();

        for (int i=0; i<10; i++) {
            Consumer consumer = new Consumer(i, countDownLatch, waitValueLockstep, map);
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
