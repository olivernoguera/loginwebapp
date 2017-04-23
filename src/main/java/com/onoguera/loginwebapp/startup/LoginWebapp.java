package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.server.Server;
import com.onoguera.loginwebapp.server.ServerImpl;

import java.io.IOException;

public class LoginWebapp {

    private static final int THREAD_POOL_SIZE_DEFAULT = 20;
    private static final int PORT_PROPERTY_DEFAULT = 8080;

    public static void main(String[] args) throws IOException {
        Server server = new ServerImpl(PORT_PROPERTY_DEFAULT, THREAD_POOL_SIZE_DEFAULT);
        LauncherApp launcherApp = new LauncherApp(server);
        shutDown(launcherApp);
        launcherApp.start();

    }

    private static void shutDown(LauncherApp launcherApp) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> launcherApp.stop()));
    }
}
