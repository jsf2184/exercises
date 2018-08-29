package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.Arrays;

public class IntClientHandler implements Runnable{
    private static final Logger _log = Logger.getLogger(IntClientHandler.class);

    DataStreamPair _streamPair;


    public IntClientHandler(Socket clientSocket) {
        _streamPair = new DataStreamPair(clientSocket);
    }

    public void run() {

        if (!_streamPair.setup()) {
            return;
        }

        _log.info("IntClientHandler.run(): finished streamPair.setup()");
        try {
            Integer input;
            while ((input = _streamPair.readInteger()) != null) {
                _log.info(String.format("IntClientHandler receives: %s", input));
                int[] msg = new int[input];
                for (int i=0; i<input; i++) {
                    msg[i] = input;
                }
                _streamPair.writeIntegerArray(msg);
                _log.info(String.format("IntClientHandler sent respose: %s", Arrays.toString(msg)));
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

