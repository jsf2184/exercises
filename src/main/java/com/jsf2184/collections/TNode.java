package com.jsf2184.collections;

import com.jsf2184.utility.LoggerUtility;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.Stack;
import java.util.function.Supplier;

public class TNode {

    private static final Logger _log = Logger.getLogger(TNode.class);
    Integer _val;
    TNode _left;
    TNode _right;

    public TNode(int val) {
        _val = val;
    }

    public Integer getVal() {
        return _val;
    }

    public TNode getLeft() {
        return _left;
    }

    public void setLeft(TNode left) {
        _left = left;
    }

    public TNode getRight() {
        return _right;
    }

    public void setRight(TNode right) {
        _right = right;
    }

    public static void breadthTravers(TNode root, Consumer<Integer> func) {
        Queue<TNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TNode node = queue.remove();
            Integer val = node.getVal();
            func.accept(val);

            TNode left = node.getLeft();
            if (left != null) {
                queue.add(left);
            }
            TNode right = node.getRight();
            if (right != null) {
                queue.add(right);
            }
        }
    }

    public static TNode buildBreadthTree(int depth) {
        Queue<TNode> queue = new LinkedList<>();
        int nextVal = 1;
        TNode root = new TNode(nextVal++);
        queue.add(root);
        
        for (int d= 1; d< depth; d++) {
            TNode node;
            List<TNode> additions = new ArrayList<>();
            while (!queue.isEmpty()) {
                node = queue.remove();
                TNode left = new TNode(nextVal++);
                node.setLeft(left);
                additions.add(left);
                TNode right = new TNode(nextVal++);
                node.setRight(right);
                additions.add(right);
            }
            queue.addAll(additions);
        }
        return root;
    }

    public static TNode buildDepthTree2(int maxDepth, Supplier<Integer> valProvider) {
        Stack<TNode> stack = new Stack<>();
        Stack<Integer> dstack = new Stack<>();
        if (maxDepth <= 0) {
            return null;
        }
        TNode root = new TNode(valProvider.get());
        TNode node = root;
        int d = 1;

        while(true) {
            while (d < maxDepth) {
                TNode left = new TNode(valProvider.get());
                node.setLeft(left);
                stack.push(node);
                dstack.push(d);
                d++;
                node = left;
            }

            if (stack.empty()) {
                break;
            }
            node = stack.pop();
            d = dstack.pop();

            TNode right = new TNode(valProvider.get());
            node.setRight(right);
            node = right;
            d++;
        }
        return root;
    }

    public static TNode buildDepthTree(int maxDepth) {
        AtomicInteger val = new AtomicInteger(1);
        TNode root = new TNode(val.getAndIncrement());
        buildDepthTree(root, 1, maxDepth, val);
        return root;
    }

    public static void buildDepthTree(TNode node,
                                      int depth,
                                      int maxDepth,
                                      AtomicInteger val)
    {
        if (depth < maxDepth) {
            TNode left = new TNode(val.getAndIncrement());
            node.setLeft(left);
            buildDepthTree(left, depth+1, maxDepth, val);
            TNode right = new TNode(val.getAndIncrement());
            node.setRight(right);
            buildDepthTree(right, depth+1, maxDepth, val);
        }
    }

    public static void traverseDepthTree(TNode node, Consumer<Integer> consumer) {
        Integer val = node.getVal();
        consumer.accept(val);
        TNode left = node.getLeft();
        if (left != null) {
            traverseDepthTree(left, consumer);
        }
        TNode right = node.getRight();
        if (right != null) {
            traverseDepthTree(right, consumer);
        }
    }
}
