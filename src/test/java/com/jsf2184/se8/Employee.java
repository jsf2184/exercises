package com.jsf2184.se8;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Employee {

    private static final String names[] = {"Bob", "Mary", "Joe", "Sam", "Ellen"};
    private static final String locations[] = {"Chicago", "Austin", "Clayton", "NY"};

    private final String _firstName;
    private final String _location;
    private final int _id;

    public Employee(String firstName, String location, int id) {
        _firstName = firstName;
        _location = location;
        _id = id;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLocation() {
        return _location;
    }

    public int getId() {
        return _id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "_firstName='" + _firstName + '\'' +
                ", _location='" + _location + '\'' +
                ", _id=" + _id +
                '}';
    }

    public static List<Employee> createEmployees(int n) {
        Random random = new Random();
        List<Employee> res = IntStream.range(1, n + 1)
                .boxed()
                .map(i -> {
            Employee employee = new Employee(names[random.nextInt(names.length)],
                                      locations[random.nextInt(locations.length)],
                                      i);
            return employee;})
        .collect(Collectors.toList());
        return res;
    }
}
