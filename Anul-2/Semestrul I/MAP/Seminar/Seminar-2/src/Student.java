import java.util.Objects;

public class Student extends Entity<Long> implements Comparable<Student>{
    private String nume;
    private float media;
    public Student(String nume, float media) {
        this.nume = nume;
        this.media = media;
    }

    @Override
    public String toString() {
        return "Student:" +
                "Nume='" + nume + '\'' +
                ", Media=" + media +
                '.';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Float.compare(media, student.media) == 0 && Objects.equals(nume, student.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, media);
    }
    @Override
    public int compareTo(Student o){
        int rezult=this.nume.compareTo(o.nume);
//        if (rezult==0)
//            return Float.compare(media, o.media);
        return rezult;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }
}
