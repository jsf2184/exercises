package com.jsf2184.utility;

import org.junit.Assert;
import org.junit.Test;

public class SinglyLinkedList {

    public static class Node {
        Node _next;
        int _value;

        public Node(int value) {
            _next = null;
            _value = value;
        }

        public Node getNext() {
            return _next;
        }

        public int getValue() {
            return _value;
        }

        public void setNext(Node next) {
            _next = next;
        }

        public void setValue(int value) {
            _value = value;
        }
    }


    Node _first;
    Node _last;

    public SinglyLinkedList() {
        _first = null;
        _last = null;
    }

    public void add(int v) {
        Node node = new Node(v);
        if (_first == null) {
            _first = node;
            _last = node;
        } else {
            _last._next = node;
            _last = node;
        }
    }

    public Integer next() {
        if (empty()) {
            return null;
        }
        int res = _first.getValue();
        _first = _first.getNext();
        if (_first == null) {
            _last = null;
        }
        return res;
    }

    public boolean empty() {
        if (_first == null || _last == null) {
            Assert.assertNull(_last);
            Assert.assertNull(_first);
            return true;
        }
        return false;
    }

}





