package com.jsf2184.fb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

@Slf4j
public class LRUAgainTest {

    interface Item<K, V> {
        K getId();
        V getValue();
    }

    @Data
    @AllArgsConstructor
    public static class Order implements Item<Integer, Integer> {
        Integer id;
        Integer value;
    }

    @Data
    @AllArgsConstructor
    public static class Node<T extends Item<K,V>, K, V> {
        T value;
        Integer sequenceNum;
    }


    public static class Lru<T extends Item<K,V>, K, V> {
        int recordLimit;
        HashMap<K, Node<T, K, V>> valueMap;
        TreeMap<Integer,  Node<T, K, V>> sequenceMap;
        int numInserted;

        public Lru(int recordLimit) {
            this.recordLimit = recordLimit;
            valueMap = new HashMap<>();
            sequenceMap = new TreeMap<>();
            numInserted = 0;
        }

        public void store(Item<K, V> item) {
            if (item == null) {
                return;
            }
            final K id = item.getId();
            final Node<T, K, V> node = valueMap.get(id);
            if (node != null) {
                // this was a pre-existing value. remove it from the usageMap and the value map
                removeNode(node);
            } else {
                // this is a new node.
                if (valueMap.size() >= recordLimit) {
                    // need to get rid of another item
                    removeStalest();
                }
            }
            createAndStoreNode(item);
        }

        public Item<K, V> getItem(K id) {
            final Node<T, K, V> node = valueMap.get(id);
            if (node == null) {
                return null;
            }
            // Does it get repositioned when gotten? Assume yes for now.
            removeNode(node);
            final T value = node.value;
            createAndStoreNode(value);
            return value;
        }

        private void createAndStoreNode(Item<K, V> item) {
            numInserted++;
            Node node = new Node(item, numInserted);
            sequenceMap.put(numInserted, node);
            valueMap.put(item.getId(), node);
        }

        private void removeStalest() {
            final Map.Entry<Integer, Node<T, K, V>> entry = sequenceMap.firstEntry();
            if (entry == null) {
                return;
            }

            final Integer key = entry.getKey();
            sequenceMap.remove(key);
            final Node<T, K, V> node = entry.getValue();
            valueMap.remove(node.getValue().getId());
        }

        private void removeNode(Node<T, K, V> node) {
            final Integer sequenceNum = node.getSequenceNum();
            sequenceMap.remove(sequenceNum);
            valueMap.remove(node.getValue().getId());
        }

        private void report() {
            log.info("ValueMap");
            valueMap.values()
                    .forEach(node -> log.info("sqno:{} id: {}", node.getSequenceNum(), node.getValue().getId()));
            log.info(" SequenceMap");
            sequenceMap.values()
                    .forEach(node -> log.info("sqno:{} id: {}", node.getSequenceNum(), node.getValue().getId()));
        }
    }

    @Test
    public void testScenario() {
        Lru<Order, Integer, Integer> lru = new Lru<>(3);
        IntStream.range(1, 10).forEach(i -> addAndReport(lru, i));
    }

    public void addAndReport(Lru<Order, Integer, Integer> lru, int i) {
        log.info("\naddAndReport");
        Order order = new Order(i, i);
        lru.store(order);
        // lets keep order1 in the cache
        lru.getItem(1);
        lru.report();
    }
}
