package com.jsf2184.se8.functional;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class StreamPlay {

    // https://blog.softwaremill.com/why-when-and-how-to-return-stream-from-your-java-api-instead-of-a-collection-c30e7ebc5407
    public static class IntSource {
        int max;
        int current;

        public IntSource(int max) {
            this.max = max;
            current = 0;
        }

        Integer next() {
            if (current > max) {
                return null;
            }
            return current++;
        }
    }

    public static class Streamer {
        IntSource intSource;

        public Streamer(int max) {
            this.intSource = new IntSource(max);
        }

        public Stream<Integer> getStream() {
            final Stream<Integer> stream = Stream.generate(() -> intSource.next());
            return stream;
        }
    }

    @Test
    public void testIntSource() {
        IntSource intSource = new IntSource(5);
        Integer val;
        while ((val = intSource.next()) != null) {
            log.info("val = {}", val);
        }
    }

    @Test
    public void testStreamer() {
        final IntStream range = IntStream.range(0, 5);
    }

}
