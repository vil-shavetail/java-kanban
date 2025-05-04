package ru.yandex.practicum.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;

import java.io.IOException;

public class EpicHttpHandler extends BaseHttpHandler {

    public EpicHttpHandler(TaskManager tm) {
        super.tm = tm;
    }

    @Override
    protected void handleGetTask(HttpExchange h) throws IOException {
        super.handleGetTask(h);
        String response = gson.toJson(tm.getAListOfEpics());
        if (response.equals("[]")) {
            sendNotFound(h, "Список эпиков пуст");
        } else {
            sendText(h, response, 200);
        }
    }

    @Override
    protected void handleGetTaskById(HttpExchange h) throws IOException {
        super.handleGetTaskById(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        String response = gson.toJson(tm.getEpicById(Integer.parseInt(requestURI[2])));
        if (response.equals("null")) {
            sendNotFound(h, "Такого эпика нет");
        } else {
            sendText(h, response, 200);
        }
    }

    @Override
    protected void handlePostTask(HttpExchange h) throws IOException {
        super.handlePostTask(h);
        String requestBody = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Epic newEpic = gson.fromJson(requestBody, Epic.class);
        if (newEpic.getId() > 0 && tm.getEpicById(newEpic.getId()) != null) {
            tm.update(newEpic);
            sendText(h, "Эпик с ID=" + newEpic.getId() + " обновлен", 201);
        } else {
            int taskId = tm.createEpic(newEpic).getId();
            if (taskId > 0) {
                sendText(h, "Эпик с ID: " + taskId + " добавлен", 201);
            } else {
                sendHasInteractions(h);
            }
        }
    }

    @Override
    protected void handleDeleteTask(HttpExchange h) throws IOException {
        super.handleDeleteTask(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        if (tm.getEpicById(Integer.parseInt(requestURI[2])) != null) {
            tm.deleteEpicById(Integer.parseInt(requestURI[2]));
            sendText(h, "Эпик с id=" + requestURI[2] + " удалён", 201);
        } else {
            sendNotFound(h, "Такого эпика нет");
        }
    }

    @Override
    protected void handleGetSubtasksByEpicId(HttpExchange h) throws IOException {
        super.handleGetSubtasksByEpicId(h);
        String[] requestURI = h.getRequestURI().getPath().split("/");
        String response = gson.toJson(tm.getAListOfEpicSubtasks(tm.getEpicById(Integer.parseInt(requestURI[2]))));
        if (response.equals("[]")) {
            sendNotFound(h, "Список задач пуст");
        } else {
            sendText(h, response, 200);
        }
    }

}
