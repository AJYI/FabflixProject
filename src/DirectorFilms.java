import java.util.ArrayList;
import java.util.List;

public class DirectorFilms {
    String directorName;
    List<Movies> movieTitles;

    void setDirectorName(String name) {
        directorName = name;
    }

    String getDirectorName() {
        return directorName;
    }

    void setMovieList() {
        movieTitles = new ArrayList<Movies>();
    }

    List<Movies> getMovies() {
        return movieTitles;
    }

    void addMovie(Movies movie) {
        movieTitles.add(movie);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Director Films Details - ");
        sb.append("Director Name:" + getDirectorName());
        sb.append(": ");
        for(Movies movie : getMovies()){
            sb.append(movie.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}

