package ru.yandex.practicum.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.httpserver.HttpTaskServer;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.managers.Managers.getDefault;
import static ru.yandex.practicum.managers.Managers.getDefaultHistory;

public class HistoryHttpHandlerTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    TaskManager tm = getDefault(getDefaultHistory());
    HttpTaskServer httpServer = new HttpTaskServer(tm);

    @BeforeEach
    public void setUp() {
        tm.deleteAllTasks();
        tm.deleteAllSubtasks();
        tm.deleteAllEpics();
        httpServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpServer.stop();
    }

    @Test
    public void testHistory() throws IOException, InterruptedException {
        Task task = new Task(1, "ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.createTask(task);

        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(3, "Подготовка", "Подготовка к переезду",
                TaskStatus.DONE, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5),2);
        tm.createSubtask(subtask);

        URI urlTask = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(urlTask)
                .GET()
                .build();

        URI urlEpic = URI.create("http://localhost:8080/epics/2");
        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(urlEpic)
                .GET()
                .build();

        URI urlSubtask = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest requestSubtask = HttpRequest.newBuilder()
                .uri(urlSubtask)
                .GET()
                .build();

        httpClient.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        httpClient.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        httpClient.send(requestTask, HttpResponse.BodyHandlers.ofString());

        URI urlHistory = URI.create("http://localhost:8080/history");
        HttpRequest requestHistory = HttpRequest.newBuilder()
                .uri(urlHistory)
                .GET()
                .build();

        HttpResponse<String> responseHistory = httpClient.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        JsonArray jsonArray = JsonParser.parseString(responseHistory.body()).getAsJsonArray();

        assertEquals(200, responseHistory.statusCode(), "Код ответа отличен от ожидаемого значения");
        assertNotNull(responseHistory.body(), "Список истории задач пуст");
        assertEquals("Переезд", jsonArray.get(0).getAsJsonObject().get("title").getAsString(),
                "Неправильный порядок задач в истории");

        List<Task> history = tm.getHistory();
        assertNotNull(history, "Список истории задач пуст");
        assertEquals(3, history.size(), "Количество задач не соответствует ожидаемому значению");
    }
}
