package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChannelClient {
    private static final Logger _log = Logger.getLogger(ChannelClient.class);

    String _host;
    int _port;
    private SocketChannel _socketChannel;

    public ChannelClient(String host, int port) {
        _host = host;
        _port = port;
    }

    public boolean connect(int retries) {
        InetSocketAddress remote = new InetSocketAddress(_host, _port);
        for (int i=0; i<retries; i++) {
            try {
                _socketChannel = SocketChannel.open(remote);
                return true;
            } catch (IOException e) {
                _log.warn(String.format("ChannelClient.connect(): failed to connect to %s, attempt = %d/%d",
                                        remote, i+1, retries));
            }
        }
        return false;
    }

    public void sendReceive(int n) {
        try {
            ByteBuffer inBuffer = ByteBuffer.allocate(30);
            for (int i = 0; i < n; i++) {
                String str = Integer.toString(i) + "\n";
                ByteBuffer outBuffer = ByteBuffer.wrap(str.getBytes());
                try {
                    _socketChannel.write(outBuffer);
                    _log.info(String.format("ChannelClient.sendReceive(): sent %s", str));
                } catch (IOException e) {
                    _log.warn("ChannelClient.sendReceive(): exception, writing channel", e);
                    break;
                }
                StringBuilder sb = new StringBuilder();
                boolean readingMsg = true;
                int lastPosition = 0;
                while (readingMsg) {
                    try {
                        int num = _socketChannel.read(inBuffer);
                        if (num == -1) {
                            return;
                        }
                        lastPosition = inBuffer.position() - 1;
                        char lastChar = (char) inBuffer.get(lastPosition);
                        if (lastChar == '\n') {
                            readingMsg = false;
                        }
                    } catch (IOException e) {
                        _log.warn("ChannelClient.sendReceive(): exception, reading channel", e);
                    }
                }

                byte bytes[] = new byte[lastPosition];
                String line = new String(inBuffer.array(), 0, lastPosition);
                _log.info(String.format("ChannelClient.sendReceive(): received %s", line));
                inBuffer.clear();
            }
        } finally {
            close();
        }
    }

    public void close() {
        try {
            _socketChannel.close();
        } catch (IOException e) {
            _log.warn("ChannelClient.sendReceive(): exception, closing channel", e);
        }
    }


}
