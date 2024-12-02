package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public Task createTask(Task task) {
        tasks.put(task.setId(id), task);
        id++;
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epics.put(epic.setId(id), epic);
        id++;
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtasks.put(subtask.setId(id), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubs().add(id);
        id++;
        modifyEpicStatus(getEpicById(subtask.getEpicId()));
        return subtask;
    }

    @Override
    public void update(Task task) {
        Task prevTask = getTaskById(task.getId());
        if (Objects.isNull(prevTask)) {
            System.out.println("Обновление невозможно!");
        } else {
            if(prevTask.getId() == task.getId() &&
                (!prevTask.getTitle().equals(task.getTitle()) ||
                !prevTask.getDescription().equals(task.getDescription()) ||
                !prevTask.getStatus().equals(task.getStatus()))) {
                tasks.put(task.getId(), task);
                System.out.println("Задача с идентификатором - " + task.getId() + " успешно обновлена!");
            } else {
                System.out.println("Объекты идентичны. Нечего обновлять!");
            }
        }
    }

    @Override
    public void update(Epic epic) {
        Epic prevEpic = getEpicById(epic.getId());
        if (Objects.isNull(prevEpic)) {
            System.out.println("Обновление невозможно!");
        } else {
            if(prevEpic.getId() == epic.getId() &&
                    (!prevEpic.getTitle().equals(epic.getTitle()) ||
                    !prevEpic.getDescription().equals(epic.getDescription()) ||
                    !prevEpic.getStatus().equals(epic.getStatus()) ||
                    !prevEpic.getSubs().equals(epic.getSubs()))) {
                epics.put(epic.getId(), epic);
                System.out.println("Эпик с идентификатором - " + epic.getId() + " успешно обновлена!");
            } else {
                System.out.println("Объекты идентичны. Нечего обновлять!");
            }
        }
    }

    @Override
    public void update(Subtask subtask) {
        Subtask prevSubtask = getSubtaskById(subtask.getId());
        if (Objects.isNull(prevSubtask)) {
            System.out.println("Обновление невозможно!");
        } else {
            if(prevSubtask.getId() == subtask.getId() &&
                (!prevSubtask.getTitle().equals(subtask.getTitle()) ||
                !prevSubtask.getDescription().equals(subtask.getDescription()) ||
                !prevSubtask.getStatus().equals(subtask.getStatus()) ||
                prevSubtask.getEpicId() != subtask.getEpicId())) {
                subtasks.put(subtask.getId(), subtask);
                modifyEpicStatus(getEpicById(subtask.getEpicId()));
                System.out.println("Подзадача с идентификатором - " + subtask.getId() + " успешно обновлена!");
            } else {
                System.out.println("Идентификаторы подзадач не совпадают. Обновление невозможно");
            }
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if(!tasks.isEmpty() && tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача с идентификатором " + id + ", отсуствует в списке задач." +
                    "Удаление невозможно!");
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if(!epics.isEmpty() && epics.containsKey(id)) {
            if(!epics.get(id).getSubs().isEmpty()) {
                ArrayList<Integer> subs = epics.get(id).getSubs();
                for (Integer integer : subs) {
                    subtasks.remove(integer);
                }
            }
            epics.remove(id);
        } else {
            System.out.println("Эпик с идентификатором " + id + ", отсуствует в списке задач." +
                    "Удаление невозможно!");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if(!subtasks.isEmpty() && subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            epics.get(epicId).getSubs().remove((Integer) id);
            modifyEpicStatus(getEpicById(epicId));
        } else {
            System.out.println("Подзадача с идентификатором " + id + ", отсуствует в списке задач." +
                    "Удаление невозможно!");
        }
    }

    @Override
    public void deleteAllTasks() {
        if(!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void deleteAllEpics() {
        if(!epics.isEmpty()) {
            epics.clear();
            subtasks.clear();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        if(!subtasks.isEmpty()) {
            for (int key : subtasks.keySet()) {
                int epicId = subtasks.get(key).getEpicId();
                epics.get(epicId).getSubs().clear();
                modifyEpicStatus(getEpicById(epicId));
            }
            subtasks.clear();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task;
        if(!tasks.isEmpty() && tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task);

        } else {
            System.out.println("Задача с указанным идентификатором отсвуствует в списке задач.");
            task = null;
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic;
        if(!epics.isEmpty() && epics.containsKey(id)) {
            epic = epics.get(id);
            historyManager.add(epic);

        } else {
            System.out.println("Эпик с указанным идентификатором отсвуствует в списке задач.");
            epic = null;
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask;
        if(!subtasks.isEmpty() && subtasks.containsKey(id)) {
            subtask = subtasks.get(id);
            historyManager.add(subtask);

        } else {
            System.out.println("Подзадача с указанным идентификатором отсвуствует в списке задач.");
            subtask = null;
        }
        return subtask;
    }

    @Override
    public List<Task> getAListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAListOfSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getAListOfEpicSubtasks(Epic epic) {
        ArrayList<Subtask> aListOfEpicSubtasks = new ArrayList<>();
        if(Objects.nonNull(epic)) {
            if (!epic.getSubs().isEmpty()) {
                ArrayList<Integer> subs = epic.getSubs();
                for (int key : subs) {
                    aListOfEpicSubtasks.add(subtasks.get(key));
                }
            }
        }
        return aListOfEpicSubtasks;
    }

    public void printTasks() {
        if(!tasks.isEmpty()) {
            for (int key : tasks.keySet()) {
                System.out.println("Задача №" + key + " " + tasks.get(key).getTitle() +
                        ", описание: " + tasks.get(key).getDescription() + ", статус: " +
                        tasks.get(key).getStatus() + ", идентификатор: " +
                        tasks.get(key).getId() + ".");
            }
        } else {
            System.out.println("Список задач с типом Задача пуст!");
        }
    }

    public void printEpics() {
        if(!epics.isEmpty()) {
            for (int key : epics.keySet()) {
                if (epics.get(key).getSubs().isEmpty()) {
                    System.out.println("Эпик №" + key + ", " + epics.get(key).getTitle() +
                            ", описание: " + epics.get(key).getDescription() + ", статус: " +
                            epics.get(key).getStatus() + ", идентификатор: " +
                            epics.get(key).getId() + ".");
                } else {
                    System.out.println("Эпик №" + key + ", " + epics.get(key).getTitle() +
                            ", описание: " + epics.get(key).getDescription() + ", статус: " +
                            epics.get(key).getStatus() + ", идентификатор: " +
                            epics.get(key).getId() + ", идентификаторы подзадач: " +
                            epics.get(key).getSubs() + ".");
                }
            }
        } else {
            System.out.println("Список задач c типом Эпик пуст!");
        }
    }

    public void printSubtasks() {
        if(!subtasks.isEmpty()) {
            for (int key : subtasks.keySet()) {
                System.out.println("Подзадача №" + key + ", " + subtasks.get(key).getTitle() +
                        ", описание: " + subtasks.get(key).getDescription() + ", статус: " +
                        subtasks.get(key).getStatus() + ", идентификатор: " +
                        subtasks.get(key).getId() + ", иднетификатор эпика: " +
                        subtasks.get(key).getEpicId() + ".");
            }
        } else {
            System.out.println("Список задач с типом Подзадача пуст!");
        }
    }
    public void printTasksOfAllTypes() {
        printTasks();
        printEpics();
        printSubtasks();
    }

    @Override
    public void printEpicSubtasks(Epic epic) {
        if(Objects.nonNull(epic)) {
            if (!epic.getSubs().isEmpty()) {
                ArrayList<Integer> subs = epic.getSubs();
                System.out.println("Список подзадач для Эпика с иднентификатором - №" + epic.getId() + ".");
                for (int key : subs) {
                    System.out.println("Подзадача №" + key + ", " + subtasks.get(key).getTitle() +
                            ", описание: " + subtasks.get(key).getDescription() + ", статус: " +
                            subtasks.get(key).getStatus() + ", идентификатор: " +
                            subtasks.get(key).getId() + ", иднетификатор эпика: " +
                            subtasks.get(key).getEpicId() + ".");
                }
            }
        }
    }

    private void modifyEpicStatus(Epic epic) {
        if(epic.getSubs().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            epics.put(epic.getId(), epic);
        } else {
            ArrayList<Integer> subs = epic.getSubs();
            int countOne = 0;
            int countTwo = 0;
            int countThree = 0;
            for (int key : subs) {
                switch (subtasks.get(key).getStatus()) {
                    case TaskStatus.NEW:
                        epic.setStatus(TaskStatus.NEW);
                        countOne = countOne + 1;
                        break;
                    case TaskStatus.DONE:
                        epic.setStatus(TaskStatus.DONE);
                        countTwo = countTwo + 1;
                        break;
                    default:

                        countThree = countThree + 1;
                }
            }
            if(countThree != 0) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (countOne !=0 && countTwo !=0) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (countOne != 0) {
                epic.setStatus(TaskStatus.NEW);
            } else if (countTwo != 0) {
                epic.setStatus(TaskStatus.DONE);
            }
            epics.put(epic.getId(), epic);
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
