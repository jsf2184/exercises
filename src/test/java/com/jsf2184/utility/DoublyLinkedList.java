package com.jsf2184.utility;

import org.junit.Assert;

public class DoublyLinkedList {

    public static class KeyVal {
        int _key;
        int _value;

        public KeyVal(int key, int value) {
            _key = key;
            _value = value;
        }

        public int getKey() {
            return _key;
        }

        public int getValue() {
            return _value;
        }

        public void setValue(int value) {
            _value = value;
        }

        @Override
        public String toString() {
            return "KeyVal{" +
                    "_key=" + _key +
                    ", _value=" + _value +
                    '}';
        }
    }
    public static class Node {
        KeyVal _keyVal;
        Node _prev;
        Node _next;

        public Node(int key, int value) {
            _keyVal = new KeyVal(key, value);
            _next = null;
            _prev = null;
        }

        public int getKey() {
            return _keyVal.getKey();
        }

        public int getValue() {
            return _keyVal.getValue();
        }

        public KeyVal getKeyVal() {
            return _keyVal;
        }

        public Node getNext() {
            return _next;
        }

        public Node getPrev() {
            return _prev;
        }

        public void setValue(int value) {
            _keyVal.setValue(value);
        }

        public void setPrev(Node prev) {
            _prev = prev;
        }

        public void setNext(Node next) {
            _next = next;
        }

        public String toString() {
            return  _keyVal.toString();
        }
    }

    Node _first;
    Node _last;

    public DoublyLinkedList() {
        _first = null;
        _last = null;
    }

    public Node add(int key, int value) {
        Node res = add(new Node(key, value));
        return res;
    }

    public Node add(Node node) {
        if (_first == null) {
            Assert.assertNull(_last);
            _first = node;
            _last = node;
            return node;
        } else {
            _last.setNext(node);
            node.setPrev(_last);
            _last = node;
        }
        return node;
    }

    public Node find(int key) {
        Node current = _first;
        while (current != null) {
            if (current.getKey() == key) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    public void printForward() {
        Node current = _first;
        while (current != null) {
            System.out.println(current.toString());
            current = current.getNext();
        }
    }

    public void printBackward() {
        Node current = _last;
        while (current != null) {
            System.out.println(current.toString());
            current = current.getPrev();
        }
    }

    public Node next() {
        if (empty()) {
            return null;
        }
        Node res = _first;
        remove(_first);
        return res;
    }

    public void remove(Node node) {
        if (node._next != null) {
            node._next.setPrev(node._prev);
        } else {
            _last = node._prev;
        }
        if (node._prev != null) {
            node._prev._next = node._next;
        } else {
            _first = node._next;
        }
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
