package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.managers.TaskManager;

import java.io.IOException;

public class PrioritizationHttpHandler extends BaseHttpHandler {

    TaskManager tm;

    public PrioritizationHttpHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handleGetPrioritization(HttpExchange h) throws IOException {
        String response = gson.toJson(tm.getPrioritizedTasks());
        if (response.equals("[]")) {
            sendNotFound(h, "Список задач по приоритетам - пуст.");
        } else {
            sendText(h, response, 200);
        }
    }

}
