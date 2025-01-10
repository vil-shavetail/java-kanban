package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HandMadeHistoryLinkedList {
    private final Map<Integer, Node> data = new HashMap<>();
    private Node head;
    private Node tail;

    void linkLast(Task task) {
        Node node = new Node();
        node.setTask(task);

        if (data.containsKey(task.getId())) {
            removeNode(data.get(task.getId()));
        }

        if (head == null) {
            tail = node;
            head = node;
            node.setNext(null);
            node.setPrevious(null);
        } else {
            node.setPrevious(tail);
            node.setNext(null);
            tail.setNext(node);
            tail = node;
        }

        data.put(task.getId(), node);
    }

    List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.getTask());
            node = node.getNext();
        }
        return history;
    }

    void removeNode(Node node) {
        if (node != null) {
            data.remove(node.getTask().getId());
            Node previous = node.getPrevious();
            Node next = node.getNext();

            if (head == node) {
                head = node.getNext();
            }

            if (tail == node) {
                tail = node.getPrevious();
            }

            if (previous != null) {
                previous.setNext(next);
            }

            if (next != null) {
                next.setPrevious(previous);
            }
        }
    }

    Node getNode(int id) {
        return data.get(id);
    }

    private static class Node {
        private Task task;
        private Node previous;
        private Node next;

        private Node getNext() {
            return next;
        }

        private Node getPrevious() {
            return previous;
        }

        private Task getTask() {
            return task;
        }

        private void setNext(Node next) {
            this.next = next;
        }

        private void setPrevious(Node previous) {
            this.previous = previous;
        }

        private void setTask(Task task) {
            this.task = task;
        }
    }
}
