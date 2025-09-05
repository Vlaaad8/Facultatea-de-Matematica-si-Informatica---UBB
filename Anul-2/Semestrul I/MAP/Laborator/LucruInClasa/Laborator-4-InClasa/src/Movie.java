public class Movie {
    private String nume;
    int anAparitie;

    @Override
    public String toString() {
        return "Movie{" +
                "nume='" + nume + '\'' +
                ", anAparitie=" + anAparitie +
                '}';
    }

    public Movie(String nume, int anAparitie) {
        this.nume = nume;
        this.anAparitie = anAparitie;
    }
}
