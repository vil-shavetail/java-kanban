package ru.yandex.practicum.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subs = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description, TaskStatus status, ArrayList<Integer> subs) {
        super(id, title, description, status);
        this.subs = subs;
    }

    public ArrayList<Integer> getSubs() {
        return subs;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subs=" + subs +
                '}';
    }
}
