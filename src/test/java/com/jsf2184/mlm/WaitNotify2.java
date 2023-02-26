package com.jsf2184.mlm;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class WaitNotify2 {

    public static class Relay {

        Integer msg = null;
        int last;
        public Relay(int last) {
            this.last = last;
        }

        public synchronized void  write(int msg) {
            long threadId = Thread.currentThread().getId();
            System.out.printf("Thread %d is attempting to write %d\n", threadId, msg);
            while(this.msg != null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            this.msg = msg;
            System.out.printf("Thread %d wrote %d\n", threadId, msg);

            notifyAll();
        }

        public synchronized Integer read() {
            while(msg == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            Integer res =  msg;
            if (res != last) {
                msg = null;
            }
            notifyAll();
            return res;
        }
    }

    static final int LAST = 100;
    static final int RECEIVER_COUNT = 2;
    static final int WRITER_COUNT = 3;

    @Test
    public void twoReaders() {

        Relay relay = new Relay(LAST);

        final CountDownLatch latch = new CountDownLatch(RECEIVER_COUNT);
        final Map<Integer, AtomicInteger> map = new ConcurrentHashMap<>();
        final AtomicInteger nextPub = new AtomicInteger(0);

        Runnable writer = () -> {
            int val;
            long threadId = Thread.currentThread().getId();
            System.out.printf("Writer %d started\n", threadId);

            int numWritten = 0;
            while((val = nextPub.getAndIncrement()) <= LAST) {
                relay.write(val);
                numWritten++;
            }
            System.out.printf("Writer %d wrote %d messages\n", threadId, numWritten);
        };

        Runnable reader = () -> {
            long threadId = Thread.currentThread().getId();
            System.out.printf("Started Reading Thread %d\n", threadId);

            while(true) {
                Integer msg = relay.read();
                AtomicInteger receiptCount = map.computeIfAbsent(msg, (k) -> new AtomicInteger(0));
                receiptCount.incrementAndGet();
                if (msg == LAST) {
                    System.out.printf("Thread %d saw last msg, quitting\n", threadId);
                    break;
                }
            }
            latch.countDown();
        };
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        IntStream.range(0, RECEIVER_COUNT).forEach(i -> executorService.submit(reader));
        IntStream.range(0, WRITER_COUNT).forEach(i -> executorService.submit(writer));

//        IntStream.range(0, LAST+1).forEach(relay::write);
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        IntStream.range(0,LAST).forEach(i -> Assert.assertEquals(map.get(i).intValue(), 1));
        Assert.assertEquals(map.get(LAST).intValue(), RECEIVER_COUNT);


    }
}
