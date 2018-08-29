package com.jsf2184.se8.iterate;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class LinkedListTests {
    LinkedList<Integer> populate(int n) {
        LinkedList<Integer> res = new LinkedList<>();
        IntStream.range(0, n).boxed().forEach(res::add);
        return res;
    }

    public <T> void print(LinkedList<T> list) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T curr = it.next();
            System.out.println(curr);
        }
    }

    public <T> void printWithEnum(List<T> list) {
        Enumeration<T> enumeration = Collections.enumeration(list);
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }
    }


    @SuppressWarnings("Java8CollectionRemoveIf")
    public <T> void delete(LinkedList<T> list, T val) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T curr = it.next();
            if (curr.equals(val)) {
                it.remove();
            }
        }
    }


    @Test
    public void testPopAndPrint() {
        LinkedList<Integer> list = populate(10);
        print(list);
    }

    @Test
    public void testPopAndPrintWithEnumeration() {
        LinkedList<Integer> list = populate(10);
        printWithEnum(list);
    }


    @Test
    public void testDelete() {
        LinkedList<Integer> list = populate(10);
        delete(list, 3);
        print(list);
    }

    @Test
    public void iterateSizeZeroPlay() {
        LinkedList<Integer> list = populate(0);
        Iterator<Integer> iterator = list.iterator();
        System.out.printf("next count %d, hasNext = %s\n", 0, iterator.hasNext());
    }

    @Test
    public void iterateCanCallHasNextOverAndOverWithoutChangingOutcome() {
        LinkedList<Integer> list = populate(1);
        Iterator<Integer> iterator = list.iterator();
        IntStream.range(0, 100).forEach(x -> Assert.assertTrue(iterator.hasNext()));
        Assert.assertEquals(0, iterator.next().intValue());
        IntStream.range(0, 100).forEach(x -> Assert.assertFalse(iterator.hasNext()));
    }

    // Consider a list with two elements: 0, 1
    @Test
    public void iteratorOnListWithNoElementsHasNextIsFalse() {
        LinkedList<Integer> list = populate(0);
        Iterator<Integer> iterator = list.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorOnListWithOneElementsHasNextIsTrueUntilWeDoAGetNext() {
        LinkedList<Integer> list = populate(1);
        Iterator<Integer> iterator = list.iterator();
        Assert.assertTrue(iterator.hasNext());
        // actually get the value.
        Integer value = iterator.next();
        Assert.assertEquals(0, value.intValue());

        // Note that we are now pointing at the first element if we were going to remove at the iterator are thinking
        // about the (nonexistent) element after first one.
        //
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(1, list.size());

        // Now lets delete the zero in the element we are pointing at.
        iterator.remove();
        Assert.assertEquals(0, list.size());
    }


    @Test
    public void testCantRemoveUntilWeArePointingAtAnElement() {
        LinkedList<Integer> list = populate(1);
        Iterator<Integer> iterator = list.iterator();
        Assert.assertTrue(iterator.hasNext());
        boolean caught = false;
        try {
            iterator.remove();
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName());
            Assert.assertEquals("IllegalStateException", e.getClass().getSimpleName());
            caught = true;
        }
        Assert.assertTrue(caught);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void iteratorHasNextAndGetNextStillWorkAfterWeRemoveTheCurrentElement() {
        LinkedList<Integer> list = populate(2);
        Iterator<Integer> iterator = list.iterator();
        Assert.assertTrue(iterator.hasNext());
        // actually get the value.
        Integer value = iterator.next();
        Assert.assertEquals(0, value.intValue());

        // Remove the zero element we were pointing at.
        iterator.remove();

        // hasNext is still true
        Assert.assertTrue(iterator.hasNext());
        // next() still works
        value = iterator.next();
        Assert.assertEquals(1, value.intValue());

        // and the size of the list is 1 instead of two because of the removal.
        Assert.assertEquals(1, list.size());
    }


}
