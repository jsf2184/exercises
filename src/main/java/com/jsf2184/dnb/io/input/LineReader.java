package com.jsf2184.dnb.io.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LineReader {
    String fileName;
    BufferedReader bufferedReader;

    public LineReader(String fileName) {
        this.fileName = fileName;
    }

    public void open() throws FileNotFoundException {
        FileReader fileReader = new FileReader(fileName);
        bufferedReader = new BufferedReader(fileReader);
    }

    public String readLine() throws IOException {
        String line = bufferedReader.readLine();
        return line;
    }

    public void close() throws IOException {
        bufferedReader.close();
    }
}
