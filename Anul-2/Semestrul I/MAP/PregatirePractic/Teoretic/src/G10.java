class PizzaWithCheese extends Pizza {
    private String topping;
    public PizzaWithCheese(int id,String topping) {
        super(id);
        this.topping=topping;
    }
    public boolean equals(PizzaWithCheese obj) {
        return super.equals(obj) &&
                this.topping.equals(obj.topping);
    }}
