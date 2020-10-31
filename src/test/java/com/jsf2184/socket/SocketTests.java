package com.jsf2184.socket;

import com.jsf2184.BlockingQueueTests;
import com.jsf2184.utility.LoggerUtility;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SocketTests {

    private static final Logger _log = Logger.getLogger(BlockingQueueTests.class);


    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }

    @Test
    public void testClientFailToConnect() {
        Client client = new StringClient("127.0.0.1", 9999);
        Assert.assertFalse( client.connect(3));
    }

    @Test
    @Ignore
    public void testClientConnects() {
        StringClient client = new StringClient("127.0.0.1", 9999);
        Assert.assertTrue(client.connect(3));
        client.sendReceive(5);
    }

    @Test
    public void testNioChannelClientDoesntConnect() {
        ChannelClient client = new ChannelClient("127.0.0.1", 9999);
        Assert.assertFalse(client.connect(3));
    }

    @Test
    public void testNioChannelClientTalking() {
        ChannelClient client = new ChannelClient("127.0.0.1", 9999);
        Assert.assertTrue(client.connect(3));
        client.sendReceive(3);
    }


    @Test
    @Ignore
    public void testServer() {
        Server server = new Server(9999, StringClientHandler::new);
        server.run();
    }
}
