package com.jsf2184.se8.nio;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class ByteBufferTests {
    final static int IntSize = Integer.SIZE/Byte.SIZE;

    public static void summarize(String label, ByteBuffer bb) {
        System.out.printf("%s: position = %d, limit = %d, capacity = %d\n",
                          label, bb.position(), bb.limit(), bb.capacity());
    }

    @Test
    public void testWriteReadRewind() {
        int n = 10;
        ByteBuffer sut = ByteBuffer.allocate(n * IntSize);
        IntStream.range(0, n).forEach(sut::putInt);
        summarize("after writes", sut);
        sut.flip();
        summarize("after flip", sut);
        HashSet<Integer> retrieved = new HashSet<>();

        while(sut.remaining() > 0) {
            retrieved.add(sut.getInt());
        }
        summarize("after gets(1)", sut);
        Set<Integer> expected = buildExpected(n);
        Assert.assertEquals(expected, retrieved);

        sut.rewind();
        summarize("after rewind", sut);

        retrieved.clear();
        while(sut.remaining() > 0) {
            retrieved.add(sut.getInt());
        }
        summarize("after gets(2)", sut);
        Assert.assertEquals(expected, retrieved);
    }

    void validate(IntBuffer buffer, int position, int limit, int capacity) {
        Assert.assertEquals(position, buffer.position());
        Assert.assertEquals(limit, buffer.limit());
        Assert.assertEquals(capacity, buffer.capacity());
    }
    void validate(ByteBuffer buffer, int position, int limit, int capacity) {
        Assert.assertEquals(position, buffer.position());
        Assert.assertEquals(limit, buffer.limit());
        Assert.assertEquals(capacity, buffer.capacity());
    }


    @Test
    public void testAsIntBuffer() {

        // In this test, we create an IntBuffer as an alternate view on our original ByteBuffer.

        int n = 10;
        ByteBuffer sut = ByteBuffer.allocate(n * IntSize);
        IntStream.range(0, n).forEach(sut::putInt);
        sut.flip();

        // consumer a few values;
        sut.getInt();
        sut.getInt();
        sut.getInt();
        validate(sut, 12, 40, 40);

        IntBuffer intBuffer = sut.asIntBuffer();
        // Verify that the intBuffer we created was created as a reflection of the ByteBuffer sut and
        // its current position. (that is our IntBuffer won't have access to the 3 integers that were already
        // consumed in the sut.
        //
        validate(intBuffer, 0, 7, 7);
        HashSet<Integer> retrieved = new HashSet<>();

        // And as we consume the integers, we'll be able to get the 7 values
        while(intBuffer.hasRemaining()) {
            retrieved.add(intBuffer.get());
        }
        Set<Integer> expected = buildExpected(3, n - 3);
        Assert.assertEquals(expected, retrieved);

        // How about if we rewind sut. Will that effect intBuffer?
        sut.rewind();
        retrieved.clear();
        while(intBuffer.hasRemaining()) {
            retrieved.add(intBuffer.get());
        }
        // I think not.
        Assert.assertEquals(0, retrieved.size());

        // How about if we rewind intBuffer?
        intBuffer.rewind();
        while(intBuffer.hasRemaining()) {
            retrieved.add(intBuffer.get());
        }
        // I think it will get element 3 thru 9 again.
        Assert.assertEquals(expected, retrieved);


    }


    @Test
    public void testWrappingByteArray() {
        int n = 10;
         byte[] raw = new byte[10];
        IntStream.range(0, n).forEach(x -> raw[x] = (byte) x);
        ByteBuffer sut = ByteBuffer.wrap(raw);
        HashSet<Byte> retrieved = new HashSet<>();
        while(sut.remaining() > 0) {
            retrieved.add(sut.get());
        }
        Set<Byte> expectedBytes = buildExpectedBytes(n);
        Assert.assertEquals(expectedBytes, retrieved);

        // Now poke a value in raw and see if it shows up in the ByteBuffer.
        sut.rewind();
        raw[n-1] = 99;
        expectedBytes.remove((byte) 9);
        expectedBytes.add((byte) 99);

        retrieved = new HashSet<>();
        while(sut.remaining() > 0) {
            retrieved.add(sut.get());
        }
        Assert.assertEquals(expectedBytes, retrieved);

        // now poke a value into the ByteArray and see if it show up in the byte[].
        sut.put(0, (byte) 100);
        retrieved.clear();
        sut.rewind();
        while(sut.remaining() > 0) {
            retrieved.add(sut.get());
        }
        expectedBytes.remove((byte) 0);
        expectedBytes.add((byte) 100);
        Assert.assertEquals(expectedBytes, retrieved);

        // And see if the byte[] has been changed too.
        retrieved.clear();
        for (byte b : raw) {
            retrieved.add(b);
        }
        Assert.assertEquals(expectedBytes, retrieved);

    }

    @Test
    public void testWithStrings() {
        String s1 = "abcd";
        ByteBuffer sut = ByteBuffer.allocate(s1.length());
        sut.put(s1.getBytes());
        String s2 = new String(sut.array());
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void testIntoSeveralStrings() {
        String s1 = "abc|def|ghi|jkl|mnop|qrst|uvwx|yz";
        ByteBuffer sut = ByteBuffer.allocate(s1.length());
        sut.put(s1.getBytes());
        sut.flip();
        StringBuilder sb = new StringBuilder();
        while (sut.hasRemaining()) {
            int chunkSize = Math.min(sut.remaining(), 3);
            byte[] part = new byte[chunkSize];
            sut.get(part);
            String partStr = new String(part);
            sb.append(partStr);
        }
        String builtStr = sb.toString();
        String[] parts = builtStr.split("\\|");
        Assert.assertEquals(s1, builtStr);

    }

    @Test
    public void writeReadAlternate() {
        IntBuffer sut = IntBuffer.allocate(2);
        validate(sut, 0, 2, 2);
        sut.put(0);
        validate(sut, 1, 2, 2);

        // get ready to pull an int out of the buffer
        sut.flip();
        validate(sut, 0, 1, 2);

        Assert.assertEquals(0, sut.get());
        validate(sut, 1, 1, 2);

        // now if we were to try to write an int (as is), we'd get an exception cuz we'd bump into our limit of 1.
        boolean caught = false;
        try {
            sut.put(1);
        } catch (Exception ignore) {
            caught = true;
        }
        Assert.assertTrue(caught);

        // so, how do we resume writing. One approach is to reset the limit and position.
        sut.limit(2);
        sut.position(1);
        validate(sut, 1, 2, 2);
        sut.put(1);

        Assert.assertEquals(0, sut.get(0));
        Assert.assertEquals(1, sut.get(1));


    }

    public Set<Integer> buildExpected(int n) {
        Set<Integer> res = buildExpected(0, n);
        return res;
    }

    public Set<Integer> buildExpected(int start, int count) {
        HashSet<Integer> res = new HashSet<>();
        IntStream.range(start, start+count).forEach(res::add);
        return res;
    }

    public Set<Byte> buildExpectedBytes(int n) {
        HashSet<Byte> res = new HashSet<>();
        IntStream.range(0, n).forEach(x -> res.add((byte)x));
        return res;
    }

}
