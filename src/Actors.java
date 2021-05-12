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

//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("Actor Details - ");
//        sb.append("Actor Name:" + getName());
//        sb.append(", ");
//        sb.append("Actor Date Of Birth:" + getBirthYear());
//        sb.append(", ");
//
//        return sb.toString();
//    }

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

//    public static void main(String[] args){
//        Actors check = new Actors();
//        check.name = "Alex";
//        check.birthYear = 1990;
//
//        HashSet<Actors> ActorsSet = new HashSet<Actors>();
//        ActorsSet.add(check);
//
//        Actors check2 = new Actors();
//        check2.name = "Alex";
//        check2.birthYear = null;
//        ActorsSet.add(check2);
//
//        Actors check3 = new Actors();
//        check3.name = "Alex";
//        check3.birthYear = null;
//        ActorsSet.add(check3);
//
//
//        for (Actors a : ActorsSet){
//            System.out.println(a.getName() + " and " + a.getBirthYear());
//        }
//
//        Actors check4 = new Actors();
//        check4.name = "Alex";
//        check4.birthYear = 199;
//
//        if (ActorsSet.contains(check4)){
//            System.out.println("ture");
//        }
//        else{
//            System.out.println("False");
//        }
//
//    }
}
