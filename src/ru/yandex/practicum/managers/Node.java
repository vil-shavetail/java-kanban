package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

public class Node {
    private Task task;
    private Node previous;
    private Node next;

    public Node getNext() {
        return next;
    }

    public Node getPrevious() {
        return previous;
    }

    public Task getTask() {
        return task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
