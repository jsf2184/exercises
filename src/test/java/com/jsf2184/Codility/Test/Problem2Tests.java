package com.jsf2184.Codility.Test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Problem2Tests {

    public int solution(Tree T) {
        if (T == null) {
            return 0;
        }
        // Use traverse() to
        //   * Obtain the maximum value of all the 'x' values in the tree (for test data: 6)
        //
        //   * Build a list of nodes in the order we want to process them. A node is a little wrapper that contains
        //     the Tree object and the depth level of that particular element.
        //
        // Note the nodeList we build for the sample data looks like this: A1, B2, D3, G4, C2, E3, F3
        //
        List<Node> nodeList = new ArrayList<>();
        int maxTreeVal = traverse(T, nodeList);

        // Our MaxPathKeeper can be a little more thrifty with space since he knows the maximumTreeVal from the tree
        MaxPathKeeper maxPathKeeper = new MaxPathKeeper(maxTreeVal);

        // March through the nodeList, using it to feed data to the maxPathKeeper. It uses the level to know when data values
        // should be backed out of the maxPathKeeper.
        //
        int currentLevel = 0;
        for (int i=0; i<nodeList.size(); i++) {
            Node node = nodeList.get(i);

            if (node.getLevel() <= currentLevel) {
                // We popped back up the tree so we need to remove all the values from the pathKeeper in all the nodes
                // prior to the pop-back.
                //
                int popBackCount = (currentLevel - node.getLevel()) + 1;
                // For each of those pop-back values, remove them from our maxPathKeeper since they won't be part of
                // the new path we are about to pursue.
                //
                for (int j = 1; j <= popBackCount; j++) {
                    Node popBackNode = nodeList.get(i-j);
                    maxPathKeeper.removeValue(popBackNode.getX());
                }
            }
            // Now, add in the value from our current node.
            maxPathKeeper.addValue(node.getX());
            // And set our currentLevel to that of this node.
            currentLevel = node.getLevel();
        }
        // the answer should be waiting for us in maxPathKeeper.
        return maxPathKeeper.getMaxLength();
    }

    // A little wrapper class that adds 'level' to the info that is in a Tree instance.
    public static class Node {
        private int level;
        private Tree tree;

        public Node(int level, Tree tree) {
            this.level = level;
            this.tree = tree;
        }
        public int getX() {
            return tree.x;
        }
        public Tree getR() {
            return tree.r;
        }
        public Tree getL() {
            return tree.l;
        }

        public String toString() {
            // Makes it easier to look at a node in the debugger.
            String res = String.format("Node: x = %d, level = %d", getX(), getLevel());
            return res;
        }

        public int getLevel() {
            return level;
        }
    }

    public static class MaxPathKeeper {
        private int[] valueCounts;
        private int currentLength;
        private int maxLength;

        public MaxPathKeeper(int largestValue) {
            valueCounts = new int[largestValue+1];
            currentLength = 0;
            maxLength = 0;
        }

        public void addValue(int x) {
            int count = valueCounts[x] + 1;
            if (count == 1) {
                // a new distinct value
                currentLength++;
                maxLength = Math.max(currentLength, getMaxLength());
            }
            // store the updated count before leaving
            valueCounts[x] = count;
        }

        public void removeValue(int x) {
            // we how have one less instance of this value.
            int count = valueCounts[x] -1;
            if (count == 0) {
                // our length has diminished after we removed this entry
                currentLength--;
            }
            // store the updated count before leaving
            valueCounts[x] = count;
        }

        public int getMaxLength() {
            return maxLength;
        }
    }

    // return the largest element in the tree and build our nodeList.
    public int traverse(Tree root, List<Node> nodeList) {

        int maxValue = 0;

        Stack<Node> stack = new Stack<>();
        stack.push(new Node(1, root));

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (current.getX() > maxValue) {
                maxValue = current.getX();
            }
            nodeList.add(current);
            if (current.getR() != null) {
                stack.push(new Node(current.getLevel() + 1, current.getR()));
            }
            if (current.getL() != null) {
                stack.push(new Node(current.getLevel() + 1, current.getL()));
            }
        }
        return maxValue;
    }


    @Test
    public void testSolutionWithSampleData3() {
        Tree tree = buildSampleData3();
        int res = solution(tree);
        Assert.assertEquals(3, res);
    }

    @Test
    public void testSolutionWithSampleData4() {
        Tree tree = buildSampleData4();
        int res = solution(tree);
        Assert.assertEquals(4, res);
    }


    @Test
    public void testSolutionWithSampleData1() {
        Tree tree = buildSampleData1();
        int res = solution(tree);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testSolutionWithSampleData2() {
        Tree tree = buildSampleData2();
        int res = solution(tree);
        Assert.assertEquals(2, res);
    }


    @Test
    public void testSolutionWithNullData() {
        int res = solution(null);
        Assert.assertEquals(0, res);
    }

    @Test
    public void testSolutionWithOneElement() {
        int res = solution(new Tree(5));
        Assert.assertEquals(1, res);
    }


    @Test
    public void testTraverse3() {
        Tree tree = buildSampleData3();
        List<Node> nodeList = new ArrayList<>();
        int maxValue = traverse(tree, nodeList);
        Assert.assertEquals(6, maxValue);
        printNodeList(nodeList);
    }


    public static void printNodeList(List<Node> nodeList) {
        for (Node node : nodeList) {
            System.out.printf("val = %d, level = %d \n", node.getX(), node.getLevel());
        }
    }

    Tree buildSampleData3() {
        Tree a = new Tree(4);
        Tree b = new Tree(5);
        Tree d = new Tree(4);
        Tree g = new Tree(5);
        Tree c = new Tree(6);
        Tree e = new Tree(1);
        Tree f = new Tree(6);

        a.l = b;
        b.l = d;
        d.l = g;
        a.r = c;
        c.l = e;
        c.r = f;

        return a;

    }

    Tree buildSampleData4() {
        Tree a = new Tree(4);
        Tree b = new Tree(5);
        Tree d = new Tree(4);
        Tree g = new Tree(5);
        Tree c = new Tree(6);
        Tree e = new Tree(1);
        Tree f = new Tree(6);
        Tree h = new Tree(5);
        Tree i = new Tree(5);

        a.l = b;
        b.l = d;
        d.l = g;
        a.r = c;
        c.l = e;
        c.r = f;
        e.r = h;
        e.l = i;

        return a;

    }



    Tree buildSampleData2() {
        Tree a = new Tree(4);
        Tree b = new Tree(5);
        Tree d = new Tree(4);
        Tree g = new Tree(5);
        Tree c = new Tree(6);
        Tree e = new Tree(4);
        Tree f = new Tree(6);

        a.l = b;
        b.l = d;
        d.l = g;
        a.r = c;
        c.l = e;
        c.r = f;

        return a;

    }



    Tree buildSampleData1() {
        Tree a = new Tree(4);
        Tree b = new Tree(4);
        Tree d = new Tree(4);
        Tree g = new Tree(4);
        Tree c = new Tree(4);
        Tree e = new Tree(4);
        Tree f = new Tree(4);

        a.l = b;
        b.l = d;
        d.l = g;
        a.r = c;
        c.l = e;
        c.r = f;

        return a;

    }


    public static class Tree {
        int x;
        Tree l;
        Tree r;

        public Tree(int data) {
            x = data;
            l = null;
            r = null;
        }
    }
}
