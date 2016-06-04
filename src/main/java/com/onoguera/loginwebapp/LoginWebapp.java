package com.onoguera.loginwebapp;

import com.onoguera.loginwebapp.server.Server;
import com.onoguera.loginwebapp.server.ServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginWebapp
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginWebapp.class);
	private static final int THREAD_POOL_SIZE_DEFAULT = 20;
	private static final int PORT_PROPERTY_DEFAULT = 8080;

	public static void main(String[] args) throws IOException
	{
		Server server = new ServerImpl(PORT_PROPERTY_DEFAULT, THREAD_POOL_SIZE_DEFAULT);
		LauncherApp launcherApp = new LauncherApp(server);
		shutDown(launcherApp);
		launcherApp.start();

	}
	
	private static void shutDown(LauncherApp launcherApp)
	{
		Runtime.getRuntime().addShutdownHook(new Thread( ()-> launcherApp.stop()));

	}
}
