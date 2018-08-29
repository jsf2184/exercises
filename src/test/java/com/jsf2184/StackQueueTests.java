package com.jsf2184;

import org.junit.Assert;
import org.junit.Test;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.Supplier;

public class StackQueueTests {

    public static class StackQueue<T> {
        Stack<T> _inStack;
        Stack<T> _outStack;

        public StackQueue() {
            _inStack = new Stack<>();
            _outStack = new Stack<>();
        }
        public boolean empty() {
            boolean res = _inStack.empty() && _outStack.isEmpty();
            return res;
        }

        public void push(T rec) {
            _inStack.push(rec);
        }

        public T pop() {
            peekPopPrep();
            T res = _outStack.pop();
            return res;
        }

        public T peek() {
            peekPopPrep();
            T res = _outStack.peek();
            return res;
        }

        void peekPopPrep() {
            if (_outStack.empty()) {
                move();
            }
            if (_outStack.empty()) {
                throw new EmptyStackException();
            }
        }

        void move() {
            while (!_inStack.empty()) {
                T elem = _inStack.pop();
                _outStack.push(elem);
            }
        }
    }

    public void validateException(Supplier<Integer> supplier) {
        boolean caught = false;
        try {
            supplier.get();
        } catch (EmptyStackException ignore) {
            caught= true;
        }
        Assert.assertTrue(caught);
    }

    public void validate(StackQueue<Integer> sut, Integer expected) {
        if (expected == null) {
            Assert.assertTrue(sut.empty());
            validateException(sut::peek);
            validateException(sut::pop);
        } else {
            Assert.assertEquals(expected, sut.peek());
            Assert.assertEquals(expected, sut.pop());
        }
    }
    @Test
    public void testPushMove() {
        StackQueue<Integer> sut = new StackQueue<>();
        sut.push(1);
        sut.push(2);
        sut.push(3);
        validate(sut, 1);
        validate(sut, 2);
        sut.push(4);
        sut.push(5);
        validate(sut, 3);
        validate(sut, 4);
        validate(sut, 5);

        validate(sut, null);

    }

}
