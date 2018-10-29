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
        List<Employee> employees = Employee.createEmployees(10);
        System.out.println("Here is the list of employees");
        Employee.createEmployees(10).forEach(System.out::println);
        Map<String, List<Employee>> map = employees
                .stream()
                .collect(Collectors.groupingBy(Employee::getFirstName));

        System.out.println("\nHere is the map of employees, grouping by name");
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
