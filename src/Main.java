public class Main {

    public static void main(String[] args) {

        TaskManager tm = new TaskManager();
        tm.createEpic(new Epic("Переезд", "Большая задача по перезду", TaskStatus.IN_PROGRESS));
        tm.createSubtask(new Subtask("Подготовка", "Подготовка к пеерезду", TaskStatus.DONE, 0));
        tm.createSubtask(new Subtask("Траспортировка", "Перевоз вещей", TaskStatus.IN_PROGRESS, 0));
        tm.createTask(new Task("ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.NEW));
        System.out.println("Поехали!");
        tm.printAllTask();
//        tm.updateTaskStatus(0);
        tm.printAllTask();
        tm.deleteTaskById(1);
        tm.printAllTask();
        tm.deleteAllTask();
        tm.createTask(new Task("ТЗ-4", "Реализация технического задания четвертого спринта", TaskStatus.NEW));
        tm.printAllTask();
    }
}
