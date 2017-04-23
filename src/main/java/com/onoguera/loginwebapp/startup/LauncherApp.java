package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.server.Server;

import java.io.IOException;

public final class LauncherApp {


    private final Server server;

    public LauncherApp(Server server) throws IOException {
        this.server = server;

    }

    public void start() throws IOException {
        AppContext.startContext();
        LoaderEntities.loadEntities();
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
    }
}
