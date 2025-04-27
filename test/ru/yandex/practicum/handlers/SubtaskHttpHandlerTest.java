package ru.yandex.practicum.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.adapters.DurationTypeAdapter;
import ru.yandex.practicum.adapters.LocalDateTimeTypeAdapter;
import ru.yandex.practicum.httpserver.HttpTaskServer;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.managers.Managers.getDefault;
import static ru.yandex.practicum.managers.Managers.getDefaultHistory;

public class SubtaskHttpHandlerTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    TaskManager tm = getDefault(getDefaultHistory());
    HttpTaskServer httpServer = new HttpTaskServer(tm);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

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
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(2,"Подготовка", "Подготовка к переезду", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10), 1);

        String taskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Subtask> tasksFromManager = tm.getAListOfSubtask();
        assertNotNull(tasksFromManager, "Список задач пуст");
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("Подготовка", tasksFromManager.iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(2,"Подготовка", "Подготовка к переезду", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10), 1);
        tm.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().split(",")[2].contains("Подготовка"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(2, "Подготовка", "Подготовка к переезду", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10), 1);
        tm.createSubtask(subtask);

        Subtask subtaskUpdate = new Subtask(2, "Подготовка", "Подготовка к переезду", TaskStatus.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(10), 1);
        subtaskUpdate.setId(subtask.getId());
        String taskJson = gson.toJson(subtaskUpdate);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Subtask> tasksFromManager = tm.getAListOfSubtask();
        assertNotNull(tasksFromManager, "Список задач пуст");
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals(TaskStatus.IN_PROGRESS, tasksFromManager.iterator().next().getStatus(),
                "Статус задачи отличается от ожидаемого");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(2,"Подготовка", "Подготовка к переезду", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10), 1);
        tm.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().split(",")[2].contains("Подготовка"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(2,"Подготовка", "Подготовка к переезду", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10), 1);
        tm.createSubtask(subtask);

        Subtask subtaskSave = new Subtask(3,"Транспортировка", "Перевоз вещей", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), 1);

        tm.createSubtask(subtaskSave);

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Subtask> tasksFromManager = tm.getAListOfSubtask();
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("Транспортировка", tasksFromManager.stream().iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
    }

}
