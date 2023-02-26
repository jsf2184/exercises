package com.jsf2184.mlm;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class IteratorPlay {
    @Test
    public void onArrayList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> it = list.iterator();
        // you can call hasNext a gazillion times, since it doesn't advance the cursor
        // it will always return true
        IntStream.range(0, 10).forEach((i) -> Assert.assertTrue(it.hasNext()));

        while(it.hasNext()) {

        }

    }

    public int identity(int i) {
        return i;
    }

    @Test
    public void testIter() {
        String s = "abc";
        Iterator<Character> it = new StringIterator(s);
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(it.next(), (Character) 'a');
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(it.next(), (Character) 'b');
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(it.next(), (Character) 'c');
        Assert.assertFalse(it.hasNext());
        boolean caught = false;
        try {
            it.next();
        } catch (NoSuchElementException e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testInterleave() {
        List<Integer> l0 = Arrays.asList(0,3,6);
        List<Integer> l1 = Arrays.asList(1,4,7,9);
        List<Integer> l2 = Arrays.asList(2,5,8,10,11);
        List<Iterator<Integer>> iterators =  new ArrayList<>(Arrays.asList(l0.iterator(), l1.iterator(), l2.iterator()));

        List<Integer> res = interleave(iterators);
        Assert.assertEquals(res, Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11));
    }

    public static <E> List<E> interleave(List<Iterator<E>> iterators) {
        List<Integer> removals = new ArrayList<>();
        List<E> res = new ArrayList<>();
        while(!iterators.isEmpty()) {
            for (int i=0; i<iterators.size(); i++) {
                Iterator<E> it = iterators.get(i);
                if (it.hasNext()) {
                    res.add(it.next());
                } else {
                    removals.add(i);
                }
            }
            if (!removals.isEmpty()) {
               for(int i=removals.size()-1; i>=0; i--) {
                   Integer r = removals.get(i);
                   iterators.remove(r.intValue());
               }
               removals.clear();
            }
        }
        return res;
    }

    public static class StringIterator implements Iterator<Character> {
        String str;
        int pos = 0;

        public StringIterator(String str) {
            this.str = str;
        }

        @Override
        public boolean hasNext() {
            return pos < str.length();
        }

        @Override
        public Character next() {
            if (hasNext()) {
                return str.charAt(pos++);
            }
            throw new NoSuchElementException("reached end of string");
        }
    }
}
