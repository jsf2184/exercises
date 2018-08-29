package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class StringStreamPair extends StreamPair<BufferedReader, PrintWriter> {
    private static final Logger _log = Logger.getLogger(StringStreamPair.class);


    public StringStreamPair(Socket socket) {
        super(socket,
              is -> new BufferedReader(new InputStreamReader(is)),
              os -> new PrintWriter(os, true));
    }

    public String readLine() {
        BufferedReader bufferedReader = getInputStream();
        if (bufferedReader == null) {
            return null;
        }
        try {
            _log.info("StringStreamPair.readLine entered");
            String res = bufferedReader.readLine();
            _log.info(String.format("StringStreamPair.readLine returning: %s", res));
            return res;
        } catch (IOException e) {
            _log.error("StringStreamPair.readLine() exception", e);
            close();
            return null;
        }
    }

    public boolean writeLine(String s) {
        PrintWriter printWriter = getOutputStream();
        if (printWriter == null) {
            return false;
        }
        _log.info(String.format("StreamPair.writeLine about to write: %s", s));
        printWriter.println(s);
        _log.info(String.format("StreamPair.writeLine back from writing: %s", s));
        return true;
    }
}
