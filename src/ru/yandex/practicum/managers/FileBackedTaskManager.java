package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.io.File;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    protected final HistoryManager historyManager;

    public FileBackedTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void save() {


    }

    public static void loadFromFile(File file) {

    }

    @Override
    public Task createTask(Task task) {
        task = super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic = super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask = super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;

    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    String toString(Task task) {
        String taskString;
        taskString = String.join(", ", Integer.toString(task.getId()), getTaskType(task).toString(),
                task.getTitle(), task.getStatus().toString(), task.getDescription(), getEpicId(task));
        return taskString;
    }

    Task fromString(String value) {

        String[] taskAttributes = value.split(",");
        if (taskAttributes[1].equals((TaskType.TASK).toString())) {
            return new Task(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    taskAttributes[3], TaskStatus.valueOf(taskAttributes[4]));
        } else if (taskAttributes[1].equals((TaskType.EPIC).toString())) {
            return new Epic(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    taskAttributes[3], TaskStatus.valueOf(taskAttributes[4]));
        } else {
            return new Subtask(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    taskAttributes[3], TaskStatus.valueOf(taskAttributes[4]),
                    Integer.parseInt(taskAttributes[5]));
        }

    }

    private TaskType getTaskType(Task task) {
        if (Objects.requireNonNull(task) instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }

    private String getEpicId(Task task) {
        String epicId = "";
        if (task instanceof Subtask) {
            epicId = Integer.toString(((Subtask) task).getEpicId());
        }
        return epicId;
    }

}
