package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;

public class TaskHttpHandler extends BaseHttpHandler {

    public TaskManager tm;

    public TaskHttpHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    protected void handleGetTask(HttpExchange h) throws IOException {
        super.handleGetTask(h);
        String response = gson.toJson(tm.getAListOfTasks());
        if (response.equals("[]")) {
            sendNotFound(h, "Список задач пуст");
        } else {
            sendText(h, response, 200);
        }
    }

    @Override
    protected void handleGetTaskById(HttpExchange h) throws IOException {
        super.handleGetTaskById(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        String response = gson.toJson(tm.getTaskById(Integer.parseInt(requestURI[2])));
        if (response.equals("null")) {
            sendNotFound(h, "Такой задачи нет");
        } else {
            sendText(h, response, 200);
        }
    }

    @Override
    protected void handlePostTask(HttpExchange h) throws IOException {
        super.handlePostTask(h);
        String requestBody = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Task newTask = gson.fromJson(requestBody, Task.class);

        if (newTask.getId() > 0 && tm.getTaskById(newTask.getId()) != null) {
            tm.update(newTask);
            sendText(h, "Задача с ID=" + newTask.getId() + " обновлена", 201);
        } else {
            int taskId = tm.createTask(newTask).getId();
            if (taskId > 0) {
                sendText(h, "Задача добавлена с ID: " + taskId, 201);
            } else {
                sendHasInteractions(h);
            }
        }
    }

    @Override
    protected void handleDeleteTask(HttpExchange h) throws IOException {
        super.handleDeleteTask(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        if (tm.getTaskById(Integer.parseInt(requestURI[2])) != null) {
            tm.deleteTaskById(Integer.parseInt(requestURI[2]));
            sendText(h, "Задача с id=" + requestURI[2] + " удалена", 201);
        } else {
            sendNotFound(h, "Такой задачи нет");
        }
    }

}
