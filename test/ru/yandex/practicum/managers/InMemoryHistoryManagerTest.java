package ru.yandex.practicum.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Task;

class InMemoryHistoryManagerTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void createManagers() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addToHistoryAndGetHistory() {
        Task task = new Task("ТЗ-5", "Реализация технического задания пятого спринта");
        taskManager.createTask(task);
        historyManager.add(task);

        Assertions.assertEquals(1, historyManager.getHistory().size());

    }

    @Test
    void deletePreviousTaskFromHistoryWhenMaxHistorySize10Achieved() {
        Task task1 = new Task("ТЗ-1", "Реализация технического задания №1");
        Task task2 = new Task("ТЗ-2", "Реализация технического задания №2");
        Task task3 = new Task("ТЗ-3", "Реализация технического задания №3");
        Task task4 = new Task("ТЗ-4", "Реализация технического задания №4");
        Task task5 = new Task("ТЗ-5", "Реализация технического задания №5");
        Task task6 = new Task("ТЗ-6", "Реализация технического задания №6");
        Task task7 = new Task("ТЗ-7", "Реализация технического задания №7");
        Task task8 = new Task("ТЗ-8", "Реализация технического задания №8");
        Task task9 = new Task("ТЗ-9", "Реализация технического задания №9");
        Task task10 = new Task("ТЗ-10", "Реализация технического задания №10");
        Task task11 = new Task("ТЗ-11", "Реализация технического задания №11");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);
        taskManager.createTask(task7);
        taskManager.createTask(task8);
        taskManager.createTask(task9);
        taskManager.createTask(task10);
        taskManager.createTask(task10);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);
        historyManager.add(task9);
        historyManager.add(task10);

        Assertions.assertEquals(10, historyManager.getHistory().size());
        Assertions.assertEquals("ТЗ-1", historyManager.getHistory().getFirst().getTitle());
        Assertions.assertEquals("ТЗ-10", historyManager.getHistory().getLast().getTitle());
        historyManager.add(task11);
        Assertions.assertEquals("ТЗ-2", historyManager.getHistory().getFirst().getTitle());
        Assertions.assertEquals("ТЗ-11", historyManager.getHistory().getLast().getTitle());

    }
}