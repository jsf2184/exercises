package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class StringClient extends  Client{
    private static final Logger _log = Logger.getLogger(StringClient.class);

    StringStreamPair _streamPair;

    public StringClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected boolean derivedSetup(Socket socket) {
        _streamPair = new StringStreamPair(socket);
        return _streamPair.setup();
    }

    public void sendReceive(int n) {
        for (int i=0; i<n; i++) {
            String msg = Integer.toString(i);
            _log.info(String.format("Client attempts to write %s", msg));
            _streamPair.writeLine(msg);
            _log.info(String.format("Client back from writeLine(): %s, now attempt to read", msg));
            String response = _streamPair.readLine();
            _log.info(String.format("received: %s", response));
        }
        _streamPair.writeLine("quit");
        _log.info(String.format("received: %s",  _streamPair.readLine()));
        _streamPair.close();
    }


}
