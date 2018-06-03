package com.jsf2184.se8.streams;

import com.jsf2184.se8.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamPipeTests {
    @Test
    public void testListToMap() {
        List<Person> people = Person.createPersonList();
        Map<String, Integer> map = people
                .stream()
                .collect(Collectors.toMap(Person::getName, Person::getAge));
        Assert.assertEquals(4, map.size());
        Assert.assertEquals(24, (int) map.get("Alex"));
        Assert.assertEquals(22, (int) map.get("Zack"));
        Assert.assertEquals(61, (int) map.get("Jeff"));
        Assert.assertEquals(62, (int) map.get("Caryn"));
    }

    @Test
    public void demoLazyStreams() {
        List<Person> people = Person.createPersonList();
        System.out.println("Create filtered Stream");
        Stream<Person> filteredStream = people.stream().filter(p -> {
            System.out.printf("Evaluating %s\n", p);
            return p.getAge() < 30;
        });
        System.out.println("Done Creating filtered Stream");
        filteredStream.forEach(System.out::println);
    }

    @Test
    public void testIntegerStreamToIntStreamConversion() {
        List<Person> people = Person.createPersonList();
        // Note that it returned a stream of Integer objects
        Stream<Integer> integerStream = people.stream().map(Person::getAge);
        IntStream intStream = integerStream.mapToInt(Integer::intValue);
        System.out.printf("total age of all people: %d\n", intStream.sum());

    }

    @Test
    public void testIntegerSummingWitoutIntConversion() {
        List<Person> people = Person.createPersonList();
        // Note that it returned a stream of Integer objects
        Optional<Integer> sum = people
                .stream()
                .map(Person::getAge)
                .reduce(Integer::sum);
        sum.ifPresent(integer -> System.out.printf("total age of all people: %d\n", integer));
        Integer total = sum.orElse(0);
        System.out.printf("total age of all people: %d\n", total);
    }

    @Test
    public void testIntegerSummingWhenListIsEmpty() {
        List<Person> people = new ArrayList<>();
        // Note that it returned a stream of Integer objects
        Optional<Integer> sum = people
                .stream()
                .map(Person::getAge)
                .reduce(Integer::sum);

        Assert.assertFalse(sum.isPresent());
        Integer integer = sum.orElse(0);
        Assert.assertEquals(0, integer.intValue());


    }

    @Test
    public void testFlatMap() {
        List<Person> people = Person.createPersonList();
        System.out.println("The complete set of all children ages");
        people.stream().flatMap(p -> p.getChildAges().stream()).distinct().forEach(System.out::println);

    }





}
