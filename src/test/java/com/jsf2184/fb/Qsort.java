package com.jsf2184.fb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Qsort {

    public static ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Data
    @AllArgsConstructor
    public static class Args {
        int start;
        int end;
    }

    @Test
    public void testQsort() {
        int[] arr = {1, 7,6,10, 13,14,9, 16,3,8, 15, 12};
        qSortRecurse(arr);
        log.info("results: {}", arr);
    }

    @Test
    public void testQsortParallel() {
        int[] arr = {1, 7,6,10, 13,14,9, 16,3,8, 15, 12};
        final Args args = new Args(0, arr.length - 1);
        sortParallel(arr);
        log.info("results: {}", arr);
    }

    public static int partition(int[] arr, int start, int end) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("partition(): start={}, end = {}", start, end);
        int numSmaller=start;
        for (int i=start, pivot=arr[end]; i < end; i++) {
            if (arr[i] < pivot) {
                swap(arr, i, numSmaller);
                numSmaller++;
            }
        }
        swap(arr, end, numSmaller);
        return numSmaller;
    }

    public static void qSortRecurse(int[] arr) {
        qSortRecurse(arr, 0, arr.length-1);
    }

    public static void qSortRecurse(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        final int p = partition(arr, start, end);
        qSortRecurse(arr, start, p-1);
        qSortRecurse(arr, p+1, end);
    }

    public static void sortParallel(int[] arr) {
        final Args args = new Args(0, arr.length - 1);
        ConcurrentHashMap<Integer, Future<?>> concurrentMap = new ConcurrentHashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        final Future<?> future = executorService.submit(() -> {
            sortParallel(arr, args, atomicInteger, concurrentMap);
        });
        concurrentMap.put(1, future);
        Set<Integer> done = new HashSet<>();
        while (true) {
            final int doneSize = done.size();
            final int atomicValue = atomicInteger.get();
            log.info("doneSize = {}, atomicValue = {}", doneSize, atomicValue);
            if (doneSize >= atomicValue) {
                break;
            }
            for (int k : concurrentMap.keySet()) {
                if (done.contains(k)) {
                    continue;
                }
                final Future<?> f = concurrentMap.get(k);
                try {
                    f.get();
                    log.info("Adding {} to doneset", k);
                    done.add(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sleep();
        }
        log.info("numFinished = {}", done.size());
    }

    public static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sortParallel(int[] arr,
                                    Args args,
                                    AtomicInteger atomicInteger,
                                    ConcurrentHashMap<Integer, Future<?>> concurrentMap) {
        if (args.start < args.end) {
            int newCount = atomicInteger.incrementAndGet();
            final int p = partition(arr, args.start, args.end);
            final Args args1 = new Args(args.start, p - 1);
            final Future<?> future1 = executorService.submit(() -> {
                sortParallel(arr, args1, atomicInteger, concurrentMap);
            });
            concurrentMap.put(newCount, future1);

            newCount = atomicInteger.incrementAndGet();
            final Args args2 = new Args(p + 1, args.end);
            final Future<?> future2 = executorService.submit(() -> {
                sortParallel(arr, args2, atomicInteger, concurrentMap);
            });
            concurrentMap.put(newCount, future2);
        }
    }


    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
