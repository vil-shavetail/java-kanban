package ru.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subs = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status, startTime, duration);
        this.endTime = getEndTime();
    }

    public Epic(Integer id, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration, ArrayList<Integer> subs) {
        super(id, title, description, status, startTime, duration);
        this.subs = subs;
        this.endTime = super.getEndTime();
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
