import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoviesCast {
    String title, id;
    //List<String> actors;
    Set<String> actors;

    public MoviesCast() { actors = new HashSet<String>(); }

    void setId(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    List<String> getActors() {
        List<String> tempList = new ArrayList<>(actors);
        return tempList;
    }

    void addActor(String actor) {
        actors.add(actor);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Cast Details - ");
        sb.append("Movie ID: " + getId());
        sb.append(", ");
        sb.append("Movie Title:" + getTitle());
        sb.append(", ");
        sb.append("Movie Actors: ");
        for(String actor : getActors()){
            sb.append(actor.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

}
