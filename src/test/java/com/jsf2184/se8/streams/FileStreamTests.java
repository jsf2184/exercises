package com.jsf2184.se8.streams;

import com.jsf2184.utility.ResourceUtility;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileStreamTests {

    @Test
    public void readFileTraditional() throws Exception {
        File resourceFile = ResourceUtility.getResourceFile("lineFile.txt");
        FileReader fileReader = new FileReader(resourceFile);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test
    public void readFileWithStream() throws Exception {
        Stream<String> fileStream = getFileStream("lineFile.txt");
        fileStream.forEach(System.out::println);
    }

    public static Stream<String> getFileStream(final String name) throws Exception {
        File resourceFile = ResourceUtility.getResourceFile(name);
        Stream<String> fileStream = Files.lines(resourceFile.toPath());
        return fileStream;
    }


}
