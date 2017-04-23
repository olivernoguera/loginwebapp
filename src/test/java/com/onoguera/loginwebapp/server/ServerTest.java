package com.onoguera.loginwebapp.server;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class ServerTest {
    private Server server;

    @Test
    public void startServer() throws IOException {
        server = new ServerImpl(8080,20);
        server.start();
        server.stop(1);

    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void stopServer() throws IOException {
        server = new ServerImpl(8080,20);
        server.start();
        server.start();
    }


    @Test(expected = IllegalArgumentException.class)
    public void startServerWithBadPort() throws IOException {
        server = new ServerImpl(515151515,20);
        server.start();


    }
}
