public class Actors {
    String name;
    Integer birthYear;

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

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Actor Details - ");
        sb.append("Actor Name:" + getName());
        sb.append(", ");
        sb.append("Actor Date Of Birth:" + getBirthYear());
        sb.append(", ");

        return sb.toString();
    }

}
