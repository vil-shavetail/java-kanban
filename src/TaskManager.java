import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private static int id = 0;
    public static int getId() {
        return id;
    }

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Integer> sub = new ArrayList<>();

    public void createTask(Task task) {
        tasks.put(id, task);
        id++;
    }

    public void createEpic(Epic epic) {
        epics.put(id, epic);
        id++;
    }

    public void createSubtask(Subtask subtask) {
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        sub.add(id);
        epic.setSub(sub);
        id++;
    }

    public void deleteTaskById(int id) {
        if(!tasks.isEmpty()) {
            tasks.remove(id);
        }
        if(!epics.isEmpty()) {
            epics.remove(id);
        }
        if(!subtasks.isEmpty()) {
            subtasks.remove(id);
        }

    }

    public  void deleteAllTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        id = 0;
    }

    public void printAllTask() {
        printOnlyTasks();
        printOnlyEpics();
        printOnlySubtasks();
    }

    public void printOnlyTasks() {
        if(!tasks.isEmpty()) {
            for (int key : tasks.keySet()) {
                System.out.println("Задача №" + key + ", " + tasks.get(key).getTitle() +
                        ", описание: " + tasks.get(key).getDescription() + ", статус: " +
                        tasks.get(key).getStatus() + ", идентификатор: " +
                        tasks.get(key).getId() + ".");
            }
        }
    }

    public void printOnlyEpics() {
        if(!epics.isEmpty()) {
            for (int key : epics.keySet()) {
                if (epics.get(key).getSub().isEmpty()) {
                    System.out.println("Задача №" + key + ", " + epics.get(key).getTitle() +
                            ", описание: " + epics.get(key).getDescription() + ", статус: " +
                            epics.get(key).getStatus() + ", идентификатор: " +
                            epics.get(key).getId() + ".");
                } else {
                    System.out.println("Задача №" + key + ", " + epics.get(key).getTitle() +
                            ", описание: " + epics.get(key).getDescription() + ", статус: " +
                            epics.get(key).getStatus() + ", идентификатор: " +
                            epics.get(key).getId() + ", идентификаторы подзадач: " +
                            epics.get(key).getSub() + ".");
                }
            }
        }
    }

    public void printOnlySubtasks() {
        if(!subtasks.isEmpty()) {
            for (int key : subtasks.keySet()) {
                System.out.println("Задача №" + key + ", " + subtasks.get(key).getTitle() +
                        ", описание: " + subtasks.get(key).getDescription() + ", статус: " +
                        subtasks.get(key).getStatus() + ", идентификатор: " +
                        subtasks.get(key).getId() + ", иднетификатор эпика: " +
                        subtasks.get(key).getEpicId() + ".");
            }
        }
    }

    public void updateTaskStatus(int id) {
        Task task = tasks.get(id);
        task.setStatus(TaskStatus.DONE);
        tasks.put(id, task);
    }

}
