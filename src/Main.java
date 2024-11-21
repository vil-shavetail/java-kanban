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
        tm.create(new Task(tm.getId(),"ТЗ-4", "Реализация технического задания четвертого спринта"));
        tm.create(new Epic(tm.getId(), "Переезд", "Большая задача по перезду"));
        tm.create(new Subtask(tm.getId(), "Подготовка", "Подготовка к пеерезду", 2));
        tm.create(new Subtask(tm.getId(), "Траспортировка", "Перевоз вещей", 2));
        tm.create(new Task(tm.getId(),"ТЗ-3","Реализация технического задания третьего спринта"));
        System.out.println("Поехали! Денострация базового функционала!");
        tm.printTasksOfAllTypes();
        System.out.println();
        tm.printEpicSubtasks(tm.getEpicById(2));
        tm.printEpicSubtasks(tm.getEpicById(1));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Subtask(3, "Подготовка", "Подготовка к пеерезду", TaskStatus.DONE, 2));
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
        tm.create(new Task(tm.getId(),"ТЗ-4", "Реализация технического задания четвертого спринта"));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Task(1, "ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.IN_PROGRESS));
        System.out.println();
        tm.printTasksOfAllTypes();
        tm.update(new Task(6, "ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.IN_PROGRESS));
        tm.create(new Epic(tm.getId(), "Переезд", "Большая задача по перезду"));
        tm.create(new Subtask(tm.getId(), "Подготовка", "Подготовка к пеерезду", 7));
        tm.create(new Subtask(tm.getId(), "Траспортировка", "Перевоз вещей", 7));
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
        System.out.println("Демонстрация функционала завершена!");
    }
}
