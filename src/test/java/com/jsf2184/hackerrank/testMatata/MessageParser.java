package com.jsf2184.hackerrank.testMatata;

import java.nio.ByteBuffer;

public class MessageParser {
    ByteBuffer buffer;

    public MessageParser(byte[] input)
    {
        buffer = ByteBuffer.wrap(input);
    }

    long getMillis() {
        long res = buffer.getLong();
        return res;
    }



}
