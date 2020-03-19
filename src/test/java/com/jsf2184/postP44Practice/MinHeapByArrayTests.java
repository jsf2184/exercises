package com.jsf2184.postP44Practice;

import org.junit.Test;

public class MinHeapByArrayTests {

    @Test
    public void testSimpleScenario() {
        MinHeapByArray minHeap = new MinHeapByArray(5);
        minHeap.push(3);
        minHeap.push(7);
        minHeap.push(2);
        minHeap.push(9);
        minHeap.push(1);

        minHeap.print();

        System.out.println(minHeap.peek());
        System.out.println(minHeap.pop());
        System.out.println(minHeap.peek());
    }
}
