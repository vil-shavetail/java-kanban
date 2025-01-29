package ru.yandex.practicum.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Task;
import java.nio.file.Files;
import java.nio.file.Path;

class FileBackedTaskManagerTest {

    @Test
    void testCheckThatFileCreated() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory());
        fileBackedTaskManager.createTask(new Task("ТЗ-5", "Реализация технического задания пятого спринта"));
        Assertions.assertTrue(Files.exists(Path.of("tasks.csv")));
    }

    @Test
    void testLoadFromANonExistentFile() {
        Path path = Path.of("test.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory());
        try {
            fileBackedTaskManager.loadFromFile(path.toFile());
        } catch (ManagerSaveException e) {
            Assertions.assertEquals("test.csv (No such file or directory)", e.getMessage());
        }
    }

    @Test
    void testLoadFromAnExistentFile() {
        Path path = Path.of("savedTask.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory());
        try {
            fileBackedTaskManager.loadFromFile(path.toFile());
            Assertions.assertEquals(1, fileBackedTaskManager.getAListOfTasks().size());
        } catch (ManagerSaveException e) {
            Assertions.assertEquals("test.csv (No such file or directory)", e.getMessage());
        }
    }
}