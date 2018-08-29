package com.jsf2184.threads;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapSynchTests {

    Map<Integer, Integer> createMap(int n) {
        HashMap<Integer, Integer> res = new HashMap<>();
        addToMap(res, 0, n);
        return res;
    }

    Map<Integer, Integer> createSyncMap(int n) {
        Map<Integer, Integer> res = Collections.synchronizedMap(createMap(n));
        return res;
    }

    Map<Integer, Integer> createConcurrentMap(int n) {
        Map<Integer, Integer> res = new ConcurrentHashMap<>();
        addToMap(res, 0, n);
        return res;
    }

    void addToMap(Map<Integer, Integer> map,  int start, int end) {
        IntStream.range(start, end).forEach(i -> {map.put(i, i*i);});
    }

    public Integer getMaxLoop(Map<Integer, Integer> map, AtomicInteger catchCount) {
        int largest = 0;
        try {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                largest = Math.max(largest, entry.getValue());
            }
        } catch (Exception ignore) {
            catchCount.addAndGet(1);
        }
        return largest;
    }


    @Test
    public void testConcurrentWriterAndReadersWithUnsynchronizedMap() throws  Exception {
        Map<Integer, Integer> map = createMap(100000);
        runConcurrentWriterAndReaders(map);
    }

    @Test
    public void testConcurrentWriterAndReadersWithSynchronizedMap() throws  Exception {
        Map<Integer, Integer> map = createSyncMap(100000);
        runConcurrentWriterAndReaders(map);
    }

    @Test
    public void testConcurrentWriterAndReadersWithConcurrentHashMap() throws  Exception {
        Map<Integer, Integer> map = createConcurrentMap(100000);
        runConcurrentWriterAndReaders(map);
    }


    public void runConcurrentWriterAndReaders(Map<Integer, Integer> map) throws  Exception {
        AtomicInteger catchCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Integer>> futures = new ArrayList<>();
        executorService.submit(() -> {addToMap(map, 100000, 200000);});
        for (int i=0; i< 10; i++) {
            Future<Integer> future = executorService.submit(() -> {
                return getMaxLoop(map, catchCount);
            });
            futures.add(future);
        }
        long largestAns = 0;
        for (Future<Integer> future : futures) {
            Integer ans = future.get();
            largestAns = Math.max(largestAns, ans);
        }
        System.out.printf("Largest answer is %d, catchCount = %d\n", largestAns, catchCount.get());
        executorService.shutdownNow();
    }

}
