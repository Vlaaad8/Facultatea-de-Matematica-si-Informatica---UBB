public class Cerc {
    private double raza;

    public Cerc(double raza) {
        this.raza = raza;
    }

    public Cerc(){
        this.raza=0;
    }

    public double getRaza() {
        return raza;
    }

    public void setRaza(double raza) {
        this.raza = raza;
    }

    @Override
    public String toString() {
        return "Cerc{" +
                "raza=" + raza +
                '}';
    }
}
