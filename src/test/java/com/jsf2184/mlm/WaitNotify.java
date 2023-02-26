package com.jsf2184.mlm;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class WaitNotify {

    public static class Message {
        int msg;
        int sendCount = 0;
        int recvCount = 0;
        public synchronized void send(int msg) {
            while (sendCount > recvCount) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
            this.msg = msg;
            sendCount++;
            System.out.println("Sent a message");
            notifyAll();
        }
        public synchronized int receive() {
            while(recvCount>=sendCount) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
            recvCount++;
            System.out.printf("Got a message %s\n", msg);
            notifyAll();
            return msg;
        }
    }

    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void onePubOneSend() {
        final Message message = new Message();
        final AtomicInteger receiptCount = new AtomicInteger(0);
        ConcurrentHashMap<Integer, AtomicInteger> map = new ConcurrentHashMap<>();

        int numMessages = 100;
        CountDownLatch latch = new CountDownLatch(numMessages);
        Runnable listen = () -> {
            while(receiptCount.intValue() < numMessages) {
                int msg = message.receive();
                int cnt = receiptCount.incrementAndGet();
                AtomicInteger msgReceiveCount = map.computeIfAbsent(msg, k-> new AtomicInteger(0));
                msgReceiveCount.incrementAndGet();

                System.out.printf("Thread: %d, received number: %d with  message content: %d\n",
                                  Thread.currentThread().getId(), cnt, msg);
                latch.countDown();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(listen);
        IntStream.range(0, numMessages).parallel().forEach(message::send);
        try {
            latch.await();
        } catch (InterruptedException ignore) {
        }
        IntStream.range(0, numMessages).forEach(i -> Assert.assertEquals(map.get(i).intValue(), 1));
        System.out.println("Joined the listen thread");
    }
}
