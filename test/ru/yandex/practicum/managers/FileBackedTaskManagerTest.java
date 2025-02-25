package ru.yandex.practicum.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

class FileBackedTaskManagerTest {

    @Test
    void testCheckThatFileCreated() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("tasks.csv"));
        fileBackedTaskManager.createTask(new Task("ТЗ-1", "Реализация технического задания пятого спринта"));
        Assertions.assertTrue(Files.exists(Path.of("tasks.csv")));
    }

    @Test
    void testLoadFromANonExistentFile() {
        Path path = Path.of("test.csv");
        new FileBackedTaskManager(path.toFile());
        try {
            FileBackedTaskManager.loadFromFile(path.toFile());
        } catch (ManagerSaveException e) {
            Assertions.assertEquals("test.csv (No such file or directory)", e.getMessage());
        }
    }

    @Test
    void testLoadFromAnExistentFile() {
        Path path = Path.of("savedTask.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("savedTask.csv"));
        try {
            fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("savedTask.csv"));
            Assertions.assertEquals(1, fileBackedTaskManager.getAListOfTasks().size());
        } catch (ManagerSaveException e) {
            Assertions.assertEquals("test.csv (No such file or directory)", e.getMessage());
        }
    }
}