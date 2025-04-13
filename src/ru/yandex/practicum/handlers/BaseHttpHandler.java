package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /hello запроса от клиента.");

        String response = "Hey! Glad to see you on our server.";
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public void sendText() {

    }

    public void sendNotFound() {

    }

    public void sendHasInteractions() {

    }
}
