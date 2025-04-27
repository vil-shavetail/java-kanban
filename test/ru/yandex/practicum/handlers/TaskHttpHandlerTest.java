package ru.yandex.practicum.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.adapters.DurationTypeAdapter;
import ru.yandex.practicum.adapters.LocalDateTimeTypeAdapter;
import ru.yandex.practicum.httpserver.HttpTaskServer;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Task;
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

public class TaskHttpHandlerTest {

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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(1,"ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(480));

        String taskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Task> tasksFromManager = tm.getAListOfTasks();
        assertNotNull(tasksFromManager, "Список задач пуст");
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("ТЗ-9", tasksFromManager.iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task(1,"ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.NEW, LocalDateTime.now(),  Duration.ofMinutes(480));

        tm.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().split(",")[1].contains("ТЗ-9"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(1, "ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(480));

        tm.createTask(task);

        Task taskUpdate = new Task(1, "ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(480));
        taskUpdate.setId(task.getId());
        String taskJson = gson.toJson(taskUpdate);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Task> tasksFromManager = tm.getAListOfTasks();
        assertNotNull(tasksFromManager, "Список задач пуст");
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("ТЗ-9", tasksFromManager.iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
        assertEquals(TaskStatus.IN_PROGRESS, tasksFromManager.iterator().next().getStatus(),
                "Статус задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task(1,"ТЗ-9", "Реализация технического задания девятого спринта",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        tm.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().split(",")[1].contains("ТЗ-9"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(1,"ТЗ-8", "Реализация технического задания восьмого спринта",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(420));

        tm.createTask(task);

        Task taskSave = new Task(2,"ТЗ-9", "Реализация технического задания восьмого спринта",
                TaskStatus.NEW, LocalDateTime.now().plusHours(12), Duration.ofMinutes(480));

        tm.createTask(taskSave);

        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Task> tasksFromManager = tm.getAListOfTasks();
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("ТЗ-8", tasksFromManager.iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
    }

}
