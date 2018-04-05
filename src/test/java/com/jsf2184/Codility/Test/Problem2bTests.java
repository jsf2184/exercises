package com.jsf2184.Codility.Test;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.time.DateTimeException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class Problem2bTests {

    // A little wrapper class that adds 'level' to the info that is in a Tree instance.
    public static class Node {
        private Tree tree;
        boolean usedRight;

        public Node(Tree tree) {
            this.usedRight = false;
            this.tree = tree;
        }
        public int getX() {
            return tree.x;
        }
        public int getId() {
            return tree.id;}

        public Tree getR() {
            if (!usedRight) {
                usedRight = true;
                return tree.r;
            }
            return null;
        }
        public Tree getL() {
            return tree.l;
        }

        public boolean getUsedRight() {
            return usedRight;
        }

        public String toString() {
            // Makes it easier to look at a node in the debugger.
            String res = String.format("Node: id=%d, x=%d, usedRight=%s",
                                        getId(), getX(), getUsedRight());
            return res;
        }

    }

    public static class MaxPathKeeper {
        private List<Integer> valueCounts;
        private int currentLength;
        private int maxLength;

        public MaxPathKeeper() {
            valueCounts = new ArrayList<>();
            currentLength = 0;
            maxLength = 0;
        }

        public int getValue(int x) {
            growTo(x);
            Integer res = valueCounts.get(x);
            return res;
        }

        public void growTo(int x) {
            int len = valueCounts.size();
            for (int i=len; i<=x; i++) {
                valueCounts.add(0);
            }
        }

        public void addValue(int x) {
            int count = getValue(x) + 1;
            if (count == 1) {
                // a new distinct value
                currentLength++;
                maxLength = Math.max(currentLength, getMaxLength());
            }
            // store the updated count before leaving
            valueCounts.set(x, count);
        }

        public void removeValue(int x) {
            // we how have one less instance of this value.
            int count = getValue(x) -1;
            if (count == 0) {
                // our length has diminished after we removed this entry
                currentLength--;
            }
            // store the updated count before leaving
            valueCounts.set(x, count);
        }

        public int getMaxLength() {
            return maxLength;
        }
    }


    public int recurse(Tree root) {
        if (root == null) {
            return 0;
        }
        Set<Integer> set = new HashSet<>();
        AtomicInteger longest = new AtomicInteger(0);
        recurse(root, set, longest);
        return longest.intValue();
    }

    public void recurse(Tree tree, Set<Integer> set, AtomicInteger longest) {
        int x = tree.x;
        set.add(x);
        if (tree.l == null && tree.r == null) {
            int setSize = set.size();
            if (setSize > longest.get()) {
                longest.set(setSize);
            }
            return;
        }

        if (tree.l != null) {
            Set<Integer> childSet = new HashSet<>(set);
            recurse(tree.l, childSet, longest);
        }

        if (tree.r != null) {
            Set<Integer> childSet = new HashSet<>(set);
            recurse(tree.r, childSet, longest);
        }

    }
    public int traverse(Tree root) {

        if (root == null) {
            return 0;
        }
        Stack<Node> stack = new Stack<>();
        Node current = new Node(root);
        MaxPathKeeper maxPathKeeper = new MaxPathKeeper();

        while (current != null) {
            int x = current.getX();
            maxPathKeeper.addValue(x);
            stack.push(current);
            Tree l = current.getL();
            if (l != null) {
                current = new Node(l);
            } else {
                while (true) {

                    if (stack.isEmpty()) {
                        current = null;
                        break;
                    }
                    current = stack.pop();
                    Tree r = current.getR();
                    if (r == null) {
                        // we are completely done with current.
                        x = current.getX();
                        maxPathKeeper.removeValue(x);
                    } else {
                        // put current back on stack.
                        stack.push(current);
                        current = new Node(r); // use this new current value.
                        break;
                    }
                }
            }
        }
        int maxLength = maxPathKeeper.getMaxLength();
        return maxLength;
    }

    public int testIt(Tree tree) {
        int recurseRes = recurse(tree);
        int traverseRes = traverse(tree);
        Assert.assertEquals(recurseRes, traverseRes);
        return recurseRes;
    }

    @Test
    public void testSolutionWithSampleData3() {
        Tree tree = buildSampleData3();
        int res = testIt(tree);
        Assert.assertEquals(3, res);
    }

    @Test
    public void testSolutionWithSampleData4() {
        Tree tree = buildSampleData4();
        int res = testIt(tree);
        Assert.assertEquals(4, res);
    }


    @Test
    public void testSolutionWithSampleData1() {
        Tree tree = buildSampleData1();
        int res = testIt(tree);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testSolutionWithSampleData2() {
        Tree tree = buildSampleData2();
        int res = testIt(tree);
        Assert.assertEquals(2, res);
    }


    @Test
    public void testSolutionWithNullData() {
        int res = testIt(null);
        Assert.assertEquals(0, res);
    }

    @Test
    public void testSolutionWithOneElement() {
        int res = testIt(new Tree(5));
        Assert.assertEquals(1, res);
    }

    @Test
    public void testTreeWithUniqueValues() {
        for (int d=1; d<=10; d++) {
            AtomicInteger v = new AtomicInteger(0);
            Tree tree = buildDepthTree(d, v::incrementAndGet);
            int actual = testIt(tree);
            Assert.assertEquals(d, actual);
        }
    }

    @Test
    public void testTreeWithRandomValues() {
        Random random = new Random();
        for (int d=16; d<=20; d++) {
            int choices =  12; // Math.max(4,  (int) Math.pow(2, d) / 32);

            System.out.println(choices);
            Tree tree = buildDepthTree(d, () -> random.nextInt(choices));
            int res = testIt(tree);
            System.out.printf("with d=%d, choices=%d, res=%d\n", d, choices, res);
        }
    }


    @Test
    public void timeTestTreeWithRandomValues() {
        Random random = new Random();
        int choices =  12; // Math.max(4,  (int) Math.pow(2, d) / 32);
        int maxDepth = 26;
        Tree tree = buildDepthTree(maxDepth, () -> random.nextInt(choices));
        int repeats = 10;
        long traverseTime = timeWithRandomValues(repeats, this::traverse, tree);
        System.out.printf("With maxDepth=%d, choices=%d, repeats=%d, traverseTime=%d\n",
                          maxDepth, choices, repeats, traverseTime);
        long recurseTime = timeWithRandomValues(repeats, this::recurse, tree);
        System.out.printf("With maxDepth=%d, choices=%d, repeats=%d, recurseTime=%d\n",
                          maxDepth, choices, repeats, recurseTime);

    }

    public long timeWithRandomValues(int repeats, Function<Tree, Integer> f, Tree tree) {
        DateTime start = DateTime.now();
        for (int i=0; i<repeats; i++) {
            f.apply(tree);
        }
        DateTime finish = DateTime.now();
        long res = finish.getMillis() - start.getMillis();
        return res;

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
        static int NumCreated= 0;
        
        int x;
        int id;
        Tree l;
        Tree r;

        public Tree(int data) {
            x = data;
            l = null;
            r = null;
            id = NumCreated++;
        }

        public String toString() {
            String res = String.format("id=%d, x=%d", id, x);
            return res;
        }
    }


    public static Tree buildDepthTree(int maxDepth, Supplier<Integer> valProvider) {
        Stack<Tree> stack = new Stack<>();
        Stack<Integer> dstack = new Stack<>();
        if (maxDepth <= 0) {
            return null;
        }

        Integer val = valProvider.get();
        Tree root = new Tree(val);
        Tree current = root;
        int d = 1;

        while(true) {
            while (d < maxDepth) {
                val = valProvider.get();
                Tree left = new Tree(val);
                if (val == 0) {
                    break;  // try to introduce some shorter paths.
                }
                current.l = left;
                stack.push(current);
                dstack.push(d);
                d++;
                current = left;
            }

            if (stack.empty()) {
                break;
            }
            current = stack.pop();
            d = dstack.pop();

            val = valProvider.get();

            if (val != 0) {
                Tree right = new Tree(val);
                current.r = right;
                current = right;
            }
            d++;
        }
        return root;
    }

}
