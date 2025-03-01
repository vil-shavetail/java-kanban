package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getAListOfTasks();

    List<Epic> getAListOfEpics();

    List<Subtask> getAListOfSubtask();

    List<Subtask> getAListOfEpicSubtasks(Epic epic);

    void printEpicSubtasks(Epic epic);

    List<Task> getHistory();

    void printTasksOfAllTypes();

    TreeSet<Task> getPrioritizedTasks();
}
