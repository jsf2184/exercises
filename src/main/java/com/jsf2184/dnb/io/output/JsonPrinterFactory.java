package com.jsf2184.dnb.io.output;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class JsonPrinterFactory {
    String filename;

    public JsonPrinterFactory(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public JsonPrinter createJsonPrinter() throws FileNotFoundException {
        return new JsonPrinter(createOutputStream());
    }

    BufferedOutputStream createOutputStream() throws FileNotFoundException {
        BufferedOutputStream result = new BufferedOutputStream(new FileOutputStream(filename));
        return result;
    }

}
