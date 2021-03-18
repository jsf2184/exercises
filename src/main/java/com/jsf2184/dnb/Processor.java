package com.jsf2184.dnb;

import com.jsf2184.dnb.io.output.JsonPrinterFactory;
import com.jsf2184.dnb.io.output.OutputManager;
import com.jsf2184.dnb.parse.LogParser;
import com.jsf2184.dnb.parse.errors.ErrorDetector;
import com.jsf2184.dnb.utility.ArgParser;
import com.jsf2184.dnb.io.input.LineReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class Processor {
    LineReader lineReader;
    LogParser logParser;
    ErrorDetector errorDetector;
    OutputManager outputManager;

    public static void main( String[] args )
    {
        ArgParser argParser = new ArgParser(args);
        if (!argParser.process()) {
            return;
        }
        LineReader lineReader = new LineReader(argParser.getInputFileName());
        LogParser logParser = new LogParser();
        ErrorDetector errorDetector = new ErrorDetector();
        OutputManager outputManager = new OutputManager(argParser.getOutputFileName(),
                                                        new LogEntryWindow(),
                                                        new JsonPrinterFactory(createTempFileName()));

        Processor processor = new Processor(lineReader,
                                            logParser,
                                            errorDetector,
                                            outputManager);

        processor.run();

    }

    public Processor(LineReader lineReader,
                     LogParser logParser,
                     ErrorDetector errorDetector,
                     OutputManager outputManager)
    {
        this.lineReader = lineReader;
        this.logParser = logParser;
        this.errorDetector = errorDetector;
        this.outputManager = outputManager;
    }

    public void run() {
        try {
            lineReader.open();
        } catch (Exception e) {
            log.error("Failed to open lineReader: {}", e.getMessage());
            return;
        }
        try {
            outputManager.open();
        } catch (Exception e) {
            log.error("Failed to open outputManager: {}", e.getMessage());
            return;
        }
        try {
            runLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runLoop() throws IOException {
        String line;
        while ((line = lineReader.readLine()) != null) {
            log.info("line: {}", line);
        }

    }

    public static String createTempFileName() {
        final String result = "tmp." + UUID.randomUUID().toString() + ".json";
        return result;
    }




}
