package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HandMadeHistoryLinkedList handMadeHistoryLinkedList = new HandMadeHistoryLinkedList();

    @Override
    public void add(Task task) {
        handMadeHistoryLinkedList.linkLast(task);

    }

    @Override
    public void remove(int id) {
        handMadeHistoryLinkedList.removeNode(handMadeHistoryLinkedList.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return handMadeHistoryLinkedList.getTasks();
    }
}
