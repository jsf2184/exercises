package com.jsf2184;

import com.jsf2184.utility.LoggerUtility;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SpliteratorTests {

    private static final Logger _log = Logger.getLogger(SpliteratorTests.class);
    ConcurrentMap<Integer, Integer> _concurrentMap;
    List<Integer> _inputs;
//    List<Spliterator<Integer>> _spliterators;


    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }

    @Before
    public void testSetup() {
        _concurrentMap = new ConcurrentHashMap<>();
        _inputs = new ArrayList<>();
        for (int i=0; i<131072; i++) {
            _inputs.add(i);
        }
//        _spliterators = createSpliteratorsRecursive(8, _inputs);

    }

    @Test
    public void testCreateSpliteratorWithStack() {
        verifyCreateSpliterators(_inputs, 8);
    }

    public void verifyCreateSpliterators(List<Integer> inputs, int depth) {
        // In our example we have an original list which is 131072 long. When we pass in a depth of 8, what we are
        // saying is that we want to reduce its size in half 8 times for the entire list. That is, we want to
        // divide it into pieces that are 2^8 = 256 smaller than the original. That would result in
        // 131072 / 256 = 512 spliterators.
        List<Spliterator<Integer>> spliterators = createSpliteratorsWithHelpFromStack(depth, inputs);
        int divisor = (int) Math.pow(2, depth);  // 256
        int expectedSize = inputs.size() / divisor;  // 131062
        spliterators.forEach(s -> Assert.assertEquals(expectedSize, s.getExactSizeIfKnown()));
    }

    public static List<Spliterator<Integer>> createSpliteratorsWithHelpFromStack(int depth, List<Integer> inputs) {
        Stack<Spliterator<Integer>> stack = new Stack<>();
        Stack<Integer> depthStack = new Stack<>();
        List<Spliterator<Integer>> res = new ArrayList<>();

        Spliterator<Integer> src = inputs.spliterator();
        depthStack.push(depth);
        stack.push(src);

        while (!stack.isEmpty()) {
            depth = depthStack.pop();
            src = stack.pop();
            if (depth == 0) {
                res.add(src);
            } else {
                Spliterator<Integer> other = src.trySplit();
                stack.push(src);
                depthStack.push(depth-1);
                stack.push(other);
                depthStack.push(depth-1);
            }
        }
        return res;
    }


    public static List<Spliterator<Integer>> createSpliteratorsRecursive(int depth, List<Integer> inputs) {
        List<Spliterator<Integer>> res = new ArrayList<>();
        Spliterator<Integer> spliterator = inputs.spliterator();
        createSpliteratorsRecursive(depth, spliterator, res);
        return res;
    }
    public static void createSpliteratorsRecursive(int depth,
                                                   Spliterator<Integer> src,
                                                   List<Spliterator<Integer>> spliterators) {
        if (depth == 0) {
            spliterators.add(src);
            return;
        }
        Spliterator<Integer> copy = src.trySplit();
        createSpliteratorsRecursive(depth-1, src, spliterators);
        createSpliteratorsRecursive(depth-1, copy, spliterators);
    }

    @Test
    public void test512SpliteratorsSequentially() {
        List<Spliterator<Integer>> spliterators = createSpliteratorsWithHelpFromStack(8, _inputs);

        int numSpliterators = spliterators.size();
        _log.info(String.format("There are %d spliterators", numSpliterators));
        spliterators.forEach(s -> _log.info(String.format("Spliterator has %d elements", s.getExactSizeIfKnown())));

        AtomicInteger spidx = new AtomicInteger();
        spliterators.forEach(sp ->
        {
            spidx.incrementAndGet();
            sp.forEachRemaining(i->_concurrentMap.put(i, spidx.intValue() ));
        });

        validateMap(numSpliterators);
    }

    @Test
    public void test512SpliteratorsParallel() throws InterruptedException {
        List<Spliterator<Integer>> spliterators = createSpliteratorsWithHelpFromStack(8, _inputs);
        int numSpliterators = spliterators.size();

        _log.info(String.format("There are %d spliterators", numSpliterators));
        AtomicInteger i= new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(spliterators.size());
        spliterators.stream().map(s -> new Worker(i.getAndIncrement(), s, latch)).forEach(Thread::start);
        latch.await();
        validateMap(numSpliterators);
    }

    public void validateMap(int numSpliterators) {
        HashMap<Integer, Integer> spliteratorCounts = new HashMap<>();

        // verify that every input is in the spliterator map and count the
        // number of results attributed to each spliterator.
        //
        _inputs.forEach(i -> {
            Integer splitId = _concurrentMap.get(i);
            Assert.assertNotNull(splitId);
            spliteratorCounts.merge(splitId, 1, (o, n) -> o+n);
        });
        Integer expectedSize = _inputs.size() / numSpliterators;
        spliteratorCounts.values().forEach(v -> Assert.assertEquals(expectedSize, v));
    }

    public class Worker extends Thread {
        int _id;
        Spliterator<Integer> _spliterator;
        CountDownLatch _latch;

        public Worker(int id, Spliterator<Integer> spliterator, CountDownLatch latch) {
            _id = id;
            _spliterator = spliterator;
            _latch = latch;
        }

        @Override
        public void run() {
            _spliterator.forEachRemaining(i -> _concurrentMap.put(i, _id));
            _latch.countDown();
        }
    }



}
