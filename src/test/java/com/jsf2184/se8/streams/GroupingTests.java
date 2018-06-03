package com.jsf2184.se8.streams;

import com.jsf2184.se8.Employee;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingTests {

    @Test
    public void testCreateEmployees() {
        Employee.createEmployees(10).forEach(System.out::println);
    }

    @Test
    public void testOneLevelGroup() {
        Map<String, List<Employee>> map = Employee.createEmployees(10)
                .stream()
                .collect(Collectors.groupingBy(Employee::getFirstName));

        map.entrySet().forEach(System.out::println);
    }

    @Test
    public void testTwoLevelGroup() {
        Map<String, Map<String, List<Employee>>> twoLevelMap =
                Employee.createEmployees(10)
                .stream()
                .collect(Collectors.groupingBy(Employee::getFirstName,
                                               Collectors.groupingBy(Employee::getLocation)));

        twoLevelMap.entrySet().forEach(System.out::println);
    }


}
