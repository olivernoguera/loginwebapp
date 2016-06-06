package com.onoguera.loginwebapp.server;

import com.onoguera.loginwebapp.controller.Controller;
import com.onoguera.loginwebapp.controller.ControllerContainer;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseInternalServerError;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Created by oliver on 3/06/16.
 */
public class DispatchHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchHandler.class);
    private final ControllerContainer controllerContainer;
    private final UserService userService;

    public DispatchHandler() {
        controllerContainer = ControllerContainer.getInstance();
        userService = UserService.getInstance();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Response response;

        try {
            response = dispatch(httpExchange);
            sendResponse(httpExchange, response);
        } catch (RuntimeException e) {
            LOGGER.error("An error occurred when dispatching request.", e);
            response = new ResponseInternalServerError();
            sendResponse(httpExchange, response);
        } catch (IOException e) {
            LOGGER.error("An error occurred when dispatching request.", e);
            response = new ResponseInternalServerError();
            sendResponse(httpExchange, response);
        }

    }

    private void sendResponse(HttpExchange httpExchange, Response response) throws IOException {

        httpExchange.getResponseHeaders().add("Content-Type", response.getContentType());
        httpExchange.sendResponseHeaders(response.getHttpStatus(), response.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Response dispatch(HttpExchange httpExchange) {
        Optional<Controller> controllerOpt =
                controllerContainer.findController(httpExchange.getRequestURI().getPath());

        if (controllerOpt == null || !controllerOpt.isPresent()) {
            return new ResponseNotFound();
        }
        Controller controller = controllerOpt.get();

        Response response = controller.dispatch(
                httpExchange.getRequestURI(),
                httpExchange.getRequestBody(),
                httpExchange.getRequestMethod(),
                httpExchange.getRequestHeaders());
        return response;

    }


}
