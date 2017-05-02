package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.server.Server;

import java.io.IOException;

public final class LauncherApp {

    private int timeSessionMiliseconds = 5 * 60 * 1000;
    private final Server server;



    public LauncherApp(Server server,int timeSessionMiliseconds) throws IOException {
        this.server = server;
        this.timeSessionMiliseconds = timeSessionMiliseconds;

    }

    public void start() throws IOException {
        AppContext.startContext(timeSessionMiliseconds);
        LoaderEntities.loadEntities();
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
    }
}
