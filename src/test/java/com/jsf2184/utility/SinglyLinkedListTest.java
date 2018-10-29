package com.jsf2184.utility;

import org.junit.Assert;
import org.junit.Test;

public class SinglyLinkedListTest {

    @Test
    public void testInitialEmpty() {
        SinglyLinkedList sut = new SinglyLinkedList();
        Assert.assertNull(sut.next());
        Assert.assertTrue(sut.empty());
    }

    @Test
    public void testAddTwoAndRemoveThem() {
        SinglyLinkedList sut = new SinglyLinkedList();
        sut.add(1);
        Assert.assertFalse(sut.empty());
        sut.add(2);
        Assert.assertEquals(1, sut.next().intValue());
        Assert.assertEquals(2, sut.next().intValue());
        Assert.assertNull(sut.next());
    }


}
