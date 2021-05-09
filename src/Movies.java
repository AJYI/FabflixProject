import java.util.ArrayList;
import java.util.List;

public class Movies {
    String title, id, genre;
    Integer year;
    List<Actors> actors;

    public Movies() {
        actors = new ArrayList<Actors>();
    }

    void setId (String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    void setYear(Integer year) {
        this.year = year;
    }

    Integer getYear() {
        return year;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    void setGenre(String genre) {
        this.genre = genre;
    }

    String getGenre() {
        return genre;
    }

    List<Actors> getActors() {
        return actors;
    }



    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Details - ");
        sb.append("Movie ID:" + getId());
        sb.append(", ");
        sb.append("Movie Title:" + getTitle());
        sb.append(", ");
        sb.append("Movie Year:" + getYear());
        sb.append(", ");
        sb.append("Movie Genre: " + getGenre());
        sb.append(", ");
        sb.append("Movie Actors: ");
        for(Actors actor : getActors()){
            sb.append(actor.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

}
