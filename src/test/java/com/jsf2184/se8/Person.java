package com.jsf2184.se8;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Person {
    String _name;
    int _age;
    List<Integer> _childAges;

    public Person(String name, int age) {
        _name = name;
        _age = age;
        _childAges = popChildAges();
    }

    static List<Integer> popChildAges() {
        Random r = new Random();
        List<Integer> res = IntStream.range(0, 3).map(x -> r.nextInt(10)).boxed().collect(Collectors.toList());
        return res;
    }
    public String getName() {
        return _name;
    }

    public int getAge() {
        return _age;
    }

    public static List<Person> createPersonList() {
        List<Person> res = Arrays.asList(new Person("Jeff", 61),
                                         new Person("Caryn", 62),
                                         new Person("Zack", 22),
                                         new Person("Alex", 24));
        return res;
    }

    public List<Integer> getChildAges() {
        return _childAges;
    }

    @Override
    public String toString() {
        return "Person{" +
                "_name='" + _name + '\'' +
                ", _age=" + _age +
                ", _children = " + _childAges +
                '}';
    }
}
