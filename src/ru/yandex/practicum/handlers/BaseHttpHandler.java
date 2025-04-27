package ru.yandex.practicum.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.adapters.DurationTypeAdapter;
import ru.yandex.practicum.adapters.LocalDateTimeTypeAdapter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler implements HttpHandler {

    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    @Override
    public void handle(HttpExchange h) throws IOException {
        Endpoint endpoint = getEndpoint(h.getRequestURI().getPath(), h.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                handleGetTask(h);
                break;
            }
            case GET_TASK_BY_ID: {
                handleGetTaskById(h);
                break;
            }
            case POST_TASK: {
                handlePostTask(h);
                break;
            }
            case DELETE_TASK: {
                handleDeleteTask(h);
                break;
            }
            case GET_SUBTASK_BY_EPIC_ID: {
                handleGetSubtasksByEpicId(h);
                break;
            }
            case GET_HISTORY: {
                handleGetHistory(h);
                break;
            }
            case GET_PRIORITIZATION: {
                handleGetPrioritization(h);
                break;
            }
            default:
                sendNotFound(h, "Запрашиваемый URL отсутствует.");
        }
    }

    protected void handleGetPrioritization(HttpExchange h) throws IOException{
    }

    protected void handleGetHistory(HttpExchange h) throws IOException {
    }

    protected void handleGetSubtasksByEpicId(HttpExchange h) throws IOException {
    }

    protected void handleDeleteTask(HttpExchange h) throws IOException {
    }

    protected void handlePostTask(HttpExchange h) throws IOException {
    }

    protected void handleGetTaskById(HttpExchange h) throws IOException {
    }

    protected void handleGetTask(HttpExchange h) throws IOException {
    }

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        sendText(h, text, 404);
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        sendText(h, "Задача пересекается с существующей",406);
    }

    protected enum Endpoint { GET_TASKS, GET_TASK_BY_ID, POST_TASK, DELETE_TASK, GET_SUBTASK_BY_EPIC_ID, GET_HISTORY, GET_PRIORITIZATION, UNKNOWN }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            } else if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            } else if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            } else if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }  else if (pathParts.length == 2 && pathParts[1].equals("history")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_HISTORY;
            }
        } else if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_PRIORITIZATION;
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        } else if (pathParts.length == 4 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASK_BY_EPIC_ID;
            }
        }
        return Endpoint.UNKNOWN;
    }
}
