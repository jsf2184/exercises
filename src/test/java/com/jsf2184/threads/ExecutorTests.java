package com.jsf2184.threads;

import com.jsf2184.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
public class ExecutorTests {

    public static class Job {
        CountDownLatch _latch;
        private int _id;
        private Integer _exceptionMod;

        public Job(CountDownLatch latch, int id) {
            this(latch, id, null);
        }

        public Job(CountDownLatch latch, int id, Integer exceptionMod) {
            _latch = latch;
            _id = id;
            _exceptionMod = exceptionMod;
        }

        public void run() {
            System.out.printf("Job.run(): %d, My thread is named %s\n", _id, Thread.currentThread().getName());
            if (_exceptionMod != null && _id % _exceptionMod == 0) {
                _latch.countDown();
                throw new RuntimeException(String.format("JobExecution for id = %d", _id));
            }
            Utility.sleep(100);
            _latch.countDown();
        }

        public Integer compute() {
            System.out.printf("Job.compute(): %d, My thread is named %s\n", _id, Thread.currentThread().getName());
            Utility.sleep(100);
            _latch.countDown();
            return _id;
        }

    }

    @Test
    public void testSingleThreadExecutorWithSubmits() throws InterruptedException {
        // A SingleThreadExecutor only uses one thread, so there are all done one at a time.
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        submitAndWait(executorService, 100, null);
    }

    @Test
    public void testFixedThreadPoolWithSubmits() throws InterruptedException {
        // Now we use an ExecutorService that uses 5 threads at a time.
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        submitAndWait(executorService, 100, 19);

    }

    @Test
    public void testCachedThreadPoolWithSubmits() throws InterruptedException {
        // Now we use an ExecutorService that uses 5 threads at a time.
        ExecutorService executorService = Executors.newCachedThreadPool();
        submitAndWait(executorService, 100, 19);

    }


    @Test
    public void testFixedThreadPoolWithFutures() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        submitFutures(executorService, 100);
    }

    // Submit 'count' jobs to the ExecutorService. In this submitAndWait() method, we submit 'count' number
    // of jobs. Each 'job' is basically that of a 'Runnable'. That is, it is just a void method, returning nothing.
    // So, we use an ExectuorService to launch all the jobs. Each job has access to the same CountdownLatch so
    // it can signal that it is done. The CountDownLatch was initialized to 'count', so when it hits zero, all the
    // launched little threads are done.
    //
    public void submitAndWait(ExecutorService executorService,
                              int count,
                              Integer exceptionMod) throws InterruptedException
    {
        CountDownLatch latch = new CountDownLatch(count);

        // Launch all the jobs.
        HashMap<Integer, Future<?>> futures = new HashMap<>();
        IntStream.range(0, count).forEach(i -> {
            Job job = new Job(latch, i, exceptionMod);
            // this overload of submit() returns nothing.
            Future<?> future = executorService.submit(job::run);
            futures.put(i, future);
        });

        // Use the latch to wait for their completion.
        latch.await();
        executorService.shutdown();
        for (Map.Entry<Integer, Future<?>> mapEntry : futures.entrySet()) {
            Integer i = mapEntry.getKey();
            try {
                // If there was a problem with a Job, a call to its future.get() would return an exception
                Future<?> future = mapEntry.getValue();
                Object o = future.get();
                Assert.assertNull(o);
            } catch (ExecutionException e) {
                Throwable causeException = e.getCause();
                log.warn(String.format("For value: %d, Caught ExecutionException: %s - %s",
                                        i,
                                        causeException.getClass().getSimpleName(),
                                        causeException.getMessage()));
            }
        }
    }

    // Here we submit Callables to the ExecutorService. Each Callable returns a value (an Integer) when it is finished.
    // However, it wraps the Integer in a Future which if done can yield the value that the job returned.
    //
    public void submitFutures(ExecutorService executorService, int count) throws InterruptedException, ExecutionException
    {
        Map<Integer, Future<Integer>> futureMap = new HashMap<>();
        CountDownLatch latch = new CountDownLatch(count);
        IntStream.range(0, count).forEach(i -> {
            Job job = new Job(latch, i);
            // this overload of submit() returns a Future<Integer>
            Future<Integer> future = executorService.submit(job::compute);
            // store it for later access.
            futureMap.put(i, future);
        });
        latch.await();
        for (Integer k : futureMap.keySet()) {
            Future<Integer> future = futureMap.get(k);
            Assert.assertTrue(future.isDone());
            Assert.assertEquals(k, future.get());
        }

        executorService.shutdown();
    }

    @Test
    public void testFutureTimeout() {
        Callable<Integer> callable = () -> {
            log.info("Entered callable, wait 3 seconds");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                log.warn("Callable caught exception: ", e);
            }
            int res = 10;
           log.info("Exiting callable, after 3 seconds, returning: " + 10);
            return  res;
        };
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<Integer> integerFuture = executorService.submit(callable);
        try {
            Integer val = integerFuture.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("In integerFuture.get(): Caught exception: " + e.getClass().getSimpleName());
        }
        executorService.shutdown();
        log.info("Wait for executorService to complete");
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("In executorService.awaitTermination(), caught exception: ", e);
        }
        if (executorService.isTerminated()) {
            log.info("ExecutorService graceful termination");
        } else {
            log.info("ExecutorService forcing termination");
            executorService.shutdownNow();
        }
        log.info("ExecutorService completed");
    }

    public static class Sum {
        int v1;
        int v2;
        int sum;

        public Sum(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public Sum(Sum s1, Sum s2) {
            this.v1 = s1.sum;
            this.v2 = s2.sum;
        }

        public Sum calc() {
            sleep(100);
            sum = v1 + v2;
            log.info("Added {} + {} = {}", v1, v2, sum);
            return this;
        }

    }




    @Test
    public void testParallelSum() throws ExecutionException, InterruptedException {
        final int result = parallelSum(10);
        Assert.assertEquals(55, result);
    }

    public static int parallelSum(int count) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Future<Sum>> pendingSums = new ArrayList<>();
        int start = count %2 == 0 ? 1 : 0;

        for (int i=start; i<=count; i+=2) {
            Sum sum = new Sum(i, i+1);
            final Future<Sum> sumFuture = executorService.submit(sum::calc);
            pendingSums.add(sumFuture);
        }

        int i=0;
        while (true) {
            final Future<Sum> sumFuture1 = pendingSums.get(i++);
            final Sum sum1 = sumFuture1.get();
            if (i >= pendingSums.size()) {
                return sum1.sum;
            }
            final Future<Sum> sumFuture2 = pendingSums.get(i++);
            final Sum sum2 = sumFuture2.get();
            final Sum next = new Sum(sum1, sum2);
            final Future<Sum> nextFuture = executorService.submit(next::calc);
            pendingSums.add(nextFuture);
        }
    }


    public static void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (Exception e) {
        }
    }
    
    

}
