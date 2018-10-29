package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

public class StreamPair <IS extends Closeable, OS extends Closeable> {


    public static class StreamHolder<T extends Closeable> {
        T _stream;

        public StreamHolder(T stream) {
            _stream = stream;
        }

        public T getStream() {
            return _stream;
        }

        public void close() {
            if (_stream != null) {
                String streamName = _stream.getClass().getSimpleName();
                try {
                    _stream.close();
                    _stream = null;
                    _log.info(String.format("StreamHolder.close(): Successfully closed stream of type: %s", streamName));
                } catch (IOException e) {
                    _log.error(String.format("StreamHolder.close(): Error closed stream of type: %s", streamName), e);
                }
            }
        }
    }
    private static final Logger _log = Logger.getLogger(StreamPair.class);

    Socket _socket;
    Function<InputStream, IS> _isFunc;
    Function<OutputStream, OS> _osFunc;

    StreamHolder<IS> _inputStreamHolder;
    StreamHolder<OS> _outputStreamHolder;

    public StreamPair(Socket socket,
                      Function<InputStream, IS> isFunc,
                      Function<OutputStream, OS> osFunc)
    {
        _socket = socket;
        _isFunc = isFunc;
        _osFunc = osFunc;
    }

    protected  IS getInputStream() {
        if (_inputStreamHolder != null) {
            return _inputStreamHolder.getStream();
        }
        return null;
    }

    protected  OS getOutputStream() {
        if (_outputStreamHolder != null) {
            return _outputStreamHolder.getStream();
        }
        return null;
    }


    public boolean setup() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = _socket.getInputStream();
            outputStream = _socket.getOutputStream();
        } catch (Exception e) {
            _log.error("StreamPair.setup(): caught exception getting streams", e);
            close();
        }
        _inputStreamHolder = new StreamHolder<>(_isFunc.apply(inputStream));
        _outputStreamHolder = new StreamHolder<>(_osFunc.apply(outputStream));

        return true;
    }

    public void close() {
        closeStream(_inputStreamHolder);
        closeStream(_outputStreamHolder);
    }

    private static <T extends Closeable> void closeStream(StreamHolder<T> streamHolder) {
        if (streamHolder == null) {
            return;
        }
        streamHolder.close();
    }

}
