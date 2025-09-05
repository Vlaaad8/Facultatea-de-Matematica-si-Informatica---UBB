public class ComplexNumber {
    double real;
    double imaginary;

    @Override
    public String toString() {
        return real + "+" + imaginary + "i";
    }

    public ComplexNumber(double re, double im) {
        this.real = re;
        this.imaginary = im;
    }

    public double getImaginary() {
        return imaginary;
    }

    public double getReal() {
        return real;
    }

    public ComplexNumber addition(ComplexNumber m) {
        double realPart = this.getReal() + m.getReal();
        double imaginaryPart = this.getImaginary() + m.getImaginary();
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber subtraction(ComplexNumber m) {
        double realPart = this.getReal() - m.getReal();
        double imaginaryPart = this.getImaginary() - m.getImaginary();
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber multiplication(ComplexNumber m) {
        double realPart = this.getReal() * m.getReal() - this.getImaginary() * m.getImaginary();
        double imaginaryPart = this.getReal() * m.getImaginary() + this.getImaginary() * m.getReal();
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber division(ComplexNumber m) {
        double divider = m.getReal() * m.getReal() + m.getImaginary() * m.getImaginary();
        double realPart = (this.getReal() * m.getReal() + this.getImaginary() * m.getImaginary()) / divider;
        double imaginaryPart = (this.getImaginary() * m.getReal() - this.getReal() * m.getImaginary()) / divider;
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public ComplexNumber conjugate() {
        double realPart = this.getReal();
        double imaginaryPart = -this.getImaginary();
        return new ComplexNumber(realPart, imaginaryPart);
    }

}
