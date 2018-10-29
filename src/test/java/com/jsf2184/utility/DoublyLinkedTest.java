package com.jsf2184.utility;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class DoublyLinkedTest {

    @Test
    public void testInitialEmpty() {
        DoublyLinkedList sut = new DoublyLinkedList();
        Assert.assertNull(sut.next());
        Assert.assertTrue(sut.empty());
    }
    
    @Test
    public void addOne() {
        DoublyLinkedList sut = new DoublyLinkedList();
        sut.add(1,1);
        printForward(sut);
        printBackward(sut);
        DoublyLinkedList.Node node = sut.find(1);
        validateNode(node, 1, 1);
        node = sut.next();
        validateNode(node, 1, 1);
        Assert.assertTrue(sut.empty());
    }

    @Test
    public void populate3RemoveInOrder() {
        DoublyLinkedList sut = populateList(3);
        printForward(sut);
        printBackward(sut);
        IntStream.range(0, 3).forEach(i -> {
            DoublyLinkedList.Node node = sut.next();
            validateNode(node, i, i);
        });
        Assert.assertTrue(sut.empty());
    }

    @Test
    public void populate3AndFindAndRemoveEach() {
        for (int i=0; i<3; i++ ){
            Set<Integer> set = new HashSet<>();
            DoublyLinkedList sut = populateList(3);
            DoublyLinkedList.Node node = sut.find(i);
            validateNode(node, i, i);
            sut.remove(node);
            printForward(sut);
            printBackward(sut);
            set.add(node.getValue());

            // Now remove 2 more
            DoublyLinkedList.Node node1 = sut.next();
            DoublyLinkedList.Node node2 = sut.next();
            int v1 = node1.getValue();
            int v2 = node2.getValue();
            Assert.assertTrue(v1 < v2);
            set.add(v1);
            set.add(v2);
            Assert.assertTrue(sut.empty());
            Assert.assertEquals(3, set.size());
            Assert.assertTrue(set.contains(0));
            Assert.assertTrue(set.contains(1));
            Assert.assertTrue(set.contains(2));
        }
    }


    public DoublyLinkedList populateList(int n) {
        DoublyLinkedList sut = new DoublyLinkedList();
        IntStream.range(0, n).forEach(i -> sut.add(i, i));
        return sut;
    }

    void validateNode(DoublyLinkedList.Node node, int key, int value) {
        Assert.assertNotNull(node);
        Assert.assertEquals(key, node.getKey());
        Assert.assertEquals(value, node.getValue());
    }
    void printForward(DoublyLinkedList sut) {
        System.out.println("\nList Printed forward");
        sut.printForward();
    }

    void printBackward(DoublyLinkedList sut) {
        System.out.println("\nList Printed backward");
        sut.printBackward();
    }


}
