package com.jsf2184.postP44Practice;

public class MinHeapByArray {
    private int[] heap;
    private int size;

    public MinHeapByArray(int capacity) {
        this.heap = new int[capacity+1];
        this.heap[0] = Integer.MIN_VALUE;
        this.size = 0;
    }

    private void swap(int i, int j) {
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void heapifyDown(int k) {
        int smallest = k;
        int leftIndex = 2*k;
        int rightIndex = 2*k + 1;

        if (leftIndex <= heap.length && heap[leftIndex] < heap[smallest]){
            smallest = leftIndex;
        }

        if (rightIndex <= heap.length && heap[rightIndex] < heap[smallest]) {
            smallest = rightIndex;
        }

        if (smallest != k) {
            swap(k, smallest);
            heapifyDown(smallest);
        }
    }

    private void heapifyUp(int childIdx) {
        // need to create relationship where parent is always less than its child, so small values
        // will keep percolating to the top.
        while (true) {
            int parentIdx = childIdx/2;
            int childVal = heap[childIdx];
            int parentVal = heap[parentIdx];
            if (parentVal < childVal) {
                // We are done. Nothing to do.
                break;
            }
            swap(childIdx , childIdx/2);
            childIdx = childIdx/2;
        }
    }

    public void print(){
        for (int i = 1; i <= size/2; i++) {
            System.out.printf("Parent: %d, Left child: %d, Right child: %d %s", heap[i], heap[i*2], heap[i*2+1], System.lineSeparator());
        }
    }

    public void push(int x) {
        heap[++size] = x;
        heapifyUp(size);
    }

    public int pop() {
        int head = heap[1];
        heap[1] = heap[size--];
        heapifyDown(1);

        return head;
    }

    public int peek() {
        return heap[1];
    }

}
