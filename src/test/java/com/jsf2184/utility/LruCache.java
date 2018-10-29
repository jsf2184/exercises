package com.jsf2184.utility;

import com.jsf2184.HashMapTests;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_MULTIPLYPeer;

import java.util.HashMap;

public class LruCache {
    DoublyLinkedList _list;
    HashMap<Integer, DoublyLinkedList.Node> _map;
    private int _capacity;

    public LruCache(int capacity) {
        _capacity = capacity;
    }

    public void add(int key, int value) {
        DoublyLinkedList.Node node = _map.get(key);
        if (node != null) {
            node.setValue(value);
            // remove and reinsert the node at the end of the list
            _list.remove(node);
            _list.add(node);
        } else {
            node = _list.add(key, value);
            _map.put(key, node);
        }
    }


}
