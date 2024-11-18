import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> sub;
    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }


    public ArrayList<Integer> getSub() {
        return sub;
    }

    public void setSub(ArrayList<Integer> sub) {
        this.sub = sub;
    }
}
