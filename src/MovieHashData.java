import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieHashData {
    private String title, directorName;
    private Integer year;

    public MovieHashData(){

    }

    void setTitle(String title){
        this.title = title;
    }

    String getTitle(){
        return title;
    }

    void setDirectorName(String directorName){
        this.directorName = directorName;
    }

    String getDirectorName(){
        return directorName;
    }

    void setYear(Integer year){
        this.year = year;
    }

    Integer getYear(){
        return year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, directorName, year);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MovieHashData other = (MovieHashData) obj;

        // Our equal constraint
        if(other.getDirectorName().equals(((MovieHashData) obj).getDirectorName())){
            if (other.getTitle() == ((MovieHashData) obj).getTitle()){
                if ((other.getYear() == ((MovieHashData)obj).getYear())){
                    return true;
                }
            }
        }
        return false;
    }

}
