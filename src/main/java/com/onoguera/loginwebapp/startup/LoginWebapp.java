package com.onoguera.loginwebapp.startup;

import com.onoguera.loginwebapp.server.Server;
import com.onoguera.loginwebapp.server.ServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginWebapp {

    private static final int THREAD_POOL_SIZE_DEFAULT = 20;
    private static final int PORT_PROPERTY_DEFAULT = 8080;
    private static final int TIME_TO_EXPIRED_SESSIONS = 5 * 60 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginWebapp.class);

    public static void main(String[] args) throws IOException {


        Integer timeToExpiredSession = TIME_TO_EXPIRED_SESSIONS;
        if( args.length > 1 ){
            printUsage();
            System.exit(1);

        }
        if( args.length == 1 ){
            try{
                timeToExpiredSession = Integer.parseInt(args[0]);
                LOGGER.info("Sessions will expire every {} milliseconds.", timeToExpiredSession);
            }catch (NumberFormatException e){
                LOGGER.error("Argument arg[0] {} must be an integer.", args[0]);
                printUsage();
                System.exit(1);
            }
        }
        Server server = new ServerImpl(PORT_PROPERTY_DEFAULT, THREAD_POOL_SIZE_DEFAULT);
        LauncherApp launcherApp = new LauncherApp(server,timeToExpiredSession);
        shutDown(launcherApp);
        launcherApp.start();

    }

    private static void printUsage(){

        LOGGER.error("{} usage:  test-web-application.jar [periodToExpireSession] " );
        LOGGER.error("{} periodToExpireSession, argument optional - Time to expired sessions on milliseconds ");
    }


    private static void shutDown(LauncherApp launcherApp) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> launcherApp.stop()));
    }
}
