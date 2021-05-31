import java.util.*;

/*
A object class for full text search
 */
public class FullTextSearchMovies {
    private String id;
    private String title;
    private int year;
    private String directors;
    private String genres;
    private String actors;
    private String starIds;
    private Float ratings;

    public FullTextSearchMovies(String id, String title, int year, String directors, String genres, String actors, String starIds, Float ratings){
        this.id = id;
        this.title = title;
        this.year = year;
        this.directors = directors;
        this.genres = genres;
        this.actors = actors;
        this.starIds = starIds;
        this.ratings = ratings;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getDirectors() {
        return directors;
    }

    public String getGenres() {
        return genres;
    }

    public String getActors() {
        return actors;
    }

    public String getStarIds() {
        return starIds;
    }

    public Float getRatings() {
        return ratings;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FullTextSearchMovies other = (FullTextSearchMovies) obj;

        // Our equal constraint
        if(other.getId().equals(((FullTextSearchMovies) obj).getId())){
            if (other.getTitle() == ((FullTextSearchMovies) obj).getTitle()){
                return true;
            }
        }
        return false;
    }

}
