package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Subtask;

import java.io.IOException;

public class SubtaskHttpHandler extends BaseHttpHandler {

    public TaskManager tm;

    public SubtaskHttpHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    protected void handleGetTask(HttpExchange h) throws IOException {
        super.handleGetTask(h);
        String response = gson.toJson(tm.getAListOfSubtask());
        if (response.equals("[]")) {
            sendNotFound(h, "Список подзадач пуст");
        } else {
            sendText(h, response, 200);
        }
    }

    @Override
    protected void handleGetTaskById(HttpExchange h) throws IOException {
        super.handleGetTaskById(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        String response = gson.toJson(tm.getSubtaskById(Integer.parseInt(requestURI[2])));
        if (response.equals("null")) {
            sendNotFound(h, "Такой подзадачи нет");
        } else {
            sendText(h, response, 200);
        }
    }

    @Override
    protected void handlePostTask(HttpExchange h) throws IOException {
        super.handlePostTask(h);
        String requestBody = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Subtask newTask = gson.fromJson(requestBody, Subtask.class);

        if (newTask.getId() > 0 && tm.getSubtaskById(newTask.getId()) != null) {
            tm.update(newTask);
            sendText(h, "Подзадача с ID=" + newTask.getId() + " обновлена", 201);
        } else {
            int taskId = tm.createSubtask(newTask).getId();
            if (taskId > 0) {
                sendText(h, "Подзадача добавлена с ID: " + taskId, 201);
            } else {
                sendHasInteractions(h);
            }
        }
    }

    @Override
    protected void handleDeleteTask(HttpExchange h) throws IOException {
        super.handleDeleteTask(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        if (tm.getSubtaskById(Integer.parseInt(requestURI[2])) != null) {
            tm.deleteSubtaskById(Integer.parseInt(requestURI[2]));
            sendText(h, "Подзадача с id=" + requestURI[2] + " удалена", 201);
        } else {
            sendNotFound(h, "Такой подзадачи нет");
        }
    }

}
