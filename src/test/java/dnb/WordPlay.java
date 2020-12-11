package dnb;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class WordPlay {
    @Test
    public void run() {
        splitIt("a b c");
        splitIt("a   b c,d;e.f\tg");
    }
    public String[] splitIt(String s) {
        final String[] parts = s.split("[\\s,\\.;]+");
        log.info("{}: {}",parts.length, parts);
        return parts;
    }
}
