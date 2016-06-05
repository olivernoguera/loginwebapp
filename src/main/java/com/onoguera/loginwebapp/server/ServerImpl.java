package com.onoguera.loginwebapp.server;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * This class implements a httpserver
 */
public class ServerImpl implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerImpl.class);
    private static final String CONTEXT_PATH = "/";

    private final HttpServer server;

    public ServerImpl(int port, int threadPoolSize) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(threadPoolSize));
        server.createContext(CONTEXT_PATH, new DispatchHandler());
    }

    /**
     * Start server
     */
    public void start() {

        LOGGER.info("{} Starting server on port {}", getClass().getSimpleName(), server.getAddress().getPort());

        server.start();

        LOGGER.info("{} Listening on http://localhost: {}", getClass().getSimpleName(), server.getAddress().getPort());
        LOGGER.info("{} Server started {}", getClass().getSimpleName(), server.getAddress().getPort());
    }

    /**
     * Stop server
     *
     * @delay is delay to stop in milliseconds
     */
    public void stop(int delay) {

        LOGGER.info("{} Stopping server with delay: {}", getClass().getSimpleName(), delay);

        server.stop(delay);

        LOGGER.info("{} Server stopped", getClass().getSimpleName());
    }


}
