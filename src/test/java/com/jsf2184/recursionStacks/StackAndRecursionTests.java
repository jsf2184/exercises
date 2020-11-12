package com.jsf2184.recursionStacks;

import com.jsf2184.BlockingQueueTests;
import com.jsf2184.utility.LoggerUtility;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackAndRecursionTests {

    private static final Logger _log = Logger.getLogger(StackAndRecursionTests.class);

    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }


    public static class Node {
        int _value;
        Node _left;
        Node _right;

        public Node(int value, Node left, Node right) {
            _value = value;
            _left = left;
            _right = right;
        }

        public Node(int value) {
            this(value, null, null);
        }

        public int getValue() {
            return _value;
        }

        public void setValue(int value) {
            _value = value;
        }

        public Node getLeft() {
            return _left;
        }

        public void setLeft(Node left) {
            _left = left;
        }

        public Node getRight() {
            return _right;
        }

        public void setRight(Node right) {
            _right = right;
        }
    }

    public static Node buildTree() {
        Node n20 = new Node(20);
        Node n35 = new Node(35);
        Node n42 = new Node(42);
        Node n60 = new Node(60);
        Node n80 = new Node(80);

        Node n40 = new Node(40, n35, n42);
        Node n70 = new Node(70, n60, n80);

        Node n30 = new Node(30, n20, n40);
        Node n50 = new Node(50, n30, n70);
        return n50;
    }

    void recursePreOrder(Node n, List<Integer> results ) {
        if (n == null) {
            return;
        }
        results.add(n.getValue());
        recursePreOrder(n.getLeft(), results);
        recursePreOrder(n.getRight(), results);
    }

    void stackPreOrder(Node n, List<Integer> results ) {
        if (n == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(n);

        while (!stack.empty()) {
            Node current = stack.pop();
            results.add(current.getValue());
            pushToStack(current.getRight(), stack);
            pushToStack(current.getLeft(), stack);
        }
    }

    void recurseInOrder(Node n, List<Integer> results ) {
        if (n == null) {
            return;
        }
        recurseInOrder(n.getLeft(), results);
        results.add(n.getValue());
        recurseInOrder(n.getRight(), results);
    }



    public static class NodeProgress {
        Node node;
        int state;

        public NodeProgress(Node node) {
            this.node = node;
            state = 0;
        }
        public void incState() {
            state+=1;
        }

        public Node getNode() {
            return node;
        }

        public int getState() {
            return state;
        }
    }
    
    public static NodeProgress createNodeProgress(Node node) {
        if (node == null) {
            return  null;
        }
        return  new NodeProgress(node);
    }

    void stackInOrder(Node n, List<Integer> results ) {
        if (n == null) {
            return;
        }
        Stack<NodeProgress> stack = new Stack<>();
        pushToStack(createNodeProgress(n), stack);

        while (!stack.empty()) {
            NodeProgress nodeProgress = stack.pop();
            final Node current = nodeProgress.getNode();
            final int state = nodeProgress.getState();
            switch (state) {
                case 0:
                    nodeProgress.incState();
                    pushToStack(nodeProgress, stack);
                    pushToStack(createNodeProgress(current.getLeft()), stack);
                    break;
                case 1:
                    results.add(current.getValue());
                    pushToStack(createNodeProgress(current.getRight()), stack);

            }
        }
    }

    void recursePostOrder(Node n, List<Integer> results ) {
        if (n == null) {
            return;
        }
        recursePostOrder(n.getLeft(), results);
        recursePostOrder(n.getRight(), results);
        results.add(n.getValue());
    }

    void stackPostOrder(Node n, List<Integer> results ) {
        if (n == null) {
            return;
        }
        Stack<NodeProgress> stack = new Stack<>();
        pushToStack(createNodeProgress(n), stack);

        while (!stack.empty()) {
            NodeProgress nodeProgress = stack.pop();
            final Node current = nodeProgress.getNode();
            final int state = nodeProgress.getState();
            switch (state) {
                case 0:
                    nodeProgress.incState();
                    pushToStack(nodeProgress, stack);
                    pushToStack(createNodeProgress(current.getLeft()), stack);
                    break;
                case 1:
                    nodeProgress.incState();
                    pushToStack(nodeProgress, stack);
                    pushToStack(createNodeProgress(current.getRight()), stack);
                    break;
                case 2:
                    results.add(current.getValue());
            }
        }
    }



    public  static <N> void pushToStack(N n, Stack<N> stack) {
        if (n != null) {
            stack.push(n);
        }
    }

    @Test
    public void testPreorder() {
        List<Integer> recurseResults = new ArrayList<>();
        final Node root = buildTree();
        recursePreOrder(root, recurseResults);
        List<Integer> stackResults = new ArrayList<>();
        stackPreOrder(root, stackResults);

        _log.info(recurseResults);
        _log.info(stackResults);
        Assert.assertEquals(recurseResults, stackResults);
    }

    @Test
    public void testInorder() {
        List<Integer> recurseResults = new ArrayList<>();
        final Node root = buildTree();
        recurseInOrder(root, recurseResults);
        List<Integer> stackResults = new ArrayList<>();
        stackInOrder(root, stackResults);

        _log.info(recurseResults);
        _log.info(stackResults);
        Assert.assertEquals(recurseResults, stackResults);
    }

    @Test
    public void testPostorder() {
        List<Integer> recurseResults = new ArrayList<>();
        final Node root = buildTree();
        recursePostOrder(root, recurseResults);
        List<Integer> stackResults = new ArrayList<>();
        stackPostOrder(root, stackResults);

        _log.info(recurseResults);
        _log.info(stackResults);
        Assert.assertEquals(recurseResults, stackResults);
    }

}
