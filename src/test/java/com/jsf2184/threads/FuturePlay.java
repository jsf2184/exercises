package com.jsf2184.threads;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FuturePlay {
    @Test
    public void completableFutureCanBeCompletedBeforeGetAndGottenMoreThanOnce() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete("abc");
        final String s = completableFuture.get();
        Assert.assertEquals("abc", s);
        final String t = completableFuture.get();
        Assert.assertEquals("abc", t);

    }

    @Test
    public void runAsynchTest() throws InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync( () -> {
            try {
                log.info("started in runAsynch");
                TimeUnit.SECONDS.sleep(5);
                log.info("finished in runAsynch");
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        });
        while(!future.isDone()) {
            log.info("Waiting for runAsynch to finish");
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void supplyAsynchTest() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync( () -> {
            try {
                log.info("started in supplyAsync");
                TimeUnit.SECONDS.sleep(5);
                log.info("finished in supplyAsync");
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
            return "hi";
        });
        while(!future.isDone()) {
            log.info("Waiting for supplyAsync to finish");
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("got '{}'", future.get());
    }

    @Test
    public void supplyAsynchAndApplyTest() throws InterruptedException, ExecutionException {
        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("started in supplyAsync");
                TimeUnit.SECONDS.sleep(5);
                log.info("finished in supplyAsync");
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
            return "123";
        }).thenApply(s -> {
            log.info("started in thenApply");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            log.info("finished in thenApply");
            return Integer.parseInt(s);

        });
        while(!future.isDone()) {
            log.info("Waiting for supplyAsync to finish");
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("got '{}'", future.get());
    }


}
