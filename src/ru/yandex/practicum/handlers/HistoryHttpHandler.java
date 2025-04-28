package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.managers.TaskManager;

import java.io.IOException;

public class HistoryHttpHandler extends BaseHttpHandler {

    public HistoryHttpHandler(TaskManager tm) {
        super.tm = tm;
    }

    @Override
    public void handleGetHistory(HttpExchange h) throws IOException {
        String response = gson.toJson(tm.getHistory());
        if (response.equals("[]")) {
            sendNotFound(h, "История списка задач пуста.");
        } else {
            sendText(h, response, 200);
        }
    }

}
