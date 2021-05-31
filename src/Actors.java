import java.util.*;

public class Actors {
    private String name;
    private Integer birthYear;

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    Integer getBirthYear() {
        return birthYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthYear);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Actors other = (Actors) obj;

        // Our equal constraint
        if(other.getName().equals(((Actors) obj).getName())){
            if (other.getBirthYear() == ((Actors) obj).getBirthYear()){
                return true;
            }
        }
        return false;
    }

}
