package com.jsf2184.socket;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class DataStreamPair extends StreamPair<DataInputStream, DataOutputStream>
{
    private static final Logger _log = Logger.getLogger(DataStreamPair.class);

    public DataStreamPair(Socket socket) {
        super(socket,
              DataInputStream::new,
              DataOutputStream::new);
    }


    public Integer readInteger() {
        DataInputStream inputStream = getInputStream();
        if (inputStream != null) {
            try {
                int res = inputStream.readInt();
                return res;
            } catch (Exception e) {
                _log.warn("DataStreamPair.readInteger(): caught exception", e);
                close();
                return null;
            }
        }
        return null;
    }

    public int[] readIntegerArray() {
        DataInputStream inputStream = getInputStream();
        if (inputStream != null) {
            try {
                int size = inputStream.readInt();
                int res[] = new int[size];
                for (int i = 0; i < size; i++) {
                    res[i] = inputStream.readInt();
                }
                return res;
            } catch (Exception e) {
                _log.warn("DataStreamPair.readIntegerArray(): caught exception", e);
                close();
            }
        }
        return null;
    }

    public boolean writeInteger(int x) {
        DataOutputStream outputStream = getOutputStream();
        if (outputStream == null) {
            return false;
        }

        try {
            outputStream.writeInt(x);
        } catch (Exception e) {
            _log.warn("DataStreamPair.writeInteger(): caught exception", e);
            close();
            return false;
        }
        return true;
    }

    public boolean writeIntegerArray(int[] array) throws IOException {

        DataOutputStream outputStream = getOutputStream();
        if (outputStream == null) {
            return false;
        }

        _log.info(String.format("DataStreamPair.writeIntegerArray() about to write: %s", Arrays.toString(array)));
        try {
            outputStream.writeInt(array.length);
            for (int x : array) {
                outputStream.writeInt(x);
            }
            _log.info(String.format("DataStreamPair.writeIntegerArray() back from writing: %s",  Arrays.toString(array)));
        } catch (IOException e) {
            _log.warn("DataStreamPair.writeIntegerArray() caught exception ", e);
            close();
            return false;
        }
        return true;
    }
}
