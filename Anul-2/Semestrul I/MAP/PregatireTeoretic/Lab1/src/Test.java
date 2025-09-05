class Base {
    public void print() {
        System.out.println("Base");
    }
}
class Derived extends Base {
    @Override
    public void print() {
        System.out.println("Derived");
    }
}

