import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskStatus;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> sub = new ArrayList<>();
        sub.add(3);
        TaskManager tm = new TaskManager();
        tm.createTask(new Task("ТЗ-4", "Реализация технического задания четвертого спринта"));
        tm.createEpic(new Epic("Переезд", "Большая задача по перезду"));
        tm.createSubtask(new Subtask("Подготовка", "Подготовка к пеерезду", 2));
        tm.createSubtask(new Subtask("Траспортировка", "Перевоз вещей", 2));
        tm.createTask(new Task("ТЗ-3","Реализация технического задания третьего спринта"));
        System.out.println("Поехали! Демонстрация базового функционала!");
        tm.printTasksOfAllTypes();
        System.out.println();
        tm.printEpicSubtasks(tm.getEpicById(2));
        tm.printEpicSubtasks(tm.getEpicById(1));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Subtask(3, "Подготовка", "Подготовка к пеерезду 20", TaskStatus.DONE, 2));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteSubtaskById(4);
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Epic(2, "Переезд", "Большая задача по перезду", TaskStatus.IN_PROGRESS, sub));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteEpicById(2);
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteTaskById(15);
        tm.deleteTaskById(1);
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteAllTasks();
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.createTask(new Task("ТЗ-4", "Реализация технического задания четвертого спринта"));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Task(1, "ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.IN_PROGRESS));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Task(6, "ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.IN_PROGRESS));
        tm.createEpic(new Epic("Переезд", "Большая задача по перезду"));
        tm.createSubtask(new Subtask("Подготовка", "Подготовка к пеерезду", 7));
        tm.createSubtask(new Subtask("Траспортировка", "Перевоз вещей", 7));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteAllTasks();
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteAllSubtasks();
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteAllEpics();
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.createEpic(new Epic( "Переезд", "Большая задача по перезду"));
        tm.createSubtask(new Subtask("Подготовка", "Подготовка к пеерезду", 10));
        tm.createSubtask(new Subtask("Траспортировка", "Перевоз вещей", 10));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteAllEpics();
        System.out.println();
        tm.printTasksOfAllTypes();
        Task taskOne = tm.createTask(new Task("ТЗ-4", "Реализация технического задания четвертого спринта"));
        Epic epicOne = tm.createEpic(new Epic("Переезд", "Большая задача по перезду"));
        Subtask subtaskOne = tm.createSubtask(new Subtask("Подготовка", "Подготовка к пеерезду", 14));
        System.out.println(taskOne.toString());
        System.out.println(epicOne.toString());
        System.out.println(subtaskOne.toString());
        System.out.println("Демонстрация функционала завершена!");
    }
}
