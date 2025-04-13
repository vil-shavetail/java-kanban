package ru.yandex.practicum.httpserver;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.handlers.BaseHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer httpServer;

    HttpTaskServer() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/hello", new BaseHttpHandler());

    }

    public static void main(String[] args) {
        HttpTaskServer httpServer = new HttpTaskServer();
        httpServer.start();
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(5);
        System.out.println("HTTP-сервер по " + PORT + " порту - остановлен!");
    }
}
