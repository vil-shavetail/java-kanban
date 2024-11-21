package ru.yandex.practicum.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subs = new ArrayList<>();

    public Epic(Integer id, String title, String description) {
        super(id, title, description);
    }

    public Epic(Integer id, String title, String description, TaskStatus status, ArrayList<Integer> subs) {
        super(id, title, description, status);
        this.subs = subs;
    }

    public ArrayList<Integer> getSubs() {
        return subs;
    }
}
