package com.jsf2184.socket;

import java.net.Socket;

public class StreamPairFactory {

    StreamPair create(Socket socket, Type type) {
        switch (type) {
            case StringType:
                return createStringStreamPair(socket);
            case DataType:
                return createDataStreamPair(socket);
        }
        return null;
    }

    enum Type {
        StringType,
        DataType
    }

    static StreamPair createStringStreamPair(Socket socket) {
        return new StringStreamPair(socket);
    }

    static StreamPair createDataStreamPair(Socket socket) {
        return new DataStreamPair(socket);
    }

}
