import ru.yandex.practicum.managers.FileBackedTaskManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskStatus;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager tm  = Managers.getDefault(Managers.getDefaultHistory());
        ArrayList<Integer> sub = new ArrayList<>();
        sub.add(3);
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
        tm.update(new Subtask(3, "Подготовка", "Подготовка к пеерезду 20", TaskStatus.DONE,
                LocalDateTime.of(2024, 9, 11, 9, 5, 33), Duration.ofMinutes(70),2));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.deleteSubtaskById(4);
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Epic(2, "Переезд", "Большая задача по перезду", TaskStatus.IN_PROGRESS,
                LocalDateTime.MIN, Duration.ZERO, sub));
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
        tm.update(new Task(1, "ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 11, 12, 12, 35, 30),
                Duration.ofMinutes(30)));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Task(6, "ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 15, 10, 25, 45),
                Duration.ofMinutes(90)));
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
        tm.createEpic(new Epic("Переезд", "Большая задача по перезду"));
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
        Subtask subtaskTwo = tm.createSubtask(new Subtask("Транспортировка", "Транспортировка вещей к новому месту", 14));
        Task taskTwo = tm.createTask(new Task("ТЗ-3", "Реализация технического задания третьего спринта"));
        System.out.println(taskOne.toString());
        System.out.println(taskTwo.toString());
        System.out.println();
        System.out.println(epicOne.toString());
        System.out.println();
        System.out.println(subtaskOne.toString());
        System.out.println(subtaskTwo.toString());
        System.out.println();
        System.out.println(tm.getAListOfTasks());
        System.out.println();
        System.out.println(tm.getAListOfEpics());
        System.out.println();
        System.out.println(tm.getAListOfSubtask());
        System.out.println();
        System.out.println(tm.getAListOfEpicSubtasks(epicOne));
        System.out.println("Демонстрация функционала завершена!");
        System.out.println(tm.getHistory());

        Path path = Path.of("tasks.csv");
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File(String.valueOf(path)));
        System.out.println("We are at the step one: " + fbtm.getAListOfTasks());
        Task first = fbtm.createTask(new Task(1, "ТЗ-9", "Реализация технического задания четвертого спринта", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 15, 10, 25, 45),
                Duration.ofMinutes(25)));
        Task second = fbtm.createTask(new Task(2, "ТЗ-8", "Реализация технического задания четвертого спринта", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 15, 10, 10, 45),
                Duration.ofMinutes(5)));
        System.out.println("Task: " + fbtm.getTaskById(first.getId()));
        System.out.println("Task: " + fbtm.getTaskById(second.getId()));
        FileBackedTaskManager.loadFromFile(new File(String.valueOf(path)));
        System.out.println("We are at the step three" + fbtm.getAListOfTasks());
//        System.out.println("Task: " + fbtm.getTaskById(first.getId()));
//        System.out.println("Task: " + fbtm.getTaskById(second.getId()));
        System.out.println("History: " + fbtm.getHistory());
        System.out.println("Приоритизация: " + fbtm.getPrioritizedTasks());
    }
}
