package com.jsf2184.fb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class LruWithLinkedHash {

    interface Item<K> {
        K getId();
    }

    @Data
    @AllArgsConstructor
    public static class Order implements Item<Integer> {
        Integer id;
    }

    public static class Lru<T extends Item<K>, K> extends LinkedHashMap<K, T> {
        int recordLimit;

        public Lru(int recordLimit) {
            super(recordLimit, 0.75f, true);  //last arg refresh on access, not just insertion
            this.recordLimit = recordLimit;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, T> eldest) {
            return this.size() > recordLimit;
        }

        public void store(T item) {
            if (item == null) {
                return;
            }
            final K id = item.getId();
            put(id, item);
        }
    }

    @Test
    public void testScenario() {
        Lru<Order, Integer> lru = new Lru<>(3);
        IntStream.range(1, 10).forEach(i -> addAndReport(lru, i));
    }


    public void addAndReport(Lru<Order, Integer> lru, int i) {
        log.info("addAndReport: after inserting {}", i);
        Order order = new Order(i);
        lru.store(order);
        // lets keep order1 in the cache
        lru.get(1);
        report(lru, i);
    }

    public static void report(Lru<Order, Integer> lru, int i) {
        final List<Integer> orders = lru.values().stream().map(Order::getId).collect(Collectors.toList());
        log.info("Report() after inserting {}: {}", i, orders);
    }
}

