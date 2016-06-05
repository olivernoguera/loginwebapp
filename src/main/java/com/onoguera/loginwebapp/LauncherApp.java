package com.onoguera.loginwebapp;

import com.onoguera.loginwebapp.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LauncherApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(LauncherApp.class);

    private final Server server;
    private final LoaderEntities loaderEntities;

    public LauncherApp(Server server) throws IOException {
        this.server = server;
        this.loaderEntities = new LoaderEntities();
    }

    public void start() throws IOException {
        this.loaderEntities.loadEntities();
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
    }
}
