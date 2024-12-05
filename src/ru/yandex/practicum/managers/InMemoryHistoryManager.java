package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private static final int MAX_HISTORY_SIZE = 10;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(!(history.size() < MAX_HISTORY_SIZE)) {
            history.removeFirst();
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
