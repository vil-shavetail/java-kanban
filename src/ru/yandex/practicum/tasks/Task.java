package ru.yandex.practicum.tasks;

import java.util.Objects;

public class Task {

    private final int id;
    private final String title;
    private final String description;
    private TaskStatus status;

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Task(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(Integer id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
