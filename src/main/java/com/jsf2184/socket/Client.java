package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public abstract class Client {
    private static final Logger _log = Logger.getLogger(Client.class);

    String _host;
    int _port;
    StringStreamPair _streamPair;

    public Client(String host, int port) {
        _host = host;
        _port = port;
    }

    public boolean connect(int retries) {
        for (int i=0; i<retries; i++) {
            Socket socket;
            try {
                socket = new Socket(_host, _port);
                if (derivedSetup(socket)) {
                    return true;
                }
            } catch (IOException e) {
               _log.error(String.format("Unable to connect to server %s:%d", _host, _port));
            }
        }
        return false;
    }

    protected abstract boolean derivedSetup(Socket socket);



}
