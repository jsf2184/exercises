package com.jsf2184.dnb.io.output;

import com.jsf2184.dnb.LogEntry;
import com.jsf2184.dnb.LogEntryWindow;

import java.io.IOException;

public class OutputManager {

    private final String outputFileName;
    private final LogEntryWindow logEntryWindow;
    private final JsonPrinterFactory jsonPrinterFactory;
    JsonPrinter jsonPrinter;

    public OutputManager(String outputFileName,
                         LogEntryWindow logEntryWindow,
                         JsonPrinterFactory jsonPrinterFactory) {
        this.outputFileName = outputFileName;
        this.logEntryWindow = logEntryWindow;
        this.jsonPrinterFactory = jsonPrinterFactory;
        jsonPrinter = null;
    }

    public void open() throws IOException {
        jsonPrinter = jsonPrinterFactory.createJsonPrinter();
        jsonPrinter.start();
    }

    public void close() throws IOException {
        jsonPrinter.close();
    }
    public void addLogEntry(boolean hasError, LogEntry logEntry) throws IOException {
        logEntryWindow.add(logEntry);
        if (hasError) {
            jsonPrinter.writeWindow(logEntryWindow);
        }
    }
}
