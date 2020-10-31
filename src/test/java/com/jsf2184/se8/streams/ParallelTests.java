package com.jsf2184.se8.streams;

import com.jsf2184.BlockingQueueTests;
import com.jsf2184.se8.Employee;
import com.jsf2184.se8.Person;
import com.jsf2184.utility.LoggerUtility;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ParallelTests {

    private static final Logger _log = Logger.getLogger(ParallelTests.class);

    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }


    @Test
    public void testForkPoolSize() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        _log.info(String.format("forkJoinPool.getParallelism() = %d", forkJoinPool.getParallelism()));
    }

    Predicate<Employee> _filter = e -> {
        boolean res = e.getId() % 2 == 0;
        _log.info(String.format("Filtering employee: %s, res=%s",
                                e,
                                res));
        return res;
    };

    Function<Employee, Integer> _mapFunc = e -> {
        _log.info(String.format("Mapping employee: %s",
                          e));
        return e.getId();

    };

    @Test
    public void testSerial() {
        Employee.createEmployees(20)
                .stream()
                .parallel()
                .filter(_filter)
                .map(_mapFunc)
                .forEach(v -> _log.info(String.format("terminal value: %d", v)) );
    }

    @Test
    public void testParallelIntoList() {
        List<Integer> res = Employee.createEmployees(20)
                .stream()
                .parallel()
                .filter(_filter)
                .map(_mapFunc)
                .collect(Collectors.toList());
        res.forEach(i -> System.out.printf("output element: %d\n", i));
    }



}
