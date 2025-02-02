package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static HistoryManager historyManager;
    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        FileBackedTaskManager.historyManager = historyManager;
        this.file = file;
    }

    public void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        try (FileWriter fileWriter = new FileWriter(String.valueOf(file), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(HEADER);
            bufferedWriter.newLine();

            for (Task task : getAListOfTasks()) {
                bufferedWriter.write(toString(task));
                bufferedWriter.newLine();
            }

            for (Epic epic : getAListOfEpics()) {
                bufferedWriter.write(toString(epic));
                bufferedWriter.newLine();
            }

            for (Subtask subtask : getAListOfSubtask()) {
                bufferedWriter.write(toString(subtask));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(historyManager));
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(historyManager, file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file,
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                if (!line.equals(HEADER)) {
                    Task task = fromString(line);
                    if (task instanceof Epic) {
                        fileBackedTaskManager.createEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        fileBackedTaskManager.createSubtask((Subtask) task);
                    } else if (task instanceof Task) {
                        fileBackedTaskManager.createTask(task);
                    } else {
                        String historyLine = bufferedReader.readLine();
                        for (int id : historyFromString(historyLine)) {
                            historyManager.add(task);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return fileBackedTaskManager;
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
        taskString = String.join(",", Integer.toString(task.getId()), getTaskType(task).toString(),
                task.getTitle(), task.getStatus().toString(), task.getDescription(), getEpicId(task));
        return taskString;
    }

    static Task fromString(String value) {
        String[] taskAttributes = value.split(",");
        if (taskAttributes[1].equals((TaskType.TASK).toString())) {
            return new Task(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    taskAttributes[4], TaskStatus.valueOf(taskAttributes[3]));
        } else if (taskAttributes[1].equals((TaskType.EPIC).toString())) {
            return new Epic(Integer.parseInt(taskAttributes[0]), taskAttributes[2],
                    taskAttributes[4], TaskStatus.valueOf(taskAttributes[3]));
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

    private static String historyToString(HistoryManager historyManager) {
        String historyLine = "";
        List<Task> history = historyManager.getHistory();
        List<String> historyId = new ArrayList<>();

        for (Task task : history) {
            historyId.add(String.valueOf(task.getId()));
        }

        historyLine = String.join(",", historyId);
        return historyLine;
    }

    private static List<Integer> historyFromString(String line) {
        List<Integer> historyList = new ArrayList<>();
        if (line != null && !line.isEmpty()) {
            String[] values = line.split(",");

            for (String value : values) {
                historyList.add(Integer.parseInt(value));
            }
        }
        return historyList;
    }

}
