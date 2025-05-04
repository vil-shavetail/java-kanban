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
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.managers.Managers.getDefault;
import static ru.yandex.practicum.managers.Managers.getDefaultHistory;

public class PrioritizationHttpHandlerTest {

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
    public void testPrioritization() throws IOException, InterruptedException {
        Task task = new Task(1, "ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.createTask(task);

        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(3,"Подготовка", "Подготовка к переезду", TaskStatus.DONE,
                LocalDateTime.now().plusMinutes(10),
                Duration.ofMinutes(5), 2);
        tm.createSubtask(subtask);

        URI urlPrioritized = URI.create("http://localhost:8080/prioritized");
        HttpRequest requestPrioritized = HttpRequest.newBuilder()
                .uri(urlPrioritized)
                .GET()
                .build();

        HttpResponse<String> responsePrioritized = httpClient.send(requestPrioritized, HttpResponse.BodyHandlers.ofString());
        JsonArray jsonArray = JsonParser.parseString(responsePrioritized.body()).getAsJsonArray();

        assertEquals(200, responsePrioritized.statusCode(), "Код ответа отличен от ожидаемого значения");
        assertNotNull(responsePrioritized.body(), "Список задач по приоритету пустой");
        assertEquals("Переезд", jsonArray.get(0).getAsJsonObject().get("title").getAsString(),
                "Неправильный порядок задач по приоритету");
        assertEquals("ТЗ-9", jsonArray.get(1).getAsJsonObject().get("title").getAsString(),
                "Неправильный порядок задач по приоритету");

        TreeSet<Task> prioritizedTasks = tm.getPrioritizedTasks();
        assertNotNull(prioritizedTasks, "Список задач по приоритету пустой");
        assertEquals(3, prioritizedTasks.size(),
                "Количество задач не соответствует ожидаемому значению");

    }

}
