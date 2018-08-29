package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class Server {
    private static final Logger _log = Logger.getLogger(Server.class);

    int _port;
    private Function<Socket, Runnable> _clientHandlerFactory;
    boolean _running;

    public Server(int port, Function<Socket, Runnable> clientHandlerFactory) {
        _port = port;
        _clientHandlerFactory = clientHandlerFactory;
    }

    public void run() {
        ServerSocket serverSocket;
        try {
            // Create a 'standing' Server socket to listen on a well known port for new connections.
            serverSocket = new ServerSocket(_port);
        } catch (IOException e) {
            _log.error("Server.run(): abort trying to create ServerSocket ", e);
            return;
        }
        _running = true;
        while (_running) {
            Socket clientSocket;
            try {
                // When a new client connects, the serverSocket will yield a socket devoted to serving the new client
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                _log.error("Server.run(): error from serverSocket.accept() ", e);
                break;
            }
            _log.info("Server accepts new client");
            // Get a distinct runnable devoted to handling that particular client. Note that were this real, we would
            // start the runnable in a new thread.
            //
            Runnable clientHandler = _clientHandlerFactory.apply(clientSocket);
            clientHandler.run();
        }
    }
}
