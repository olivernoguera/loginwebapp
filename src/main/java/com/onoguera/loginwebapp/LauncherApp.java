package com.onoguera.loginwebapp;

import com.onoguera.loginwebapp.server.Server;

import java.io.IOException;

public class LauncherApp {


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
