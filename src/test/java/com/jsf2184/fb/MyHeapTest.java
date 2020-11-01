package com.jsf2184.fb;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Slf4j
public class MyHeapTest {

    @Test
    public void addNumbers() {
        Heap heap = new Heap();
        addIt(heap, 9);
        addIt(heap, 12);
        addIt(heap, 6);
        addIt(heap, 5);
        addIt(heap, 13);
        addIt(heap, 23);
        addIt(heap, 11);
        addIt(heap, 18);
        addIt(heap, 13);
        addIt(heap, 43);
        addIt(heap, 8);
        addIt(heap, 2);

        while(true) {
            Integer popped = heap.pop();
            if (popped == null) {
                break;
            }
            log.info("popped = {}", popped);
        }
    }

    public void addIt(Heap heap, int n) {
        heap.add(n);
        heap.print();

    }
    public static class Heap {
        ArrayList<Integer> values;

        public Heap() {
            this.values = new ArrayList<>();
            // throw a dummy value  in element 0;
            values.add(0);
        }

        public void add(int val) {
            Integer parent;
            Integer current = values.size();
            values.add(val);
            while ((parent = getParent(current)) != null && val < values.get(parent)) {
                swap(current, parent);
                current = parent;
            }
        }

        public Integer pop() {
            if (size() == 0) {
                return null;
            }
            int result = values.get(1);
            int gap = 1;
            int last = size();
            while (gap <= last) {
                int c1 = 2 * gap;
                int c2 = c1 + 1;
                Integer child1 = c1 <= last ? values.get(c1) : null;
                Integer child2 = c2 <= last ? values.get(c2) : null;
                if (child1 == null && child2 == null) {
                    break;
                }
                if (child1 != null && (child2 == null || child1 < child2)) {
                    values.set(gap, child1);
                    gap = c1;
                } else {
                    values.set(gap, child2);
                    gap = c2;
                }
            }
            values.remove(last);
            return result;
        }

        int size() {
            return values.size() -1;
        }

        public void print() {
            log.info("Heap of size {}", values.size()-1);
            IntStream.range(1, values.size())
                     .forEach(i -> log.info("[{}] = {}", i, values.get(i) ));
        }

        private void swap(Integer current, Integer parent) {
            int temp = values.get(current);
            values.set(current, values.get(parent));
            values.set(parent, temp);
        }

        public Integer getParent(int i) {
            if (i < 1) {
                return null;
            }
            return i/2;
        }


    }


}
