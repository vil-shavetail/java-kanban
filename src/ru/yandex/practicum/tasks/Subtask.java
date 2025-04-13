package ru.yandex.practicum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId  = epicId;
    }

    public Subtask(Integer id, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(id, title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", epicId=" + epicId +
                '}';
    }
}
