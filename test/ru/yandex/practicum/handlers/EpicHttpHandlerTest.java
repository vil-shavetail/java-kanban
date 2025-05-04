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

public class EpicHttpHandlerTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    TaskManager tm = getDefault(getDefaultHistory());;
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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");

        String taskJson = gson.toJson(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Epic> tasksFromManager = tm.getAListOfEpics();
        assertNotNull(tasksFromManager, "Список задач пуст");
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("Переезд", tasksFromManager.iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");

        tm.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().split(",")[2].contains("Переезд"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");

        tm.createEpic(epic);

        Epic epicUpdate = new Epic(1,"Переезд", "Большая задача по переезду", TaskStatus.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(120));
        epicUpdate.setId(epic.getId());
        String taskJson = gson.toJson(epicUpdate);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Epic> tasksFromManager = tm.getAListOfEpics();
        assertNotNull(tasksFromManager, "Список задач пуст");
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals(TaskStatus.IN_PROGRESS, tasksFromManager.iterator().next().getStatus(),
                "Статус задачи отличен от ожидаемого");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");

        tm.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().split(",")[2].contains("Переезд"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");

        tm.createEpic(epic);

        Epic epicSave = new Epic("Сплав", "Большая задача по подготовке майского сплава");

        tm.createEpic(epicSave);

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        Collection<Epic> tasksFromManager = tm.getAListOfEpics();
        assertEquals(1, tasksFromManager.size(),
                "Количество задач не соответствует ожидаемому значению");
        assertEquals("Сплав", tasksFromManager.iterator().next().getTitle(),
                "Наименование задачи не соответствует ожидаемому значению");
    }

    @Test
    public void testGetSubtasksByEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "Большая задача по переезду");
        tm.createEpic(epic);

        Subtask subtask = new Subtask(2, "Подготовка", "Подготовка к переезду", TaskStatus.DONE,
                LocalDateTime.now(), Duration.ofMinutes(5), 1);
        tm.createSubtask(subtask);

        Subtask subtaskSave = new Subtask(3, "Транспортировка", "Перевоз вещей", TaskStatus.DONE,
                LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5), 1);
        tm.createSubtask(subtaskSave);

        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа отличен от ожидаемого значения");

        assertNotNull(response.body(), "Список задач пуст");
        assertTrue(response.body().contains("Подготовка"),
                "Наименование задачи не соответствует ожидаемому значению");
        assertTrue(response.body().contains("Транспортировка"),
                "Наименование задачи не соответствует ожидаемому значению");
    }

}
