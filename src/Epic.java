import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> sub;
    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description, TaskStatus status, ArrayList<Integer> sub) {
        super(id, title, description, status);
        this.sub = sub;
    }

    public ArrayList<Integer> getSub() {
        return sub;
    }

    public void setSub(ArrayList<Integer> sub) {
        this.sub = sub;
    }
}
