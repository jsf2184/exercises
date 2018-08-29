package com.jsf2184.se8.nio;

import com.jsf2184.utility.ResourceUtility;
import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class TextReadingTests {

    @Test
    public void testRead() throws Exception {
        String resourcePath = ResourceUtility.getResourcePath("codeFile.txt");
        RandomAccessFile file = new RandomAccessFile(resourcePath, "r");
        FileChannel channel = file.getChannel();
        System.out.printf("Channel has size: %d\n", channel.size());

        ByteBuffer buf = ByteBuffer.allocate(20);

        StringBuilder sb = new StringBuilder();
        int bytesRead = channel.read(buf);
        int total = 0;
        while (bytesRead != -1) {
            buf.flip();
            System.out.printf("Read %d bytes\n", bytesRead);
            int i=0;
            while (buf.hasRemaining()) {
                char aChar = (char) buf.get();
                sb.append(aChar);
                i++;
                total++;
            }
            System.out.printf("Appended %d bytes\n", i);

            buf.clear();
            bytesRead = channel.read(buf);
        }
        System.out.println(sb.toString());
        System.out.printf("Total bytes: %d\n", total);

    }

}
