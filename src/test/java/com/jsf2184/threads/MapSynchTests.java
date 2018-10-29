package com.jsf2184.threads;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
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

    Map<Integer, Integer> createHashTable(int n) {
        Map<Integer, Integer> res = new Hashtable<>();
        addToMap(res, 0, n);
        return res;
    }

    void addToMap(Map<Integer, Integer> map,  int start, int end) {
        IntStream.range(start, end).forEach(i -> map.put(i, i*i));
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

    @Test
    public void testConcurrentWriterAndReadersWithHashTable() throws  Exception {
        Map<Integer, Integer> map = createHashTable(100000);
        runConcurrentWriterAndReaders(map);
    }

    @Test
    public void testDeleteFromHashMapWhileIterating() {
        Map<Integer, Integer> map = createMap(100);
        testDeleteWhileRemoving(map, "HashMap");
    }

    @Test
    public void testDeleteFromConcurrentHashMapWhileIterating() {
        Map<Integer, Integer> map = createConcurrentMap(100);
        testDeleteWhileRemoving(map, "HashMap");
        Assert.assertEquals(50, map.size());
        for (int i=1; i<100; i+=2) {
            Integer val = map.get(i);
            Assert.assertNotNull(val);
        }
    }

    void testDeleteWhileRemoving(Map<Integer, Integer> map, String label) {
        int res = 0;
        try {
            for (Integer key : map.keySet()) {
                if (key % 2 == 0) {
                    map.remove(key);
                }
            }
        } catch (Exception ignore) {
            res++;
        }
        System.out.printf("testDeleteWhileRemoving from %s, caught %d exceptions\n", label, res);
    }


    public void runConcurrentWriterAndReaders(Map<Integer, Integer> map) throws  Exception {
        AtomicInteger catchCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(11);
        List<Future<Integer>> futures = new ArrayList<>();

        // Start our writer who is writing w/ keys 100000 to 199999
        executorService.submit(() -> addToMap(map, 100000, 200000));

        // And start a bunch of readers
        for (int i=0; i< 10; i++) {
            Future<Integer> future = executorService.submit(() -> getMaxLoop(map, catchCount));
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
