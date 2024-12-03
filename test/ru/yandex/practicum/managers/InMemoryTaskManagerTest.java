package ru.yandex.practicum.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskStatus;

import java.util.List;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void createTasksOfAllTypes() {
        taskManager = Managers.getDefault();
        taskManager.createTask(new Task("ТЗ-3", "Реализация технического задания третьего спринта"));
        taskManager.createEpic(new Epic("Переезд", "Большая задача по перезду"));
        taskManager.createSubtask(new Subtask("Подготовка", "Подготовка к пеерезду", 2));
        taskManager.createSubtask(new Subtask("Траспортировка", "Перевоз вещей", 2));
        taskManager.createTask(new Task("ТЗ-4","Реализация технического задания четвертого спринта"));
    }

    @Test
    void checkCreatedTasksOfAllTypes() {
        List<Task> tasks = taskManager.getAListOfTasks();
        Assertions.assertEquals(2, tasks.size());
        Assertions.assertEquals(1, tasks.getFirst().getId());
        Assertions.assertEquals("ТЗ-3", tasks.getFirst().getTitle());
        Assertions.assertEquals("Реализация технического задания третьего спринта",
                tasks.getFirst().getDescription());
        Assertions.assertEquals(TaskStatus.NEW,
                tasks.getFirst().getStatus());
        Assertions.assertEquals(5, tasks.getLast().getId());
        Assertions.assertEquals("ТЗ-4", tasks.getLast().getTitle());
        Assertions.assertEquals("Реализация технического задания четвертого спринта",
                tasks.getLast().getDescription());
        Assertions.assertEquals(TaskStatus.NEW,
                tasks.getLast().getStatus());

        List<Epic> epics = taskManager.getAListOfEpics();
        Assertions.assertEquals(1, epics.size());
        Assertions.assertEquals(2, epics.getFirst().getId());
        Assertions.assertEquals("Переезд", epics.getFirst().getTitle());
        Assertions.assertEquals("Большая задача по перезду", epics.getFirst().getDescription());
        Assertions.assertEquals(2, epics.getFirst().getSubs().size());
        Assertions.assertEquals(3, epics.getFirst().getSubs().getFirst());
        Assertions.assertEquals(4, epics.getFirst().getSubs().getLast());
        Assertions.assertEquals(TaskStatus.NEW, epics.getFirst().getStatus());

        List<Subtask> subtasks = taskManager.getAListOfSubtask();
        Assertions.assertEquals(2, subtasks.size());
        Assertions.assertEquals(3, subtasks.getFirst().getId());
        Assertions.assertEquals("Подготовка", subtasks.getFirst().getTitle());
        Assertions.assertEquals("Подготовка к пеерезду", subtasks.getFirst().getDescription());
        Assertions.assertEquals(2, subtasks.getFirst().getEpicId());
        Assertions.assertEquals(TaskStatus.NEW, subtasks.getFirst().getStatus());
        Assertions.assertEquals(4, subtasks.getLast().getId());
        Assertions.assertEquals("Траспортировка", subtasks.getLast().getTitle());
        Assertions.assertEquals("Перевоз вещей", subtasks.getLast().getDescription());
        Assertions.assertEquals(2, subtasks.getLast().getEpicId());
        Assertions.assertEquals(TaskStatus.NEW, subtasks.getLast().getStatus());


    }

    @Test
    void updateTaskOfAllTypes() {
        taskManager.update(new Task(1, "ТЗ-3", "Реализация технического задания третьего спринта",
                TaskStatus.IN_PROGRESS));
        taskManager.update(new Subtask(3, "Подготовка", "Подготовка к пеерезду",
                TaskStatus.DONE, 2));

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(1).getStatus());
        Assertions.assertEquals(TaskStatus.DONE, taskManager.getSubtaskById(3).getStatus());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(2).getStatus());
        taskManager.update(new Epic(2, "Переезд", "Большая задача по перезду", TaskStatus.DONE,
                taskManager.getEpicById(2).getSubs()));
        Assertions.assertEquals(TaskStatus.DONE, taskManager.getEpicById(2).getStatus());
    }

    @Test
    void deleteTasksOfAllTypesById() {
        Assertions.assertEquals(2, taskManager.getAListOfTasks().size());
        taskManager.deleteTaskById(1);
        Assertions.assertNull(taskManager.getTaskById(1));
        Assertions.assertEquals(1, taskManager.getAListOfTasks().size());
        Assertions.assertEquals(2, taskManager.getAListOfSubtask().size());
        taskManager.deleteSubtaskById(4);
        Assertions.assertNull(taskManager.getSubtaskById(4));
        Assertions.assertEquals(1, taskManager.getAListOfSubtask().size());
        Assertions.assertEquals(1, taskManager.getAListOfEpics().size());
        taskManager.deleteEpicById(2);
        Assertions.assertNull(taskManager.getSubtaskById(2));
        Assertions.assertNull(taskManager.getSubtaskById(3));
        Assertions.assertEquals(0, taskManager.getAListOfSubtask().size());
        Assertions.assertEquals(0, taskManager.getAListOfEpics().size());
        taskManager.deleteTaskById(5);
        Assertions.assertEquals(0, taskManager.getAListOfTasks().size());
    }

    @Test
    void deleteAllTasksOfAllTypes() {
        Assertions.assertEquals(2, taskManager.getAListOfTasks().size());
        Assertions.assertEquals(1, taskManager.getAListOfEpics().size());
        Assertions.assertEquals(2, taskManager.getAListOfSubtask().size());
        taskManager.deleteAllSubtasks();
        Assertions.assertEquals(0, taskManager.getAListOfSubtask().size());
        taskManager.deleteAllEpics();
        Assertions.assertEquals(0, taskManager.getAListOfEpics().size());
        taskManager.deleteAllTasks();
        Assertions.assertEquals(0, taskManager.getAListOfTasks().size());

    }

    @Test
    void getAllTypeOfTasksById() {
        Assertions.assertEquals("ТЗ-3", taskManager.getTaskById(1).getTitle());
        Assertions.assertEquals("Переезд", taskManager.getEpicById(2).getTitle());
        Assertions.assertEquals("Подготовка", taskManager.getSubtaskById(3).getTitle());
        Assertions.assertEquals("Траспортировка", taskManager.getSubtaskById(4).getTitle());
        Assertions.assertEquals("ТЗ-4", taskManager.getTaskById(5).getTitle());
    }

}