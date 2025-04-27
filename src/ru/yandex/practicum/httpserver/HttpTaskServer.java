package ru.yandex.practicum.httpserver;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.handlers.*;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager tm) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.createContext("/tasks", new TaskHttpHandler(tm));
        httpServer.createContext("/subtasks", new SubtaskHttpHandler(tm));
        httpServer.createContext("/epics", new EpicHttpHandler(tm));
        httpServer.createContext("/history", new HistoryHttpHandler(tm));
        httpServer.createContext("/prioritized", new PrioritizationHttpHandler(tm));

    }

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault(Managers.getDefaultHistory());
        HttpTaskServer httpServer = new HttpTaskServer(tm);
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
