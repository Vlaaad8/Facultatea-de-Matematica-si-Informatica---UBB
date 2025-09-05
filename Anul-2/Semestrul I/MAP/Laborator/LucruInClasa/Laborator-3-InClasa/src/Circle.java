public class Circle {
    private double radius;
    Circle(double r){
        radius = r;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "radius=" + radius +
                '}';
    }

    public double getRadius() {
        return radius;
    }
}
