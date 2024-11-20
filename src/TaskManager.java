import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {

    private static int id = 1;
    public static int getId() {
        return id;
    }

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Integer> sub = new ArrayList<>();

    public void create(Task task) {
        tasks.put(id, task);
        id++;
    }

    public void create(Epic epic) {
        epics.put(id, epic);
        id++;
    }

    public void create(Subtask subtask) {
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        sub.add(id);
        epic.setSub(sub);
        id++;
    }

    public void update(Task task) {
        Task prevTask = getTaskById(task.getId());
        if (Objects.isNull(prevTask)) {
            System.out.println("Обновление невозможно!");
        } else {
            if(prevTask.equals(task)) {
                tasks.put(task.getId(), task);
                System.out.println("Задача с идентификатором - " + task.getId() + " успешно обновлена!");
            } else {
                System.out.println("Идентификаторы задач не совпадают. Обновление невозможно");
            }
        }
    }

    public void update(Epic epic) {
        Epic prevEpic = getEpicById(epic.getId());
        if (Objects.isNull(prevEpic)) {
            System.out.println("Обновление невозможно!");
        } else {
            if(prevEpic.equals(epic)) {
                modifyEpicStatus(getEpicById(epic.getId()));
                System.out.println("Эпик с идентификатором - " + epic.getId() + " успешно обновлена!");
            } else {
                System.out.println("Идентификаторы эпиков не совпадают. Обновление невозможно");
            }
        }
    }

    public void update(Subtask subtask) {
        Subtask prevSubtask = getSubtaskById(subtask.getId());
        if (Objects.isNull(prevSubtask)) {
            System.out.println("Обновление невозможно!");
        } else {
            if(prevSubtask.equals(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                modifyEpicStatus(getEpicById(subtask.getEpicId()));
                System.out.println("Подзадача с идентификатором - " + subtask.getId() + " успешно обновлена!");
            } else {
                System.out.println("Идентификаторы подзадач не совпадают. Обновление невозможно");
            }
        }
    }

    public void deleteTaskById(int id) {
        if(!tasks.isEmpty() && tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача с идентификатором " + id + ", отсуствует в списке задач." +
                    "Удаление невозможно!");
        }
    }

    public void deleteEpicById(int id) {
        if(!epics.isEmpty() && epics.containsKey(id)) {
            if(!epics.get(id).getSub().isEmpty()) {
                ArrayList<Integer> subs = epics.get(id).getSub();
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

    public void deleteSubtaskById(int id) {
        if(!subtasks.isEmpty() && subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            ArrayList<Integer> subs = epics.get(epicId).getSub();
            int i = 0;
            while (i < subs.size()) {
                if(subs.get(i) == id) {
                    subs.remove(i);
                    i++;
                    continue;
                }
                i++;
            }
            modifyEpicStatus(getEpicById(epicId));
        } else {
            System.out.println("Подзадача с идентификатором " + id + ", отсуствует в списке задач." +
                    "Удаление невозможно!");
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        id = 1;
    }

    public Task getTaskById(int id) {
        Task task;
        if(!tasks.isEmpty() && tasks.containsKey(id)) {
            task = tasks.get(id);
        } else {
            System.out.println("Задача с указанным идентификатором отсвуствует в списке задач.");
            task = null;
        }
        return task;
    }

    public Epic getEpicById(int id) {
        Epic epic;
        if(!epics.isEmpty() && epics.containsKey(id)) {
            epic = epics.get(id);
        } else {
            System.out.println("Эпик с указанным идентификатором отсвуствует в списке задач.");
            epic = null;
        }
        return epic;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask;
        if(!subtasks.isEmpty() && subtasks.containsKey(id)) {
            subtask = subtasks.get(id);
        } else {
            System.out.println("Подзадача с указанным идентификатором отсвуствует в списке задач.");
            subtask = null;
        }
        return subtask;
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
                if (epics.get(key).getSub().isEmpty()) {
                    System.out.println("Эпик №" + key + ", " + epics.get(key).getTitle() +
                            ", описание: " + epics.get(key).getDescription() + ", статус: " +
                            epics.get(key).getStatus() + ", идентификатор: " +
                            epics.get(key).getId() + ".");
                } else {
                    System.out.println("Эпик №" + key + ", ," + epics.get(key).getTitle() +
                            ", описание: " + epics.get(key).getDescription() + ", статус: " +
                            epics.get(key).getStatus() + ", идентификатор: " +
                            epics.get(key).getId() + ", идентификаторы подзадач: " +
                            epics.get(key).getSub() + ".");
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

    public void printEpicSubtasks(Epic epic) {
        if(Objects.nonNull(epic)) {
            if (!epic.getSub().isEmpty()) {
                ArrayList<Integer> subs = epic.getSub();
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

    public void modifyEpicStatus(Epic epic) {
        if(epic.getSub().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            epics.put(epic.getId(), epic);
        } else {
            ArrayList<Integer> subs = epic.getSub();
            int countOne = 0;
            int countTwo = 0;
            int countThree = 0;
            for (int key : subs) {
                switch (subtasks.get(key).getStatus()) {
                    case NEW:
                        epic.setStatus(TaskStatus.NEW);
                        countOne = countOne + 1;
                        break;
                    case DONE:
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

}
