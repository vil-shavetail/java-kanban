public class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId  = epicId;
    }

    public Subtask(Integer id, String title, String description, TaskStatus status, Integer epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
