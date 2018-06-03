package com.jsf2184.se8.streams;

import com.jsf2184.se8.Employee;
import com.jsf2184.se8.Person;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParallelTests {
    @Test
    public void testForkPoolSize() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println(forkJoinPool.getParallelism());
    }

    Predicate<Employee> _filter = e -> {
        boolean res = e.getId() % 2 == 0;
        System.out.printf("Filtering employee: %s, res=%s, thread: %s\n",
                          e,
                          res,
                          Thread.currentThread().getName());
        return res;
    };

    Function<Employee, Integer> _mapFunc = e -> {
        System.out.printf("Mapping employee: %s, thread: %s\n",
                          e,
                          Thread.currentThread().getName());
        return e.getId();

    };

    @Test
    public void testSerial() {
        Employee.createEmployees(20)
                .stream()
                .parallel()
                .filter(_filter)
                .map(_mapFunc)
                .forEach(System.out::println);
    }




}
