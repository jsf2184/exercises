package com.jsf2184.threads;

import com.jsf2184.utility.LoggerUtility;
import com.jsf2184.utility.Utility;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;


public class WaitTests {

    private static final Logger _log = Logger.getLogger(WaitTests.class);

    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }


    // With this PubValue class the publisher DOES NOT wait for a client to consume before possibly
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
                    // wait for the other thread to notify.
                    wait();
                } catch (InterruptedException ignore) {
                }
            }
            return _value;
        }
    }



    // For this test, we don't use our ReliablePubValue, we just use the plain PubValue that does not
    // prevent the writer from writing over values that have not ever been received.
    //
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
            _log.info(String.format("getNext(): returned %d", v));
        }
        Assert.assertEquals(10, v);
        _log.info(String.format("getNext(): returned %d", v));
    }

    public static class ReliablePubValue {
        int _value = 0;
        boolean _empty = true;
        boolean _done = false;

        public synchronized void publish() {
            // publisher uses waitSignal() to wait until the _empty flag is true, meaning that a consumer has
            // retrieved the last value that was published.
            //
            waitSignal(() -> _empty, "pubber");
            _value++;
            _empty = false;
            // notify consumers that we have a new value.
            notifyAll();
        }

        public synchronized void setDone() {
            waitSignal(() -> _empty, "pubDone");
            _done = true;
            notifyAll();
        }

        public synchronized Integer getNext(String id) {
            // Use waitSignal to wait until a published value is available (i.e. not empty)
            waitSignal(() -> !_empty || _done, id);
            if (_done) {
                notifyAll();
                return null;
            }
            _empty = true;
            notifyAll();
            return _value;
        }

        // Because we us this in multiple places, we pass in the condition we are waiting for...
        //  - empty if we are the publisher
        //  - not empty if we are the receiver.
        // Note also that we count on the calling method to be synchronized

        public void waitSignal(Supplier<Boolean> condition, String id) {
            while (!condition.get()) {
                try {
                    _log.info(String.format("ReliablePubValue.waitSignal(): begin wait() w/ id=%s", id));
                    wait();
                    _log.info(String.format("ReliablePubValue.waitSignal(): waking from wait(), w/ id=%s", id));

                } catch (InterruptedException ignore) {
                }
            }
            // When we get here, the condition is true and we have the lock.
            _log.info(String.format("ReliablePubValue.waitSignal(): returning for id=%s", id));
        }
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
            Integer v;
            while (true)  {
                v = _reliablePubValue.getNext(Integer.toString(_id));
                if (v == null) {
                    break;
                }
                _log.info(String.format("Consumer[%02d]: received value: %d",  _id,  v));
                AtomicInteger atomicInteger = _map.computeIfAbsent(v, (k) -> new AtomicInteger(0));
                atomicInteger.incrementAndGet();
                _countDownLatch.countDown();
            }
            _log.info(String.format("Consumer[%02d]: has completed",  _id));

        }
    }
    @Test
    public void testLockstep() throws InterruptedException {
        ReliablePubValue reliablePubValue = new ReliablePubValue();

        // producerThread will pub values 1 through 100, waiting for confirmation that prior
        // value was received before moving to the next value.
        //
        Thread producerThread = new Thread(
                () -> {
                    IntStream.range(0, 100).forEach(x -> reliablePubValue.publish());
                    reliablePubValue.setDone();
                }
        );
        producerThread.start();

        List<Thread> subscriberThreads = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ConcurrentHashMap <Integer, AtomicInteger> map = new ConcurrentHashMap<>();

        for (int i=0; i<4; i++) {
            // create 4 consumer threads. Each Consumer, when it successfully gets a value will
            // use the value it received as a key into a HashMap. The value in the map will be
            // a count of the times that number was received.
            //
            Consumer consumer = new Consumer(i, countDownLatch, reliablePubValue, map);
            Thread subscriber = new Thread(consumer);
            subscriberThreads.add(subscriber);
        }

        subscriberThreads.forEach(Thread::start);
        countDownLatch.await();
        for (int i=1; i<= 100; i++) {
            AtomicInteger atomicInteger = map.get(i);
            _log.info(String.format("Verify map for %d", i));
            Assert.assertNotNull(atomicInteger);
            Assert.assertEquals(1, atomicInteger.get());
        }

        Utility.sleep(100);
        AtomicInteger threadsRunning = new AtomicInteger(0);
        subscriberThreads.forEach(s -> {if (s.isAlive()) threadsRunning.incrementAndGet(); });
        _log.info(String.format("ThreadAliveCount = %d", threadsRunning.intValue()));

    }

}
