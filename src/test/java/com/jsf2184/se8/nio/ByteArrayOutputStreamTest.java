package com.jsf2184.se8.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ByteArrayOutputStreamTest {

    @Test
    public void writeByteTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        outputStream.write(3);

        outputStream.write(new byte[] {4, 5, 6});
        outputStream.write("abc".getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        final byte[] bytes = outputStream.toByteArray();
        log.info("len = {}", bytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
         int val;
        while ((val = inputStream.read()) > 0) {
            log.info("val: {}", val);
        }

    }

    @Test
    public void printWriteTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.print(1);
        printWriter.print(2);
        printWriter.print(3);
        printWriter.close();
        final byte[] bytes = outputStream.toByteArray();
        log.info("len = {}", bytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        int val;
        while ((val = inputStream.read()) > 0) {
            log.info("val: {}", val);
        }

    }

    @Test
    public void streamWriterTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
        streamWriter.write(1);
        streamWriter.write(2);
        streamWriter.write(3);
        streamWriter.close();
        final byte[] bytes = outputStream.toByteArray();
        log.info("len = {}", bytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        int val;
        while ((val = inputStream.read()) > 0) {
            log.info("val: {}", val);
        }

    }





}
