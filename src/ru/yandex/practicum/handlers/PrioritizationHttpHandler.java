package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.managers.TaskManager;

import java.io.IOException;

public class PrioritizationHttpHandler extends BaseHttpHandler {

    public PrioritizationHttpHandler(TaskManager tm) {
        super.tm = tm;
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
