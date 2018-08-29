package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.net.Socket;

public class StringClientHandler implements Runnable{
    private static final Logger _log = Logger.getLogger(StringClientHandler.class);

    StringStreamPair _streamPair;


    public StringClientHandler(Socket clientSocket) {
        _streamPair = new StringStreamPair(clientSocket);
    }

    public void run() {

        if (!_streamPair.setup()) {
            return;
        }

        _log.info("StringClientHandler.run(): finished streamPair.setup()");
        try {
            String line;
            while ((line = _streamPair.readLine()) != null) {
                _log.info(String.format("Server receives: %s", line));
                String response = line + line;
                _log.info(String.format("Server sends: %s", response));
                _streamPair.writeLine(response);
                if (line.equals("quit")) {
                    break;
                }
            }
        } catch (Exception e) {
            _log.error("StringClientHandler.run(): caught exception from readLoop", e);

        } finally {
            close();
        }
    }

    private  void close() {
        _streamPair.close();
    }
}

